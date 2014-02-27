package structure.impl;

import monitoring.impl.configs.NonDetConfig;
import structure.intf.Binding;
import structure.intf.Transition;

/**
 * This class represents a Quantified Event Automaton (QEA) with the following
 * characteristics:
 * <ul>
 * <li>There is one quantified variable
 * <li>It can contain any number of free variables
 * <li>The transitions in the function delta consist of a start state, an event
 * and an end state. Optionally, it can be associated to a guard and/or an
 * assignment
 * <li>The QEA is non-deterministic
 * </ul>
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class NonSimpleNonDetQEA extends NonSimpleQEA {

	/**
	 * Transition function delta for this QEA. For a given Transition in the
	 * array, the first index corresponds to the start state and the second
	 * index corresponds to the event name. For a given start state and event
	 * name, there may be an array of transitions (non-deterministic)
	 */
	private Transition[][][] delta;

	/**
	 * Creates a NonSimpleNonDetQEA for the specified number of states, number
	 * of events, initial state and quantification type
	 * 
	 * @param numStates
	 *            Number of states
	 * @param numEvents
	 *            Number of events
	 * @param initialState
	 *            Initial state
	 * @param quantification
	 *            Quantification type
	 */
	public NonSimpleNonDetQEA(int numStates, int numEvents, int initialState,
			Quantification quantification, int freeVariablesCount) {
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
		delta[startState][event] = new Transition[] { transition };
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
		delta[startState][event] = transitions;
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

			// TODO Remove cast
			TransitionImpl[] transitions = (TransitionImpl[]) delta[config
					.getStates()[0]][event];

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
				Object[] prevBinding = updateBinding(binding, args,
						transitions[0]);

				// If there is a guard and is not satisfied, rollback the
				// binding and return the failing state
				if (transitions[0].getGuard() != null
						&& !transitions[0].getGuard().check(binding)) {

					config.setState(0, 0); // Failing state
					rollBackBinding(binding, transitions[0], prevBinding);

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
				int endStatesCount = transitions.length;

				// Iterate over transitions
				for (int i = 0; i < transitions.length; i++) {

					// Copy the initial binding // TODO Check cast
					bindings[i] = (BindingImpl) config.getBindings()[0].copy();

					// Update binding for free variables
					updateBinding(bindings[i], args, transitions[i]);

					// If there is a guard and is not satisfied, rollback the
					// binding and set the failing state
					if (transitions[i].getGuard() != null
							&& !transitions[i].getGuard().check(bindings[i])) {

						endStates[i] = 0;
						endStatesCount--;

					} else {

						// If there is an assignment, execute it
						if (transitions[i].getAssignment() != null) {
							// TODO Check cast
							bindings[i] = (BindingImpl) transitions[i]
									.getAssignment().apply(bindings[i]);
						}

						// Set the end state
						endStates[i] = transitions[i].getEndState();
						if (endStates[i] == 0) { // TODO Can we remove this
													// check?
							endStatesCount--;
						}
					}
				}

				if (endStatesCount == 0) { // All end states are failing states

					// Set failing state, leave binding as it is
					config.setState(0, 0);

				} else { // At least one non-failing end state

					if (endStatesCount > 1) {
						// Resize configuration
						config.add(endStatesCount - 1);
					}
					int i = 0;
					for (int j = 0; j < endStates.length; j++) {
						if (endStates[j] != 0) {
							config.setState(i, endStates[j]);
							config.setBinding(i, bindings[j]);
							i++;
						}
					}
				}
			}

		} else { // Multiple start states

			// TODO Implement!

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
