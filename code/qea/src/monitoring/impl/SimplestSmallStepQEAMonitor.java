package monitoring.impl;

import java.util.IdentityHashMap;
import java.util.Map;

import structure.impl.SimplestQEA;
import structure.impl.Verdict;

/**
 * A small-step monitor for the Simplest QEA
 * 
 * @author Giles Reger
 * @author Helena Cuenca
 */
public class SimplestSmallStepQEAMonitor extends SmallStepMonitor<SimplestQEA> {

	private IdentityHashMap<Object, Integer> bindings;

	private int bindingsInNonFinalStateCount;

	private int bindingsInFinalStateCount;

	/**
	 * Creates a SimplestSmallStepQEAMonitor for the specified QEA
	 * 
	 * @param qea
	 *            QEA
	 */
	SimplestSmallStepQEAMonitor(SimplestQEA qea) {
		super(qea);
		bindings = new IdentityHashMap<>();
		bindingsInNonFinalStateCount = 0;
		bindingsInFinalStateCount = 0;
	}

	@Override
	public Verdict step(int eventName, Object[] args) {
		// TODO Auto-generated method stub
		return null;
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
			startState = 1;
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
		for (Map.Entry<Object, Integer> entry : bindings.entrySet()) {
			step(eventName, entry.getKey());
		}
		return null; // TODO: What is the verdict here?
	}

	@Override
	public Verdict end() {

		// According to the quantification of the variable, return verdict
		if (qea.isQuantificationUniversal() && allBindingsInFinalState()
				|| !qea.isQuantificationUniversal()
				&& existsOneBindingInFinalState()) {
			return Verdict.SUCCESS;
		}
		return Verdict.FAILURE;
	}

	/**
	 * Determines if all bindings for the current monitor are in a final state
	 * 
	 * @return <code>true</code> if all bindings for the current monitor are in
	 *         a final state; <code>false</code> otherwise
	 */
	private boolean allBindingsInFinalState() {
		if (bindingsInNonFinalStateCount == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Determines if there is at least one binding in a final state for the
	 * current monitor
	 * 
	 * @return <code>true</code> if at least one binding is in final state;
	 *         <code>false</code> otherwise
	 */
	private boolean existsOneBindingInFinalState() {
		if (bindingsInFinalStateCount > 0) {
			return true;
		}
		return false;
	}

}
