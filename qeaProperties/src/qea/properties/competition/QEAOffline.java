package qea.properties.competition;

import qea.properties.Property;
import qea.properties.PropertyMaker;
import qea.properties.rovers.RoverCaseStudy;
import qea.structure.intf.QEA;

public class QEAOffline implements PropertyMaker {

	@Override
	public QEA make(Property property) {
		switch (property) {
		case QEA_OFFLINE_ONE:
			return makeOne();
		case QEA_OFFLINE_TWO:
			return makeTwo();
		case QEA_OFFLINE_THREE:
			return makeThree();
		case QEA_OFFLINE_FOUR:
			return makeFour();
		case QEA_OFFLINE_FIVE:
			return makeFive();
		}
		return null;
	}

	public QEA makeOne() {
		return RoverCaseStudy.makeExistsLeader();
	}

	public QEA makeTwo() {
		return RoverCaseStudy.makeGrantCancelSingle();
	}

	public QEA makeThree() {
		return RoverCaseStudy.makeNestedCommand();
	}

	public QEA makeFour() {
		return RoverCaseStudy.makeResourceLifecycle();
	}

	public QEA makeFive() {
		return RoverCaseStudy.makeRespectConflictsSingle();
	}

}
