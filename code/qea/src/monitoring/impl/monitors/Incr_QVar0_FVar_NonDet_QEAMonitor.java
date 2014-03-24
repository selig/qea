package monitoring.impl.monitors;

import monitoring.impl.IncrementalMonitor;
import monitoring.impl.configs.NonDetConfig;
import structure.impl.other.Verdict;
import structure.impl.qeas.QVar01_FVar_NonDet_QEA;

/**
 * An incremental propositional monitor for a non-simple non-deterministic QEA
 * with no quantified variables
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class Incr_QVar0_FVar_NonDet_QEAMonitor extends
		IncrementalMonitor<QVar01_FVar_NonDet_QEA> {

	/**
	 * Contains the current configuration (set of states) for the monitor
	 */
	private NonDetConfig config;

	public Incr_QVar0_FVar_NonDet_QEAMonitor(QVar01_FVar_NonDet_QEA qea) {
		super(qea);

		// Set initial state
		config = new NonDetConfig(qea.getInitialState(), qea.newBinding());
	}

	@Override
	public Verdict step(int eventName, Object[] args) {

		// Update configuration
		config = qea.getNextConfig(config, eventName, args, null, false);
		return computeVerdict();
	}

	@Override
	public Verdict step(int eventName) {
		return step(eventName, new Object[] {});
	}

	@Override
	public Verdict end() {
		return computeVerdict();
	}

	private Verdict computeVerdict() {

		// TODO Take into account strong states
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
