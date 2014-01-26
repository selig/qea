package monitoring.impl;

import java.util.List;

import monitoring.intf.Monitor;
import structure.impl.Verdict;
import structure.intf.Event;

/**
 * All incremental monitors implement the trace method using the step method.
 * 
 *TODO: Consider using "incremental" instead of "smallstep"
 * 
 * @author Giles Reger and Helena Cuenca
 */

public abstract class SmallStepMonitor implements Monitor {

	public Verdict trace(List<Event> eventList) {
		// TODO Auto-generated method stub
		return null;
	}	
	
}
