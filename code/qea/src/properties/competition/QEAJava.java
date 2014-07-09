package properties.competition;

import static structure.impl.other.Quantification.FORALL;

import java.util.Collection;

import properties.Property;
import properties.PropertyMaker;
import properties.papers.HasNextQEA;
import structure.intf.Assignment;
import structure.intf.Binding;
import structure.intf.Guard;
import structure.intf.QEA;
import creation.QEABuilder;
import exceptions.ShouldNotHappenException;

public class QEAJava implements PropertyMaker {

	@Override
	public QEA make(Property property) {
		switch (property) {
		case QEA_JAVA_ONE:
			return makeOne();
		case QEA_JAVA_TWO:
			return makeTwo();
		case QEA_JAVA_THREE:
			return makeThree();
		case QEA_JAVA_FOUR:
			return makeFour();
		}
		return null;
	}

	public QEA makeOne() {
		return new HasNextQEA();
	}

	public QEA makeTwo() {

		QEABuilder q = new QEABuilder("ExtendedHasNext");

		int ITERATOR = 1;
		int NEXT = 2;

		final int i = -1;
		final int s = 1;
		final int c = 2;

		q.addQuantification(FORALL, i);

		q.startTransition(1);
		q.eventName(ITERATOR);
		q.addVarArg(c);
		q.addVarArg(i);
		q.addAssignment(new Assignment("x_" + s + " = x_ " + c + ".size()") {

			@Override
			public int[] vars() {
				return new int[] { s, c };
			}

			@Override
			public Binding apply(Binding binding, boolean copy) {
				Collection cVal = (Collection) binding.getForced(c);
				Binding result = copy ? binding.copy() : binding;
				result.setValue(s, cVal.size());
				return result;
			}
		});
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(NEXT);
		q.addVarArg(i);
		q.addGuard(Guard.isGreaterThanConstant(s, 0));
		q.addAssignment(Assignment.decrement(s));
		q.endTransition(2);

		q.addFinalStates(1, 2);
		q.setSkipStates(1);

		QEA qea = q.make();

		qea.record_event_name("iterator", 1);
		qea.record_event_name("next", 2);

		return qea;
	}

	public QEA makeThree() {

		QEABuilder q = new QEABuilder("ExtendedHasNext");

		int ADD = 1;
		int OBSERVE = 2;
		int REMOVE = 3;

		final int s = -1;
		final int o = -2;
		final int h = 1;

		q.addQuantification(FORALL, s);
		q.addQuantification(FORALL, o);

		q.startTransition(1);
		q.eventName(ADD);
		q.addVarArg(s);
		q.addVarArg(o);
		q.addAssignment(new Assignment("h = o.hashCode") {

			@Override
			public int[] vars() {
				return new int[] { h, o };
			}

			@Override
			public Binding apply(Binding binding, boolean copy) {
				// TODO We need the value of the qVar here!!!
				return null;
			}
		});
		q.endTransition(2);

		Guard gHashCode = new Guard("h == o.hashCode()") {

			@Override
			public int[] vars() {
				return new int[] { h, o };
			}

			@Override
			public boolean usesQvars() {
				return true;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				if (qvar == o) {
					if (binding.getForcedAsInteger(h) == firstQval.hashCode()) {
						return true;
					}
					return false;
				}
				throw new ShouldNotHappenException(
						"This guard needs the variable x_" + o
								+ " to be checked");
			}

			@Override
			public boolean check(Binding binding) {
				throw new ShouldNotHappenException(
						"This guard needs a quantified variable to be checked");
			}
		};

		q.startTransition(2);
		q.eventName(OBSERVE);
		q.addVarArg(s);
		q.addVarArg(o);
		q.addGuard(gHashCode);
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(REMOVE);
		q.addVarArg(s);
		q.addVarArg(o);
		q.addGuard(gHashCode);
		q.endTransition(1);

		q.addFinalStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("add", 1);
		qea.record_event_name("observe", 2);
		qea.record_event_name("remove", 3);

		return qea;
	}

	public QEA makeFour() {

		QEABuilder q = new QEABuilder("DeadlockAvoidance");

		int LOCK = 1;
		int UNLOCK = 2;

		final int l1 = -1;
		final int l2 = -2;

		q.addQuantification(FORALL, l1);
		q.addQuantification(FORALL, l2, Guard.isNotEqual(l1, l2));

		q.addTransition(1, LOCK, new int[] { l1 }, 2);
		q.addTransition(2, UNLOCK, new int[] { l1 }, 1);
		q.addTransition(2, LOCK, new int[] { l2 }, 3);
		q.addTransition(3, LOCK, new int[] { l2 }, 4);
		q.addTransition(4, UNLOCK, new int[] { l2 }, 3);
		q.addTransition(4, LOCK, new int[] { l1 }, 5);

		q.addFinalStates(1, 2, 3, 4);
		q.setSkipStates(1, 2, 3, 4, 5);

		QEA qea = q.make();

		qea.record_event_name("lock", 1);
		qea.record_event_name("unlock", 2);

		return qea;
	}

}
