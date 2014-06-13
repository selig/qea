package monitoring.impl.monitors;

import monitoring.impl.GarbageMode;
import monitoring.impl.IncrementalMonitor;
import monitoring.impl.RestartMode;
import monitoring.impl.configs.DetConfig;
import structure.impl.other.Verdict;
import structure.impl.qeas.QVar01_FVar_Det_QEA;

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

	public Incr_QVar0_FVar_Det_QEAMonitor(RestartMode restart,
			GarbageMode garbage, QVar01_FVar_Det_QEA qea) {
		super(restart, garbage, qea);
		config = new DetConfig(qea.getInitialState(), qea.newBinding());
	}

	@Override
	public Verdict step(int eventName, Object[] args) {

		if (saved != null) {
			if (!restart())
				return saved;
		}

		// Update configuration
		config = qea.getNextConfig(config, eventName, args, null, false);
		return computeVerdict(false);
	}

	private static final Object[] dummyargs = new Object[] {};

	@Override
	public Verdict step(int eventName) {
		return step(eventName, dummyargs);
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

		if (qea.isStateFinal(config.getState())) {
			if (end || qea.isStateStrong(config.getState())) {
				saved = Verdict.SUCCESS;
				return Verdict.SUCCESS;
			}
			return Verdict.WEAK_SUCCESS;
		} else {
			if (end || qea.isStateStrong(config.getState())) {
				saved = Verdict.FAILURE;
				return Verdict.FAILURE;
			}
			return Verdict.WEAK_FAILURE;
		}
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
		config = new DetConfig(qea.getInitialState(), qea.newBinding());
		return 1;

	}

}
