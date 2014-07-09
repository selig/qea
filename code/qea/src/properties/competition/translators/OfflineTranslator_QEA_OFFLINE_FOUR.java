package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import structure.impl.other.Verdict;

/**
 * Translator for the property QEA Offline 4
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class OfflineTranslator_QEA_OFFLINE_FOUR extends OfflineTranslator {

	private static final int REQUEST = 1;
	private static final int GRANT = 2;
	private static final int DENY = 3;
	private static final int RESCIND = 4;
	private static final int CANCEL = 5;

	private static final String REQUEST_STR = "request";
	private static final String GRANT_STR = "grant";
	private static final String DENY_STR = "deny";
	private static final String RESCIND_STR = "rescind";
	private static final String CANCEL_STR = "cancel";

	@Override
	public Verdict translateAndStep(String eventName, String[] paramNames,
			String[] paramValues) {
		switch (eventName) {
		case REQUEST_STR:
			return monitor.step(REQUEST,
					new Object[] { paramValues[0].intern() });
		case GRANT_STR:
			return monitor
					.step(GRANT, new Object[] { paramValues[0].intern() });
		case DENY_STR:
			return monitor.step(DENY, new Object[] { paramValues[0].intern() });
		case RESCIND_STR:
			return monitor.step(RESCIND,
					new Object[] { paramValues[0].intern() });
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
