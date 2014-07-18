package properties.competition;

/*
 * Taken from the JavaRV-MMT benchmarks - check if they have non mmt benchmarks too
 */

import static structure.impl.other.Quantification.FORALL;

import java.awt.geom.Line2D;

import properties.Property;
import properties.PropertyMaker;
import structure.intf.Assignment;
import structure.intf.Binding;
import structure.intf.Guard;
import structure.intf.QEA;
import creation.QEABuilder;

public class JavaRV_mmt implements PropertyMaker {

	@Override
	public QEA make(Property property) {
		switch (property) {
		case JAVARV_MMT_ONE:
			return makeOne();
		case JAVARV_MMT_TWO:
			return makeTwo();
		case JAVARV_MMT_THREE:
			return makeThree();
		case JAVARV_MMT_FOUR:
			return makeFour();
		case JAVARV_MMT_FIVE:
			return makeFive();
		default:
			return null;
		}
	}

	public QEA makeOne() {

		QEABuilder q = new QEABuilder("JAVARV_MMT_ONE");

		final int STEP = 1;
		final int counterOld = 1;
		final int counterNew = 2;

		q.addTransition(1, STEP, new int[] { counterOld }, 2);

		q.startTransition(2);
		q.eventName(STEP);
		q.addVarArg(counterNew);
		q.addGuard(new Guard("counterNew_=_counterOld_+_1") {

			@Override
			public int[] vars() {
				return new int[] { counterOld, counterNew };
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
				int counterOldVal = binding.getForcedAsInteger(counterOld);
				int counterNewVal = binding.getForcedAsInteger(counterNew);
				return counterNewVal == counterOldVal + 1;
			}
		});
		q.addAssignment(Assignment.storeVar(counterOld, counterNew));
		q.endTransition(2);

		q.addFinalStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("step", 1);

		return qea;
	}

	public QEA makeTwo() {

		QEABuilder q = new QEABuilder("JAVARV_MMT_TWO");

		int REQUEST = 1;
		int RESPONSE = 2;

		int s = -1;

		q.addQuantification(FORALL, s);

		q.addTransition(1, REQUEST, new int[] { s }, 2);
		q.addTransition(2, REQUEST, new int[] { s }, 2);
		q.addTransition(2, RESPONSE, new int[] { s }, 3);
		q.addTransition(3, RESPONSE, new int[] { s }, 3);
		q.addTransition(3, REQUEST, new int[] { s }, 2);

		q.addFinalStates(1, 3);

		QEA qea = q.make();

		qea.record_event_name("request", 1);
		qea.record_event_name("response", 2);

		return qea;
	}

	public QEA makeThree() {

		QEABuilder q = new QEABuilder("JAVARV_MMT_THREE");

		int RUN = 1;
		int LOCK_TRUE = 2;
		int UNLOCK = 3;
		int ACTION = 4;

		int i = -1;
		int j = 1;

		q.addQuantification(FORALL, i);

		q.addTransition(1, LOCK_TRUE, new int[] { j }, 1);
		q.addTransition(1, RUN, new int[] { i }, 2);
		q.addTransition(1, UNLOCK, new int[] { i }, 2);
		q.addTransition(2, LOCK_TRUE, new int[] { i }, 3);
		q.addTransition(3, UNLOCK, new int[] { i }, 2);
		q.addTransition(3, ACTION, new int[] { i }, 3);

		// This transition is required to capture the second requirement of the
		// spec i.e. A call to lock only returns true, if no thread is currently
		// holding the lock.
		// This is achieved by ensuring that when the lock is held by thread i
		// there does not exist another thread j that succesfully takes the lock
		q.addTransition(3, LOCK_TRUE, new int[] { j }, 4);

		// As we have added LOCK_TRUE (j) to the alphabet our next states may
		// now cause errors on state 2, one option is to add a looping
		// transition q.addTransition(2, LOCK_TRUE, new int[]{ j
		// },Guard.isNotEqual(i, j),2); however, my preference is to make it a
		// skip state
		q.setSkipStates(2);
		// But this requires us to add some failing transitions for the previous
		// implicit transitions given by the next state
		q.addTransition(2, RUN, new int[] { i }, 4);
		q.addTransition(2, UNLOCK, new int[] { i }, 4);
		q.addTransition(2, ACTION, new int[] { i }, 4);
		// Doing this with a skip state allows the monitor to remain
		// 'deterministic' as the non-deterministic looping transition is
		// captured by the skip behaviour

		q.addFinalStates(1, 2, 3);

		QEA qea = q.make();

		qea.record_event_name("run", 1);
		qea.record_event_name("lock_true", 2);
		qea.record_event_name("unlock", 3);
		qea.record_event_name("action", 4);

		return qea;
	}

	public QEA makeFour() {

		QEABuilder q = new QEABuilder("JAVARV_MMT_FOUR");

		final int STEP = 1;
		final int pos = 1;
		final int time = 2;
		final int newPos = 3;
		final int newTime = 4;

		q.addTransition(1, STEP, new int[] { pos, time }, 2);

		q.startTransition(2);
		q.eventName(STEP);
		q.addVarArg(newPos);
		q.addVarArg(newTime);
		q.addGuard(new Guard("velocity_<=_10") {

			@Override
			public int[] vars() {
				return new int[] { pos, time, newPos, newTime };
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

				double posVal = (double) binding.getForced(pos);
				double timeVal = (double) binding.getForced(time);
				double newPosVal = (double) binding.getForced(newPos);
				double newTimeVal = (double) binding.getForced(newTime);
				return (newPosVal - posVal) / (newTimeVal - timeVal) <= 10;
			}
		});
		q.addAssignment(new Assignment(
				"store(pos,newPos)_and_store(time,NewTime)") {

			@Override
			public int[] vars() {
				return new int[] { pos, time, newPos, newTime };
			}

			@Override
			public Binding apply(Binding binding, boolean copy) {

				double newPosVal = (double) binding.getForced(newPos);
				double newTimeVal = (double) binding.getForced(newTime);
				Binding newBinding = binding;
				if (copy) {
					newBinding = binding.copy();
				}
				newBinding.setValue(pos, newPosVal);
				newBinding.setValue(time, newTimeVal);
				return newBinding;
			}
		});
		q.endTransition(2);

		q.addFinalStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("step", 1);

		return qea;
	}

	public QEA makeFive() {

		QEABuilder q = new QEABuilder("JAVARV_MMT_FIVE");

		int BLOCK_ROUTE = 1;
		int FREE_ROUTE = 2;

		int r1 = -1;
		int r2 = -2;
		final int r1x1 = 1;
		final int r1y1 = 2;
		final int r1x2 = 3;
		final int r1y2 = 4;
		final int r2x1 = 5;
		final int r2y1 = 6;
		final int r2x2 = 7;
		final int r2y2 = 8;

		q.addQuantification(FORALL, r1);
		q.addQuantification(FORALL, r2, Guard.isNotEqual(r1, r2));

		q.addTransition(1, BLOCK_ROUTE,
				new int[] { r1, r1x1, r1y1, r1x2, r1y2 }, 2);
		q.addTransition(2, FREE_ROUTE, new int[] { r1 }, 1);

		q.startTransition(2);
		q.eventName(BLOCK_ROUTE);
		q.addVarArg(r2);
		q.addVarArg(r2x1);
		q.addVarArg(r2y1);
		q.addVarArg(r2x2);
		q.addVarArg(r2y2);
		q.addGuard(new Guard("intersect_r1_r2") {

			@Override
			public int[] vars() {
				return new int[] { r1x1, r1y1, r1x2, r1y2, r2x1, r2y1, r2x2,
						r2y2 };
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

				double r1x1Val = (double) binding.getForced(r1x1);
				double r1y1Val = (double) binding.getForced(r1y1);
				double r1x2Val = (double) binding.getForced(r1x2);
				double r1y2Val = (double) binding.getForced(r1y2);
				double r2x1Val = (double) binding.getForced(r2x1);
				double r2y1Val = (double) binding.getForced(r2y1);
				double r2x2Val = (double) binding.getForced(r2x2);
				double r2y2Val = (double) binding.getForced(r2y2);

				Line2D route1 = new Line2D.Double(r1x1Val, r1y1Val, r1x2Val,
						r1y2Val);
				Line2D route2 = new Line2D.Double(r2x1Val, r2y1Val, r2x2Val,
						r2y2Val);
				return route1.intersectsLine(route2);
			}
		});
		q.endTransition(3);

		q.addFinalStates(1, 2);
		q.setSkipStates(1, 2, 3);

		QEA qea = q.make();

		qea.record_event_name("block_route", 1);
		qea.record_event_name("free_route", 2);

		return qea;
	}
}
