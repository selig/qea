package exceptions;

/**
 * Runtime exception thrown during the monitoring process in
 * <code>XMLFileMonitorSAX</code> if a strong failure is found
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
@SuppressWarnings("serial")
public class XMLFailureException extends RuntimeException {

	public XMLFailureException(String message) {
		super(message);
	}
}
