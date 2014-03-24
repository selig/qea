package monitoring.impl.monitors;

import java.util.IdentityHashMap;

import structure.impl.other.Verdict;
import structure.impl.qeas.QVar01_NoFVar_Det_QEA;
import exceptions.ShouldNotHappenException;

/**
 * A small-step monitor for the Simplest QEA
 * 
 * @author Giles Reger
 * @author Helena Cuenca
 */
public class Incr_QVar1_NoFVar_Det_QEAMonitor extends
		Abstr_Incr_QVar1_NoFVar_QEAMonitor<QVar01_NoFVar_Det_QEA> {

	private IdentityHashMap<Object, Integer> bindings;

	/**
	 * Creates an IncrementalSimpleDetQEAMonitor for the specified QEA
	 * 
	 * @param qea
	 *            QEA
	 */
	public Incr_QVar1_NoFVar_Det_QEAMonitor(QVar01_NoFVar_Det_QEA qea) {
		super(qea);
		bindings = new IdentityHashMap<>();
	}

	@Override
	public Verdict step(int eventName, Object[] args) {
		if (args.length > 1) {
			throw new ShouldNotHappenException(
					"Was only expecting one parameter");
		}
		return step(eventName, args[0]);
	}

	@Override
	public Verdict step(int eventName, Object param1) {

		boolean existingBinding = false;
		int startState;

		// Determine if the value received corresponds to an existing binding
		if (bindings.containsKey(param1)) { // Existing binding

			// Get current state for the binding
			startState = bindings.get(param1);

			// Assign flag for counters update
			existingBinding = true;

		} else { // New binding
			startState = qea.getInitialState();
		}

		// Compute next state
		int endState = qea.getNextState(startState, eventName);

		// Update/add state for the binding
		bindings.put(param1, endState);

		// Update counters
		updateCounters(existingBinding, qea.isStateFinal(startState),
				qea.isStateFinal(endState));

		return computeVerdict();
	}

	@Override
	public Verdict step(int eventName) {
		Verdict finalVerdict = null;
		for (Object binding : bindings.keySet()) {
			// TODO Calling wrong method here
			finalVerdict = step(eventName, binding);
		}
		return finalVerdict;
	}

	@Override
	public String getStatus() {
		String ret = "Map:\n";
		for (IdentityHashMap.Entry<Object, Integer> entry : bindings.entrySet()) {
			ret += entry.getKey() + "\t->\t" + entry.getValue() + "\n";
		}
		return ret;
	}

}
