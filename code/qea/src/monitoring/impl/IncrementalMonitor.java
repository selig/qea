package monitoring.impl;

import monitoring.intf.Monitor;
import structure.impl.other.Verdict;
import structure.intf.QEA;

/**
 * This class provides implementation for the <code>trace</code> method using
 * the <code>step</code> method, as well as some utility methods to be used by
 * the subclasses
 * 
 * @author Giles Reger
 * @author Helena Cuenca
 */
public abstract class IncrementalMonitor<Q extends QEA> extends Monitor<Q> {

	/**
	 * Number of bindings for this monitor that are in a final state
	 */
	protected int bindingsInFinalStateCount;

	/**
	 * Number of bindings for this monitor that are in a non-final state
	 */
	protected int bindingsInNonFinalStateCount;

	/**
	 * Class constructor specifying the QEA to be monitored. For invocation by
	 * subclass constructors
	 * 
	 * @param qea
	 *            QEA property
	 */
	protected IncrementalMonitor(Q qea) {
		super(qea);
	}

	@Override
	public Verdict trace(int[] eventNames, Object[][] args) {

		Verdict finalVerdict = null;

		for (int i = 0; i < eventNames.length; i++) {
			switch (args[i].length) {
			case 0:
				finalVerdict = step(eventNames[i]);
				break;
			case 1:
				finalVerdict = step(eventNames[i], args[i][0]);
				break;
			case 2:
				finalVerdict = step(eventNames[i], args[i][0], args[i][1]);
				break;
			case 3:
				finalVerdict = step(eventNames[i], args[i][0], args[i][1],
						args[i][2]);
				break;
			case 4:
				finalVerdict = step(eventNames[i], args[i][0], args[i][1],
						args[i][2], args[i][3]);
				break;
			case 5:
				finalVerdict = step(eventNames[i], args[i][0], args[i][1],
						args[i][2], args[i][3], args[i][4]);
				break;
			default:
				finalVerdict = step(eventNames[i], args[i]);
				break;
			}
		}
		return finalVerdict;
	}

	/**
	 * Determines if all bindings in this monitor are in a final state
	 * 
	 * @return <code>true</code> if all bindings in this monitor are in a final
	 *         state; <code>false</code> otherwise
	 */
	protected boolean allBindingsInFinalState() {
		if (bindingsInNonFinalStateCount == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Determines if there is at least one binding in a final state in this
	 * monitor
	 * 
	 * @return <code>true</code> if at least one binding in this monitor is in
	 *         final state; <code>false</code> otherwise
	 */
	protected boolean existsOneBindingInFinalState() {
		if (bindingsInFinalStateCount > 0) {
			return true;
		}
		return false;
	}
}
