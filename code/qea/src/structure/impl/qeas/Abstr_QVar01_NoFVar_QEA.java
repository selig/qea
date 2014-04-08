package structure.impl.qeas;

import structure.impl.other.Quantification;
import structure.intf.QEA;
import exceptions.ShouldNotHappenException;

public abstract class Abstr_QVar01_NoFVar_QEA extends QEA {

	protected boolean[] finalStates;

	protected boolean[] strongStates;

	protected final int initialState;

	protected final boolean isPropositional;

	protected final boolean quantificationUniversal;
	protected final boolean negatedQuantification;	

	public Abstr_QVar01_NoFVar_QEA(int numStates, int initialState,
			Quantification quantification) {
		finalStates = new boolean[numStates + 1];
		strongStates = new boolean[numStates + 1];
		this.initialState = initialState;
		switch (quantification) {
		case FORALL:
			quantificationUniversal = true;
			negatedQuantification = false;
			isPropositional = false;
			break;
		case NOT_FORALL:
			quantificationUniversal = true;
			negatedQuantification = false;
			isPropositional = false;
			break;		
		case EXISTS:
			quantificationUniversal = false;
			isPropositional = false;
			negatedQuantification = false;			
			break;
		case NOT_EXISTS:
			quantificationUniversal = false;
			isPropositional = false;
			negatedQuantification = true;			
			break;			
		case NONE:
			quantificationUniversal = false;
			isPropositional = true;
			negatedQuantification = false;	
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
		finalStates[state] = true;
	}

	/**
	 * Adds the specified states to the set of final states
	 * 
	 * @param states
	 *            Names of states to add
	 */
	public void setStatesAsFinal(int... states) {
		for (int state : states) {
			finalStates[state] = true;
		}
	}

	/**
	 * Adds the specified state to the set of strong states
	 * 
	 * @param state
	 *            State name
	 */
	public void setStateAsStrong(int state) {
		strongStates[state] = true;
	}

	/**
	 * Adds the specified states to the set of strong states
	 * 
	 * @param states
	 *            Names of states to add
	 */
	public void setStatesAsStrong(int... states) {
		for (int state : states) {
			strongStates[state] = true;
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
		return finalStates[state];
	}

	/**
	 * Determines if the specified state is in the set of strong states
	 * 
	 * @param state
	 * @return true if the specified state is a strong state. Otherwise, false
	 */
	@Override
	public boolean isStateStrong(int state) {
		return strongStates[state];
	}

	/**
	 * @return true if the quantification for the variable of this QEA is
	 *         universal. false if it is existential
	 */
	public boolean isQuantificationUniversal() {
		return quantificationUniversal;
	}
	/**
	 * @return <code>true</code> if the quantification for the variable of this
	 *         QEA is negated; <code>false</code> if not
	 */
	public boolean isQuantificationNegated() {
		return negatedQuantification;
	}	
	
}
