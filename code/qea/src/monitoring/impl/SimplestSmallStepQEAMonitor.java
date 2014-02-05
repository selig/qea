package monitoring.impl;

import java.util.IdentityHashMap;

import structure.impl.SimplestQEA;
import structure.impl.Verdict;
import structure.intf.QEA;

/**
 * A small-step monitor for the Simplest QEA
 * 
 * @author Giles Reger
 * @author Helena Cuenca
 */

public class SimplestSmallStepQEAMonitor extends SmallStepMonitor {

	private IdentityHashMap<Object, Integer> bindings;

	private QEA qea;

	private SimplestQEA simplestQEA;

	private int bindingsInNonFinalStateCount;

	SimplestSmallStepQEAMonitor(QEA qea) {
		bindings = new IdentityHashMap<>();
		this.qea = qea;
		simplestQEA = (SimplestQEA) qea; // TODO Change this!
		bindingsInNonFinalStateCount = 0;
	}

	@Override
	public Verdict step(int eventName, Object[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Verdict step(int eventName, Object param1) {

		boolean existingBindingInNonFinalState = false;
		int startState;

		// Determine if the value received corresponds to an existing binding
		if (bindings.containsKey(param1)) { // Existing binding

			startState = bindings.get(param1);
			if (!simplestQEA.isStateFinal(startState)) {
				existingBindingInNonFinalState = true;
			}
		} else { // New binding
			startState = 1;
		}

		// Compute next state
		int endState = simplestQEA.getNextState(startState, eventName);

		// Update/add state for the binding
		bindings.put(param1, endState);

		// If applicable, update count of non-final state bindings
		if (existingBindingInNonFinalState
				&& simplestQEA.isStateFinal(endState)) {
			bindingsInNonFinalStateCount--;
		} else if (!existingBindingInNonFinalState
				&& !simplestQEA.isStateFinal(endState)) {
			bindingsInNonFinalStateCount++;
		}

		if (allBindingsInFinalState()) {
			return Verdict.WEAK_SUCCESS;
		}
		return Verdict.WEAK_FAILURE;
	}

	@Override
	public Verdict end() {
		if (allBindingsInFinalState()) {
			return Verdict.SUCCESS;
		}
		return Verdict.FAILURE;
	}

	// public Verdict step(Event event) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	/**
	 * Determines if all bindings for the current monitor are in a final state
	 * 
	 * @return true if all bindings for the current monitor are in a final
	 *         state. Otherwise, false
	 */
	private boolean allBindingsInFinalState() {
		if (bindingsInNonFinalStateCount == 0) {
			return true;
		}
		return false;
	}
}
