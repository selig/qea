package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import structure.impl.other.Verdict;

/**
 * Translator for the property MonPoly 1
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class OfflineTranslator_MONPOLY_ONE extends OfflineTranslator {

	private final int TRANS = 1;
	private final int REPORT = 2;

	private final String TRANS_STR = "trans";
	private final String REPORT_STR = "report";

	@Override
	public Verdict translateAndStep(String eventName, String[] params) {
		switch (eventName) {
		case TRANS_STR:
			return monitor.step(TRANS,
					new Object[] { params[3], Integer.valueOf(params[4]),
							Integer.valueOf(params[1]) });
		case REPORT_STR:
			return monitor.step(REPORT,
					new Object[] { params[2], Integer.valueOf(params[1]) });
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
