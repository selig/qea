package structure.impl;

import structure.intf.QEA;

/**
 * This class represents a simple Quantified Event Automaton (QEA) with the
 * following characteristics:
 * <ul>
 * <li>There is at most one quantified variable
 * <li>The transitions in the function delta consist of a start state, an event
 * and an end state, no guards or assigns are considered
 * <li>The QEA can is non-deterministic
 * </ul>
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class SimpleNonDeterministicQEA implements QEA {

	private int[] finalStates;

	private int[][][] delta;

	protected final int initialState;

	private boolean quantificationUniversal;

	public SimpleNonDeterministicQEA(int numStates, int numEvents,
			int initialState, boolean quantificationUniversal) {
		delta = new int[numStates + 1][numEvents + 1][numStates + 1];
		finalStates = new int[numStates + 1];
		this.initialState = initialState;
		this.quantificationUniversal = quantificationUniversal;
	}

	/**
	 * Adds a transition for the transition function delta of this QEA
	 * 
	 * @param startState
	 *            Start state for this transition
	 * @param event
	 *            Name of the event
	 * @param endStates
	 *            Array of end states for this transition. The size of the array
	 *            is the number of states for this QEA + 1. The array can
	 *            contain values 0 and 1; 1 indicates that the current position
	 *            of the array is an end state for this transition, while 0
	 *            indicates it's not
	 */
	public void addTransition(int startState, int event, int[] endStates) {
		System.arraycopy(endStates, 0, delta[startState][event], 0,
				endStates.length);
	}

	/**
	 * Retrieves an array of end states for a given start state and an event,
	 * according to the transition function delta of this QEA
	 * 
	 * @param previousState
	 *            Start state
	 * @param event
	 *            Name of the event
	 * @return Array of end states for this transition. The size of the array is
	 *         the number of states for this QEA + 1. The array can contain
	 *         values 0 and 1; 1 indicates that the current position of the
	 *         array is an end state for this transition, while 0 indicates it's
	 *         not
	 */
	public int[] getNextStates(int previousState, int event) {
		return delta[previousState][event];
	}

	/**
	 * Adds the specified state to the set of final states
	 * 
	 * @param state
	 *            State name
	 */
	public void setStateAsFinal(int state) {
		finalStates[state] = 1;
	}

	/**
	 * Adds the specified states to the set of final statess
	 * 
	 * @param states
	 *            Names of states to add
	 */
	public void setStatesAsFinal(int... states) {
		for (int state : states)
			finalStates[state] = 1;
	}

	@Override
	public int[] getStates() {
		int[] q = new int[delta.length];
		for (int i = 0; i < q.length; i++) {
			q[i] = i + 1;
		}
		return q;
	}

	@Override
	public int[] getEventsAlphabet() {
		int[] a = new int[delta[0].length];
		for (int i = 0; i < a.length; i++) {
			a[i] = i + 1;
		}
		return a;
	}

	@Override
	public int[] getLambda() {
		if (quantificationUniversal) {
			return new int[] { 1 };
		}
		return new int[] { -1 };
	}

	@Override
	public boolean isDeterministic() {
		return false;
	}

	@Override
	public boolean usesFreeVariables() {
		return false;
	}

	@Override
	public boolean isStateFinal(int state) {
		if (finalStates[state] == 1) {
			return true;
		}
		return false;
	}

	public boolean isQuantificationUniversal() {
		return quantificationUniversal;
	}

}
