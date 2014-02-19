package monitoring.impl.monitors;

import monitoring.impl.IncrementalMonitor;
import structure.impl.SimpleDetQEA;
import structure.impl.Verdict;
import exceptions.ShouldNotHappenException;

/**
 * A small-step monitor for a simple QEA with no quantified variables
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class IncrementalPropositionalQEAMonitor extends
		IncrementalMonitor<SimpleDetQEA> {

	private int currentState;

	public IncrementalPropositionalQEAMonitor(SimpleDetQEA qea) {
		super(qea);
		currentState = qea.getInitialState(); // Set initial state
	}

	@Override
	public Verdict step(int eventName, Object[] args) {
		throw new ShouldNotHappenException(
				"Never call propositional QEA with arguments");
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
