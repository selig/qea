package monitoring.intf;

import structure.impl.Verdict;
import structure.intf.QEA;

/*
 * A monitor object takes an event consisting of a name and an array of arguments and produces a verdict
 */
public abstract class Monitor<Q extends QEA> {

	// for storing the property
	protected final Q qea;

	/**
	 * Class constructor specifying the QEA for which the monitor is to be
	 * created
	 * 
	 * @param q
	 *            QEA
	 */
	public Monitor(Q q) {
		qea = q;
	}

	public abstract Verdict step(int eventName, Object[] args);

	public abstract Verdict step(int eventName);

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

	/**
	 * Computes the final verdict of the monitoring process
	 * 
	 * @return Final verdict of the monitoring process
	 */
	public abstract Verdict end();

	/**
	 * Processes a trace of events
	 * 
	 * @param eventNames
	 *            Names of the events to be processed
	 * @param args
	 *            Arguments for each one of the events. The array
	 *            <code>args[i]</code> contains the parameters for the event
	 *            <code>eventNames[i]</code>. To indicate that an event has no
	 *            arguments, it's corresponding array of arguments must be an
	 *            array of size 0.
	 * @return Partial verdict of the monitoring process after processing the
	 *         specified trace of events
	 */
	public abstract Verdict trace(int[] eventNames, Object[][] args);

}
