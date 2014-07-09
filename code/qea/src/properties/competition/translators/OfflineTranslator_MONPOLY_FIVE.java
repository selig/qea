package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import structure.impl.other.Verdict;

/**
 * Translator for the property MonPoly 5
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class OfflineTranslator_MONPOLY_FIVE extends OfflineTranslator {

	private static final int INSERT_DB2 = 1;
	private static final int INSERT_DB3 = 2;

	private static final String INSERT_STR = "insert";

	@Override
	public Verdict translateAndStep(String eventName, String[] paramNames,
			String[] paramValues) {
		switch (eventName) {

		case INSERT_STR:

			// TODO Check in the trace how events are reported
			return monitor.step(
					INSERT_DB2,
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
