package structure.impl.other;

import java.util.Arrays;

import structure.intf.Binding;

/**
 * Represents the binding for a set of free variables
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class FBindingImpl extends Binding {

	/**
	 * Array of values for the variables. The size of the array is equal to the
	 * number of free variables. The position of a value in the array
	 * corresponds to the name of the variable - 1
	 */
	protected Object[] values;

	/**
	 * Creates a new Binding with the specified number of variables
	 * 
	 * @param variablesCount
	 *            Number of variables for this binding
	 */
	public FBindingImpl(int variablesCount) {
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
		return values[variableName - 1];
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
		values[variableName - 1] = value;
	}

	@Override
	public Binding copy() {

		Binding binding = new FBindingImpl(values.length);
		for (int i = 0; i < values.length; i++) {
			binding.setValue(i + 1, values[i]);
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
	public String toString() {
		String[] out = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			if (values[i] == null) {
				out[i] = "-";
			} else {
				out[i] = ""+System.identityHashCode(values[i]);//values[i].toString();
			}
		}
		return Arrays.toString(out);
	}

	@Override
	public int hashCode(){
		//return Arrays.hashCode(values);
		//The following is based on the above but with identity hash codes
		if(values==null) return 0;
		int result = 1;
		for(Object element : values)
			result = 31 * result + (element==null ? 0 : System.identityHashCode(element));
		
		return result;
	}	
	@Override
	public boolean equals(Object other){
		if(other instanceof FBindingImpl){
			FBindingImpl otherB = (FBindingImpl) other;
			//return Arrays.equals(values, otherB.values);
			for(int i=0;i<values.length;i++)
				if(values[i] != otherB.values[i])
					return false;
			return true;
		}
		return false;
	}

	@Override
	public boolean update(int[] variableNames, Object[] args) {
		assert(variableNames.length==args.length);
		for(int i=0;i<variableNames.length;i++){
			int var = variableNames[i];
			if(var>0){
				setValue(var,args[i]);
			}
		}
		return true;
	}
	
}
