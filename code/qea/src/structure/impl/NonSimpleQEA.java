package structure.impl;

import structure.intf.Binding;
import structure.intf.QEA;
import exceptions.ShouldNotHappenException;

/**
 * A QEA with at most one quantified variable
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public abstract class NonSimpleQEA implements QEA { // TODO Check name

	protected int[] finalStates; // TODO Can we use a boolean array of here?

	protected final int initialState;

	protected final boolean isPropositional;

	protected final boolean quantificationUniversal;

	protected final int freeVariablesCount;

	public NonSimpleQEA(int numStates, int initialState,
			Quantification quantification, int freeVariablesCount) {
		finalStates = new int[numStates + 1];
		this.initialState = initialState;
		this.freeVariablesCount = freeVariablesCount;
		switch (quantification) {
		case FORALL:
			this.quantificationUniversal = true;
			this.isPropositional = false;
			break;
		case EXISTS:
			this.quantificationUniversal = false;
			this.isPropositional = false;
			break;
		case NONE:
			this.quantificationUniversal = false;
			this.isPropositional = true;
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
		for (int state : states)
			finalStates[state] = 1;
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
	 * Creates a new binding of free variable for the current QEA
	 * 
	 * @return Binding
	 */
	public Binding newBinding() {
		return new BindingImpl(freeVariablesCount);
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
		if (isPropositional)
			return new int[] {};
		if (quantificationUniversal)
			return new int[] { -1 };
		return new int[] { 1 };
	}

	@Override
	public boolean usesFreeVariables() {
		return true;
	}

	@Override
	public boolean isStateFinal(int state) {
		if (finalStates[state] == 1) {
			return true;
		}
		return false;
	}

	/**
	 * @return <code>true</code> if the quantification for the variable of this
	 *         QEA is universal; <code>false</code> if it is existential
	 */
	public boolean isQuantificationUniversal() {
		return quantificationUniversal;
	}

}
