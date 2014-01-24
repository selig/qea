package structure.impl;

import structure.intf.QEA;

/**
 * This class represents a simple Quantified Event Automaton (QEA) with the
 * following restrictions:
 * 
 * - There is only one variable and its quantification is all universal
 * 
 * - State and event names are represented by integers starting in 1
 * 
 * - The transitions in the function delta consist of a start state, an event
 * and an end state, no guards or assigns are considered
 * 
 * @author Helena Cuenca
 */
public class SimplestQEA implements QEA {

	private int[] finalStates;

	private int[][] delta;

	private int initialState;

	public SimplestQEA(int numStates, int numEvents, int initialState) {
		delta = new int[numStates + 1][numEvents + 1];
		finalStates = new int[numStates + 1];
		this.initialState = initialState;
	}

	/**
	 * Adds a transition for the transition function delta of this QEA
	 * 
	 * @param startState
	 *            Start state for this transition
	 * @param event
	 *            Name of the event
	 * @param endState
	 *            End state for this transition
	 */
	public void addTransition(int startState, int event, int endState) {
		delta[startState][event] = endState;
	}

	/**
	 * Retrieves the end state for a given start state and an event, according
	 * to the transition function delta of this QEA
	 * 
	 * @param previousState
	 *            Start state
	 * @param event
	 *            Name of the event
	 * @return End state name, or 0 if the transition is not defined in the
	 *         function delta
	 */
	public int getNextState(int previousState, int event) {
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
	 * Retrieves the set of states Q for this QEA
	 * 
	 * @return Array of integers with the names of the states
	 */
	public int[] getStates() {
		int[] q = new int[delta.length];
		for (int i = 0; i < q.length; i++) {
			q[i] = i + 1;
		}
		return q;
	}

	/**
	 * Retrieves the alphabet of events A for this QEA
	 * 
	 * @return Array of integers with the names of the events
	 */
	public int[] getEventsAlphabet() {
		int[] a = new int[delta[0].length];
		for (int i = 0; i < a.length; i++) {
			a[i] = i + 1;
		}
		return a;
	}
}
