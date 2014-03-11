package monitoring.impl.monitors;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import monitoring.impl.configs.NonDetConfig;
import structure.impl.NonSimpleNonDetGenQEA;
import structure.impl.TransitionImpl;
import structure.impl.Verdict;
import structure.intf.Transition;

/**
 * An incremental monitor for a non-simple non-deterministic generic QEA
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 * @param <Q>
 */
public class IncrementalNonSimpleNonDetGenQEAMonitor extends
		IncrementalNonSimpleQEAMonitor<NonSimpleNonDetGenQEA> {

	private IdentityHashMap<Object, NonDetConfig> bindings;

	/**
	 * Maps the event names defined in the QEA of this monitor to the signatures
	 * defined for it
	 */
	private HashMap<Integer, int[][]> eventsIndex;

	/**
	 * Creates a <code>IncrementalNonSimpleNonDetGenQEAMonitor</code> to monitor
	 * the specified QEA property
	 * 
	 * @param qea
	 *            QEA property
	 */
	public IncrementalNonSimpleNonDetGenQEAMonitor(NonSimpleNonDetGenQEA qea) {
		super(qea);
		bindings = new IdentityHashMap<>();
		eventsIndex = new HashMap<>();
		buildEventsIndex();
	}

	/**
	 * Builds an index of the different signatures for every event in the QEA of
	 * this monitor
	 */
	private void buildEventsIndex() {

		int[] states = qea.getStates();
		int[] eventsAlphabet = qea.getEventsAlphabet();
		int[] signaturesCountPerEvent = new int[eventsAlphabet.length];

		// Count number of signatures per event
		for (int i = 0; i < states.length; i++) {
			for (int j = 0; j < eventsAlphabet.length; j++) {

				Transition[] transitions = qea.getTransitions(states[i],
						eventsAlphabet[j]);

				// If at least one transition is defined, count the signatures
				if (transitions != null) {
					signaturesCountPerEvent[j] += transitions.length;
				}
			}
		}

		// Put events in the map and initialise parameters matrix
		for (int i = 0; i < signaturesCountPerEvent.length; i++) {
			eventsIndex.put(eventsAlphabet[i],
					new int[signaturesCountPerEvent[i]][]);
			signaturesCountPerEvent[i] = 0;
		}

		// Populate index
		for (int i = 0; i < states.length; i++) {
			for (int j = 0; j < eventsAlphabet.length; j++) {

				int eventName = eventsAlphabet[j];

				// Get the transitions for the start state - event
				Transition[] transitions = qea.getTransitions(states[i],
						eventName);

				if (transitions != null) {

					// Add the set of parameters of each transition to the
					// index
					for (Transition transition : transitions) {

						int[][] parameters = eventsIndex.get(eventName);
						parameters[signaturesCountPerEvent[j]] = ((TransitionImpl) transition)
								.getVariableNames();

						signaturesCountPerEvent[j]++;
					}
				}
			}
		}
	}

	@Override
	public Verdict step(int eventName, Object[] args) {

		int[][] eventSignatures = eventsIndex.get(eventName);

		if (containsSignatureOnlyFVars(eventSignatures)) {

			// If there is a signature for the event with only free variables
			// (no quantified variables), apply event to all bindings
			for (Map.Entry<Object, NonDetConfig> entry : bindings.entrySet()) {
				stepNoVerdict(eventName, args, entry.getKey());
			}

		} else {

			// Apply event to each value of quantified variable
			for (int[] eventSignature : eventSignatures) {
				Object qVarValue = getQVarValue(args, eventSignature);
				stepNoVerdict(eventName, args, qVarValue);
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
		for (Map.Entry<Object, NonDetConfig> entry : bindings.entrySet()) {
			finalVerdict = step(eventName, entry.getKey());
		}
		return finalVerdict;
	}

	/**
	 * Processes the specified event with the specified arguments for the
	 * specified binding (quantified variable value) without producing a verdict
	 * 
	 * @param eventName
	 *            Name of the event
	 * @param args
	 *            Array of arguments
	 * @param qVarValue
	 *            Quantified variable value
	 */
	private void stepNoVerdict(int eventName, Object[] args, Object qVarValue) {

		boolean existingBinding = false;
		NonDetConfig config;

		// Determine if the value received corresponds to an existing binding
		if (bindings.containsKey(qVarValue)) { // Existing quantified
												// variable binding
			// Get current configuration for the binding
			config = bindings.get(qVarValue);

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
		bindings.put(qVarValue, config);

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
