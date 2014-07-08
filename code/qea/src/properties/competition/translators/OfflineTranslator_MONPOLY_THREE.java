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

	private final int ACC_S = 1;
	private final int ACC_F = 2;
	private final int MGR_S = 3;
	private final int MGR_F = 4;
	private final int APPROVE = 5;
	private final int PUBLISH = 6;

	private final String ACC_S_STR = "acc_S";
	private final String ACC_F_STR = "acc_F";
	private final String MGR_S_STR = "mgr_S";
	private final String MGR_F_STR = "mgr_F";
	private final String APPROVE_STR = "approve";
	private final String PUBLISH_STR = "publish";

	@Override
	public Verdict translateAndStep(String eventName, String[] params) {
		switch (eventName) {

		case ACC_S_STR:
			return monitor.step(ACC_S, new Object[] { params[2] });

		case ACC_F_STR:
			return monitor.step(ACC_F, new Object[] { params[2] });

		case MGR_S_STR:
			return monitor.step(MGR_S, new Object[] { params[2], params[3] });

		case MGR_F_STR:
			return monitor.step(MGR_F, new Object[] { params[2], params[3] });

		case APPROVE_STR:
			return monitor.step(APPROVE, new Object[] { params[2], params[3],
					Integer.valueOf(params[1]) });

		case PUBLISH_STR:
			return monitor.step(PUBLISH, new Object[] { params[2], params[3],
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
