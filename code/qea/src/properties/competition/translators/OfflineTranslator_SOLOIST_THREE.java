package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import structure.impl.other.Verdict;

/**
 * Translator for the property ZOT+SOLOIST 3
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class OfflineTranslator_SOLOIST_THREE extends OfflineTranslator {

	private static final int INVCHECKACCESS_COMPLETE = 1;
	private static final int REPLOGON = 2;
	
	private static final String INVCHECKACCESS_STR = "invcheckaccess";
	private static final String EVENT_TYPE_COMPLETE_STR = "complete";
	private static final String REPLOGON_STR = "replogon";

	@Override
	public Verdict translateAndStep(String eventName, String[] paramNames,
			String[] paramValues) {

		switch (eventName) {
		case INVCHECKACCESS_STR:
			if (paramValues[1].equals(EVENT_TYPE_COMPLETE_STR)) {
				return monitor.step(INVCHECKACCESS_COMPLETE);
			}
			return null;

		case REPLOGON_STR:
			return monitor.step(REPLOGON);

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
