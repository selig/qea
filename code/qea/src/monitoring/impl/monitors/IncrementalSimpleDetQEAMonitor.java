package monitoring.impl.monitors;

import java.util.IdentityHashMap;
import java.util.Map;

import monitoring.impl.configs.NonDetConfig;

import structure.impl.SimpleDetQEA;
import structure.impl.Verdict;
import exceptions.ShouldNotHappenException;

/**
 * A small-step monitor for the Simplest QEA
 * 
 * @author Giles Reger
 * @author Helena Cuenca
 */
public class IncrementalSimpleDetQEAMonitor extends
		IncrementalSimpleQEAMonitor<SimpleDetQEA> {

	private IdentityHashMap<Object, Integer> bindings;

	/**
	 * Creates an IncrementalSimpleDetQEAMonitor for the specified QEA
	 * 
	 * @param qea
	 *            QEA
	 */
	public IncrementalSimpleDetQEAMonitor(SimpleDetQEA qea) {
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

		// If applicable, update counters
		if (existingBinding) {
			if (qea.isStateFinal(startState) && !qea.isStateFinal(endState)) {
				bindingsInNonFinalStateCount++;
				bindingsInFinalStateCount--;
			} else if (!qea.isStateFinal(startState)
					&& qea.isStateFinal(endState)) {
				bindingsInNonFinalStateCount--;
				bindingsInFinalStateCount++;
			}
		} else {
			if (qea.isStateFinal(endState)) {
				bindingsInFinalStateCount++;
			} else {
				bindingsInNonFinalStateCount++;
			}
		}

		// According to the quantification of the variable, return verdict
		if (qea.isQuantificationUniversal() && allBindingsInFinalState()
				|| !qea.isQuantificationUniversal()
				&& existsOneBindingInFinalState()) {
			return Verdict.WEAK_SUCCESS;
		}
		return Verdict.WEAK_FAILURE;
	}

	@Override
	public Verdict step(int eventName) {
		Verdict finalVerdict = null;
		for (Map.Entry<Object, Integer> entry : bindings.entrySet()) {
			finalVerdict = step(eventName, entry.getKey());
		}
		return finalVerdict;
	}

	@Override
	public String getStatus() {
		String ret = "Map:\n";
		for (IdentityHashMap.Entry<Object, Integer> entry : bindings
				.entrySet()) {
			ret += entry.getKey() + "\t->\t" + entry.getValue() + "\n";
		}
		return ret;
	}	
	
}
