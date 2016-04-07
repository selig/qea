package qea.structure.intf;

import qea.exceptions.BindingException;

public abstract class Binding {

	/**
	 * Returns the value of the variable with the specified name
	 * 
	 * @param variableName
	 *            Variable name
	 * @return Value of the variable
	 */
	public abstract Object getValue(int variableName);

	/**
	 * Sets the value of the variable with the specified name
	 * 
	 * @param variableName
	 *            Variable name
	 * @param value
	 *            Value of the variable
	 */
	public abstract void setValue(int variableName, Object value);

	public abstract Binding copy();

	/**
	 * Removes the values for the current binding, leaving it as an empty
	 * binding
	 */
	public abstract void setEmpty();

	/**
	 * Helper method for getting the value of a variable and throwing an
	 * exception if the value is not there
	 * 
	 * @param var
	 *            Name of the variable
	 * @return Value of var in binding
	 */
	public Object getForced(int var) {
		Object val = getValue(var);
		if (val == null) {
			throw new BindingException("Variable x_" + var + " expected in "
					+ this);
		}
		return val;
	}

	/**
	 * Helper method for getting the value of a variable as an integer and
	 * throwing an exception if the value is not there or is not an integer
	 * 
	 * @param var
	 *            Name of the variable
	 * @return the value of var in binding
	 */
	public Integer getForcedAsInteger(int var) {
		Object val = getValue(var);
		if (val == null) {
			throw new BindingException("Variable x_" + var + " expected in "
					+ this);
		}
		if (!(val instanceof Integer)) {
			throw new BindingException("Variable x_" + var
					+ " expected as integer in " + this);
		}

		return (Integer) val;
	}
	public Double getForcedAsDouble(int var) {
		Object val = getValue(var);
		if (val == null) {
			throw new BindingException("Variable x_" + var + " expected in "
					+ this);
		}
		if (!(val instanceof Double)) {
			throw new BindingException("Variable x_" + var
					+ " expected as integer in " + this);
		}

		return (Double) val;
	}	

	/**
	 * Helper method to perform matching update In the free variable case we
	 * update the binding for all free variables in variableNames with their
	 * corresponding value in args
	 * 
	 * In the quantified variable case we will do the same (for quantified
	 * variables) if a value does not already exist - if it does and the values
	 * clash we return false and do no updates
	 * 
	 * @param variableNames
	 * @param args
	 * @return true if matching successful and binding updated
	 */
	public abstract boolean update(int[] variableNames, Object[] args);

	/**
	 * A version of the above update method that returns a new binding instead.
	 * Where the previous would return false this returns null
	 * 
	 * @param variableNames
	 * @param args
	 * @return
	 */
	public Binding extend(int[] variableNames, Object[] args) {
		assert variableNames.length == args.length;
		Binding binding = copy();
		boolean okay = binding.update(variableNames, args);
		if (!okay) {
			return null;
		}
		return binding;
	}


	abstract public int size();
	

	@Override
	public abstract boolean equals(Object other);
	@Override
	public abstract int hashCode();

}
