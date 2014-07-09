package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import structure.impl.other.Verdict;

/**
 * Translator for the property QEA Offline 5
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class OfflineTranslator_QEA_OFFLINE_FIVE extends OfflineTranslator {

	private final int CONFLICT = 1;
	private final int GRANT = 2;
	private final int CANCEL = 3;

	private final String CONFLICT_STR = "conflict";
	private final String GRANT_STR = "grant";
	private final String CANCEL_STR = "cancel";

	@Override
	public Verdict translateAndStep(String eventName, String[] paramNames,
			String[] paramValues) {
		switch (eventName) {
		case CONFLICT_STR:
			return monitor.step(
					CONFLICT,
					new Object[] { paramValues[0].intern(),
							paramValues[1].intern() });
		case GRANT_STR:
			return monitor
					.step(GRANT, new Object[] { paramValues[0].intern() });

		case CANCEL_STR:
			return monitor.step(CANCEL,
					new Object[] { paramValues[0].intern() });
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
