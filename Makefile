JFLAGS = -g
JC = javac

defult: build

build:
	$(JC) *.java -d ./
	jar cfe hcp_sat.jar hcp_sat.Transformare hcp_sat/*.class

clean:
	rm -rf hcp_sat
	rm -f *.jar
	rm -f *.out

run: build
	java -jar hcp_sat.jar graph.in