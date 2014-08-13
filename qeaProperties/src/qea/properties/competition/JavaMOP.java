package qea.properties.competition;

import static qea.structure.impl.other.Quantification.FORALL;
import qea.properties.Property;
import qea.properties.PropertyMaker;
import qea.properties.papers.DaCapo;
import qea.properties.papers.HasNextQEA;
import qea.structure.intf.Assignment;
import qea.structure.intf.Guard;
import qea.structure.intf.QEA;
import qea.creation.QEABuilder;

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
		}
		return null;
	}

	public QEA makeOne() {
		// This is just the HasNext property we know and love
		QEA qea = new HasNextQEA();
		qea.setName("JAVAMOP_ONE");
		return qea;
	}

	public QEA makeTwo() {

		/*
		 * Note - this has a disjoint alphabet, we should implement this
		 * optimisation!
		 */

		QEABuilder q = new QEABuilder("JAVAMOP_TWO");

		int LOCK = 1;
		int UNLOCK = 2;
		int thread = -1;
		int lock = -2;
		int count = 1;

		q.addQuantification(FORALL, thread);
		q.addQuantification(FORALL, lock);

		q.addTransition(1, LOCK, new int[] { thread, lock },
				Assignment.setVal(count, 1), 2);
		q.addTransition(2, LOCK, new int[] { thread, lock },
				Assignment.increment(count), 2);
		q.addTransition(2, UNLOCK, new int[] { thread, lock },
				Guard.isGreaterThanConstant(count, 1),
				Assignment.decrement(count), 2);
		q.addTransition(2, UNLOCK, new int[] { thread, lock },
				Guard.isSemEqualToConstant(count, 1),
				Assignment.setVal(count, 0), 1);

		q.addFinalStates(1);

		QEA qea = q.make();
		qea.record_event_name("lock", LOCK);
		qea.record_event_name("unlock", UNLOCK);
		return qea;
	}

	public QEA makeThree() {

		QEABuilder q = new QEABuilder("JAVAMOP_THREE");

		int A = 1;
		int B = 2;
		int C = 3;
		int a = 1;
		int b = 2;
		int c = 3;

		// If we move away from state 1 they are no longer equal
		q.addTransition(
				1,
				A,
				Assignment.list(Assignment.incrementOrSet(a),
								Assignment.ensure(b, 0),
								Assignment.ensure(c, 0)), 2);
		q.addTransition(
				1,
				B,
				Assignment.list(Assignment.incrementOrSet(b),
								Assignment.ensure(a, 0),
								Assignment.ensure(c, 0)), 2);
		q.addTransition(
				1,
				C,
				Assignment.list(Assignment.incrementOrSet(c),
								Assignment.ensure(a, 0),
								Assignment.ensure(b, 0)), 2);

		// If we are one less than the two others then we go to state 1
		q.addTransition(
				2,
				A,
				Guard.and(Guard.differenceEqualToVal(b, a, 1),
						Guard.differenceEqualToVal(c, a, 1)),
				Assignment.increment(a), 1);
		q.addTransition(
				2,
				B,
				Guard.and(Guard.differenceEqualToVal(a, b, 1),
						Guard.differenceEqualToVal(c, b, 1)),
				Assignment.increment(b), 1);
		q.addTransition(
				2,
				C,
				Guard.and(Guard.differenceEqualToVal(a, c, 1),
						Guard.differenceEqualToVal(b, c, 1)),
				Assignment.increment(c), 1);

		// Otherwise we stay still and increment
		q.addTransition(
				2,
				A,
				Guard.or(Guard.differenceNotEqualToVal(b, a, 1),
						Guard.differenceNotEqualToVal(c, a, 1)),
				Assignment.increment(a), 2);
		q.addTransition(
				2,
				B,
				Guard.or(Guard.differenceNotEqualToVal(a, b, 1),
						Guard.differenceNotEqualToVal(c, b, 1)),
				Assignment.increment(b), 2);
		q.addTransition(
				2,
				C,
				Guard.or(Guard.differenceNotEqualToVal(a, c, 1),
						Guard.differenceNotEqualToVal(b, c, 1)),
				Assignment.increment(c), 2);

		q.addFinalStates(1);

		QEA qea = q.make();
		qea.record_event_name("a", A);
		qea.record_event_name("b", B);
		qea.record_event_name("c", C);

		return qea;
	}

	public QEA makeFour() {
		QEA qea = DaCapo.makeUnsafeMapIter();
		qea.setName("JAVAMOP_FOUR");
		return qea;
	}

}
