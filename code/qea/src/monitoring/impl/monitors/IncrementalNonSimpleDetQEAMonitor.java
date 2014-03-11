package monitoring.impl.monitors;

import java.util.IdentityHashMap;
import java.util.Map;

import monitoring.impl.configs.DetConfig;
import monitoring.impl.configs.NonDetConfig;
import structure.impl.NonSimpleDetQEA;
import structure.impl.Verdict;

/**
 * An incremental monitor for a non-simple deterministic QEA
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class IncrementalNonSimpleDetQEAMonitor extends
		IncrementalNonSimpleQEAMonitor<NonSimpleDetQEA> {

	/**
	 * Maps the current values of the quantified variable to the deterministic
	 * configuration for each binding. The configuration contains the state and
	 * the bindings for the free variables
	 */
	private IdentityHashMap<Object, DetConfig> bindings;

	/**
	 * Creates an IncrementalNonSimpleDetQEAMonitor for the specified QEA
	 * 
	 * @param qea
	 *            QEA
	 */
	public IncrementalNonSimpleDetQEAMonitor(NonSimpleDetQEA qea) {
		super(qea);
		bindings = new IdentityHashMap<>();
	}

	@Override
	public Verdict step(int eventName, Object[] args) {

		boolean existingBinding = false;
		DetConfig config;

		// Obtain the value for the quantified variable
		// Assumption: The (unique) quantified variable is present in all events
		// and it’s always the first argument
		Object quantifiedVar = args[0];

		// Determine if the value received corresponds to an existing binding
		if (bindings.containsKey(quantifiedVar)) { // Existing quantified
													// variable binding

			// Get current configuration for the binding
			config = bindings.get(quantifiedVar);

			// Assign flag for counters update
			existingBinding = true;

		} else { // New quantified variable binding

			// Create configuration for the new binding
			config = new DetConfig(qea.getInitialState());
			config.setBinding(qea.newBinding());
		}

		// Flag needed to update counters later
		boolean startConfigFinal = qea.isStateFinal(config.getState());

		// Compute next configuration
		config = qea.getNextConfig(config, eventName, args);

		// Flag needed to update counters later
		boolean endConfigFinal = qea.isStateFinal(config.getState());

		// Update/add configuration for the binding
		bindings.put(quantifiedVar, config);

		// If applicable, update counters
		// TODO Consider extracting the update counters logic in another method
		if (existingBinding) {
			if (startConfigFinal && !endConfigFinal) {
				bindingsInNonFinalStateCount++;
				bindingsInFinalStateCount--;
			} else if (!startConfigFinal && endConfigFinal) {
				bindingsInNonFinalStateCount--;
				bindingsInFinalStateCount++;
			}
		} else {
			if (endConfigFinal) {
				bindingsInFinalStateCount++;
			} else {
				bindingsInNonFinalStateCount++;
			}
		}

		// According to the quantification of the variable, return verdict
		if (qea.isQuantificationUniversal() && allBindingsInFinalState()
				|| !qea.isQuantificationUniversal()
				&& existsOneBindingInFinalState()) {
			return Verdict.WEAK_SUCCESS;
		}
		return Verdict.WEAK_FAILURE;
	}

	@Override
	public Verdict step(int eventName) {
		Verdict finalVerdict = null;
		for (Map.Entry<Object, DetConfig> entry : bindings.entrySet()) {
			finalVerdict = step(eventName, entry.getKey());
		}
		return finalVerdict;
	}

	@Override
	public String toString() {
		String ret = "Map:\n";
		for (IdentityHashMap.Entry<Object, DetConfig> entry : bindings
				.entrySet()) {
			ret += entry.getKey() + "\t->\t" + entry.getValue() + "\n";
		}
		return ret;
	}
	
	@Override
	public String getStatus() {
		String ret = "Map:\n";
		for (IdentityHashMap.Entry<Object, DetConfig> entry : bindings
				.entrySet()) {
			ret += entry.getKey() + "\t->\t" + entry.getValue() + "\n";
		}
		return ret;
	}	
}
