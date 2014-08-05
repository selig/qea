package exceptions;

/**
 * Runtime exception thrown when an inconsistency is found during the creation
 * of a QEA or the monitoring process. For example: an event is sent to the
 * monitor with a wrong number of parameters
 * 
 * @author Giles Reger
 */
@SuppressWarnings("serial")
public class ShouldNotHappenException extends RuntimeException {

	public ShouldNotHappenException(String message) {
		super(message);
	}
}
