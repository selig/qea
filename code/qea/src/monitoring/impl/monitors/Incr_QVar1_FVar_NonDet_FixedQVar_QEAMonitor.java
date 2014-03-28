package monitoring.impl.monitors;

import java.util.IdentityHashMap;

import monitoring.impl.configs.NonDetConfig;
import structure.impl.other.Verdict;
import structure.impl.qeas.QVar1_FVar_NonDet_FixedQVar_QEA;

/**
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class Incr_QVar1_FVar_NonDet_FixedQVar_QEAMonitor extends
		Abstr_Incr_QVar1_FVar_QEAMonitor<QVar1_FVar_NonDet_FixedQVar_QEA> {

	private IdentityHashMap<Object, NonDetConfig> bindings;

	public Incr_QVar1_FVar_NonDet_FixedQVar_QEAMonitor(
			QVar1_FVar_NonDet_FixedQVar_QEA qea) {
		super(qea);
		bindings = new IdentityHashMap<>();
	}

	@Override
	public Verdict step(int eventName, Object[] args) {

		boolean existingBinding = false;
		boolean startConfigFinal = false;
		NonDetConfig config;

		// Obtain the value for the quantified variable
		// Assumption: The (unique) quantified variable is present in all events
		// and it’s always the first argument
		Object quantifiedVar = args[0];

		// Determine if the value received corresponds to an existing binding
		if (bindings.containsKey(quantifiedVar)) { // Existing quantified
													// variable binding
			// Get current configuration for the binding
			config = bindings.get(quantifiedVar);

			// Assign flags for counters update
			existingBinding = true;
			startConfigFinal = qea.containsFinalState(config);

		} else { // New quantified variable binding

			// Create configuration for the new binding
			config = new NonDetConfig(qea.getInitialState(), qea.newBinding());
		}

		// Compute next configuration
		config = qea.getNextConfig(config, eventName, args);

		// Update/add configuration for the binding
		bindings.put(quantifiedVar, config);

		// Determine if there is a final/non-final strong state
		boolean endConfigFinal = checkFinalAndStrongStates(config);

		// Update counters
		updateCounters(existingBinding, startConfigFinal, endConfigFinal);

		return computeVerdict(false);
	}

	@Override
	public Verdict step(int eventName) {
		return step(eventName, new Object[] {});
	}

	@Override
	public String getStatus() {
		String ret = "Map:\n";
		for (IdentityHashMap.Entry<Object, NonDetConfig> entry : bindings
				.entrySet()) {
			ret += entry.getKey() + "\t->\t" + entry.getValue() + "\n";
		}
		return ret;
	}

}
