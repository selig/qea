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
	private NonDetConfig config;

	public Incr_QVar0_NoFVar_NonDet_QEAMonitor(QVar01_NoFVar_NonDet_QEA qea) {
		super(qea);

		// Set initial state
		config = new NonDetConfig(qea.getInitialState());
	}

	@Override
	public Verdict step(int eventName, Object[] args) {
		throw new ShouldNotHappenException(
				"Never call propositional QEA with arguments");
	}

	@Override
	public Verdict step(int eventName) {

		// Update configuration
		config = qea.getNextConfig(config, eventName);

		// Determine if there is a final/non-final strong state
		checkFinalAndStrongStates(config);

		return computeVerdict(false);
	}

	@Override
	public Verdict end() {
		return computeVerdict(true);
	}

	/**
	 * Computes the verdict for this monitor according to the current state of
	 * the binding(s)
	 * 
	 * @param end
	 *            <code>true</code> if all the events have been processed and a
	 *            final verdict is to be computed; <code>false</code> otherwise
	 * 
	 * @return <ul>
	 *         <li>{@link Verdict#SUCCESS} for a strong success
	 *         <li>{@link Verdict#FAILURE} for a strong failure
	 *         <li>{@link Verdict#WEAK_SUCCESS} for a weak success
	 *         <li>{@link Verdict#WEAK_FAILURE} for a weak failure
	 *         </ul>
	 * 
	 *         A strong success or failure means that other events processed
	 *         after this will produce the same verdict, while a weak success or
	 *         failure indicates that the verdict can change
	 */
	private Verdict computeVerdict(boolean end) {

		if (qea.containsFinalState(config)) {
			if (end || finalStrongState) {
				return Verdict.SUCCESS;
			}
			return Verdict.WEAK_SUCCESS;
		}
		if (end || nonFinalStrongState) {
			return Verdict.FAILURE;
		}
		return Verdict.WEAK_FAILURE;
	}

	@Override
	public String getStatus() {
		return "Config: " + config;
	}

}
