package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import structure.impl.other.Verdict;

/**
 * Translator for the property ZOT+SOLOIST 2
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class OfflineTranslator_SOLOIST_TWO extends OfflineTranslator {

	private final int INVCHECKACCESS_START = 1;
	private final int INVCHECKACCESS_COMPLETE = 2;
	private final String INVCHECKACCESS_STR = "invcheckaccess";
	private final String EVENT_TYPE_START_STR = "start";

	@Override
	public Verdict translateAndStep(String eventName, String[] params) {

		switch (eventName) {
		case INVCHECKACCESS_STR:
			if (params[1].equals(EVENT_TYPE_START_STR)) {
				return monitor.step(INVCHECKACCESS_START,
						Integer.valueOf(params[0]));
			}
			return monitor.step(INVCHECKACCESS_COMPLETE,
					Integer.valueOf(params[0]));
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
