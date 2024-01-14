.PHONY := build


project.class: project.java 
	javac project.java

build: project.class 

run: build
	java -cp .:sqlite-jdbc-3.39.3.0.jar project
