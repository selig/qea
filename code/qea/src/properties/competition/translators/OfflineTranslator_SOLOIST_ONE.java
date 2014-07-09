package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import structure.impl.other.Verdict;

/**
 * Translator for the property ZOT+SOLOIST 1
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class OfflineTranslator_SOLOIST_ONE extends OfflineTranslator {

	private static final int WITHDRAW = 1;
	private static final int LOGOFF = 2;
	
	private static final String WITHDRAW_STR = "repwidraw";
	private static final String LOGOFF_STR = "recvlogoff";

	@Override
	public Verdict translateAndStep(String eventName, String[] paramNames,
			String[] paramValues) {
		switch (eventName) {
		case WITHDRAW_STR:
			return monitor.step(WITHDRAW, Integer.valueOf(paramValues[0]));
		case LOGOFF_STR:
			return monitor.step(LOGOFF, Integer.valueOf(paramValues[0]));
		default:
			return null;
		}
	}

	@Override
	public Verdict translateAndStep(String eventName) {
		// No event without parameters is relevant for this property
		return null;
	}

}
