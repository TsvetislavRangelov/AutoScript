#usr/bin/env bash

antlr4 AutoScript.g4 -visitor -no-listener
javac -Xlint:unchecked AutoScript*.java Main.java
java Main.java < input2.txt
