#QEA


Quantified Event Automata (QEA) is a specification formalism developed for runtime monitoring. This project is a reimplementation and extension of QEA monitoring techniques (explored in Giles Reger’s PhD work) by Master’s student Helena Cuenca.

The project is still in the development phase. For any queries contact Giles on giles.reger@manchester.ac.uk.

At the moment we assume the reader is familiar with runtime monitoring concepts. Instructions for the less experienced user will appear later.

See also [here][1] </a> for related papers and [here][2] for Giles’ thesis. For the RV competition mentioned see [here][3].
##QEA Creation

We provide a builder to construct QEA’s, found in qea.creation.QEABuilder. In the future we plan to include an external language and a parser that uses this builder.

We will use the canonical [HasNext property][4] as an example throughout this text. This is how you could specify this property.

```java
QEABuilder b = new QEABuilder("HasNext");       
                
int i = -1;
b.addQuantification(FORALL,i);
        
int HASNEXT_TRUE = 1;
int HASNEXT_FALSE = 2;
int NEXT = 3;

b.addTransition(1,HASNEXT_TRUE,i,2);
b.addTransition(2,HASNEXT_TRUE,i,2);
b.addTransition(2,NEXT,i,1);

b.addTransition(2,HASNEXT_FALSE,i,3);
b.addTransition(3,HASNEXT_FALSE,i,3);

b.addFinalStates(1,2,3);

QEA qea = b.make();
```

If you didn’t want to have to create separate events for true and false return values with hasNext you could also write

```java
QEABuilder b = new QEABuilder("HasNext");       
                
int i = -1;
int b = 1;
b.addQuantification(FORALL,i);
        
int HASNEXT = 1;
int NEXT = 2;

b.addTransition(1,HASNEXT,new int[]{i,b}, Guard.isTrue(b),2);
b.addTransition(2,HASNEXT,new int[]{i,b}, Guard.isTrue(b),2);
b.addTransition(2,NEXT,i,1);

b.addTransition(1,HASNEXT,new int[]{i,b}, Guard.not(Guard.isTrue(b)),3);
b.addTransition(3,HASNEXT,new int[]{i,b}, Guard.not(Guard.isTrue(b)),3);

b.addFinalStates(1,2,3);

QEA qea = b.make();
```

However, in general the more features that you use in the specification, the more complex monitoring is. We therefore usually aim to use the simplest features in specification. Future work will look at automated methods for simplification (wrt features).

There are lots of other things we can do when creating QEAs. For more examples see the qeaProperties project. Note that by default states have a next semantics. To make a state a skip state you need to include setSkipStates(state,…).


##Monitor Creation

Creating a monitor is straightforward using qea.monitoring.impl.MonitorFactory:

```java
MonitorFactory.create(qea)
MonitorFactory.create(qea,RestartMode.IGNORE,GarbageMode.LAZY);
```

You can either create a basic monitor or add restart and garbage modes - these are still experimental features but are usually required for pragmatic monitoring.

The restart mode tells the monitor what to do if an ultimate verdict is reached (i.e. violation of a universally quantified property). The choices are
<ul>
<li> REMOVE - remove the offending binding </li>
<li> ROLLBACK - reset the offending binding to the initial state </li>
<li> IGNORE - reset and ignore this binding in the future </li>
</ul>

The garbage mode makes certain data structures use weak references - which is only relevant for Online monitoring. The choices are:
<ul>
<li> LAZY - what javamop does i.e. use weak maps and clear null references before expanding</li>
<li> EAGER - monitor garbage collections and actively remove from structures </li>
<li> UNSAFE_LAZY - the same as lazy but don’t carry out checks to make sure the binding won’t be necessary in the future</li>
</ul>

To use a monitor you just need to submit an event name and some parameters, for example
```
monitor.step(HASNEXT,iterator,return_value);
```
we don’t currently do number of argument of type checks and using events not in the alphabet is invalid.

##Online Monitoring

We assume in online monitoring instrumentation is achieved using AspectJ. We don’t produce aspects automatically. The following is an example of an aspect that would monitor the HasNext property. Scoping might be required on pointcuts depending on exact usage.

```aspectj
package qea; 

import java.util.Iterator;

//Imports for QEA
import static qea.structure.impl.other.Quantification.FORALL;
import qea.structure.intf.QEA;
import qea.creation.QEABuilder;
import qea.monitoring.impl.*;
import qea.monitoring.intf.*;
import qea.structure.impl.other.Verdict;

public aspect HasNextAspect {

        // Declaring the events 
        private final int HASNEXT_TRUE = 1;
        private final int HASNEXT_FALSE = 2;
        private final int NEXT = 3;

        //The monitor
        Monitor monitor;
        // Required if multithreaded as monitor not thread-safe
        private Object LOCK = new Object();


        pointcut hasNext(Iterator i) : call(* java.util.Iterator+.hasNext()) && target(i);
        pointcut next(Iterator i) :  call(* java.util.Iterator+.next()) && target(i);   
                
        after(Iterator i) returning(boolean b): hasNext(i) {
                synchronized(LOCK){
                        if(b){ check(monitor.step(HASNEXT_TRUE,i)); }
                        else { check(monitor.step(HASNEXT_FALSE,i)); }
                }
        }

        before(Iterator i) : next(i) {
                synchronized(LOCK){
                        check(monitor.step(NEXT,i));
                }
        }

        private static void check(Verdict verdict){
                if(verdict==Verdict.FAILURE){
                        // Put match detection code here
                }
        }

        public HasNextAspect(){
                QEA qea = qea.properties.papers.HasNextQEA;
                monitor = MonitorFactory.create(qea);
        }
}
```

Note that event names (integers) used here need to match with those used in the QEA.

The normal commands would be required to compile and run with Aspectj - make sure qea-1.0.jar is on the classpath (and qeaProperties if you use those).

##Online Monitoring - DaCapo

It is common to use the [DaCapo benchmarks][5] for benchmarking in runtime monitoring. A useful [framework][6] has been built for load-time weaving and benchmarking with DaCapo. We have extended this so that QEA can be used in this [fork][7].



##Offline Monitoring (for the Competition)

The benchmark project contains everything required to perform the benchmarking needed in the OFFLINE track of the RV14 monitoring competition, and other stuff useful for benchmarking. In general, to monitor offline you just need to construct a monitor and manually submit events to it. These extra classes allow for parsing of log files as specified in the RV competition guidelines.

In order to monitor a trace file with the QEA offline monitor, run the class benchmark.competition.offline.QEAOfflineMain specifying in the classpath the following jar files (found in /jars):
<ul>
    <li>qea-1.0.jar</li>
    <li>qeaOfflineMonitor-1.0.jar</li>
    <li>qeaProperties-1.0.jar<\li>
</ul>

The program takes 3 parameters:
<ul>
    <li>tracePath: Location of the trace file </li>
    <li>propertyName: Name of the property to monitor. For a list of the available properties </li>
    <li>fileFormat (Optional): The possible values are: CSV and XML. Required when the extension of the trace file is not CSV or XML </li>
</ul>
For example:

java -cp qeaBenchmark-1.0.jar;qea-1.0.jar;qeaProperties-1.0.jar qea.benchmark.competition.offline.QEAOfflineMain ExistsLeader.csv QEA_OFFLINE_ONE

The following properties from the competition are available and related to the numbered properties in the competition:
<ul>
 <li>SOLOIST_ONE</li>
 <li>SOLOIST_TWO</li>
 <li>SOLOIST_THREE</li>
 <li>SOLOIST_FOUR</li>
 <li>RITHM_ONE</li>
 <li>RITHM_TWO</li>
 <li>RITHM_THREE</li>
 <li>RITHM_FOUR</li>
 <li>RITHM_FIVE</li>
 <li>MONPOLY_ONE</li>
 <li>MONPOLY_TWO</li>
 <li>MONPOLY_THREE</li>
 <li>MONPOLY_FOUR</li>
 <li>MONPOLY_FIVE</li>
 <li>STEPR_ONE</li>
 <li>STEPR_TWO</li>
 <li>STEPR_THREE</li>
 <li>STEPR_FOUR</li>
 <li>QEA_OFFLINE_ONE</li>
 <li>QEA_OFFLINE_TWO</li>
 <li>QEA_OFFLINE_THREE</li>
 <li>QEA_OFFLINE_FOUR</li>
 <li>QEA_OFFLINE_FIVE</li>
</ul>
## Maven

The developers are not very experienced with Maven, but you can add QEA to your project using

```xml
             <dependency>
                        <groupId>com.github.selig</groupId>
                        <artifactId>qea</artifactId>
                        <version>1.0</version>
                        <scope>compile</scope>
                </dependency>
```
This is what we did in our [fork][7] of the DaCapo evaluation framework.

[1]: http://www.cs.man.ac.uk/~david/sm.html
[2]: https://www.escholar.manchester.ac.uk/uk-ac-man-scw:225931
[3]: http://rv2014.imag.fr/monitoring-competition
[4]: http://en.wikipedia.org/wiki/Runtime_verification#HasNext
[5]: http://dacapobench.org/
[6]: https://github.com/parzonka/prm4j-eval
[7]: https://github.com/selig/prm4j-eval
