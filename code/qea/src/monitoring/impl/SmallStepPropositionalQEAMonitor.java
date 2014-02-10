package monitoring.impl;

import structure.impl.SimplestQEA;
import structure.impl.Verdict;

/**
 * A small-step monitor for a simple QEA with no quantified variables
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class SmallStepPropositionalQEAMonitor extends
		SmallStepMonitor<SimplestQEA> {

	private int currentState;

	public SmallStepPropositionalQEAMonitor(SimplestQEA qea) {
		super(qea);
		currentState = 1; // Set initial state
	}

	@Override
	public Verdict step(int eventName, Object[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Verdict step(int eventName) {

		// Update state
		currentState = qea.getNextState(currentState, eventName);

		// Determine verdict according to the state
		if (qea.isStateFinal(currentState)) {
			return Verdict.WEAK_SUCCESS;
		}
		return Verdict.WEAK_FAILURE;
	}

	@Override
	public Verdict end() {

		// Determine verdict according to the state
		if (qea.isStateFinal(currentState)) {
			return Verdict.SUCCESS;
		}
		return Verdict.FAILURE;
	}

}
