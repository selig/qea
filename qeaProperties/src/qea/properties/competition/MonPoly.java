package qea.properties.competition;

import static qea.structure.impl.other.Quantification.EXISTS;
import static qea.structure.impl.other.Quantification.FORALL;
import qea.properties.Property;
import qea.properties.PropertyMaker;
import qea.structure.intf.Assignment;
import qea.structure.intf.Binding;
import qea.structure.intf.Guard;
import qea.structure.intf.QEA;
import qea.creation.QEABuilder;

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
		// Timestamp unit is second
		q.addGuard(Guard.differenceLessThanVal(t2, t1, 432000));		
		q.endTransition(3);

		q.addFinalStates(1, 3);
		q.setSkipStates(1);

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

				return aVal < 2000 || ts2Val - ts1Val < 21
						&& ts2Val - ts1Val >= 2;
			}
		});
		q.endTransition(3);

		q.addFinalStates(1, 3);
		q.setSkipStates(1, 3);

		QEA qea = q.make();

		qea.record_event_name("auth", 1);
		qea.record_event_name("trans", 2);

		return qea;
	}

	public QEA makeThree() {

		QEABuilder q = new QEABuilder("MONPOLY_THREE");

		final int ACC_S = 1;
		final int ACC_F = 2;
		final int MGR_S = 3;
		final int MGR_F = 4;
		final int APPROVE = 5;
		final int PUBLISH = 6;

		final int f = -1;
		final int a = -2;
		final int m = 1;
		final int ts = 2;
		final int ts2 = 3;
		final int M = 4;

		q.addQuantification(FORALL, f);
		q.addQuantification(FORALL, a);

		q.addTransition(1, ACC_S, new int[] { a }, 2);

		q.startTransition(1);
		q.eventName(MGR_S);
		q.addVarArg(m);
		q.addVarArg(a);
		q.addAssignment(Assignment.addElementToSet(M, m));
		q.endTransition(1);

		q.startTransition(1);
		q.eventName(MGR_F);
		q.addVarArg(m);
		q.addVarArg(a);
		q.addAssignment(Assignment.removeElementFromSet(M, m));
		q.endTransition(1);

		q.startTransition(1);
		q.eventName(APPROVE);
		q.addVarArg(m);
		q.addVarArg(f);
		q.addVarArg(ts);
		q.addGuard(Guard.setContainsElement(m, M));
		q.endTransition(1);

		q.addTransition(2, ACC_F, new int[] { a }, 1);

		q.startTransition(2);
		q.eventName(MGR_S);
		q.addVarArg(m);
		q.addVarArg(a);
		q.addAssignment(Assignment.addElementToSet(M, m));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(MGR_F);
		q.addVarArg(m);
		q.addVarArg(a);
		q.addAssignment(Assignment.removeElementFromSet(M, m));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(APPROVE);
		q.addVarArg(m);
		q.addVarArg(f);
		q.addVarArg(ts);
		q.addGuard(Guard.setContainsElement(m, M));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(PUBLISH);
		q.addVarArg(a);
		q.addVarArg(f);
		q.addVarArg(ts2);
		q.addGuard(new Guard("x_" + ts + " exists && x_" + ts2 + " - x_" + ts
				+ " <= 10 days") {

			@Override
			public int[] vars() {
				return new int[] { ts, ts2 };
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

				Object tsObjVal = binding.getValue(ts);
				if (tsObjVal != null) {
					int tsVal = (int) tsObjVal;
					int ts2Val = binding.getForcedAsInteger(ts2);
					// Timestamp unit is second
					return ts2Val - tsVal <= 864000;
				}
				return false;
			}
		});
		q.endTransition(2);

		q.addFinalStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("acc_S", 1);
		qea.record_event_name("acc_F", 2);
		qea.record_event_name("mgr_S", 3);
		qea.record_event_name("mgr_F", 4);
		qea.record_event_name("approve", 5);
		qea.record_event_name("publish", 6);

		return qea;
	}

	public QEA makeFour() {

		QEABuilder q = new QEABuilder("MONPOLY_FOUR");
		q.setNegated(true);

		final int WITHDRAW = 1;

		final int u = -1;
		final int a = 1;
		final int ts = 2;
		final int ts2 = 3;
		final int s = 4;

		q.addQuantification(EXISTS, u);

		q.startTransition(1);
		q.eventName(WITHDRAW);
		q.addVarArg(u);
		q.addVarArg(a);
		q.addVarArg(ts);
		q.addAssignment(Assignment.storeVar(s, a));
		q.endTransition(1);

		q.startTransition(1);
		q.eventName(WITHDRAW);
		q.addVarArg(u);
		q.addVarArg(a);
		q.addVarArg(ts);
		q.addAssignment(Assignment.storeVar(s, a));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(WITHDRAW);
		q.addVarArg(u);
		q.addVarArg(a);
		q.addVarArg(ts2);
		q.addGuard(Guard.and(
					Guard.differenceLessThanOrEqualToVal(ts2,ts,6912000),
					Guard.sumLessThanOrEqualToVal(s,a,10000)
				));
		q.addAssignment(Assignment.add(s, a));				
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(WITHDRAW);
		q.addVarArg(u);
		q.addVarArg(a);
		q.addVarArg(ts2);
		q.addGuard(Guard.and(
				Guard.differenceLessThanOrEqualToVal(ts2,ts,6912000),
				Guard.sumGreaterThanVal(s,a,10000)
			));		
		q.endTransition(3);

		q.addFinalStates(3);
		q.setSkipStates(1, 3);

		QEA qea = q.make();

		qea.record_event_name("withdraw", 1);

		return qea;
	}

	public QEA makeFive() {

		QEABuilder q = new QEABuilder("MONPOLY_FIVE");

		final int INSERT_DB2 = 1;
		final int INSERT_DB3 = 2;

		final int d = -1;
		final int ts = 1;
		final int ts2 = 2;

		q.addQuantification(FORALL, d,
				Guard.varIsNotEqualSemToVal(d, "[unknown]"));

		q.addTransition(1, INSERT_DB2, new int[] { d, ts }, 2);

		q.startTransition(2);
		q.eventName(INSERT_DB3);
		q.addVarArg(d);
		q.addVarArg(ts2);
		q.addGuard(new Guard("x_" + ts2 + " - x_" + ts + " <= 60") {

			@Override
			public int[] vars() {
				return new int[] { ts2, ts };
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

				int tsVal = binding.getForcedAsInteger(ts);
				int ts2Val = binding.getForcedAsInteger(ts2);

				return ts2Val - tsVal <= 60;
			}
		});
		q.endTransition(3);

		q.addFinalStates(1, 3);
		q.setSkipStates(1, 3);

		QEA qea = q.make();

		qea.record_event_name("insert_db2", 1);
		qea.record_event_name("insert_db3", 2);

		return qea;
	}

}
