package properties.competition;

import static structure.impl.other.Quantification.FORALL;
import properties.Property;
import properties.PropertyMaker;
import properties.papers.DaCapo;
import properties.papers.HasNextQEA;
import structure.intf.Assignment;
import structure.intf.Binding;
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
				Guard.isSemEqualToConstant(count, 1), Assignment.setVal(count, 0),
				1);

		q.addFinalStates(1);

		QEA qea = q.make();
		qea.record_event_name("lock", LOCK);
		qea.record_event_name("unlock", UNLOCK);
		return qea;
	}

	public QEA makeThreeOld() {

		QEABuilder q = new QEABuilder("JAVAMOP_THREE");

		int A = 1;
		int B = 2;
		int C = 3;
		int a = 1;
		int b = 2;

		q.startTransition(1);
		q.eventName(A);
		q.addAssignment(Assignment.setVal(a, 1));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(A);
		q.addAssignment(Assignment.increment(a));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(B);
		q.addAssignment(Assignment.setVal(b, 1));
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

	public QEA makeThreeComplex() {

		QEABuilder q = new QEABuilder("JAVAMOP_THREE");

		final int A = 1;
		final int B = 2;
		final int C = 3;

		final int a = 1;
		final int b = 2;
		final int c = 3;

		// First level of transitions

		q.startTransition(1);
		q.eventName(A);
		q.addAssignment(Assignment.setVal(a, 1));
		q.endTransition(2);

		q.startTransition(1);
		q.eventName(B);
		q.addAssignment(Assignment.setVal(b, 1));
		q.endTransition(3);

		q.startTransition(1);
		q.eventName(C);
		q.addAssignment(Assignment.setVal(c, 1));
		q.endTransition(4);

		// Second level of transitions - From state 2 (A)

		q.startTransition(2);
		q.eventName(A);
		q.addAssignment(Assignment.increment(a));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(B);
		q.addAssignment(Assignment.setVal(b, 1));
		q.endTransition(5);

		q.startTransition(2);
		q.eventName(C);
		q.addAssignment(Assignment.setVal(c, 1));
		q.endTransition(6);

		// Second level of transitions - From state 3 (B)

		q.startTransition(3);
		q.eventName(A);
		q.addAssignment(Assignment.setVal(a, 1));
		q.endTransition(5);

		q.startTransition(3);
		q.eventName(B);
		q.addAssignment(Assignment.increment(b));
		q.endTransition(3);

		q.startTransition(3);
		q.eventName(C);
		q.addAssignment(Assignment.setVal(c, 1));
		q.endTransition(7);

		// Second level of transitions - From state 4 (C)

		q.startTransition(4);
		q.eventName(A);
		q.addAssignment(Assignment.setVal(a, 1));
		q.endTransition(6);

		q.startTransition(4);
		q.eventName(B);
		q.addAssignment(Assignment.setVal(b, 1));
		q.endTransition(7);

		q.startTransition(4);
		q.eventName(C);
		q.addAssignment(Assignment.increment(c));
		q.endTransition(4);

		// Third level of transitions - From state 5 (AB)

		q.startTransition(5);
		q.eventName(A);
		q.addAssignment(Assignment.increment(a));
		q.endTransition(5);

		q.startTransition(5);
		q.eventName(B);
		q.addAssignment(Assignment.increment(b));
		q.endTransition(5);

		q.startTransition(5);
		q.eventName(C);
		q.addGuard(Guard.or(Guard.varIsNotEqualSemToVal(a, 1),
				Guard.varIsNotEqualSemToVal(b, 1)));
		q.addAssignment(Assignment.setVal(c, 1));
		q.endTransition(8);

		q.startTransition(5);
		q.eventName(C);
		q.addGuard(Guard.and(Guard.isEqualSem(a, 1), Guard.isEqualSem(b, 1)));
		q.addAssignment(Assignment.setVal(c, 1));
		q.endTransition(9);

		// Third level of transitions - From state 6 (AC)

		q.startTransition(6);
		q.eventName(A);
		q.addAssignment(Assignment.increment(a));
		q.endTransition(6);

		q.startTransition(6);
		q.eventName(B);
		q.addGuard(Guard.or(Guard.varIsNotEqualSemToVal(a, 1),
				Guard.varIsNotEqualSemToVal(c, 1)));
		q.addAssignment(Assignment.setVal(b, 1));
		q.endTransition(8);

		q.startTransition(6);
		q.eventName(B);
		q.addGuard(Guard.and(Guard.isEqualSem(a, 1), Guard.isEqualSem(c, 1)));
		q.addAssignment(Assignment.setVal(b, 1));
		q.endTransition(9);

		q.startTransition(6);
		q.eventName(C);
		q.addAssignment(Assignment.increment(c));
		q.endTransition(6);

		// Third level of transitions - From state 7 (BC)

		q.startTransition(7);
		q.eventName(A);
		q.addGuard(Guard.or(Guard.varIsNotEqualSemToVal(b, 1),
				Guard.varIsNotEqualSemToVal(c, 1)));
		q.addAssignment(Assignment.setVal(a, 1));
		q.endTransition(8);

		q.startTransition(7);
		q.eventName(A);
		q.addGuard(Guard.and(Guard.isEqualSem(b, 1), Guard.isEqualSem(c, 1)));
		q.addAssignment(Assignment.setVal(a, 1));
		q.endTransition(9);

		q.startTransition(7);
		q.eventName(B);
		q.addAssignment(Assignment.increment(b));
		q.endTransition(7);

		q.startTransition(7);
		q.eventName(C);
		q.addAssignment(Assignment.increment(c));
		q.endTransition(7);

		// From state 8

		q.startTransition(8);
		q.eventName(A);
		q.addGuard(Guard.not(guardAllEqual(b, c, a)));
		q.addAssignment(Assignment.increment(a));
		q.endTransition(8);

		q.startTransition(8);
		q.eventName(A);
		q.addGuard(guardAllEqual(b, c, a));
		q.addAssignment(Assignment.increment(a));
		q.endTransition(9);

		q.startTransition(8);
		q.eventName(B);
		q.addGuard(Guard.not(guardAllEqual(a, c, b)));
		q.addAssignment(Assignment.increment(b));
		q.endTransition(8);

		q.startTransition(8);
		q.eventName(B);
		q.addGuard(guardAllEqual(a, c, b));
		q.addAssignment(Assignment.increment(b));
		q.endTransition(9);

		q.startTransition(8);
		q.eventName(C);
		q.addGuard(Guard.not(guardAllEqual(a, b, c)));
		q.addAssignment(Assignment.increment(c));
		q.endTransition(8);

		q.startTransition(8);
		q.eventName(C);
		q.addGuard(guardAllEqual(a, b, c));
		q.addAssignment(Assignment.increment(c));
		q.endTransition(9);

		// From state 9

		q.startTransition(9);
		q.eventName(A);
		q.addAssignment(Assignment.increment(a));
		q.endTransition(8);

		q.startTransition(9);
		q.eventName(B);
		q.addAssignment(Assignment.increment(b));
		q.endTransition(8);

		q.startTransition(9);
		q.eventName(C);
		q.addAssignment(Assignment.increment(c));
		q.endTransition(8);

		q.addFinalStates(1, 9);

		QEA qea = q.make();

		qea.record_event_name("A", A);
		qea.record_event_name("B", B);
		qea.record_event_name("C", C);

		return qea;
	}

	private Guard guardAllEqual(final int x, final int y, final int z) {
		return new Guard("x_" + x + " = x_" + y + " && x_" + z + " == x_" + x
				+ " - 1") {

			@Override
			public int[] vars() {
				return new int[] { x, y, z };
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

				int xVal = binding.getForcedAsInteger(x);
				int yVal = binding.getForcedAsInteger(y);
				int zVal = binding.getForcedAsInteger(z);

				return xVal == yVal && zVal == xVal - 1;
			}
		};
	}

	private Guard guardNotAllEqual(final int x, final int y, final int z) {
		return new Guard("x_" + x + " != x_" + y + " || x_" + z + " != x_" + x
				+ " - 1") {

			@Override
			public int[] vars() {
				return new int[] { x, y, z };
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

				int xVal = binding.getForcedAsInteger(x);
				int yVal = binding.getForcedAsInteger(y);
				int zVal = binding.getForcedAsInteger(z);

				return xVal != yVal || zVal != xVal - 1;
			}
		};
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
		q.addTransition(1,A,Assignment.list(Assignment.incrementOrSet(a),
											Assignment.ensure(b,0),
											Assignment.ensure(c,0)),2);
		q.addTransition(1,B,Assignment.list(Assignment.incrementOrSet(b),
											Assignment.ensure(a,0),
											Assignment.ensure(c,0)),2);
		q.addTransition(1,C,Assignment.list(Assignment.incrementOrSet(c),
											Assignment.ensure(a,0),
											Assignment.ensure(b,0)),2);

		//If we are one less than the two others then we go to state 1
		q.addTransition(2,A,Guard.and(Guard.differenceEqualToVal(b,a,1),
									  Guard.differenceEqualToVal(c,a,1)),
									  Assignment.increment(a),1);
		q.addTransition(2,B,Guard.and(Guard.differenceEqualToVal(a,b,1),
				  					  Guard.differenceEqualToVal(c,b,1)),
				  					  Assignment.increment(a),1);
		q.addTransition(2,C,Guard.and(Guard.differenceEqualToVal(a,c,1),
				  					  Guard.differenceEqualToVal(b,c,1)),
				  					  Assignment.increment(a),1);

		//Otherwise we stay still and increment
		q.addTransition(2,A,Guard.or(Guard.differenceNotEqualToVal(b,a,1),
				  					  Guard.differenceNotEqualToVal(c,a,1)),
				  					  Assignment.increment(a),2);
		q.addTransition(2,B,Guard.or(Guard.differenceNotEqualToVal(a,b,1),
				  					  Guard.differenceNotEqualToVal(c,b,1)),
				  					  Assignment.increment(a),2);
		q.addTransition(2,C,Guard.or(Guard.differenceNotEqualToVal(a,c,1),
				  					  Guard.differenceNotEqualToVal(b,c,1)),
				  					  Assignment.increment(a),2);
		
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
