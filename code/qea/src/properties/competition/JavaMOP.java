package properties.competition;

import static structure.impl.other.Quantification.FORALL;
import properties.Property;
import properties.PropertyMaker;
import properties.papers.DaCapo;
import properties.papers.HasNextQEA;
import structure.intf.Assignment;
import structure.intf.Guard;
import structure.intf.QEA;
import creation.QEABuilder;

public class JavaMOP implements PropertyMaker {

	@Override
	public QEA make(Property property) {
		switch (property) {
		case JAVAMOP_ONE:
			return makeOne();
		case JAVAMOP_TWO:
			return makeTwo();
		case JAVAMOP_THREE:
			return makeThree();
		case JAVAMOP_FOUR:
			return makeFour();
		case JAVAMOP_FIVE:
			return makeFive();
		}
		return null;
	}

	public QEA makeOne() {
		// This is just the HasNext property we know and love
		return new HasNextQEA();
	}

	public QEA makeTwo() {

		/*
		 * Note - this has a disjoint alphabet, we should implement this
		 * optimisation!
		 */

		QEABuilder q = new QEABuilder("SafeLock");

		int LOCK = 1;
		int UNLOCK = 2;
		int thread = -1;
		int lock = -2;
		int count = 1;

		q.addQuantification(FORALL, thread);
		q.addQuantification(FORALL, lock);

		q.addTransition(1, LOCK, new int[] { thread, lock },
				Assignment.set(count, 1), 2);
		q.addTransition(2, LOCK, new int[] { thread, lock },
				Assignment.increment(count), 2);
		q.addTransition(2, UNLOCK, new int[] { thread, lock },
				Guard.isGreaterThanConstant(count, 1),
				Assignment.decrement(count), 2);
		q.addTransition(2, UNLOCK, new int[] { thread, lock },
				Guard.isSemEqualToConstant(count, 1), Assignment.set(count, 0),
				1);

		q.addFinalStates(1, 2);

		QEA qea = q.make();
		qea.record_event_name("lock", LOCK);
		qea.record_event_name("unlock", UNLOCK);
		return qea;
	}

	public QEA makeThree() {

		QEABuilder q = new QEABuilder("A^n B^n C^n");

		int A = 1;
		int B = 2;
		int C = 3;
		int a = 1;
		int b = 2;

		q.startTransition(1);
		q.eventName(A);
		q.addAssignment(Assignment.set(a, 1));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(A);
		q.addAssignment(Assignment.increment(a));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(B);
		q.addAssignment(Assignment.set(b, 1));
		q.endTransition(3);

		q.startTransition(3);
		q.eventName(B);
		q.addGuard(Guard.isLessThanOrEqualTo(b, a));
		q.addAssignment(Assignment.increment(b));
		q.endTransition(3);

		q.startTransition(3);
		q.eventName(C);
		q.addGuard(Guard.isEqual(a, b));
		q.addAssignment(Assignment.decrement(b));
		q.endTransition(4);

		q.startTransition(4);
		q.eventName(C);
		q.addGuard(Guard.varIsGreaterThanVal(b, 0));
		q.addAssignment(Assignment.decrement(b));
		q.endTransition(4);

		q.startTransition(4);
		q.eventName(C);
		q.addGuard(Guard.varIsEqualToIntVal(b, 0));
		q.endTransition(5);

		q.addFinalStates(5);

		QEA qea = q.make();
		qea.record_event_name("a", A);
		qea.record_event_name("b", B);
		qea.record_event_name("c", C);

		return qea;
	}

	public QEA makeFour() {
		return DaCapo.makeUnsafeMapIter();
	}

	public QEA makeFive() {
		// There isn't one
		return null;
	}

}
