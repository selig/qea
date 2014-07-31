package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import properties.Property;
import properties.TranslatorMaker;

public class CSRV14TranslatorMaker implements TranslatorMaker {

	private static SoloistTranslators soloist = new SoloistTranslators();
	private static RiTHMTranslators rithm = new RiTHMTranslators();
	private static MonPolyTranslators monPoly = new MonPolyTranslators();
	private static SteprTranslators stepr = new SteprTranslators();
	private static QEAOfflineTranslators qeaOffline = new QEAOfflineTranslators();

	@Override
	public OfflineTranslator make(Property property) {

		switch (property) {

		case SOLOIST_ONE:
		case SOLOIST_TWO:
		case SOLOIST_THREE:
		case SOLOIST_FOUR:
			return soloist.make(property);

		case RITHM_ONE:
		case RITHM_TWO:
		case RITHM_THREE:
		case RITHM_FOUR:
		case RITHM_FIVE:
			return rithm.make(property);

		case MONPOLY_ONE:
		case MONPOLY_TWO:
		case MONPOLY_THREE:
		case MONPOLY_FOUR:
		case MONPOLY_FIVE:
			return monPoly.make(property);

		case STEPR_ONE:
		case STEPR_TWO:
		case STEPR_THREE:
		case STEPR_FOUR:
			return stepr.make(property);

		case QEA_OFFLINE_ONE:
		case QEA_OFFLINE_TWO:
		case QEA_OFFLINE_THREE:
		case QEA_OFFLINE_FOUR:
		case QEA_OFFLINE_FIVE:
			return qeaOffline.make(property);

		default:
			return null;
		}
	}
}
