package monitoring.impl.monitors;

import monitoring.impl.IncrementalMonitor;
import monitoring.impl.configs.NonDetConfig;
import structure.impl.QVar01_FVar_NonDet_FixedQVar_QEA;
import structure.impl.Verdict;

/**
 * An incremental propositional monitor for a non-simple non-deterministic QEA
 * with no quantified variables
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class Incr_QVar0_FVar_NonDet_QEAMonitor extends
		IncrementalMonitor<QVar01_FVar_NonDet_FixedQVar_QEA> {

	/**
	 * Contains the current configuration (set of states) for the monitor
	 */
	private NonDetConfig config;

	public Incr_QVar0_FVar_NonDet_QEAMonitor(
			QVar01_FVar_NonDet_FixedQVar_QEA qea) {
		super(qea);

		// Set initial state
		config.setStates(new int[] { qea.getInitialState() });
	}

	@Override
	public Verdict step(int eventName, Object[] args) {

		// Update configuration
		config = qea.getNextConfig(config, eventName, args);

		// Determine verdict according to the current configuration
		if (qea.containsFinalState(config)) {
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

		// Determine verdict according to the current configuration
		if (qea.containsFinalState(config)) {
			return Verdict.SUCCESS;
		}
		return Verdict.FAILURE;
	}

	@Override
	public String getStatus() {
		return "Config: " + config;
	}

}