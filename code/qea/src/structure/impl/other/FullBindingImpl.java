package structure.impl.other;

import java.util.Arrays;

import structure.intf.Binding;

/**
 * Represents the binding for a set of quantified and free variables
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class FullBindingImpl extends Binding {

	/**
	 * Internal bindings for free and quantified variables
	 */
	private BindingImpl freeBinding;
	private QBindingImpl quantBinding;

	/**
	 * Creates a new Binding with the specified number of variables
	 * 
	 * @param freeCount
	 *            Number of free variables for this binding
	 * @param quantCount
	 *            Number of quantified variables for this binding
	 */
	public FullBindingImpl(int freeCount, int quantCount) {
		freeBinding = new BindingImpl(freeCount);
		quantBinding = new QBindingImpl(quantCount);
	}
	/**
	 * Creates a new Binding with previous bindings
	 * 
	 * @param free
	 *            Binding for the free variables
	 * @param quant
	 *            Bindings for the quantified variables
	 */
	public FullBindingImpl(BindingImpl free, QBindingImpl quant) {
		freeBinding = free;
		quantBinding = quant;
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
		if(variableName>=0)
			return freeBinding.getValue(variableName);
		else
			return quantBinding.getValue(variableName);
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
		if(variableName>=0)
			freeBinding.setValue(variableName,value);
		else
			quantBinding.setValue(variableName,value);
	}


	@Override
	public Binding copy() {
		BindingImpl copyFree = (BindingImpl) freeBinding.copy();
		QBindingImpl copyQuant = (QBindingImpl) quantBinding.copy();
		return new FullBindingImpl(copyFree,copyQuant);
	}	
	
	@Override
	public void setEmpty() {

		// Make all references null
		freeBinding.setEmpty();
		quantBinding.setEmpty();	
	}

	@Override
	public String toString() {
		return "f"+freeBinding+"q"+quantBinding;
	}

}
