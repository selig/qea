package online;

import java.util.Collection;
import java.util.Iterator;

import qea.monitoring.impl.MonitorFactory;
import qea.monitoring.intf.Monitor;
import qea.structure.impl.other.Verdict;
import qea.structure.intf.QEA;
import qeaExamples.SafeIterator;

public aspect SafeIterAspect {

	private final Monitor monitor;
	private final Object LOCK = new Object();

	private final int ITERATOR = 1;
	private final int NEXT = 2;

	public SafeIterAspect(){
		QEA qea = SafeIterator.make();
		monitor = MonitorFactory.create(qea);
		System.err.println("[qea.SafeIterator] Started"); 
	}
	private void check(Verdict v){
		if(v==Verdict.FAILURE){
			System.err.println("Warning, you are about to call next on an iterator more times than allowed");			
			StackTraceElement[] st = Thread.currentThread().getStackTrace();
			System.err.println("At "+st[st.length-1]);
		}
	}

	pointcut create(Collection c) : call(Iterator Collection.iterator()) && target(c);
	pointcut next(Iterator i) :  call(* java.util.Iterator.next()) && target(i);	


	after(Collection c) returning(Iterator i): create(c) {
		synchronized(LOCK){
			check(monitor.step(ITERATOR,i,c.size())); 
		}
	}
	
	before(Iterator i) : next(i) {
		synchronized(LOCK){
			check(monitor.step(NEXT,i));
		}
	}



}
