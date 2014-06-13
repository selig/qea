package structure.impl.qeas;

import monitoring.impl.configs.DetConfig;
import structure.impl.other.Quantification;
import structure.impl.other.Transition;
import structure.intf.Binding;
import structure.intf.Guard;
import structure.intf.QEA_det_free;

/**
 * This class represents a Quantified Event Automaton (QEA) with the following
 * characteristics:
 * <ul>
 * <li>There is at most one quantified variable
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
public class QVar01_FVar_Det_QEA extends Abstr_QVar01_FVar_QEA implements
		QEA_det_free {

	private final QEAType qeaType = QEAType.QVAR01_FVAR_DET_QEA;

	/**
	 * Transition function delta for this QEA. For a given Transition in the
	 * array, the first index corresponds to the start state and the second
	 * index corresponds to the event name
	 */
	private Transition[][] delta;

	/**
	 * Creates a <code>QVar01_FVar_Det_QEA</code> for the specified number of
	 * states, number of events, initial state, quantification type and number
	 * of free variables
	 * 
	 * @param numStates
	 *            Number of states. The states are named: 1, 2,...
	 *            <code>numStates</code>
	 * @param numEvents
	 *            Number of events. The events are named: 1, 2,...
	 *            <code>numEvents</code>
	 * @param initialState
	 *            Initial state
	 * @param quantification
	 *            Quantification type
	 * @param freeVariablesCount
	 *            Number of free variables
	 */
	public QVar01_FVar_Det_QEA(int numStates, int numEvents, int initialState,
			Quantification quantification, int freeVariablesCount) {
		super(numStates, initialState, quantification, freeVariablesCount);
		delta = new Transition[numStates + 1][numEvents + 1];
	}

	/**
	 * Adds/replace a transition for the transition function delta of this QEA
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
	public DetConfig getNextConfig(DetConfig config, int event, Object[] args,
			Object qVarValue, boolean isQVarValue) {

		// TODO This method is very similar to getNextConfig in
		// QVar01_FVar_Det_FixedQVar_QEA

		Transition transition = delta[config.getState()][event];

		// If the event is not defined for the current start state, return the
		// failing state with an empty binding
		if (transition == null) {
			if (!skipStates[config.getState()]) {
				config.setState(0);
				config.getBinding().setEmpty();
			}
			return config;
		}

		// Check number of arguments vs. number of parameters of the event
		checkArgParamLength(args.length, transition.getVariableNames().length);

		if (isQVarValue && !qVarMatchesBinding(qVarValue, args, transition)) {
			// We did not actually match
			return config;
		}

		Binding binding = config.getBinding();

		// Update binding for free variables
		updateBinding(binding, args, transition);

		// If there is a guard and is not satisfied, rollback the binding and
		// return the failing state (if not skip)
		if (transition.getGuard() != null) {
			Guard guard = transition.getGuard();
			if (isQVarValue && !guard.check(binding, -1, qVarValue)
					|| !isQVarValue && !guard.check(binding)) {

				if (!skipStates[config.getState()])
					config.setState(0); // Failing state
				return config;
			}
		}

		// If there is an assignment, execute it
		if (transition.getAssignment() != null) {
			config.setBinding(transition.getAssignment().apply(binding, false));
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

	/**
	 * Returns the transition for the specified start state and event name,
	 * according to the transition function delta of this QEA
	 * 
	 * @param startState
	 *            Start state
	 * @param eventName
	 *            Event name
	 * @return Transition defined for the specified state and event name or
	 *         <code>null</code> if no transition is defined
	 */
	public Transition getTransition(int startState, int eventName) {
		return delta[startState][eventName];
	}

	@Override
	public QEAType getQEAType() {
		return qeaType;
	}

	@Override
	public Transition[][] getDelta() {
		return delta;
	}

}
