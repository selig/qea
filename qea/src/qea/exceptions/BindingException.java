package qea.exceptions;

/**
 * Runtime exception thrown when a variable's value is not present in a binding
 * or when its type is different to the expected one
 * 
 * @author Giles Reger
 */
@SuppressWarnings("serial")
public class BindingException extends RuntimeException {

	public BindingException(String message) {
		super(message);
	}
}
