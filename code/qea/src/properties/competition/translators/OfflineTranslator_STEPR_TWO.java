package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import structure.impl.other.Verdict;

/**
 * Translator for the property STePr 2
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class OfflineTranslator_STEPR_TWO extends OfflineTranslator {

	private static final int START1 = 1;
	private static final int STOP = 2;

	private static final String START1_STR = "start1";
	private static final String STOP_STR = "stop";

	@Override
	public Verdict translateAndStep(String eventName, String[] paramNames,
			String[] paramValues) {
		switch (paramNames[0]) {
		case START1_STR:
			// TODO Should we check the value of the parameter is true?
			return monitor.step(START1, Long.valueOf(paramValues[6]));
		case STOP_STR:
			return monitor.step(STOP, Long.valueOf(paramValues[6]));
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
