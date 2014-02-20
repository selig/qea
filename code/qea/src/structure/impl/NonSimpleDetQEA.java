package structure.impl;

import monitoring.impl.configs.DetConfig;
import structure.intf.Binding;
import structure.intf.Transition;
import exceptions.ShouldNotHappenException;

/**
 * This class represents a Quantified Event Automaton (QEA) with the following
 * characteristics:
 * <ul>
 * <li>There is one quantified variable
 * <li>It can contain any number of free variables
 * <li>The transitions in the function delta consist of a start state, an event
 * and an end state. Optionally, it can be associated to a guard and/or an
 * assignment
 * <li>The QEA is deterministic
 * </ul>
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class NonSimpleDetQEA extends NonSimpleQEA {

	/**
	 * Transition function delta for this QEA. For a given Transition in the
	 * array, the first index corresponds to the start state and the second
	 * index corresponds to the event name
	 */
	private Transition[][] delta;

	/**
	 * Creates a NonSimpleDetQEA for the specified number of states, number of
	 * events, initial state and quantification type
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
	public NonSimpleDetQEA(int numStates, int numEvents, int initialState,
			Quantification quantification) {
		super(numStates, initialState, quantification);
		delta = new Transition[numStates + 1][numEvents + 1];
	}

	/**
	 * Adds a transition for the transition function delta of this QEA
	 * 
	 * @param startState
	 *            Start state for this transition
	 * @param event
	 *            Name of the event
	 * @param transition
	 *            Object containing the rest of the information for this
	 *            transition: Parameters for the event, guard, assignment and
	 *            end state
	 */
	public void addTransition(int startState, int event, Transition transition) {
		delta[startState][event] = transition;
	}

	/**
	 * Computes the next configuration for a given start configuration, event
	 * and arguments, according to the transition function delta of this QEA
	 * 
	 * @param config
	 *            Start configuration containing the start state and binding
	 *            (values of free variables)
	 * @param event
	 *            Name of the event
	 * @param args
	 *            Arguments for the event
	 * @return End configuration containing the end state and binding (values of
	 *         free variables) after the transition
	 */
	public DetConfig getNextConfig(DetConfig config, int event, Object[] args) {

		// TODO Remove cast
		TransitionImpl transition = (TransitionImpl) delta[config.getState()][event];

		// If the event is not defined for the current start state, return the
		// failing state
		if (transition == null) {
			config.setState(0); // Failing state
			// TODO Is it OK to leave the binding as it is here?
			return config;
		}

		// Check the number of arguments is equal to the number of variables of
		// the event
		if (args.length != transition.getVariableNames().length) {
			throw new ShouldNotHappenException(
					"The number of variables defined for this event doesn't match the number of arguments");
		}

		// Update binding with the values of the free variables
		Binding binding = config.getBinding();
		for (int i = 0; i < args.length; i++) {

			// Set new value for the free variable
			if (transition.getVariableNames()[i] >= 0) {
				binding.setValue(transition.getVariableNames()[i], args[i]);
			}
		}

		// If the guard is not satisfied, return the failing state
		if (!transition.getGuard().check(config.getBinding())) {
			config.setState(0); // Failing state
			// TODO Is it OK to leave the binding as it is here?
			return config;
		}

		// Execute the assignment
		config.setBinding(transition.getAssignment().apply(binding));

		return config;

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
		return true;
	}

}
