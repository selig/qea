package properties.competition;

import static structure.impl.other.Quantification.FORALL;
import properties.Property;
import properties.PropertyMaker;
import structure.intf.Guard;
import structure.intf.QEA;
import creation.QEABuilder;

public class RiTHM implements PropertyMaker {

	@Override
	public QEA make(Property property) {
		switch (property) {
		case RITHM_ONE:
			return makeOne();
		case RITHM_TWO:
			return makeTwo();
		case RITHM_THREE:
			return makeThree();
		case RITHM_FOUR:
			return makeFour();
		case RITHM_FIVE:
			return makeFive();
		}
		return null;
	}

	public QEA makeOne() {

		QEABuilder q = new QEABuilder("RITHM_ONE");

		int EVENT = 1;
		int chunksize = 1;

		q.startTransition(1);
		q.eventName(EVENT);
		q.addVarArg(chunksize);
		q.addGuard(Guard.varIsLessThanOrEqualToVal(chunksize, 999999));
		q.endTransition(1);

		q.addFinalStates(1);

		QEA qea = q.make();

		qea.record_event_name("event", 1);

		return qea;
	}

	public QEA makeTwo() {

		QEABuilder q = new QEABuilder("RITHM_TWO");

		int EVENT = 1;

		int con = -1;
		int chunksize = 1;

		q.addQuantification(FORALL, con);

		q.startTransition(1);
		q.eventName(EVENT);
		q.addVarArg(con);
		q.addVarArg(chunksize);
		q.addGuard(Guard.varIsGreaterThanVal(chunksize, 10000));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(EVENT);
		q.addVarArg(con);
		q.addVarArg(chunksize);
		q.addGuard(Guard.varIsLessThanOrEqualToVal(chunksize, 10000));
		q.endTransition(1);

		q.addFinalStates(1);
		q.setSkipStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("event", 1);

		return qea;
	}

	public QEA makeThree() {

		QEABuilder q = new QEABuilder("RITHM_THREE");

		int EVENT = 1;

		int bandwidth = 1;

		q.startTransition(1);
		q.eventName(EVENT);
		q.addVarArg(bandwidth);
		q.addGuard(Guard.varIsLessThanOrEqualToVal(bandwidth, 100000));
		q.endTransition(1);

		q.addFinalStates(1);

		QEA qea = q.make();

		qea.record_event_name("event", 1);

		return qea;
	}

	public QEA makeFour() {

		QEABuilder q = new QEABuilder("RITHM_FOUR");

		int EVENT = 1;

		int con = -1;
		int bandwidth = 1;

		q.addQuantification(FORALL, con);

		q.startTransition(1);
		q.eventName(EVENT);
		q.addVarArg(con);
		q.addVarArg(bandwidth);
		q.addGuard(Guard.varIsGreaterThanVal(bandwidth, 10000));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(EVENT);
		q.addVarArg(con);
		q.addVarArg(bandwidth);
		q.addGuard(Guard.varIsLessThanOrEqualToVal(bandwidth, 10000));
		q.endTransition(1);

		q.addFinalStates(1);
		q.setSkipStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("event", 1);

		return qea;
	}

	public QEA makeFive() {

		QEABuilder q = new QEABuilder("RITHM_FIVE");

		int START = 1;
		int STOP = 2;

		int o = -1;

		q.addQuantification(FORALL, o);

		q.addTransition(1, START, new int[] { o }, 2);
		q.addTransition(2, STOP, new int[] { o }, 1);

		q.addFinalStates(1);
		q.setSkipStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("start", 1);
		qea.record_event_name("stop", 2);

		return qea;
	}

}
