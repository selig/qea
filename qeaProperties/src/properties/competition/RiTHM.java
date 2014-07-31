package properties.competition;

import static structure.impl.other.Quantification.FORALL;
import properties.Property;
import properties.PropertyMaker;
import structure.intf.Binding;
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

		final int EVENT = 1;

		final int bandwidth = 1;

		q.startTransition(1);
		q.eventName(EVENT);
		q.addVarArg(bandwidth);
		q.addGuard(new Guard("x_" + bandwidth + " <= 100000.0") {

			@Override
			public int[] vars() {
				return new int[] { bandwidth };
			}

			@Override
			public boolean usesQvars() {
				return false;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				return check(binding);
			}

			@Override
			public boolean check(Binding binding) {
				double bandwidthDVal = (double) binding.getForced(bandwidth);
				return bandwidthDVal <= 100000.0;
			}
		});
		q.endTransition(1);

		q.addFinalStates(1);

		QEA qea = q.make();

		qea.record_event_name("event", 1);

		return qea;
	}

	public QEA makeFour() {

		QEABuilder q = new QEABuilder("RITHM_FOUR");

		final int EVENT = 1;

		final int con = -1;
		final int bandwidth = 1;

		q.addQuantification(FORALL, con);

		q.startTransition(1);
		q.eventName(EVENT);
		q.addVarArg(con);
		q.addVarArg(bandwidth);
		q.addGuard(new Guard("x_" + bandwidth + " > 10000.0") {

			@Override
			public int[] vars() {
				return new int[] { bandwidth };
			}

			@Override
			public boolean usesQvars() {
				return false;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				return check(binding);
			}

			@Override
			public boolean check(Binding binding) {
				double bandwidthDVal = (double) binding.getForced(bandwidth);
				return bandwidthDVal > 10000.0;
			}
		});
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(EVENT);
		q.addVarArg(con);
		q.addVarArg(bandwidth);
		q.addGuard(new Guard("x_" + bandwidth + " <= 10000.0") {

			@Override
			public int[] vars() {
				return new int[] { bandwidth };
			}

			@Override
			public boolean usesQvars() {
				return false;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				return check(binding);
			}

			@Override
			public boolean check(Binding binding) {
				double bandwidthDVal = (double) binding.getForced(bandwidth);
				return bandwidthDVal <= 100000.0;
			}
		});
		q.endTransition(1);

		q.addFinalStates(1);
		q.setSkipStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("event", 1);

		return qea;
	}

	public QEA makeFive() {

		QEABuilder q = new QEABuilder("RITHM_FIVE");

		final int OPEN = 1;
		final int CLOSE = 2;

		final int p = -1;
		final int f = -2;

		q.addQuantification(FORALL, p);
		q.addQuantification(FORALL, f);

		q.addTransition(1, OPEN, new int[] { p, f }, 2);
		q.addTransition(2, CLOSE, new int[] { p, f }, 1);

		q.addFinalStates(1);
		q.setSkipStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("open", OPEN);
		qea.record_event_name("close", CLOSE);

		return qea;
	}

}
