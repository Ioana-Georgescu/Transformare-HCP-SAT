package hcp_sat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * @author Georgescu Ioana
 */
public class Transformare {
    public static void main(String[] args) {
        File in = new File("./graph.in");
        try (Scanner sc = new Scanner(in)) {
            try (PrintStream out = new PrintStream("./bexpr.out")) {
                System.setOut(out);

                int N = sc.nextInt(); // nr de noduri
                ArrayList<ArrayList<Integer>> graph = new ArrayList<>(N);

                int i, j;
                while(sc.hasNextInt()) {
                    i = sc.nextInt();
                    if(i == -1) {
                        break;
                    }
                    j = sc.nextInt();
                    while(graph.size() < i) {
                        graph.add(new ArrayList<Integer>());
                    }
                    while(graph.size() < j) {
                        graph.add(new ArrayList<Integer>());
                    }
                    graph.get(i-1).add(j);
                    graph.get(j-1).add(i);
                }

                int ok = 1;
                for(ArrayList<Integer> node : graph) {
                    if(node.size() <= 1) {
                        /*
                        cel putin 1 nod nu e conectat de restul grafului sau are doar 1 vecin
                        => nu se poate crea un ciclu hamiltonian
                        */
                        System.out.print("x1-1&~x1-1"); // are mereu valoarea 0
                        ok = 0;
                        break;
                    }
                }
                if(ok == 0) {
                    out.close();
                    sc.close();
                }

                for(i = 1; i <= N; i++) {// nod cu nod

                    if(i != 1) {
                        System.out.print("&");
                    }
                    System.out.print("(");

                    /*
                    parcurg fiecare pereche de vecini posibila
                    doar o pereche poate face parte din ciclu (adica muchiile dintre
                    cei 2 vecini si nodul curent sunt in ciclu)
                    */
                    for(int a = 0; a < graph.get(i-1).size()-1; a++) {// primul vecin

                        if(a != 0) {
                            System.out.print("|");
                        }

                        for(int b = a+1; b < graph.get(i-1).size(); b++) {// al doilea vecin

                            if(b != a+1) {
                                System.out.print("|");
                            }

                            System.out.print("(");

                            for(int k : graph.get(i-1)) {

                                if(k != graph.get(i-1).get(0)) {
                                    System.out.print("&");
                                }

                                if((k != graph.get(i-1).get(a)) && (k != graph.get(i-1).get(b))) {
                                    /*
                                    un nod care nu apartine perechii curente de vecini
                                    => muchia dintre el si nodul curent nu trebuie sa existe in ciclu
                                    */
                                    System.out.print("~");
                                }

                                // muchia dintre vecinul curent si nodul curent
                                System.out.print("x" + i + "-" + k);
                            }
                            System.out.print(")");
                        }
                    }
                    System.out.print(")");

                    // cel putin una dintre distantele de la oricare alt nod la 1 trebuie sa fie <= N/2+1
                    if(i != 1) {
                        System.out.print("&(");

                        for(j = 1; j <= N/2+1; j++) {

                            if(j != 1) {
                                System.out.print("|");
                            }
                            System.out.print("a" + j + "-" + i);
                        }
                        System.out.print(")");
                    }
                }

                for(i = 1; i <= N; i++) {// nod cu nod
                    for(int k : graph.get(i-1)) {// vecin cu vecin
                        if(i == 1) {// 1 este punctul de referinta (de plecare la parcurgerea grafului)
                            /*
                            distanta de la 1 la vecin trebuie sa corespunda sensului de parcurgere
                            (daca latura 1-k, parcursa de la 1 la k, apare in ciclu atunci a1-k = 1,
                            daca nu, atunci a1-k = 0)
                            */
                            System.out.print("&((a1-" + k + "|~x1-" + k + ")&(~a1-" + k + "|x1-" + k + "))");
                        }
                        
                        // o latura poate fi inclusa doar o data in ciclu
                        System.out.print("&(x" + i + "-" + k + "|~x" + k + "-" + i + ")");
                    }
                }

                // nu pot exista in ciclu muchii intre nodul 1 si el insusi sau noduri cu care nu e vecin
                for(i = 1; i <= N; i++) {
                    if(!graph.get(0).contains(i)) {
                        System.out.print("&~a1-" + i);
                    }
                }

                /*
                in ciclu, nodul j, aflat la distanta i de nodul 1, este precedat de unul singur
                dintre vecinii sai si urmat de unul singur dintre vecinii sai
                */
                for(i = 2; i <= N/2+1; i++) {// fiecare distanta valida in afra de 1
                    for(j = 2; j <= N; j++) {// fiecare nod in afara de 1
                        
                        /*
                        nodul curent se afla la distanta i de nodul 1
                        sau niciunul dintre vecinii sai nu se afla la distanta i-1
                        */
                        System.out.print("&((a" + i + "-" + j + "|~((");
                        int c = 0;
                        for(int k : graph.get(j-1)) {// pentru fiecare vecin al nodului curent
                            if(k != 1) {
                                if(c != 0) {
                                    System.out.print("|");
                                }
                                c++;
                                // k precede j
                                System.out.print("(a" + (i-1) + "-" + k + "&x" + k + "-" + j + ")");
                            }
                        }
                        System.out.print(")&~(");

                        // k nu apare nicaieri in ciclu inaintea lui j
                        for(int k = 1; k < i; k++) {

                            if(k != 1) {
                                System.out.print("|");
                            }
                            System.out.print("a" + k + "-" + j);
                        }
                        System.out.print(")))&(");
                        
                        /*
                        nodul curent nu se afla la distanta i de nodul 1
                        sau unul dintre vecinii sai se afla la distanta i-1
                        */
                        System.out.print("~a" + i + "-" + j + "|((");
                        c = 0;
                        for(int k : graph.get(j-1)) {// pentru fiecare vecin al nodului curent
                            if(k != 1) {
                                if(c != 0) {
                                    System.out.print("|");
                                }
                                c++;
                                // k precede j
                                System.out.print("(a" + (i-1) + "-" + k + "&x" + k + "-" + j + ")");
                            }
                        }
                        System.out.print(")&~(");

                        // k nu apare undeva in ciclu inaintea lui j
                        for(int k = 1; k < i; k++) {

                            if(k != 1) {
                                System.out.print("|");
                            }
                            System.out.print("a" + k + "-" + j);
                        }
                        System.out.print("))))");
                    }
                }

                out.close();
                sc.close();
            }
            catch(IOException e) {
                System.out.println("Failed to create bexpr.out");
                System.exit(1);
            }
        }
        catch(FileNotFoundException e) {
            System.out.println("graph.in doesn't exist");
            System.exit(1);
        }
    }
}
