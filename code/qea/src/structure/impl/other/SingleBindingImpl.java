package structure.impl.other;

import structure.intf.Binding;

/**
 * Represents the binding for a single binding
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class SingleBindingImpl extends Binding {

	/**
	 * A value for variable index
	 */
	private final int index;
	private Object value = null;

	/**
	 * Creates a new single Binding with a given value
	 * 
	 * @param value
	 *            Value for index
	 * @param index
	 *            Index of the variable
	 */
	public SingleBindingImpl(Object value, int index) {
		this.value = value;
		this.index = index;
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
		assert (variableName == index);
		return value;
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
		if (index == variableName) {
			this.value = value;
		}
	}

	@Override
	public Binding copy() {
		Binding binding = new SingleBindingImpl(value, index);
		return binding;
	}

	@Override
	public void setEmpty() {
		value = null;
	}

	@Override
	public String toString() {
		return "[" + index + ":" + value + "]";
	}

	@Override
	public boolean update(int[] variableNames, Object[] args) {
		assert(variableNames.length==args.length);
		for(int i=0;i<args.length;i++){
			int var = variableNames[i];
			if(var==index) value = args[i];
		}
		return true;
	}

}
