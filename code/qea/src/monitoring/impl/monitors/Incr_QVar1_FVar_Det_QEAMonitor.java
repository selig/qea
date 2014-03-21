package monitoring.impl.monitors;

import java.util.IdentityHashMap;

import monitoring.impl.configs.DetConfig;
import structure.impl.other.Transition;
import structure.impl.other.Verdict;
import structure.impl.qeas.QVar01_FVar_Det_QEA;

/**
 * An incremental monitor for a non-simple deterministic generic QEA
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class Incr_QVar1_FVar_Det_QEAMonitor extends
		Abstr_Incr_QVar1_FVar_QEAMonitor<QVar01_FVar_Det_QEA> {

	/**
	 * Maps the current values of the quantified variable to the deterministic
	 * configuration for each binding. The configuration contains the state and
	 * the bindings for the free variables
	 */
	private IdentityHashMap<Object, DetConfig> bindings;

	/**
	 * Configuration storing the state and free variables binding for events
	 * where the quantified variable is not present
	 */
	private DetConfig emptyBindingConfig;

	/**
	 * For each event stores a <code>true</code> indicating it has at least one
	 * signature that only contains free variables as parameters,
	 * <code>false</code> otherwise
	 */
	private boolean[] onlyFVarSignature;

	/**
	 * For each event stores the number of different positions of the quantified
	 * variable in the event signatures
	 */
	private int[] numQVarPositions;

	/**
	 * For each event stores an array showing (with a value <code>true</code>)
	 * the possible different positions of the quantified variables in the
	 * signatures
	 */
	private boolean[][] eventsMasks;

	/**
	 * Creates an <code>IncrementalNonSimpleDetGenQEAMonitor</code> to monitor
	 * the specified QEA property
	 * 
	 * @param qea
	 *            QEA property
	 */
	public Incr_QVar1_FVar_Det_QEAMonitor(QVar01_FVar_Det_QEA qea) {
		super(qea);
		bindings = new IdentityHashMap<>();
		emptyBindingConfig = new DetConfig(qea.getInitialState(),
				qea.newBinding());
		buildEventsIndices();
	}

	/**
	 * Initialise the arrays <code>onlyFVarSignature</code>,
	 * <code>numQVarPositions</code> and <code>eventsMasks</code> according to
	 * the events defined in the QEA of this monitor
	 */
	private void buildEventsIndices() {

		int numEvents = qea.getEventsAlphabet().length;
		int[] states = qea.getStates();
		int[] eventsAlphabet = qea.getEventsAlphabet();

		onlyFVarSignature = new boolean[numEvents + 1];
		numQVarPositions = new int[numEvents + 1];
		eventsMasks = new boolean[numEvents + 1][];

		// Iterate over all start states and event names
		for (int state : states) {
			for (int eventName : eventsAlphabet) {

				// Check if there is a transition for the start state and event
				Transition transition = qea.getTransition(state, eventName);
				if (transition != null) {

					boolean onlyFreeVar = true;

					// If needed, initialise array of mask for the event
					if (eventsMasks[eventName] == null) {
						eventsMasks[eventName] = new boolean[transition
								.getVariableNames().length];
					}

					// Iterate over the variables names of this signature
					int[] varNames = transition.getVariableNames();
					for (int k = 0; k < varNames.length; k++) {
						if (varNames[k] < 0) { // Quantified variable
							onlyFreeVar = false;
							if (!eventsMasks[eventName][k]) {
								eventsMasks[eventName][k] = true;
								numQVarPositions[eventName]++;
							}
							break; // Exit the loop
						}
					}

					if (onlyFreeVar) {
						// This signature only contains free variables
						onlyFVarSignature[eventName] = true;
					}
				}
			}
		}
	}

	@Override
	public Verdict step(int eventName, Object[] args) {

		boolean eventProcessedForAllExistingBindings = false;

		if (onlyFVarSignature[eventName]) {

			// Update propositional configuration
			emptyBindingConfig = qea.getNextConfig(emptyBindingConfig,
					eventName, args,null);

			// Apply event to all existing bindings
			for (Object binding : bindings.keySet()) {
				stepNoVerdict(eventName, args, binding);
			}
			eventProcessedForAllExistingBindings = true;
		}

		if (numQVarPositions[eventName] == 1) { // Only one value for the QVar

			Object qVarBinding = getFirstQVarBinding(eventsMasks[eventName],
					args);
			if (!eventProcessedForAllExistingBindings
					|| bindings.get(qVarBinding) == null) {
				stepNoVerdict(eventName, args, qVarBinding);
			}
		} else if (numQVarPositions[eventName] > 1) { // Possibly multiple
														// values for the QVar

			Object[] qVarBindings = getDistinctQVarBindings(
					eventsMasks[eventName], args, numQVarPositions[eventName]);
			for (Object qVarBinding : qVarBindings) {
				if (!eventProcessedForAllExistingBindings
						|| bindings.get(qVarBinding) == null) {
					stepNoVerdict(eventName, args, qVarBinding);
				}
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

		return step(eventName, new Object[]{});
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
		// Object[]) in Incr_QVar1_FVar_Det_FixedQVar_QEAMonitor

		boolean existingBinding = false;
		boolean startConfigFinal = false;
		DetConfig config;

		// Determine if the value received corresponds to an existing binding
		if (bindings.containsKey(qVarValue)) { // Existing quantified
												// variable binding

			// Get current configuration for the binding
			config = bindings.get(qVarValue);

			// Assign flags for counters update
			existingBinding = true;
			startConfigFinal = qea.isStateFinal(config.getState());

		} else { // New quantified variable binding

			// Create new configuration with a copy of the propositional conf.
			config = emptyBindingConfig.copy();
		}

		// Compute next configuration
		config = qea.getNextConfig(config, eventName, args,qVarValue);

		// Flag needed to update counters later
		boolean endConfigFinal = qea.isStateFinal(config.getState());

		// Update/add configuration for the binding
		bindings.put(qVarValue, config);

		// Update counters
		updateCounters(existingBinding, startConfigFinal, endConfigFinal);
	}

	@Override
	public String getStatus() {
		String ret = "Empty: "+emptyBindingConfig+"\n";
		ret += "Map:\n";
		for (IdentityHashMap.Entry<Object, DetConfig> entry : bindings
				.entrySet()) {
			ret += entry.getKey() + "\t->\t" + entry.getValue() + "\n";
		}
		return ret;
	}

}
