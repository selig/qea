package monitoring;

import structure.*;

/*
 * A monitor object takes an event consisting of a name and an array of arguments and produces a verdict
 */
public interface Monitor {

	public Verdict step(String event_name, Object[] event_args);
	
}
