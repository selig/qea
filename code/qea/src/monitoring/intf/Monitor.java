package monitoring.intf;

import java.util.List;

import structure.impl.Verdict;
import structure.intf.Event;

/*
 * A monitor object takes an event consisting of a name and an array of arguments and produces a verdict
 */
public interface Monitor {

	public Verdict step(Event event);
	
	public Verdict trace(List<Event> eventList);
	
}
