package monitoring.impl.monitors;

import monitoring.impl.IncrementalMonitor;
import monitoring.impl.configs.DetConfig;
import structure.impl.QVar01_FVar_Det_QEA;
import structure.impl.Verdict;

/**
 * An incremental monitor for a non-simple QEA with no quantified variables
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class Incr_QVar0_FVar_Det_QEAMonitor extends
		IncrementalMonitor<QVar01_FVar_Det_QEA> {

	/**
	 * Current configuration (state) for the monitor
	 */
	private DetConfig config;

	public Incr_QVar0_FVar_Det_QEAMonitor(QVar01_FVar_Det_QEA qea) {
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

	@Override
	public String getStatus() {
		return "Config: " + config;
	}

}
