package properties.papers;

import static structure.impl.other.Quantification.FORALL;
import properties.Property;
import properties.PropertyMaker;
import structure.intf.Assignment;
import structure.intf.Guard;
import structure.intf.QEA;
import creation.QEABuilder;

public class DaCapo implements PropertyMaker {

	@Override
	public QEA make(Property property) {
		return null;
	}

	public static QEA makeUnsafeIter() {

		QEABuilder b = new QEABuilder("unsafe_iterator");

		int c = -1;
		int i = -2;

		int ITERATOR = 1;
		int USE = 2;
		int UPDATE = 3;

		b.addQuantification(FORALL, c);
		b.addQuantification(FORALL, i);

		b.addTransition(1, ITERATOR, new int[] { c, i }, 2);
		b.addTransition(2, UPDATE, new int[] { c }, 3);
		b.addTransition(3, USE, new int[] { i }, 4);

		b.setSkipStates(1, 2, 3);
		b.addFinalStates(1, 2, 3);

		QEA qea = b.make();

		qea.record_event_name("iterator", ITERATOR);
		qea.record_event_name("use", USE);
		qea.record_event_name("update", UPDATE);

		return qea;
	}

	public static QEA makeUnsafeMapIter() {

		QEABuilder b = new QEABuilder("unsafe_map_iterator");

		int m = -1;
		int c = -2;
		int i = -3;

		int CREATE = 1;
		int ITERATOR = 2;
		int USE = 3;
		int UPDATE = 4;

		b.addQuantification(FORALL, m);
		b.addQuantification(FORALL, c);
		b.addQuantification(FORALL, i);

		b.addTransition(1, CREATE, new int[] { m, c }, 2);
		b.addTransition(2, ITERATOR, new int[] { c, i }, 3);
		b.addTransition(3, UPDATE, new int[] { m }, 4);
		b.addTransition(4, USE, new int[] { i }, 5);

		b.setSkipStates(1, 2, 3, 4);
		b.addFinalStates(1, 2, 3, 4);

		QEA qea = b.make();

		qea.record_event_name("create", CREATE);
		qea.record_event_name("iterator", ITERATOR);
		qea.record_event_name("use", USE);
		qea.record_event_name("update", UPDATE);

		return qea;
	}

	public static QEA makeSafeIterator() {
		QEABuilder b = new QEABuilder("safe_iterator");

		int i = -1;

		int count = 1;

		int ITERATOR = 1;
		int NEXT = 2;

		b.addQuantification(FORALL, i);

		b.addTransition(1, ITERATOR, new int[] { i, count }, 2);
		b.startTransition(2);
		b.eventName(NEXT);
		b.addVarArg(i);
		b.addGuard(Guard.isGreaterThanConstant(count, 0));
		b.addAssignment(Assignment.decrement(count));
		b.endTransition(2);

		b.addFinalStates(1, 2);

		QEA qea = b.make();

		qea.record_event_name("iterator", ITERATOR);
		qea.record_event_name("next", NEXT);

		return qea;
	}

	public static QEA makeLockOrdering() {
		QEABuilder b = new QEABuilder("lock_ordering");

		int l1 = -1;
		int l2 = -2;

		int LOCK = 1;
		int UNLOCK = 2;

		b.addQuantification(FORALL, l1);
		b.addQuantification(FORALL, l2, Guard.isIdentityLessThan(l1, l2));

		// As we use next states we'll be symmetric
		// which is why we have the < global guard

		// l1 goes first
		b.addTransition(1, LOCK, l1, 2);
		b.addTransition(2, UNLOCK, l1, 1);
		b.addTransition(2, LOCK, l2, 3);
		b.addTransition(3, UNLOCK, l2, 3);
		b.addTransition(3, LOCK, l1, 3);
		b.addTransition(3, UNLOCK, l1, 3);
		b.addTransition(3, LOCK, l2, 4);
		b.addTransition(4, UNLOCK, l2, 3);
		b.addTransition(4, LOCK, l1, 5); // State 5 is failure

		// l2 goes first
		b.addTransition(1, LOCK, l2, 6);
		b.addTransition(6, UNLOCK, l2, 1);
		b.addTransition(6, LOCK, l1, 7);
		b.addTransition(7, UNLOCK, l1, 7);
		b.addTransition(7, LOCK, l2, 7);
		b.addTransition(7, UNLOCK, l2, 7);
		b.addTransition(7, LOCK, l1, 8);
		b.addTransition(8, UNLOCK, l1, 7);
		b.addTransition(8, LOCK, l2, 5); // State 5 is failure

		b.addFinalStates(1, 2, 3, 4, 6, 7, 8);

		QEA qea = b.make();

		qea.record_event_name("lock", LOCK);
		qea.record_event_name("unlock", UNLOCK);

		return qea;
	}

	/*
	 * I've had a go at making this a Single property based on the realisation
	 * that we just need to know that an object is inside a collection, not
	 * which collection it is inside
	 */
	public static QEA makePersistentHashes() {
		QEABuilder b = new QEABuilder("persistent_hashes");

		int o = -1;

		int hash = 1;
		int count_inside = 2;
		int success = 3;
		int hash_new = 4;

		int ADD = 1;
		int OBSERVE = 2;
		int REMOVE = 3;

		b.addQuantification(FORALL, o);

		b.startTransition(1);
		b.eventName(ADD);
		b.addVarArg(o);
		b.addVarArg(hash);
		b.addVarArg(success);// should only take if add is suc
		b.addGuard(Guard.isTrue(success));
		b.addAssignment(Assignment.setVal(count_inside, 1));
		b.endTransition(2);

		b.startTransition(2);
		b.eventName(ADD);
		b.addVarArg(o);
		b.addVarArg(hash_new);
		b.addVarArg(success); // should only increment if add is suc
		b.addGuard(Guard.and(Guard.isTrue(success),
				Guard.isEqualSem(hash, hash_new)));
		b.addAssignment(Assignment.increment(count_inside));
		b.endTransition(2);

		b.startTransition(2);
		b.eventName(REMOVE);
		b.addVarArg(o);
		b.addVarArg(hash_new);
		b.addVarArg(success); // should only decrement if the remove is suc
		b.addGuard(Guard.and(
				Guard.and(Guard.isTrue(success),
						Guard.isGreaterThanConstant(count_inside, 1)),
				Guard.isEqualSem(hash, hash_new)));
		b.addAssignment(Assignment.decrement(count_inside));
		b.endTransition(2);

		b.startTransition(2);
		b.eventName(REMOVE);
		b.addVarArg(o);
		b.addVarArg(hash_new);
		b.addVarArg(success); // should only decrement if the remove is suc
		b.addGuard(Guard.and(
				Guard.and(Guard.isTrue(success),
						Guard.isSemEqualToConstant(count_inside, 1)),
				Guard.isEqualSem(hash, hash_new)));
		b.addAssignment(Assignment.decrement(count_inside));
		b.endTransition(1);

		b.startTransition(2);
		b.eventName(OBSERVE);
		b.addVarArg(o);
		b.addVarArg(hash_new);
		b.addGuard(Guard.isEqualSem(hash, hash_new));
		b.endTransition(2);

		b.addFinalStates(1, 2);

		QEA qea = b.make();

		qea.record_event_name("add", ADD);
		qea.record_event_name("observe", OBSERVE);
		qea.record_event_name("remove", REMOVE);

		return qea;
	}

}
