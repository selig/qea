package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import structure.impl.other.Verdict;

/**
 * Translator for the property MonPoly 2
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class OfflineTranslator_MONPOLY_TWO extends OfflineTranslator {

	private final int AUTH = 1;
	private final int TRANS = 2;

	private final String AUTH_STR = "auth";
	private final String TRANS_STR = "trans";

	@Override
	public Verdict translateAndStep(String eventName, String[] paramNames,
			String[] paramValues) {
		switch (eventName) {
		case AUTH_STR:
			return monitor.step(
					AUTH,
					new Object[] { paramValues[3],
							Integer.valueOf(paramValues[1]) });
		case TRANS_STR:
			return monitor.step(
					TRANS,
					new Object[] { paramValues[3],
							Integer.valueOf(paramValues[4]),
							Integer.valueOf(paramValues[1]) });
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
