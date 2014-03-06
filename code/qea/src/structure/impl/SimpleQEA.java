package structure.impl;

import structure.intf.QEA;
import exceptions.ShouldNotHappenException;

public abstract class SimpleQEA implements QEA {

	protected int[] finalStates; // TODO Can we use a boolean array of here?

	protected final int initialState;

	protected final boolean isPropositional;

	protected final boolean quantificationUniversal;

	public SimpleQEA(int numStates, int initialState,
			Quantification quantification) {
		finalStates = new int[numStates + 1];
		this.initialState = initialState;
		switch (quantification) {
		case FORALL:
			quantificationUniversal = true;
			isPropositional = false;
			break;
		case EXISTS:
			quantificationUniversal = false;
			isPropositional = false;
			break;
		case NONE:
			quantificationUniversal = false;
			isPropositional = true;
			break;
		default:
			throw new ShouldNotHappenException("Unknown quantification "
					+ quantification);
		}
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
		for (int state : states) {
			finalStates[state] = 1;
		}
	}

	@Override
	public int[] getStates() {
		int[] q = new int[finalStates.length - 1];
		for (int i = 0; i < q.length; i++) {
			q[i] = i + 1;
		}
		return q;
	}

	/**
	 * Returns the initial state for this QEA
	 * 
	 * @return Initial state
	 */
	public int getInitialState() {
		return initialState;
	}

	@Override
	public int[] getLambda() {
		if (isPropositional) {
			return new int[] {};
		}
		if (quantificationUniversal) {
			return new int[] { -1 };
		}
		return new int[] { 1 };
	}

	@Override
	public boolean usesFreeVariables() {
		return false;
	}

	/**
	 * Determines if the specified state is in the set of final states
	 * 
	 * @param state
	 * @return true if the specified state is a final state. Otherwise, false
	 */
	@Override
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
