package structure.impl;

import java.util.Arrays;

import structure.intf.Binding;

/**
 * Represents the binding for a set of free variables
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class BindingImpl extends Binding { // TODO Check name

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
	@Override
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
	@Override
	public void setValue(int variableName, Object value) {
		values[variableName] = value;
	}

	@Override
	public Binding copy() {
		Binding binding = new BindingImpl(values.length);
		// TODO Should we use System.arraycopy here?
		for (int i = 0; i < values.length; i++) {
			binding.setValue(i, values[i]);
		}
		return binding;
	}

	@Override
	public void setEmpty() {

		// Make all references null
		for (int i = 0; i < values.length; i++) {
			values[i] = null;
		}
	}
	
	@Override
	public String toString(){
		String[] out = new String[values.length];
		for(int i=0;i<values.length;i++){
			if(values[i]==null) out[i]="-";
			else out[i]=values[i].toString();
		}
		return Arrays.toString(out);
	}
	
}
