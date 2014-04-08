package structure.impl.qeas;

import structure.impl.other.FBindingImpl;
import structure.impl.other.Quantification;
import structure.impl.other.Transition;
import structure.intf.Binding;
import structure.intf.QEA;
import exceptions.ShouldNotHappenException;

/**
 * A QEA with at most one quantified variable
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public abstract class Abstr_QVar01_FVar_QEA extends QEA {

	protected boolean[] finalStates;

	protected boolean[] strongStates;

	protected final int initialState;

	protected final boolean isPropositional;

	protected final boolean quantificationUniversal;
	protected final boolean negatedQuantification;

	protected final int freeVariablesCount;

	public Abstr_QVar01_FVar_QEA(int numStates, int initialState,
			Quantification quantification, int freeVariablesCount) {
		finalStates = new boolean[numStates + 1];
		strongStates = new boolean[numStates + 1];
		this.initialState = initialState;
		this.freeVariablesCount = freeVariablesCount;
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
	 * Adds the specified states to the set of final statess
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
	 * Creates a new binding of free variable for the current QEA
	 * 
	 * @return Binding
	 */
	public Binding newBinding() {
		return new FBindingImpl(freeVariablesCount);
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
		return true;
	}

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
	 * @return <code>true</code> if the quantification for the variable of this
	 *         QEA is universal; <code>false</code> if it is existential
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

	/**
	 * Checks if the specified numbers are equal. In case they are not, throws a
	 * <code>ShouldNotHappenException</code> exception
	 * 
	 * @param argsSize
	 *            Length of the arguments
	 * @param paramSize
	 *            Length of the parameters
	 */
	protected void checkArgParamLength(int argsLength, int paramLength) {
		if (argsLength != paramLength) {
			throw new ShouldNotHappenException(
					"The number of variables defined for this event doesn't match the number of arguments");
		}
	}

	/**
	 * Updates the specified free variables binding with the specified
	 * arguments. Only the arguments that correspond to free variables,
	 * according to the variable names in the specified transition will be taken
	 * into account
	 * 
	 * @param binding
	 *            Binding to be updated
	 * @param args
	 *            Array of arguments
	 * @param transition
	 *            Transition containing the variable names to be matched with
	 *            the arguments
	 */
	protected void updateBinding(Binding binding, Object[] args,
			Transition transition) {

		for (int i = 0; i < args.length; i++) {

			// Update only free variables
			int varName = transition.getVariableNames()[i];
			if (varName >= 0) {
				binding.setValue(varName, args[i]);
			}
		}
	}

	/**
	 * Updates the specified free variables binding with the specified
	 * arguments. This method should be used for the FixedQVar optimisation;
	 * therefore, all arguments starting in the second position are considered
	 * to be free variables and will be updated
	 * 
	 * @param binding
	 *            Binding to be updated
	 * @param args
	 *            Array of arguments
	 * @param transition
	 *            Transition containing the variable names to be matched with
	 *            the arguments
	 */
	protected void updateBindingFixedQVar(Binding binding, Object[] args,
			Transition transition) {

		// Starting in the second position, all parameters are free variables
		for (int i = 1; i < args.length; i++) {
			int varName = transition.getVariableNames()[i];
			binding.setValue(varName, args[i]);
		}
	}

	/**
	 * Updates the specified free variables binding with the specified arguments
	 * and returns an array with the values in the binding that were replaced.
	 * Only the arguments that correspond to free variables, according to the
	 * variable names in the specified transition will be taken into account
	 * 
	 * @param binding
	 *            Binding to be updated
	 * @param args
	 *            Array of arguments
	 * @param transition
	 *            Transition containing the variable names to be matched with
	 *            the arguments
	 * @return Array with the values in the binding that were replaced (free
	 *         variables only). The size of the array is equal to the size of
	 *         <code>args</code>. The order of the values returned corresponds
	 *         to the order of the variables defined in the transition. If there
	 *         is a quantified variable defined in the transition, a
	 *         <code>null</code> value is returned in the corresponding position
	 */
	protected Object[] updateBindingRB(Binding binding, Object[] args,
			Transition transition) {

		Object[] prevBinding = new Object[args.length];

		for (int i = 0; i < args.length; i++) {

			int varName = transition.getVariableNames()[i];

			// Check the variable is free
			if (varName >= 0) {

				// Save previous value
				prevBinding[i] = binding.getValue(varName);

				// Set new value
				binding.setValue(varName, args[i]);
			}
		}

		return prevBinding;
	}

	/**
	 * Updates the specified free variables binding with the specified arguments
	 * and returns an array with the values in the binding that were replaced.
	 * Only the arguments that correspond to free variables, according to the
	 * variable names in the specified transition will be taken into account.
	 * This method should be used for the FixedQVar optimisation.
	 * 
	 * @param binding
	 *            Binding to be updated
	 * @param args
	 *            Array of arguments. The first position contains the value for
	 *            the quantified variable, while the values starting from the
	 *            second position correspond to free variables
	 * @param transition
	 *            Transition containing the variable names to be matched with
	 *            the arguments
	 * @return Array with the values in the binding that were replaced (free
	 *         variables only). The size of the array is equal to the size of
	 *         <code>args - 1</code>. The order of the values returned
	 *         corresponds to the order of the free variables defined in the
	 *         transition
	 */
	protected Object[] updateBindingFixedQVarRB(Binding binding, Object[] args,
			Transition transition) {

		Object[] prevBinding = new Object[args.length - 1];

		// Starting in the second position, all parameters are free variables
		for (int i = 1; i < args.length; i++) {

			int varName = transition.getVariableNames()[i];

			// Save previous value
			prevBinding[i - 1] = binding.getValue(varName);

			// Set new value for the free variable
			binding.setValue(varName, args[i]);
		}

		return prevBinding;
	}

	/**
	 * Updates the specified binding with the values in the array
	 * <code>prevBinding</code>, according to the variable names in the
	 * specified transition. This method should be used after a call to
	 * <code>updateBinding</code>
	 * 
	 * @param binding
	 *            Binding to be returned to its original values
	 * @param transition
	 *            Transition containing the variable names to be matched with
	 *            the previousBinding
	 * @param prevBinding
	 *            Array containing the values in the binding that were replaced
	 *            (only free variables)
	 */
	protected void rollBackBinding(Binding binding, Transition transition,
			Object[] prevBinding) {

		for (int i = 0; i < prevBinding.length; i++) {
			if (transition.getVariableNames()[i] >= 0) {
				binding.setValue(transition.getVariableNames()[i],
						prevBinding[i]);
			}
		}
	}

	/**
	 * Updates the specified binding with the values in the array
	 * <code>prevBinding</code>, according to the variable names in the
	 * specified transition. This method should be used after a call to
	 * <code>updateBindingFixedQVar</code>
	 * 
	 * @param binding
	 *            Binding to be returned to its original values
	 * @param transition
	 *            Transition containing the variable names to be matched with
	 *            the previousBinding
	 * @param prevBinding
	 *            Array containing the values in the binding that were replaced
	 *            (only free variables)
	 */
	protected void rollBackBindingFixedQVar(Binding binding,
			Transition transition, Object[] prevBinding) {

		for (int i = 0; i < prevBinding.length; i++) {
			binding.setValue(transition.getVariableNames()[i + 1],
					prevBinding[i]);
		}
	}
}
