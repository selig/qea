package monitoring.impl.monitors;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import monitoring.impl.configs.DetConfig;
import structure.impl.NonSimpleDetGenQEA;
import structure.impl.TransitionImpl;
import structure.impl.Verdict;

/**
 * An incremental monitor for a non-simple deterministic generic QEA
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class IncrementalNonSimpleDetGenQEAMonitor extends
		IncrementalNonSimpleQEAMonitor<NonSimpleDetGenQEA> {

	/**
	 * Maps the current values of the quantified variable to the deterministic
	 * configuration for each binding. The configuration contains the state and
	 * the bindings for the free variables
	 */
	private IdentityHashMap<Object, DetConfig> bindings;

	/**
	 * Maps the event names defined in the QEA of this monitor to the signatures
	 * defined for it
	 */
	private HashMap<Integer, int[][]> eventsIndex;

	/**
	 * Creates an <code>IncrementalNonSimpleDetGenQEAMonitor</code> to monitor
	 * the specified QEA property
	 * 
	 * @param qea
	 *            QEA property
	 */
	public IncrementalNonSimpleDetGenQEAMonitor(NonSimpleDetGenQEA qea) {
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

				// If the transition is defined, count a new signature
				if (qea.getTransition(states[i], eventsAlphabet[j]) != null) {
					signaturesCountPerEvent[j]++;
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

				// Get the transition for the start state - event
				TransitionImpl transition = (TransitionImpl) qea.getTransition(
						states[i], eventName);

				if (transition != null) {

					// Add the set of parameters in the transition to the index
					int[][] parameters = eventsIndex.get(eventName);
					parameters[signaturesCountPerEvent[j]] = transition
							.getVariableNames();

					signaturesCountPerEvent[j]++;
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
			for (Map.Entry<Object, DetConfig> entry : bindings.entrySet()) {
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
		for (Map.Entry<Object, DetConfig> entry : bindings.entrySet()) {
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

		// TODO This method contains almost the same logic of step(int,
		// Object[]) in IncrementalNonSimpleDetQEAMonitor

		boolean existingBinding = false;
		DetConfig config;

		// Determine if the value received corresponds to an existing binding
		if (bindings.containsKey(qVarValue)) { // Existing quantified
												// variable binding

			// Get current configuration for the binding
			config = bindings.get(qVarValue);

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
		bindings.put(qVarValue, config);

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
	}
}
