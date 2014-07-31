package properties.competition;

import properties.Property;
import properties.PropertyMaker;
import structure.intf.QEA;

public class CSRV14PropertyMaker implements PropertyMaker {

	private static Larva larva = new Larva();
	private static JavaRV_mmt javaRV_mmt = new JavaRV_mmt();
	private static JavaMOP javaMOP = new JavaMOP();
	private static Prm4j prm4j = new Prm4j();
	private static QEAJava qeaJava = new QEAJava();
	private static Soloist soloist = new Soloist();
	private static RiTHM rithm = new RiTHM();
	private static MonPoly monPoly = new MonPoly();
	private static Stepr stepr = new Stepr();
	private static QEAOffline qeaOffline = new QEAOffline();

	@Override
	public QEA make(Property property) {

		switch (property) {

		case LARVA_ONE:
		case LARVA_TWO:
		case LARVA_THREE:
		case LARVA_FOUR:
		case LARVA_FIVE:
		case LARVA_SIX:
		case LARVA_SEVEN:
		case LARVA_EIGHT:
		case LARVA_NINE:
		case LARVA_TEN:
			return larva.make(property);

		case JAVARV_ONE:
		case JAVARV_TWO:
		case JAVARV_THREE:
		case JAVARV_FOUR:
		case JAVARV_FIVE:
			return javaRV_mmt.make(property);

		case JAVAMOP_ONE:
		case JAVAMOP_TWO:
		case JAVAMOP_THREE:
		case JAVAMOP_FOUR:
			return javaMOP.make(property);

		case PRM4J_ONE:
		case PRM4J_TWO:
		case PRM4J_THREE:
		case PRM4J_FOUR:
		case PRM4J_FIVE:
			return prm4j.make(property);

		case QEA_JAVA_ONE:
		case QEA_JAVA_TWO:
		case QEA_JAVA_THREE:
		case QEA_JAVA_FOUR:
			return qeaJava.make(property);

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
