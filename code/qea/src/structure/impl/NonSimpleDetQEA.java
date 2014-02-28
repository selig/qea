package structure.impl;

import monitoring.impl.configs.DetConfig;
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
			Quantification quantification, int freeVariablesCount) {
		super(numStates, initialState, quantification, freeVariablesCount);
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
		// failing state with an empty binding
		if (transition == null) {
			config.setState(0);
			config.getBinding().setEmpty();
			return config;
		}

		// Check number of arguments vs. number of parameters of the event
		checkArgParamLength(args.length, transition.getVariableNames().length);

		Binding binding = config.getBinding();
		Object[] prevBinding = null;

		if (args.length > 1) {

			// Update binding for free variables
			prevBinding = updateBinding(config.getBinding(), args, transition);
		}

		// If there is a guard and is not satisfied, rollback the binding and
		// return the failing state
		if (transition.getGuard() != null
				&& !transition.getGuard().check(binding)) {

			config.setState(0); // Failing state
			if (prevBinding != null) {
				rollBackBinding(binding, transition, prevBinding);
			}
			return config;
		}

		// If there is an assignment, execute it
		if (transition.getAssignment() != null) {
			config.setBinding(transition.getAssignment().apply(binding));
		}

		// Set the end state
		config.setState(transition.getEndState());

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
