package online;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import qea.monitoring.impl.MonitorFactory;
import qea.monitoring.intf.Monitor;
import qea.structure.impl.other.Verdict;
import qea.structure.intf.QEA;
import qeaExamples.UnsafeMapIter;

public aspect UnsafeMapIteratorAspect {

	private final Monitor monitor;

	private final int CREATE = 1;
	private final int ITERATOR = 2;
	private final int UPDATE = 3;
	private final int USE = 4;

	public UnsafeMapIteratorAspect(){
		QEA qea = UnsafeMapIter.make();
		monitor = MonitorFactory.create(qea);
		System.err.println("[qea.UnsafeMapIterator] Started"); 
	}
	private void check(Verdict v){
		if(v==Verdict.FAILURE){
			System.err.println("Warning, you are about to use an Iterator over a map that has been updated");			
			StackTraceElement[] st = Thread.currentThread().getStackTrace();
			System.err.println("At "+st[st.length-1]);
		}
	}

	// create(m,c)
	pointcut create(Map map) : call(Set Map.keySet()) && target(map);
	// iterator(c,i)
	pointcut iterator(Collection col) : call(Iterator Collection.iterator()) && target(col);
	// update(m)
	pointcut update(Map map) : 
		(          call(* Map.put*(..)) 
				|| call(* Map.putAll*(..)) 
				|| call(* Map.clear()) 
				|| call(* Map.remove*(..)))  && target(map);
	// use(i)
	pointcut use(Iterator iter) : call(* Iterator.*(..)) && target(iter);



	after(Map m) returning(Collection c): create(m) {
		check(monitor.step(CREATE,m,c)); 
	}
	after(Collection c) returning(Iterator i): iterator(c) {
		check(monitor.step(ITERATOR,c,i)); 
	}	
	before(Map m) : update(m) {
		check(monitor.step(UPDATE,m));
	}	
	before(Iterator i) : use(i) {
		check(monitor.step(USE,i));
	}


}
