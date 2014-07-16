package properties.competition;

import static structure.impl.other.Quantification.FORALL;
import properties.Property;
import properties.PropertyMaker;
import structure.intf.Binding;
import structure.intf.Guard;
import structure.intf.QEA;
import creation.QEABuilder;

public class Stepr implements PropertyMaker {

	@Override
	public QEA make(Property property) {
		switch (property) {
		case STEPR_ONE:
			return makeOne();
		case STEPR_TWO:
			return makeTwo();
		case STEPR_THREE:
			return makeThree();
		case STEPR_FOUR:
			return makeFour();
		}
		return null;
	}

	public QEA makeOne() {

		QEABuilder q = new QEABuilder("STEPR_ONE");

		final int START2 = 1;
		final int ALARM = 2;

		final int ts = 1;
		final int ts2 = 2;

		q.addTransition(1, ALARM, new int[] { ts2 }, 1);
		q.addTransition(1, START2, new int[] { ts }, 2);
		q.addTransition(2, START2, new int[] { ts }, 2);

		q.startTransition(2);
		q.eventName(ALARM);
		q.addVarArg(ts2);
		q.addGuard(new Guard("x_" + ts2 + " - x_" + ts + " > 60000") {

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
				long tsVal = (long) binding.getForced(ts);
				long ts2Val = (long) binding.getForced(ts2);
				return ts2Val - tsVal > 60000;
			}
		});
		q.endTransition(1);

		q.addFinalStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("start2", START2);
		qea.record_event_name("alarm", ALARM);

		return qea;
	}

	public QEA makeTwo() {

		QEABuilder q = new QEABuilder("STEPR_TWO");

		final int START1 = 1;
		final int STOP = 2;

		final int ts = 1;
		final int ts2 = 2;

		q.addTransition(1, START1, new int[] { ts }, 2);
		q.addTransition(1, STOP, new int[] { ts2 }, 1);
		q.addTransition(2, START1, new int[] { ts }, 2);

		q.startTransition(2);
		q.eventName(STOP);
		q.addVarArg(ts2);
		q.addGuard(new Guard("x_" + ts2 + " - x_" + ts + " <= 30000") {

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
				long tsVal = (long) binding.getForced(ts);
				long ts2Val = (long) binding.getForced(ts2);
				return ts2Val - tsVal <= 30000;
			}
		});
		q.endTransition(1);

		q.addFinalStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("start1", START1);
		qea.record_event_name("stop", STOP);

		return qea;
	}

	public QEA makeThree() {

		QEABuilder q = new QEABuilder("STEPR_THREE");

		final int STEP = 1;

		final int errorCount = 1;
		final int cycleCount = 2;

		q.startTransition(1);
		q.eventName(STEP);
		q.addVarArg(errorCount);
		q.addVarArg(cycleCount);
		q.addGuard(new Guard("x_" + errorCount + " == 0 || x_" + cycleCount
				+ " == 0 || x_" + errorCount + " <= 0.00002 * x_" + cycleCount) {

			@Override
			public int[] vars() {
				return new int[] { errorCount, cycleCount };
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
				double errorCountVal = Double.valueOf((String) binding
						.getForced(errorCount));
				double cycleCountVal = Double.valueOf((String) binding
						.getForced(cycleCount));
				return errorCountVal == 0 || cycleCountVal == 0
						|| errorCountVal <= 0.00002 * cycleCountVal;
			}
		});
		q.endTransition(1);

		q.addFinalStates(1);

		QEA qea = q.make();

		qea.record_event_name("step", STEP);

		return qea;
	}

	public QEA makeFour() {

		QEABuilder q = new QEABuilder("STEPR_FOUR");

		final int INIT = 1;
		final int RUN = 2;
		final int FINISH = 3;
		final int GROUP_START = 4;
		final int PHASE_START = 5;
		final int GROUP_END = 6;

		final int p = -1;
		final int ts = 1;
		final int ts2 = 2;

		q.addQuantification(FORALL, p);

		q.addTransition(1, GROUP_START, new int[] { ts }, 2);

		q.startTransition(2);
		q.eventName(GROUP_END);
		q.addVarArg(ts2);
		q.addGuard(new Guard("x_" + ts2 + " - x_" + ts + " < 480000") {

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
				long tsVal = (long) binding.getForced(ts);
				long ts2Val = (long) binding.getForced(ts2);
				return ts2Val - tsVal < 480000;
			}
		});
		q.endTransition(1);

		q.addTransition(2, PHASE_START, 2);
		q.addTransition(2, INIT, new int[] { p }, 3);
		q.addTransition(3, RUN, new int[] { p }, 4);
		q.addTransition(4, FINISH, new int[] { p }, 2);

		q.addFinalStates(1);

		QEA qea = q.make();

		qea.record_event_name("init", INIT);
		qea.record_event_name("run", RUN);
		qea.record_event_name("finish", FINISH);
		qea.record_event_name("group_start", GROUP_START);
		qea.record_event_name("phase_start", PHASE_START);
		qea.record_event_name("group_end", GROUP_END);

		return qea;
	}

}
