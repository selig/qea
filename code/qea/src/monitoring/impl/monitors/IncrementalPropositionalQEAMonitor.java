package monitoring.impl.monitors;

import monitoring.impl.IncrementalMonitor;
import monitoring.impl.configs.DetConfig;
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

	/**
	 * Current configuration (state) for the monitor
	 */
	private DetConfig currentConfig;

	public IncrementalPropositionalQEAMonitor(SimpleDetQEA qea) {
		super(qea);
		currentConfig = new DetConfig(qea.getInitialState());
	}

	@Override
	public Verdict step(int eventName, Object[] args) {
		throw new ShouldNotHappenException(
				"Never call propositional QEA with arguments");
	}

	@Override
	public Verdict step(int eventName) {

		// Update state
		currentConfig.setState(qea.getNextState(currentConfig.getState(),
				eventName));

		// Determine verdict according to the state
		if (qea.isStateFinal(currentConfig.getState())) {
			return Verdict.WEAK_SUCCESS;
		}
		return Verdict.WEAK_FAILURE;
	}

	@Override
	public Verdict end() {

		// Determine verdict according to the state
		if (qea.isStateFinal(currentConfig.getState())) {
			return Verdict.SUCCESS;
		}
		return Verdict.FAILURE;
	}

}
