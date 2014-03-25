package structure.impl.other;

/**
 * A binding structure to represent bindings of quantified variables. One of the
 * key things is that we use -variableName instead of variableName to index the
 * array
 */
public class QBindingImpl extends FBindingImpl {

	public QBindingImpl(int variablesCount) {
		super(variablesCount);
	}

	/**
	 * Returns the value of the quantified variable with the specified name
	 * 
	 * @param variableName
	 *            Variable name
	 * @return Value of the variable
	 */
	@Override
	public Object getValue(int variableName) {
		return super.getValue(-variableName);
	}

	@Override
	public void setValue(int variableName, Object value) {
		super.setValue(-variableName, value);
	}

	private static QBindingImpl empty;
	public static QBindingImpl emptyBinding() {
		if(empty==null){
			empty = new QBindingImpl(0); // is this safe?
		}
		return empty;
	}

}
