package properties.competition;

import static structure.impl.other.Quantification.FORALL;
import properties.Property;
import properties.PropertyMaker;
import properties.papers.DaCapo;
import properties.papers.HasNextQEA;
import structure.intf.Binding;
import structure.intf.Guard;
import structure.intf.QEA;
import creation.QEABuilder;
import exceptions.ShouldNotHappenException;

public class Prm4j implements PropertyMaker {

	@Override
	public QEA make(Property property) {

		switch (property) {
		case PRM4J_ONE:
			return makeOne();
		case PRM4J_TWO:
			return makeTwo();
		case PRM4J_THREE:
			return makeThree();
		case PRM4J_FOUR:
			return makeFour();
		case PRM4J_FIVE:
			return makeFive();
		case PRM4J_SIX:
			return makeSix();
		default:
			return null;
		}
	}

	private QEA makeOne() {
		return new HasNextQEA();
	}

	private QEA makeTwo() {

		QEABuilder q = new QEABuilder("SafeSyncCollection");

		int CREATE = 1;
		int ITERATOR = 2;
		int USE = 3;
		final int c = -1;
		final int i = -2;

		q.addTransition(1, CREATE, new int[] { c }, 2);

		q.startTransition(2);
		q.eventName(ITERATOR);
		q.addVarArg(i);
		q.addGuard(new Guard("!Thread.holdsLock(c)") {

			@Override
			public int[] vars() {
				return new int[] { c };
			}

			@Override
			public boolean usesQvars() {
				return true;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				// TODO How to express the condition: !Thread.holdsLock(c)
				return false;
			}

			@Override
			public boolean check(Binding binding) {
				throw new ShouldNotHappenException(
						"This guard needs a quantified variable to be checked");
			}
		});
		q.endTransition(3);

		q.startTransition(2);
		q.eventName(ITERATOR);
		q.addVarArg(i);
		q.addGuard(new Guard("Thread.holdsLock(c)") {

			@Override
			public int[] vars() {
				return new int[] { c };
			}

			@Override
			public boolean usesQvars() {
				return true;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				// TODO How to express the condition: Thread.holdsLock(c)
				return false;
			}

			@Override
			public boolean check(Binding binding) {
				throw new ShouldNotHappenException(
						"This guard needs a quantified variable to be checked");
			}
		});
		q.endTransition(4);

		q.startTransition(4);
		q.eventName(USE);
		q.addVarArg(i);
		q.addGuard(new Guard("!Thread.holdsLock(c)") {

			@Override
			public int[] vars() {
				return new int[] { c };
			}

			@Override
			public boolean usesQvars() {
				return true;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				// TODO How to express the condition: !Thread.holdsLock(c)
				return false;
			}

			@Override
			public boolean check(Binding binding) {
				throw new ShouldNotHappenException(
						"This guard needs a quantified variable to be checked");
			}
		});
		q.endTransition(5);

		q.addFinalStates(1, 2, 4);
		q.setSkipStates(1, 2, 3, 4, 5);

		QEA qea = q.make();

		qea.record_event_name("create", 1);
		qea.record_event_name("iterator", 2);
		qea.record_event_name("use", 3);

		return qea;
	}

	private QEA makeThree() {

		QEABuilder q = new QEABuilder("SafeSyncMap");

		final int CREATE = 1;
		final int ITERATOR = 2;
		final int USE = 3;

		final int m = -1;
		final int c = -2;
		final int i = -3;
		final int m1 = 1;

		q.addQuantification(FORALL, m);
		q.addQuantification(FORALL, c);
		q.addQuantification(FORALL, i);

		q.addTransition(1, CREATE, new int[] { m1, m }, 2);
		q.addTransition(2, CREATE, new int[] { m, c }, 3);

		q.startTransition(3);
		q.eventName(ITERATOR);
		q.addGuard(new Guard("!Thread.holdsLock(c)") {

			@Override
			public int[] vars() {
				return new int[] { c };
			}

			@Override
			public boolean usesQvars() {
				return true;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				// TODO How to express the condition: !Thread.holdsLock(c)
				return false;
			}

			@Override
			public boolean check(Binding binding) {
				throw new ShouldNotHappenException(
						"This guard needs a quantified variable to be checked");
			}
		});

		return null;
	}

	private QEA makeFour() {
		// Taken from Giles' thesis, A.16
		return DaCapo.makeUnsafeIter();
	}

	private QEA makeFive() {
		// Similar to Giles' thesis A.17
		return DaCapo.makeUnsafeMapIter();
	}

	private QEA makeSix() {
		// TODO Auto-generated method stub
		return null;
	}

}
