package monitoring.impl.monitors;

import monitoring.impl.IncrementalMonitor;
import monitoring.impl.configs.DetConfig;
import structure.impl.NonSimpleDetQEA;
import structure.impl.Verdict;

/**
 * An incremental monitor for a non-simple QEA with no quantified variables
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class IncrementalNSPropositionalQEAMonitor extends
		IncrementalMonitor<NonSimpleDetQEA> {

	/**
	 * Current configuration (state) for the monitor
	 */
	private DetConfig config;

	public IncrementalNSPropositionalQEAMonitor(NonSimpleDetQEA qea) {
		super(qea);
		config.setState(qea.getInitialState());
	}

	@Override
	public Verdict step(int eventName, Object[] args) {

		// Update configuration
		config = qea.getNextConfig(config, eventName, args);

		// Determine verdict according to the current configuration
		if (qea.isStateFinal(config.getState())) {
			return Verdict.WEAK_SUCCESS;
		}
		return Verdict.WEAK_FAILURE;
	}

	@Override
	public Verdict step(int eventName) {
		// TODO Is this a valid case? What happens here?
		return null;
	}

	@Override
	public Verdict end() {

		// Determine verdict according to the state
		if (qea.isStateFinal(config.getState())) {
			return Verdict.SUCCESS;
		}
		return Verdict.FAILURE;
	}

}
