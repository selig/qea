package structure.impl;

import structure.intf.QEA;
import exceptions.ShouldNotHappenException;

/**
 * This class represents a simple Quantified Event Automaton (QEA) with the
 * following characteristics:
 * <ul>
 * <li>There is at most one quantified variable
 * <li>The transitions in the function delta consist of a start state, an event
 * and an end state, no guards or assigns are considered
 * <li>The QEA is deterministic
 * </ul>
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class SimplestQEA implements QEA {

	private int[] finalStates;

	private int[][] delta;

	protected final int initialState;

	private final boolean isPropositional;
	private final boolean quantificationUniversal;

	public SimplestQEA(int numStates, int numEvents, int initialState,
			Quantification quantification) {
		delta = new int[numStates + 1][numEvents + 1];
		finalStates = new int[numStates + 1];
		this.initialState = initialState;
		switch(quantification){
			case FORALL : this.quantificationUniversal =true; this.isPropositional=false; break;
			case EXISTS : this.quantificationUniversal =false; this.isPropositional=false; break;
			case NONE : this.quantificationUniversal =false; this.isPropositional=true; break;
			default : throw new ShouldNotHappenException("Unknown quantification "+quantification);
		}
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
	 * Adds the specified states to the set of final statess
	 * 
	 * @param states
	 *            Names of states to add
	 */
	public void setStatesAsFinal(int... states) {
		for (int state : states)
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

	/**
	 * Returns the quantification list in order. Variables are positive if
	 * universally quantified and negative if existentially quantifed.
	 * 
	 * @return Returns Lambda
	 */
	public int[] getLambda() {
		if(isPropositional) return new int[]{};
		if (quantificationUniversal) return new int[] { 1 };
		return new int[] { -1 };
	}

	/**
	 * Checks if the QEA is deterministic
	 * 
	 * @return True if the QEA is deterministic, false otherwise
	 */
	public boolean isDeterministic() {
		return true;
	}

	/**
	 * Checks if the QEA uses free variables
	 * 
	 * @return True if the QEA is uses free variables, false otherwise
	 */
	public boolean usesFreeVariables() {
		return false;
	}

	/**
	 * Determines if the specified state is in the set of final states
	 * 
	 * @param state
	 * @return true if the specified state is a final state. Otherwise, false
	 */
	public boolean isStateFinal(int state) {
		if (finalStates[state] == 1) {
			return true;
		}
		return false;
	}

	/**
	 * @return true if the quantification for the variable of this QEA is
	 *         universal. false if it is existential
	 */
	public boolean isQuantificationUniversal() {
		return quantificationUniversal;
	}

}
