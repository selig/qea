package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import structure.impl.other.Verdict;

/**
 * Translator for the property QEA Offline 1
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class OfflineTranslator_QEA_OFFLINE_ONE extends OfflineTranslator {

	private static final int PING = 1;
	private static final int ACK = 2;

	private static final String PING_STR = "ping";
	private static final String ACK_STR = "ack";

	@Override
	public Verdict translateAndStep(String eventName, String[] paramNames,
			String[] paramValues) {
		switch (eventName) {
		case PING_STR:
			return monitor.step(PING, new Object[] { paramValues[0].intern(),
					paramValues[1].intern() });
		case ACK_STR:
			return monitor.step(ACK, new Object[] { paramValues[0].intern(),
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
