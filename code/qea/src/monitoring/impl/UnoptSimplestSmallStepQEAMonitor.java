package monitoring.impl;

import java.util.IdentityHashMap;
import java.util.Map;

import structure.impl.SimplestQEA;
import structure.impl.Verdict;
import structure.intf.QEA;

/**
 * An unoptimised small-step monitor for the Simplest QEA
 * 
 * @author Giles Reger
 * @author Helena Cuenca
 */

public class UnoptSimplestSmallStepQEAMonitor extends SmallStepMonitor {

	private IdentityHashMap<Object, Integer> bindings;

	private QEA qea;

	private SimplestQEA simplestQEA;

	UnoptSimplestSmallStepQEAMonitor(QEA qea) {
		bindings = new IdentityHashMap<>();
		this.qea = qea;
		simplestQEA = (SimplestQEA) qea; // TODO Change this!
	}

	@Override
	public Verdict step(int eventName, Object[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Verdict step(int eventName, Object param1) {

		// Check if the data value corresponds to an existing binding
		if (bindings.containsKey(param1)) {

			int nextState = simplestQEA.getNextState(bindings.get(param1),
					eventName);
			if (nextState != 0) {
				bindings.put(param1, nextState);
			}
		} else { // New binding
			int nextState = simplestQEA.getNextState(1, eventName);
			if (nextState != 0) {
				bindings.put(param1, nextState);
			} else {
				// TODO Assume initial state or retrieve it from the QEA?
				bindings.put(param1, 1);
			}
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

	private boolean allBindingsInFinalState() {
		for (Map.Entry<Object, Integer> entry : bindings.entrySet()) {
			if (!simplestQEA.isStateFinal(entry.getValue())) {
				return false;
			}
		}
		return true;
	}

}
