package monitoring.intf;

import structure.impl.Verdict;

/*
 * A monitor object takes an event consisting of a name and an array of arguments and produces a verdict
 */
public abstract class Monitor {

	public abstract Verdict step(int eventName, Object[] args);

	public Verdict step(int eventName, Object param1) {
		return step(eventName, new Object[] { param1 });
	}

	public Verdict step(int eventName, Object param1, Object param2) {
		return step(eventName, new Object[] { param1, param2 });
	}

	public Verdict step(int eventName, Object param1, Object param2,
			Object param3) {
		return step(eventName, new Object[] { param1, param2, param3 });
	}

	public Verdict step(int eventName, Object param1, Object param2,
			Object param3, Object param4) {
		return step(eventName, new Object[] { param1, param2, param3, param4 });
	}

	public Verdict step(int eventName, Object param1, Object param2,
			Object param3, Object param4, Object param5) {
		return step(eventName, new Object[] { param1, param2, param3, param4,
				param5 });
	}

	public abstract Verdict end();

	// TODO: The trace method received before a list of Events, now we don't
	// have 'Event'
	// public abstract Verdict trace(List<Event> eventList);

}
