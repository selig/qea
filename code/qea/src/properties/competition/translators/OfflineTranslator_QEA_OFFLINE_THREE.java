package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import structure.impl.other.Verdict;

/**
 * Translator for the property QEA Offline 3
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class OfflineTranslator_QEA_OFFLINE_THREE extends OfflineTranslator {

	private static final int COMMAND = 1;
	private static final int SUCCEED = 2;

	private static final String COMMAND_STR = "command";
	private static final String SUCCEED_STR = "succeed";

	@Override
	public Verdict translateAndStep(String eventName, String[] paramNames,
			String[] paramValues) {
		switch (eventName) {
		case COMMAND_STR:
			return monitor.step(COMMAND,
					new Object[] { paramValues[0].intern() });
		case SUCCEED_STR:
			return monitor.step(SUCCEED,
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
