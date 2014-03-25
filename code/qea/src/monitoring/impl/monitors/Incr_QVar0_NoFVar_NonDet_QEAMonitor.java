package monitoring.impl.monitors;

import monitoring.impl.IncrementalMonitor;
import monitoring.impl.configs.NonDetConfig;
import structure.impl.other.Verdict;
import structure.impl.qeas.QVar01_NoFVar_NonDet_QEA;
import exceptions.ShouldNotHappenException;

/**
 * An incremental propositional monitor for a simple non-deterministic QEA with
 * no quantified variables
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class Incr_QVar0_NoFVar_NonDet_QEAMonitor extends
		IncrementalMonitor<QVar01_NoFVar_NonDet_QEA> {

	/**
	 * Contains the current configuration (set of states) for the monitor
	 */
	private NonDetConfig currentConfig;

	public Incr_QVar0_NoFVar_NonDet_QEAMonitor(QVar01_NoFVar_NonDet_QEA qea) {
		super(qea);

		// Set initial state
		currentConfig = new NonDetConfig(qea.getInitialState());
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
		return computeVerdict();
	}

	@Override
	public Verdict end() {
		return computeVerdict();
	}

	private Verdict computeVerdict() {

		// TODO Take into account strong states
		// Determine verdict according to the current configuration
		if (qea.containsFinalState(currentConfig)) {
			return Verdict.SUCCESS;
		}
		return Verdict.FAILURE;
	}

	@Override
	public String getStatus() {
		return "Config: " + currentConfig;
	}

}
