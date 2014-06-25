package properties.competition;

import static structure.impl.other.Quantification.FORALL;
import properties.Property;
import properties.PropertyMaker;
import structure.intf.Binding;
import structure.intf.Guard;
import structure.intf.QEA;
import creation.QEABuilder;

public class MonPoly implements PropertyMaker {

	@Override
	public QEA make(Property property) {
		switch (property) {
		case MONPOLY_ONE:
			return makeOne();
		case MONPOLY_TWO:
			return makeTwo();
		case MONPOLY_THREE:
			return makeThree();
		case MONPOLY_FOUR:
			return makeFour();
		case MONPOLY_FIVE:
			return makeFive();
		}
		return null;
	}

	public QEA makeOne() {

		QEABuilder q = new QEABuilder("MONPOLY_ONE");

		final int TRANS = 1;
		final int REPORT = 2;

		final int t = -1;
		final int a = 1;
		final int t1 = 2;
		final int t2 = 3;

		q.addQuantification(FORALL, t);

		q.startTransition(1);
		q.eventName(TRANS);
		q.addVarArg(t);
		q.addVarArg(a);
		q.addVarArg(t1);
		q.addGuard(Guard.varIsGreaterThanVal(a, 2000));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(REPORT);
		q.addVarArg(t);
		q.addVarArg(t2);
		q.addGuard(new Guard("x_" + t2 + " - x_" + t1 + " < 5 days") {

			@Override
			public int[] vars() {
				return new int[] { t1, t2 };
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

				int t1Val = binding.getForcedAsInteger(t1);
				int t2Val = binding.getForcedAsInteger(t2);
				return t2 - t1 < 432000; // TODO Check time units of timestamp
			}
		});
		q.endTransition(3);

		q.addFinalStates(1, 3);
		q.setSkipStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("trans", 1);
		qea.record_event_name("report", 2);

		return qea;
	}

	public QEA makeTwo() {

		QEABuilder q = new QEABuilder("MONPOLY_TWO");

		final int AUTH = 1;
		final int TRANS = 2;

		final int t = -1;
		final int ts1 = 1;
		final int ts2 = 2;
		final int a = 3;

		q.addQuantification(FORALL, t);

		q.addTransition(1, AUTH, new int[] { t, ts1 }, 2);

		q.startTransition(2);
		q.eventName(TRANS);
		q.addVarArg(t);
		q.addVarArg(a);
		q.addVarArg(ts2);
		q.addGuard(new Guard("x_" + a + " < 2000 || (x_" + ts2 + " - x_" + ts1
				+ " < 21 && x_" + ts2 + " - x_" + ts1 + " >= 2)") {

			@Override
			public int[] vars() {
				return new int[] { a, ts2, ts1 };
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

				int aVal = binding.getForcedAsInteger(a);
				int ts1Val = binding.getForcedAsInteger(ts1);
				int ts2Val = binding.getForcedAsInteger(ts2);

				return aVal < 2000
						|| (ts2Val - ts1Val < 21 && ts2Val - ts1Val >= 2);
			}
		});
		q.endTransition(3);

		q.addFinalStates(1, 3);
		q.setSkipStates(1, 2, 3);

		QEA qea = q.make();

		qea.record_event_name("auth", 1);
		qea.record_event_name("trans", 2);

		return qea;
	}

	public QEA makeThree() {
		// TODO Auto-generated method stub
		return null;
	}

	public QEA makeFour() {
		// TODO Auto-generated method stub
		return null;
	}

	public QEA makeFive() {
		// TODO Auto-generated method stub
		return null;
	}

}
