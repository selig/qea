package monitoring.intf;

import structure.impl.Verdict;
import structure.intf.QEA;

/**
 * Superclass for all QEA monitors. A monitor object takes an event consisting
 * of a name and, optionally, one or more arguments and produces a verdict
 * 
 * @author Giles Reger
 * @author Helena Cuenca
 * 
 * @param <Q>
 *            Type of QEA to be monitored
 */
public abstract class Monitor<Q extends QEA> {

	/**
	 * QEA property
	 */
	protected final Q qea;

	/**
	 * Class constructor specifying the QEA property to be monitored. For
	 * invocation by subclass constructors
	 * 
	 * @param qea
	 *            QEA describing the property to be monitored
	 */
	protected Monitor(Q qea) {
		this.qea = qea;
	}

	/**
	 * Processes the specified event with the specified arguments and produces a
	 * verdict according to the property described by the QEA and the state of
	 * this monitor
	 * 
	 * @param eventName
	 *            Name of the event
	 * @param args
	 *            Array of arguments
	 * @return <ul>
	 *         <li>{@link Verdict#SUCCESS} for a strong success
	 *         <li>{@link Verdict#FAILURE} for a strong failure
	 *         <li>{@link Verdict#WEAK_SUCCESS} for a weak success
	 *         <li>{@link Verdict#WEAK_FAILURE} for a weak failure
	 *         </ul>
	 * 
	 *         A strong success or failure means that other events processed
	 *         after this will produce the same verdict, while a weak success or
	 *         failure indicates that the verdict can change
	 */
	public abstract Verdict step(int eventName, Object[] args);

	/**
	 * Processes the specified event with no arguments and produces a verdict
	 * according to the property described by the QEA and the state of this
	 * monitor
	 * 
	 * @param eventName
	 *            Name of the event
	 * @return Verdict as described in the generic version of this method
	 *         <code>step(int, Object[])</code>
	 */
	public abstract Verdict step(int eventName);

	/**
	 * Processes the specified event with the specified argument and produces a
	 * verdict according to the property described by the QEA and the state of
	 * this monitor
	 * 
	 * @param eventName
	 *            Name of the event
	 * @param param1
	 *            Unique argument for the event
	 * @return Verdict as described in the generic version of this method
	 *         <code>step(int, Object[])</code>
	 */
	public Verdict step(int eventName, Object param1) {
		return step(eventName, new Object[] { param1 });
	}

	/**
	 * Processes the specified event with the two specified arguments and
	 * produces a verdict according to the property described by the QEA and the
	 * state of this monitor
	 * 
	 * @param eventName
	 *            Name of the event
	 * @param param1
	 *            First argument for the event
	 * @param param2
	 *            Second argument for the event
	 * @return Verdict as described in the generic version of this method
	 *         <code>step(int, Object[])</code>
	 */
	public Verdict step(int eventName, Object param1, Object param2) {
		return step(eventName, new Object[] { param1, param2 });
	}

	/**
	 * Processes the specified event with the three specified arguments and
	 * produces a verdict according to the property described by the QEA and the
	 * state of this monitor
	 * 
	 * @param eventName
	 *            Name of the event
	 * @param param1
	 *            First argument for the event
	 * @param param2
	 *            Second argument for the event
	 * @param param3
	 *            Third argument for the event
	 * @return Verdict as described in the generic version of this method
	 *         <code>step(int, Object[])</code>
	 */
	public Verdict step(int eventName, Object param1, Object param2,
			Object param3) {
		return step(eventName, new Object[] { param1, param2, param3 });
	}

	/**
	 * Processes the specified event with the four specified arguments and
	 * produces a verdict according to the property described by the QEA and the
	 * state of this monitor
	 * 
	 * @param eventName
	 *            Name of the event
	 * @param param1
	 *            First argument for the event
	 * @param param2
	 *            Second argument for the event
	 * @param param3
	 *            Third argument for the event
	 * @param param4
	 *            Fourth argument for the event
	 * @return Verdict as described in the generic version of this method
	 *         <code>step(int, Object[])</code>
	 */
	public Verdict step(int eventName, Object param1, Object param2,
			Object param3, Object param4) {
		return step(eventName, new Object[] { param1, param2, param3, param4 });
	}

	/**
	 * Processes the specified event with the four specified arguments and
	 * produces a verdict according to the property described by the QEA and the
	 * state of this monitor
	 * 
	 * @param eventName
	 *            Name of the event
	 * @param param1
	 *            First argument for the event
	 * @param param2
	 *            Second argument for the event
	 * @param param3
	 *            Third argument for the event
	 * @param param4
	 *            Fourth argument for the event
	 * @param param5
	 *            Fifth argument for the event
	 * @return Verdict as described in the generic version of this method
	 *         <code>step(int, Object[])</code>
	 */
	public Verdict step(int eventName, Object param1, Object param2,
			Object param3, Object param4, Object param5) {
		return step(eventName, new Object[] { param1, param2, param3, param4,
				param5 });
	}

	/**
	 * Computes the final verdict of the monitoring process. This method should
	 * be called after invoking the method <code>step</code> for all the events
	 * that need to be processed
	 * 
	 * @return Final verdict of the monitoring process
	 *         <ul>
	 *         <li>{@link Verdict#SUCCESS} for a success
	 *         <li>{@link Verdict#FAILURE} for a failure
	 *         </ul>
	 */
	public abstract Verdict end();

	/**
	 * Processes a trace (finite sequence of events) and produces a verdict
	 * 
	 * @param eventNames
	 *            Names of the events to be processed
	 * @param args
	 *            Arguments for each one of the events. The array
	 *            <code>args[i]</code> contains the parameters for the event
	 *            <code>eventNames[i]</code>. To indicate that an event has no
	 *            arguments, it's corresponding array of arguments must be an
	 *            array of size 0.
	 * @return <ul>
	 *         <li>{@link Verdict#SUCCESS} for a strong success
	 *         <li>{@link Verdict#FAILURE} for a strong failure
	 *         <li>{@link Verdict#WEAK_SUCCESS} for a weak success
	 *         <li>{@link Verdict#WEAK_FAILURE} for a weak failure
	 *         </ul>
	 * 
	 *         A strong success or failure means that other events processed
	 *         after this will produce the same verdict, while a weak success or
	 *         failure indicates that the verdict can change
	 */
	public abstract Verdict trace(int[] eventNames, Object[][] args);

	
	/*
	 * For Debugging
	 */
	public abstract String getStatus();
	@Override
	public String toString(){ return getStatus();}
	
}
