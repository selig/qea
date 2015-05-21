package qea.structure.impl.other;

import qea.structure.intf.Binding;

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
	private Binding freeBinding;
	private Binding quantBinding;

	/**
	 * Creates a new Binding with the specified number of variables
	 * 
	 * @param freeCount
	 *            Number of free variables for this binding
	 * @param quantCount
	 *            Number of quantified variables for this binding
	 */
	public FullBindingImpl(int freeCount, int quantCount) {
		freeBinding = new FBindingImpl(freeCount);
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
	public FullBindingImpl(Binding free, Binding quant) {
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
		if (variableName >= 0) {
			return freeBinding.getValue(variableName);
		} else {
			return quantBinding.getValue(variableName);
		}
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
		if (variableName >= 0) {
			freeBinding.setValue(variableName, value);
		} else {
			quantBinding.setValue(variableName, value);
		}
	}

	@Override
	public Binding copy() {
		FBindingImpl copyFree = (FBindingImpl) freeBinding.copy();
		QBindingImpl copyQuant = (QBindingImpl) quantBinding.copy();
		return new FullBindingImpl(copyFree, copyQuant);
	}

	@Override
	public void setEmpty() {

		// Make all references null
		freeBinding.setEmpty();
		quantBinding.setEmpty();
	}

	@Override
	public String toString() {
		return "f" + freeBinding + "q" + quantBinding;
	}

	@Override
	public boolean update(int[] vars, Object[] args) {
		return freeBinding.update(vars, args)
				&& quantBinding.update(vars, args);
	}

	@Override
	public boolean equals(Object other) {
		if(other instanceof FullBindingImpl){
			FullBindingImpl ob = (FullBindingImpl) other;
			return freeBinding.equals(ob.freeBinding) &&
					quantBinding.equals(ob.quantBinding);
		}
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int size() {
		return freeBinding.size() + quantBinding.size();
	}

}
