#!/bin/bash

#Setup lib directory
mkdir lib

#Get stuff for QEA 
wget --no-check-certificate https://github.com/selig/qea/blob/master/jars/qea-1.0.jar?raw=true -O lib/qea-1.0.jar

#Get libraries
wget --no-check-certificate https://github.com/selig/qea/blob/master/qea/lib/commons-collections4-4.0.jar?raw=true -O lib/commons-collections4-4.0.jar

#Get aspectj stuff
wget --no-check-certificate https://github.com/selig/qea/blob/master/qea/lib/aspectjrt.jar?raw=true -O lib/aspectjrt.jar
wget --no-check-certificate https://github.com/selig/qea/blob/master/qea/lib/aspectjtools.jar?raw=true -O lib/aspectjtools.jar

#Get example elements
wget --no-check-certificate https://github.com/selig/qea/blob/master/example/offline/Program.java
wget --no-check-certificate https://github.com/selig/qea/blob/master/example/offline/SafeIteratorAspect.java

#Compile program without instrumentation 
javac Program.java
#Run it
echo "Running the Program without monitoring, note the exception"
java Program

#Weave the monitoring aspect into it
java -cp "lib/*" org.aspectj.tools.ajc.Main -source 1.7 -sourceroots . 

#Run example
echo "Now runnig the Program with monitoring"
java -cp "lib/*":. Program 
