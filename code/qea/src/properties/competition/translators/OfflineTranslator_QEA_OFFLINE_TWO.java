package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import structure.impl.other.Verdict;

/**
 * Translator for the property QEA Offline 2
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class OfflineTranslator_QEA_OFFLINE_TWO extends OfflineTranslator {

	private static final int GRANT = 1;
	private static final int CANCEL = 2;

	private static final String GRANT_STR = "grant";
	private static final String CANCEL_STR = "cancel";

	@Override
	public Verdict translateAndStep(String eventName, String[] paramNames,
			String[] paramValues) {
		switch (eventName) {
		case GRANT_STR:
			return monitor.step(GRANT, new Object[] { paramValues[0].intern(),
					paramValues[1].intern() });
		case CANCEL_STR:
			return monitor.step(CANCEL, new Object[] { paramValues[0].intern(),
					paramValues[1].intern() });
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
