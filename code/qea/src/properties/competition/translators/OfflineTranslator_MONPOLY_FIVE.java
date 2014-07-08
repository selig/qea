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

	private final int INSERT_DB2 = 1;
	private final int INSERT_DB3 = 2;

	private final String INSERT_STR = "insert";

	@Override
	public Verdict translateAndStep(String eventName, String[] params) {
		switch (eventName) {

		case INSERT_STR:

			// TODO Check in the trace how events are reported
			return monitor.step(INSERT_DB2,
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
