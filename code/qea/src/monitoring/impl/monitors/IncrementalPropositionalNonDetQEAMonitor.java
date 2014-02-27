package monitoring.impl.monitors;

import monitoring.impl.IncrementalMonitor;
import monitoring.impl.configs.NonDetConfig;
import structure.impl.SimpleNonDetQEA;
import structure.impl.Verdict;
import exceptions.ShouldNotHappenException;

/**
 * An incremental propositional monitor for a simple non-deterministic QEA with
 * no quantified variables
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class IncrementalPropositionalNonDetQEAMonitor extends
		IncrementalMonitor<SimpleNonDetQEA> {

	/**
	 * Contains the current configuration (set of states) for the monitor
	 */
	private NonDetConfig currentConfig;

	public IncrementalPropositionalNonDetQEAMonitor(SimpleNonDetQEA qea) {
		super(qea);

		// Set initial state
		currentConfig.setStates(new int[] { qea.getInitialState() });
	}

	@Override
	public Verdict step(int eventName, Object[] args) {
		throw new ShouldNotHappenException(
				"Never call propositional QEA with arguments");
	}

	@Override
	public Verdict step(int eventName) {

		// Update configuration
		currentConfig = qea.getNextConfig(currentConfig, eventName);

		// Determine verdict according to the current configuration
		if (qea.containsFinalState(currentConfig)) {
			return Verdict.WEAK_SUCCESS;
		}
		return Verdict.WEAK_FAILURE;
	}

	@Override
	public Verdict end() {

		// Determine verdict according to the current configuration
		if (qea.containsFinalState(currentConfig)) {
			return Verdict.SUCCESS;
		}
		return Verdict.FAILURE;
	}

}
