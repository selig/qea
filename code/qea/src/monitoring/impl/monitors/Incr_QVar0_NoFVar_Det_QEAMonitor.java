package monitoring.impl.monitors;

import monitoring.impl.IncrementalMonitor;
import monitoring.impl.configs.DetConfig;
import structure.impl.QVar01_NoFVar_Det_QEA;
import structure.impl.Verdict;
import exceptions.ShouldNotHappenException;

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
	private DetConfig currentConfig;

	public Incr_QVar0_NoFVar_Det_QEAMonitor(QVar01_NoFVar_Det_QEA qea) {
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

	@Override
	public String getStatus() {
		return "Config: " + currentConfig;
	}

}
