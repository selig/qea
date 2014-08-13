package qea.monitoring.impl.monitors;

import qea.monitoring.impl.GarbageMode;
import qea.monitoring.impl.IncrementalMonitor;
import qea.monitoring.impl.RestartMode;
import qea.monitoring.impl.configs.DetConfig;
import qea.structure.impl.other.Verdict;
import qea.structure.impl.qeas.QVar01_NoFVar_Det_QEA;
import qea.exceptions.ShouldNotHappenException;

/**
 * A small-step monitor for a simple QEA with no quantified variables
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class Incr_QVar0_NoFVar_Det_QEAMonitor extends
		IncrementalMonitor<QVar01_NoFVar_Det_QEA> {

	/**
	 * Current configuration (state) for the monitor
	 */
	private DetConfig config;

	public Incr_QVar0_NoFVar_Det_QEAMonitor(RestartMode restart,
			GarbageMode garbage, QVar01_NoFVar_Det_QEA qea) {
		super(restart, garbage, qea);
		config = new DetConfig(qea.getInitialState());
	}

	@Override
	public Verdict step(int eventName, Object[] args) {
		throw new ShouldNotHappenException(
				"Never call propositional QEA with arguments");
	}

	@Override
	public Verdict step(int eventName) {

		if (saved != null) {
			if (!restart()) {
				return saved;
			}
		}

		// Update state
		config.setState(qea.getNextState(config.getState(), eventName));
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

		Verdict result;

		if (qea.isStateFinal(config.getState())) {
			if (end || qea.isStateStrong(config.getState())) {
				saved = Verdict.SUCCESS;
				result = Verdict.SUCCESS;
			} else {
				result = Verdict.WEAK_SUCCESS;
			}
		} else {
			if (end || qea.isStateStrong(config.getState())) {
				saved = Verdict.FAILURE;
				result = Verdict.FAILURE;
			} else {
				result = Verdict.WEAK_FAILURE;
			}
		}

		if (qea.isNegated()) {
			result = result.negated();
		}
		return result;
	}

	@Override
	public String getStatus() {
		return "Config: " + config;
	}

	@Override
	protected int removeStrongBindings() {
		// Not applicable to this monitor
		return 0;

	}

	@Override
	protected int ignoreStrongBindings() {
		// Not applicable to this monitor
		return 0;
	}

	@Override
	protected int rollbackStrongBindings() {
		config = new DetConfig(qea.getInitialState());
		return 1;
	}

}
