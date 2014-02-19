package structure.impl;

import structure.intf.Binding;

/**
 * Represents the binding for a set of free variables
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class BindingImpl implements Binding { // TODO Check name

	/**
	 * Array of values for the objects. The position of an object in the array
	 * corresponds to the name of the variable
	 */
	private Object[] values;

	/**
	 * Creates a new Binding with the specified number of variables
	 * 
	 * @param variablesCount
	 *            Number of variables for this binding
	 */
	public BindingImpl(int variablesCount) {
		values = new Object[variablesCount];
	}

	/**
	 * Returns the value of the variable with the specified name
	 * 
	 * @param variableName
	 *            Variable name
	 * @return Value of the variable
	 */
	public Object getValue(int variableName) {
		return values[variableName];
	}

	/**
	 * Sets the value of the variable with the specified name
	 * 
	 * @param variableName
	 *            Variable name
	 * @param value
	 *            Value of the variable
	 */
	public void setValue(int variableName, Object value) {
		values[variableName] = value;
	}

}
