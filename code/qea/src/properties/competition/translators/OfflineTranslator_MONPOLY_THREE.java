package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import structure.impl.other.Verdict;

/**
 * Translator for the property MonPoly 3
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class OfflineTranslator_MONPOLY_THREE extends OfflineTranslator {

	private static final int ACC_S = 1;
	private static final int ACC_F = 2;
	private static final int MGR_S = 3;
	private static final int MGR_F = 4;
	private static final int APPROVE = 5;
	private static final int PUBLISH = 6;

	private static final String ACC_S_STR = "acc_S";
	private static final String ACC_F_STR = "acc_F";
	private static final String MGR_S_STR = "mgr_S";
	private static final String MGR_F_STR = "mgr_F";
	private static final String APPROVE_STR = "approve";
	private static final String PUBLISH_STR = "publish";

	@Override
	public Verdict translateAndStep(String eventName, String[] paramNames,
			String[] paramValues) {
		switch (eventName) {

		case ACC_S_STR:
			return monitor.step(ACC_S, new Object[] { paramValues[2] });

		case ACC_F_STR:
			return monitor.step(ACC_F, new Object[] { paramValues[2] });

		case MGR_S_STR:
			return monitor.step(MGR_S, new Object[] { paramValues[2],
					paramValues[3] });

		case MGR_F_STR:
			return monitor.step(MGR_F, new Object[] { paramValues[2],
					paramValues[3] });

		case APPROVE_STR:
			return monitor.step(APPROVE, new Object[] { paramValues[2],
					paramValues[3], Integer.valueOf(paramValues[1]) });

		case PUBLISH_STR:
			return monitor.step(PUBLISH, new Object[] { paramValues[2],
					paramValues[3], Integer.valueOf(paramValues[1]) });

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
