package monitoring.impl;

import monitoring.intf.Monitor;
import structure.intf.QEA;

/**
 * All incremental monitors implement the trace method using the step method.
 * 
 * TODO: Consider using "incremental" instead of "smallstep"
 * 
 * @author Giles Reger
 * @author Helena Cuenca
 */

public abstract class SmallStepMonitor<Q extends QEA> extends Monitor<Q> {

	public SmallStepMonitor(Q q){super(q);}
	
	// public Verdict trace(List<Event> eventList) {
	// // TODO Auto-generated method stub
	// return null;
	// }

}
