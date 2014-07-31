package properties.competition;

import static structure.impl.other.Quantification.FORALL;
import properties.Property;
import properties.PropertyMaker;
import structure.intf.Assignment;
import structure.intf.Binding;
import structure.intf.Guard;
import structure.intf.QEA;
import creation.QEABuilder;

public class Larva implements PropertyMaker {

	@Override
	public QEA make(Property property) {
		switch (property) {
		case LARVA_ONE:
			return makeOne();
		case LARVA_TWO:
			return makeTwo();
		case LARVA_THREE:
			return makeThree();
		case LARVA_FOUR:
			return makeFour();
		case LARVA_FIVE:
			return makeFive();
		case LARVA_SIX:
			return makeSix();
		case LARVA_SEVEN:
			return makeSeven();
		case LARVA_EIGHT:
			return makeEight();
		case LARVA_NINE:
			return makeNine();
		case LARVA_TEN:
			return makeTen();
		}
		return null;
	}

	public QEA makeOne() {

		QEABuilder q = new QEABuilder("LARVA_ONE");

		int MAKE_GOLD_USER = 1;
		final int country = 1;

		q.startTransition(1);
		q.eventName(MAKE_GOLD_USER);
		q.addVarArg(country);
		q.addGuard(Guard.varIsNotEqualSemToVal(country, "Argentina"));
		q.endTransition(2);

		q.addFinalStates(1);
		q.setSkipStates(1);

		QEA qea = q.make();

		qea.record_event_name("make_gold_user", 1);

		return qea;
	}

	public QEA makeTwo() {

		QEABuilder q = new QEABuilder("LARVA_TWO");

		int INITIALISE = 1;
		int USER_LOGIN = 2;

		q.addTransition(1, INITIALISE, 2);
		q.addTransition(2, USER_LOGIN, 2);

		q.addFinalStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("initialise", 1);
		qea.record_event_name("user_login", 2);

		return qea;
	}

	public QEA makeThree() {
		
		QEABuilder q = new QEABuilder("LARVA_THREE");

		int TRANSACTION = 1;
		int balance = 1;

		q.startTransition(1);
		q.eventName(TRANSACTION);
		q.addVarArg(balance);
		q.addGuard(Guard.doubleVarIsGreaterThanOrEqualToVal(balance, 0));
		q.endTransition(1);

		q.addFinalStates(1);

		QEA qea = q.make();

		qea.record_event_name("transaction", 1);

		return qea;
	}

	public QEA makeFour() {

		QEABuilder q = new QEABuilder("LARVA_FOUR");

		int APPROVE = 1;
		int accountId = 1;
		int S = 2;

		q.startTransition(1);
		q.eventName(APPROVE);
		q.addVarArg(accountId);
		q.addAssignment(Assignment.createSetFromElement(S, accountId));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(APPROVE);
		q.addVarArg(accountId);
		q.addGuard(Guard.setNotContainsElement(accountId, S));
		q.addAssignment(Assignment.addElementToSet(S, accountId));
		q.endTransition(2);

		q.addFinalStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("approve", 1);

		return qea;
	}

	public QEA makeFive() {

		QEABuilder q = new QEABuilder("LARVA_FIVE");

		int WITHDRAW = 1;
		int DISABLE = 2;
		int ACTIVATE = 3;
		int u = -1;

		q.addQuantification(FORALL, u);

		q.addTransition(1, ACTIVATE, new int[] { u }, 1);
		q.addTransition(1, WITHDRAW, new int[] { u }, 1);
		q.addTransition(1, DISABLE, new int[] { u }, 2);
		q.addTransition(2, DISABLE, new int[] { u }, 2);
		q.addTransition(2, ACTIVATE, new int[] { u }, 1);

		q.addFinalStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("withdraw", 1);
		qea.record_event_name("disable", 2);
		qea.record_event_name("activate", 3);

		return qea;
	}

	public QEA makeSix() {

		QEABuilder q = new QEABuilder("LARVA_SIX");

		int TRANSFER = 1;
		int GREY_LIST = 2;
		int WHITE_LIST = 3;

		int u = -1;
		q.addQuantification(FORALL, u);

		q.addTransition(1, TRANSFER, new int[] { u }, 1);
		q.addTransition(1, WHITE_LIST, new int[] { u }, 1);
		q.addTransition(1, GREY_LIST, new int[] { u }, 2);
		q.addTransition(2, TRANSFER, new int[] { u }, 3);
		q.addTransition(3, TRANSFER, new int[] { u }, 4);
		q.addTransition(4, TRANSFER, new int[] { u }, 5);
		q.addTransition(5, TRANSFER, new int[] { u }, 5);
		q.addTransition(5, WHITE_LIST, new int[] { u }, 1);

		q.addFinalStates(1, 2, 3, 4, 5);

		QEA qea = q.make();

		qea.record_event_name("transfer", 1);
		qea.record_event_name("grey_list", 2);
		qea.record_event_name("white_list", 3);

		return qea;
	}

	public QEA makeSeven() {

		QEABuilder q = new QEABuilder("LARVA_SEVEN");

		int NEW_ACCOUNT = 1;

		int user = -1;
		int sid = -2;

		int n = 1;

		q.addQuantification(FORALL, user);
		q.addQuantification(FORALL, sid);

		q.startTransition(1);
		q.eventName(NEW_ACCOUNT);
		q.addVarArg(user);
		q.addVarArg(sid);
		q.addAssignment(Assignment.setVal(n, 1));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(NEW_ACCOUNT);
		q.addVarArg(user);
		q.addVarArg(sid);
		q.addGuard(Guard.varIsLessThanVal(n, 10));
		q.addAssignment(Assignment.increment(n));
		q.endTransition(2);

		q.addFinalStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("new_account", 1);

		return qea;
	}

	public QEA makeEight() {

		QEABuilder q = new QEABuilder("LARVA_EIGHT");

		int TRANSFER = 1;
		int RECONCILE = 2;

		final int amount = 1;
		final int count = 2;
		final int total = 3;

		q.startTransition(1);
		q.eventName(TRANSFER);
		q.addVarArg(amount);
		q.addAssignment(new Assignment("x_" + count + " = 1; x_" + total
				+ " = x_" + amount) {

			@Override
			public int[] vars() {
				return new int[] { count, total, amount };
			}

			@Override
			public Binding apply(Binding binding, boolean copy) {

				double amountVal = (double) binding.getForced(amount);
				Binding result = copy ? binding.copy() : binding;
				result.setValue(count, 1);
				result.setValue(total, amountVal);
				return result;
			}
		});
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(TRANSFER);
		q.addVarArg(amount);
		q.addGuard(new Guard("x_" + count + " < 1000; x_" + total
				+ " < 1000000") {

			@Override
			public int[] vars() {
				return new int[] { count, total };
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

				int countVal = binding.getForcedAsInteger(count);
				double totalVal = (double) binding.getForced(total);
				if (countVal < 1000 && totalVal < 1000000) {
					return true;
				}
				return false;
			}
		});
		q.addAssignment(new Assignment("x_" + count + "++; x_" + total
				+ " += x_" + amount) {

			@Override
			public int[] vars() {
				return new int[] { amount, count, total };
			}

			@Override
			public Binding apply(Binding binding, boolean copy) {

				int countVal = binding.getForcedAsInteger(count);
				double amountVal = (double) binding.getForced(amount);
				double totalVal = (double) binding.getForced(total);

				Binding result = copy ? binding.copy() : binding;
				result.setValue(count, countVal + 1);
				result.setValue(total, totalVal + amountVal);
				return result;
			}
		});
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(RECONCILE);
		q.addAssignment(new Assignment("x_" + total + " = 0; x_" + count
				+ " = 0") {

			@Override
			public int[] vars() {
				return new int[] { total, count };
			}

			@Override
			public Binding apply(Binding binding, boolean copy) {
				Binding result = copy ? binding.copy() : binding;
				binding.setValue(total, Double.valueOf(0));
				binding.setValue(count, 0);
				return result;
			}
		});
		q.endTransition(2);

		q.addFinalStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("transfer", 1);
		qea.record_event_name("reconcile", 2);

		return qea;
	}

	public QEA makeNine() {

		QEABuilder q = new QEABuilder("LARVA_NINE");

		int OPEN_SESSION = 1;
		int CLOSE_SESSION = 2;

		int u = -1;

		q.addQuantification(FORALL, u);

		q.addTransition(1, OPEN_SESSION, new int[] { u }, 2);
		q.addTransition(2, OPEN_SESSION, new int[] { u }, 3);
		q.addTransition(2, CLOSE_SESSION, new int[] { u }, 1);
		q.addTransition(3, OPEN_SESSION, new int[] { u }, 4);
		q.addTransition(3, CLOSE_SESSION, new int[] { u }, 2);
		q.addTransition(4, CLOSE_SESSION, new int[] { u }, 3);

		q.addFinalStates(1, 2, 3, 4);

		QEA qea = q.make();

		qea.record_event_name("open_session", 1);
		qea.record_event_name("close_session", 2);

		return qea;
	}

	public QEA makeTen() {

		QEABuilder q = new QEABuilder("LARVA_TEN");

		int OPEN_SESSION = 1;
		int CLOSE_SESSION = 2;
		int LOG = 3;

		int s = -1;

		q.addQuantification(FORALL, s);

		q.addTransition(1, OPEN_SESSION, new int[] { s }, 2);
		q.addTransition(2, LOG, new int[] { s }, 2);
		q.addTransition(2, CLOSE_SESSION, new int[] { s }, 1);

		q.addFinalStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("open_session", 1);
		qea.record_event_name("close_session", 2);
		qea.record_event_name("log", 3);

		return qea;
	}

}
