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
	public Verdict translateAndStep(String eventName, String[] params) {
		switch (eventName) {

		case WITHDRAW_STR:
			return monitor.step(WITHDRAW,
					new Object[] { params[2], Integer.valueOf(params[3]),
							Integer.valueOf(params[1]) });
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
