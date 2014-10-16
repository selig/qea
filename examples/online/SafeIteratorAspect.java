
import java.util.Iterator;
import java.util.Collection;

//Imports for QEA
import static qea.structure.impl.other.Quantification.FORALL;
import qea.structure.intf.QEA;
import qea.creation.QEABuilder;
import qea.monitoring.impl.*;
import qea.monitoring.intf.*;
import qea.structure.impl.other.Verdict;
import qea.structure.intf.*;

public aspect SafeIteratorAspect {

        // Declaring the events 
        private final int ITERATOR = 1;
        private final int NEXT = 2;

	//The monitor
	Monitor monitor;
	private Object LOCK = new Object();


	@SuppressWarnings("unchecked")
	pointcut create(Collection c) : (call(Iterator Collection.iterator()) && target(c));
	@SuppressWarnings("unchecked")
	pointcut next(Iterator i) :  call(* java.util.Iterator.next()) && target(i);	
		
	@SuppressWarnings("unchecked")
	after(Collection c) returning(Iterator i): create(c) {
		synchronized(LOCK){
			check(monitor.step(ITERATOR,i,c.size())); 
		}
        }
        @SuppressWarnings("unchecked")
    	before(Iterator i) : next(i) {
		synchronized(LOCK){
			check(monitor.step(NEXT,i));
		}
        }

	private void check(Verdict verdict){
		if(verdict==Verdict.FAILURE){
			System.err.println("Warning, you are about to call next on an iterator more times than allowed");
			System.err.println("Aborting..");
			System.exit(0);
		}
	}

	public void init(){
		QEABuilder b = new QEABuilder("SafeIterator");	
		
		int i = -1;
		int size = 1;
		b.addQuantification(FORALL,i);
	
		b.addTransition(1,ITERATOR,new int[]{i,size},2);
		b.addTransition(2,NEXT,new int[]{i},Guard.varIsGreaterThanVal(size,0),
					 Assignment.decrement(size),2);

		// In case the iterator is created by some other means
		b.addTransition(1,NEXT, new int[]{i}, 3);
		b.addTransition(3,NEXT, new int[]{i}, 3);

		// An iterator may be returned again, in which case size must be the same
		// This happens in the case of the empty iterator
		int other_size=2;
		b.addTransition(2,ITERATOR, new int[]{i,other_size}, Guard.isEqual(other_size,size), 2);

		b.addFinalStates(1,2,3);

		// Need to make with garbage reset
		monitor = MonitorFactory.create(b.make(),RestartMode.IGNORE,GarbageMode.LAZY);
	}


	public SafeIteratorAspect(){
		init();
		System.out.println("[qea.SafeIterator] Started"); 
	}
}


