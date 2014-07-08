package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import structure.impl.other.Verdict;

/**
 * Translator for the property MonPoly 4
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class OfflineTranslator_MONPOLY_FOUR extends OfflineTranslator {

	private final int WITHDRAW = 1;

	private final String WITHDRAW_STR = "withdraw";

	@Override
	public Verdict translateAndStep(String eventName, String[] paramNames,
			String[] paramValues) {
		switch (eventName) {

		case WITHDRAW_STR:
			return monitor.step(
					WITHDRAW,
					new Object[] { paramValues[2],
							Integer.valueOf(paramValues[3]),
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
