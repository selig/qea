package structure.impl;

import monitoring.impl.configs.NonDetConfig;
import structure.intf.Binding;

/**
 * This class represents a Quantified Event Automaton (QEA) with the following
 * characteristics:
 * <ul>
 * <li>There is exactly one quantified variable
 * <li>It can contain any number of free variables
 * <li>The transitions in the function delta consist of a start state, an event
 * and an end state. Optionally, it can be associated to a guard and/or an
 * assignment
 * <li>The QEA is non-deterministic
 * <li>Fixed quantified variable optimisation: The array of parameters of an
 * event always contains the quantified variable in the first position
 * </ul>
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class QVar1_FVar_NonDet_FixedQVar_QEA extends NonSimpleQEA {

	/**
	 * Transition function delta for this QEA. For a given Transition in the
	 * array, the first index corresponds to the start state and the second
	 * index corresponds to the event name. For a given start state and event
	 * name, there may be an array of transitions (non-deterministic)
	 */
	private Transition[][][] delta;

	/**
	 * Creates a <code>QVar01_FVar_NonDet_FixedQVar_QEA</code> for the specified
	 * number of states, number of events, initial state and quantification type
	 * 
	 * @param numStates
	 *            Number of states
	 * @param numEvents
	 *            Number of events
	 * @param initialState
	 *            Initial state
	 * @param quantification
	 *            Quantification type
	 * @param freeVariablesCount
	 *            Number of free variables
	 */
	public QVar1_FVar_NonDet_FixedQVar_QEA(int numStates, int numEvents,
			int initialState, Quantification quantification,
			int freeVariablesCount) {
		super(numStates, initialState, quantification, freeVariablesCount);
		delta = new Transition[numStates + 1][numEvents + 1][];
	}

	/**
	 * Adds a transition to the transition function delta of this QEA
	 * 
	 * @param startState
	 *            Start state for the transition
	 * @param event
	 *            Name of the event
	 * @param transitions
	 *            Object containing the rest of the information of the
	 *            transition: Parameters for the event, guard, assignment and
	 *            end state
	 */
	public void addTransition(int startState, int event, Transition transition) {
		if (delta[startState][event] == null) {
			delta[startState][event] = new Transition[] { transition };
		} else {
			// Resize transitions array
			Transition[] newTransitions = new Transition[delta[startState][event].length + 1];
			System.arraycopy(delta[startState][event], 0, newTransitions, 0,
					delta[startState][event].length);
			newTransitions[delta[startState][event].length] = transition;
			delta[startState][event] = newTransitions;
		}

	}

	/**
	 * Adds a set of transitions to the transition function delta of this QEA
	 * 
	 * @param startState
	 *            Start state for the transition
	 * @param event
	 *            Name of the event
	 * @param transitions
	 *            Array of objects containing the rest of the information of the
	 *            transitions: Parameters for the event, guard, assignment and
	 *            end state
	 */
	public void addTransitions(int startState, int event,
			Transition[] transitions) {
		if (delta[startState][event] == null) {
			delta[startState][event] = transitions;
		} else {
			// Resize transitions array
			int prevTransCount = delta[startState][event].length;
			Transition[] newTransitions = new Transition[prevTransCount
					+ transitions.length];
			System.arraycopy(delta[startState][event], 0, newTransitions, 0,
					delta[startState][event].length);
			System.arraycopy(transitions, 0, newTransitions, prevTransCount,
					transitions.length);
			delta[startState][event] = newTransitions;
		}
	}

	/**
	 * Computes the next configuration for a given start configuration, event
	 * and arguments, according to the transition function delta of this QEA
	 * 
	 * @param config
	 *            Start configuration containing the set of start states and the
	 *            set of bindings
	 * @param event
	 *            Name of the event
	 * @param args
	 *            Arguments for the event
	 * @return End configuration containing the set of end state and the set of
	 *         bindings after the transition
	 */
	public NonDetConfig getNextConfig(NonDetConfig config, int event,
			Object[] args) {

		if (config.getStates().length == 1) { // Only one start state

			Transition[] transitions = delta[config.getStates()[0]][event];

			// If the event is not defined for the unique start state, return
			// the failing state with an empty binding
			if (transitions == null) {
				config.setState(0, 0);
				config.getBindings()[0].setEmpty();
				return config;
			}

			// Check number of arguments vs. number of parameters of the event
			// for the first transition only
			checkArgParamLength(args.length,
					transitions[0].getVariableNames().length);

			if (transitions.length == 1) { // One start state - one transition

				// Update binding for free variables
				Binding binding = config.getBindings()[0];
				Object[] prevBinding = null;

				if (args.length > 1) {
					prevBinding = updateBindingFixedQVar(binding, args,
							transitions[0]);
				}

				// If there is a guard and is not satisfied, rollback the
				// binding and return the failing state
				if (transitions[0].getGuard() != null
						&& !transitions[0].getGuard().check(binding)) {

					config.setState(0, 0); // Failing state
					if (prevBinding != null) {
						rollBackBindingFixedQVar(binding, transitions[0],
								prevBinding);
					}
					return config;
				}

				// If there is an assignment, execute it
				if (transitions[0].getAssignment() != null) {
					config.setBinding(0,
							transitions[0].getAssignment().apply(binding));
				}

				// Set the end state
				config.setState(0, transitions[0].getEndState());

			} else { // One start state - Multiple transitions

				// Create as many states and bindings as there are transitions
				int[] endStates = new int[transitions.length];
				BindingImpl[] bindings = new BindingImpl[transitions.length];

				// Initialise end states count
				int endStatesCount = 0;

				// Iterate over the transitions
				for (Transition transition : transitions) {

					// Copy the binding of the start state
					BindingImpl binding = (BindingImpl) config.getBindings()[0]
							.copy();

					if (args.length > 1) {

						// Update binding for free variables
						updateBindingFixedQVar(binding, args, transition);
					}

					// If there is a guard, check it is satisfied
					if (transition.getGuard() == null
							|| transition.getGuard().check(binding)) {

						// If there is an assignment, execute it
						if (transition.getAssignment() != null) {
							binding = (BindingImpl) transition.getAssignment()
									.apply(binding);
						}

						// Copy end state and binding in the arrays
						endStates[endStatesCount] = transition.getEndState();
						bindings[endStatesCount] = binding;

						// TODO We are assuming here that there's no explicit
						// transition to the failing state
						endStatesCount++;
					}
				}

				if (endStatesCount == 0) { // All end states are failing states

					// Set failing state, leave binding as it is
					config.setState(0, 0);
					return config;
				}

				// Copy the states and bindings into the correct sized array if
				// needed and update the configuration
				if (endStatesCount != transitions.length) {
					int[] reducedEndStates = new int[endStatesCount];
					BindingImpl[] reducedBindings = new BindingImpl[endStatesCount];
					System.arraycopy(endStates, 0, reducedEndStates, 0,
							endStatesCount);
					System.arraycopy(bindings, 0, reducedBindings, 0,
							endStatesCount);
					config.setStates(reducedEndStates);
					config.setBindings(reducedBindings);
				} else {
					config.setStates(endStates);
					config.setBindings(bindings);
				}
			}

		} else { // Multiple start states

			int[] startStates = config.getStates();
			int maxEndStates = 0;
			boolean argsNumberChecked = false;
			Transition[][] transitions = new Transition[startStates.length][];

			// Compute maximum number of end states
			for (int i = 0; i < startStates.length; i++) {

				transitions[i] = delta[startStates[i]][event];
				if (transitions[i] != null) {
					maxEndStates += transitions[i].length;

					// Check number of arguments vs. number of parameters of the
					// event for one transition only
					if (!argsNumberChecked) {
						checkArgParamLength(args.length,
								transitions[i][0].getVariableNames().length);
					}
				}
			}

			// If the event is not defined for any of the current start states,
			// return the failing state with an empty binding
			if (maxEndStates == 0) {
				config.setStates(new int[] { 0 });
				config.setBindings(new Binding[] { newBinding() });
				return config;
			}

			// Create as many states and bindings as there are end states
			int[] endStates = new int[maxEndStates];
			BindingImpl[] bindings = new BindingImpl[maxEndStates];

			// Initialise end states count
			int endStatesCount = 0;

			// Iterate over the transitions
			for (int i = 0; i < transitions.length; i++) {

				if (transitions[i] != null) {
					for (int j = 0; j < transitions[i].length; j++) {

						// Copy the initial binding
						BindingImpl binding = (BindingImpl) config
								.getBindings()[i].copy();

						if (args.length > 1) {

							// Update binding for free variables
							updateBindingFixedQVar(binding, args,
									transitions[i][j]);
						}

						// If there is a guard, check it is satisfied
						if (transitions[i][j].getGuard() == null
								|| transitions[i][j].getGuard().check(binding)) {

							// If there is an assignment, execute it
							if (transitions[i][j].getAssignment() != null) {
								binding = (BindingImpl) transitions[i][j]
										.getAssignment().apply(binding);
							}

							// Copy end state and binding in the arrays
							endStates[endStatesCount] = transitions[i][j]
									.getEndState();
							bindings[endStatesCount] = binding;

							// TODO We are assuming here that there's no
							// explicit transition to the failing state
							endStatesCount++;
						}
					}
				}
			}

			if (endStatesCount == 0) { // All end states are failing states
				config.setStates(new int[] { 0 });
				// TODO What happens with the binding here? What binding should
				// we rollback to if there are multiple (one for each start
				// state)?
				config.setBindings(new Binding[] { newBinding() });
				return config;
			}

			// Copy the states and bindings into the correct sized array if
			// needed and update the configuration
			if (endStatesCount != maxEndStates) {
				int[] reducedEndStates = new int[endStatesCount];
				BindingImpl[] reducedBindings = new BindingImpl[endStatesCount];
				System.arraycopy(endStates, 0, reducedEndStates, 0,
						endStatesCount);
				System.arraycopy(bindings, 0, reducedBindings, 0,
						endStatesCount);
				config.setStates(reducedEndStates);
				config.setBindings(reducedBindings);
			} else {
				config.setStates(endStates);
				config.setBindings(bindings);
			}
		}

		return config;
	}

	/**
	 * Determines if the set of states in the specified configuration contains
	 * at least one final state
	 * 
	 * @param config
	 *            Configuration encapsulating the set of states to be checked
	 * @return <code>true</code> if the set of states in the specified
	 *         configuration contains at least one final state;
	 *         <code>false</code> otherwise
	 */
	public boolean containsFinalState(NonDetConfig config) {
		for (int i = 0; i < config.getStates().length; i++) {
			if (isStateFinal(config.getStates()[i])) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int[] getEventsAlphabet() {
		int[] a = new int[delta[0].length - 1];
		for (int i = 0; i < a.length; i++) {
			a[i] = i + 1;
		}
		return a;
	}

	@Override
	public boolean isDeterministic() {
		return false;
	}

}
