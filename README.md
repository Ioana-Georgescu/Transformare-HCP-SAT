# Transformare-HCP-SAT
Implementarea unei reduceri HCP ≤p SAT in timp polinomial in Java

Algoritmul va primi ca input o instanta a problemei Hamiltonian Cycle (un graf) intr-un fisier denumit graph.in. Acesta va avea urmatorul format:
<N = numarul de noduri din graf>
<muchie simbolozata prin nodurile intre care se afla>
-1
Nodurile vor fi numerotate incepand de la 1
1 ≤ N ≤ 100

Exemplu de input:
4
1 2
3 4
2 3
-1

Output-ul va fi o instanta a problemei SAT (o expresie booleana) in fisierul bexpr.out.
Operatori:
	& -> AND
	| -> OR
	∼ -> NOT

Semnificatia variabilelor:
	xi−j = 1 daca muchia (i, j) apartine drumului ales; 0 altfel
		1 ≤ i, j ≤ N

	ai−j = 1 daca cea mai scurta cale de la 1 la j, in drumul ales, are lungimea i; 0 altfel
		1 ≤ i ≤ N/2+1, 1 ≤ j ≤ N
