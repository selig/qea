#!/bin/bash

#Setup lib directory
mkdir lib

#Get stuff for QEA 
wget --no-check-certificate https://github.com/selig/qea/blob/master/jars/qea-1.0.jar?raw=true -O lib/qea-1.0.jar

#Get libraries
wget --no-check-certificate https://github.com/selig/qea/blob/master/qea/lib/commons-collections4-4.0.jar?raw=true -O lib/commons-collections4-4.0.jar
wget --no-check-certificate https://github.com/selig/qea/blob/master/qea/lib/tools.jar?raw=true -O lib/tools.jar

#Get example elements
wget --no-check-certificate https://github.com/selig/qea/blob/master/qea/example/offline/trace.csv
wget --no-check-certificate https://github.com/selig/qea/blob/master/qea/example/offline/Example.java

#Compile example
javac -cp "lib/*" Example.java

#Run example
java -cp "lib/*" Example
