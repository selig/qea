package monitoring.impl.monitors;

import java.util.IdentityHashMap;

import monitoring.impl.configs.NonDetConfig;
import structure.impl.NonSimpleNonDetQEA;
import structure.impl.Verdict;

/**
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class IncrementalNonSimpleNonDetQEAMonitor extends
		IncrementalNonSimpleQEAMonitor<NonSimpleNonDetQEA> {

	private IdentityHashMap<Object, NonDetConfig> bindings;

	public IncrementalNonSimpleNonDetQEAMonitor(NonSimpleNonDetQEA qea) {
		super(qea);
		bindings = new IdentityHashMap<>();
	}

	@Override
	public Verdict step(int eventName, Object[] args) {

		boolean existingBinding = false;
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

			// Assign flag for counters update
			existingBinding = true;

		} else { // New quantified variable binding

			// Create configuration for the new binding
			config = new NonDetConfig(qea.getInitialState(), qea.newBinding());
		}

		// Flag needed to update counters later
		boolean startConfigFinal = qea.containsFinalState(config);

		// Compute next configuration
		config = qea.getNextConfig(config, eventName, args);

		// Flag needed to update counters later
		boolean endConfigFinal = qea.containsFinalState(config);

		// Update/add configuration for the binding
		bindings.put(quantifiedVar, config);

		// If applicable, update counters
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
		// TODO Auto-generated method stub
		return null;
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
