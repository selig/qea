package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import structure.impl.other.Verdict;

/**
 * Translator for the property STePr 3
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class OfflineTranslator_STEPR_THREE extends OfflineTranslator {

	private final int STEP = 1;

	private final String STEP_STR = "step";

	@Override
	public Verdict translateAndStep(String eventName, String[] paramNames,
			String[] paramValues) {

		switch (eventName) {
		case STEP_STR:
			return monitor.step(STEP, paramValues[1], paramValues[2]);

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
