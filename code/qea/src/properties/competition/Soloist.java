package properties.competition;

import java.util.HashSet;
import java.util.Iterator;

import properties.Property;
import properties.PropertyMaker;
import structure.intf.Assignment;
import structure.intf.Binding;
import structure.intf.Guard;
import structure.intf.QEA;
import creation.QEABuilder;

public class Soloist implements PropertyMaker {

	@Override
	public QEA make(Property property) {
		switch (property) {
		case SOLOIST_ONE:
			return makeOneSimple();
		case SOLOIST_TWO:
			return makeTwo();
		case SOLOIST_THREE:
			return makeThree();
		case SOLOIST_FOUR:
			return makeFour();
		}
		return null;
	}

	public QEA makeOneNegatedLimit3() {

		QEABuilder q = new QEABuilder("SOLOIST_ONE");
		q.setNegated(true);

		int WITHDRAW = 1;
		int LOGOFF = 2;

		int t1 = 1;
		int t2 = 2;
		int t3 = 3;
		int t4 = 4;
		int t = 5;

		// Transitions from state 1

		q.addTransition(1, WITHDRAW, new int[] { t1 }, 1);
		q.addTransition(1, WITHDRAW, new int[] { t1 }, 2);

		// Transitions from state 2

		q.startTransition(2);
		q.eventName(WITHDRAW);
		q.addVarArg(t2);
		q.addGuard(lessOrEqualToDiff(t1, t2, 600));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(WITHDRAW);
		q.addVarArg(t2);
		q.addGuard(lessOrEqualToDiff(t1, t2, 600));
		q.endTransition(3);

		q.startTransition(2);
		q.eventName(WITHDRAW);
		q.addVarArg(t2);
		q.addGuard(moreThanDiff(t1, t2, 600));
		q.endTransition(6);

		// Transitions from state 3

		q.startTransition(3);
		q.eventName(WITHDRAW);
		q.addVarArg(t3);
		q.addGuard(lessOrEqualToDiff(t1, t3, 600));
		q.endTransition(3);

		q.startTransition(3);
		q.eventName(WITHDRAW);
		q.addVarArg(t3);
		q.addGuard(lessOrEqualToDiff(t1, t3, 600));
		q.endTransition(4);

		q.startTransition(3);
		q.eventName(WITHDRAW);
		q.addVarArg(t3);
		q.addGuard(moreThanDiff(t1, t3, 600));
		q.endTransition(6);

		// Transitions from state 4

		q.startTransition(4);
		q.eventName(WITHDRAW);
		q.addVarArg(t3);
		q.addGuard(lessOrEqualToDiff(t1, t3, 600));
		q.endTransition(4);

		q.startTransition(4);
		q.eventName(WITHDRAW);
		q.addVarArg(t3);
		q.addGuard(lessOrEqualToDiff(t1, t3, 600));
		q.endTransition(5);

		q.startTransition(4);
		q.eventName(WITHDRAW);
		q.addVarArg(t3);
		q.addGuard(moreThanDiff(t1, t3, 600));
		q.endTransition(6);

		// Transitions from state 5

		q.startTransition(5);
		q.eventName(WITHDRAW);
		q.addVarArg(t4);
		q.addGuard(moreThanDiff(t1, t3, 600));
		q.endTransition(6);

		q.startTransition(5);
		q.eventName(LOGOFF);
		q.addVarArg(t);
		q.addGuard(lessOrEqualToDiff(t1, t, 600));
		q.endTransition(7);

		q.addFinalStates(7);
		q.setSkipStates(1, 2, 3, 4, 5, 6, 7);

		QEA qea = q.make();

		qea.record_event_name("repwidraw", 1);
		qea.record_event_name("recvlogoff", 2);

		return qea;
	}

	private static Guard lessOrEqualToDiff(final int var0, final int var1,
			final int diff) {
		return new Guard("x_" + var1 + " - x_" + var0 + " <= " + diff) {

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {

				int var0Val = (Integer) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				int var1Val = (Integer) (var1 == qvar ? firstQval : binding
						.getForced(var1));
				return var1Val - var0Val <= diff;
			}

			@Override
			public boolean check(Binding binding) {

				int var0Val = binding.getForcedAsInteger(var0);
				int var1Val = binding.getForcedAsInteger(var1);
				return var1Val - var0Val <= diff;
			}
		};
	}

	private static Guard moreThanDiff(final int var0, final int var1,
			final int diff) {
		return new Guard("x_" + var1 + " - x_" + var0 + " > " + diff) {

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {

				int var0Val = (Integer) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				int var1Val = (Integer) (var1 == qvar ? firstQval : binding
						.getForced(var1));
				return var1Val - var0Val > diff;
			}

			@Override
			public boolean check(Binding binding) {

				int var0Val = binding.getForcedAsInteger(var0);
				int var1Val = binding.getForcedAsInteger(var1);
				return var1Val - var0Val > diff;
			}
		};
	}

	public QEA makeOneNegatedLimitN() {

		QEABuilder q = new QEABuilder("SOLOIST_ONE");
		q.setNegated(true);

		int WITHDRAW = 1;
		int LOGOFF = 2;

		int count = 1;
		int t1 = 2;
		int t2 = 3;
		int _ = 4;
		int t = 5;

		// Max number of withdrawal operations performed within 10 minutes
		// before logoff
		int LIMIT = 3;

		q.startTransition(1);
		q.eventName(WITHDRAW);
		q.addVarArg(t1);
		q.addAssignment(Assignment.set(count, 1));
		q.endTransition(2);

		q.addTransition(1, WITHDRAW, new int[] { _ }, 1);

		q.startTransition(2);
		q.eventName(WITHDRAW);
		q.addVarArg(t2);
		q.addGuard(Guard.and(lessOrEqualToDiff(t1, t2, 600),
				Guard.varIsLessThanVal(count, LIMIT)));
		q.addAssignment(Assignment.increment(count));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(WITHDRAW);
		q.addVarArg(t2);
		q.addGuard(Guard.and(lessOrEqualToDiff(t1, t2, 600),
				Guard.varIsEqualToIntVal(count, LIMIT)));
		q.addAssignment(Assignment.increment(count));
		q.endTransition(3);

		q.startTransition(2);
		q.eventName(WITHDRAW);
		q.addVarArg(t2);
		q.addGuard(moreThanDiff(t1, t2, 600));
		q.endTransition(4);

		q.startTransition(3);
		q.eventName(WITHDRAW);
		q.addVarArg(t);
		q.addGuard(moreThanDiff(t1, t, 600));
		q.endTransition(4);

		q.startTransition(3);
		q.eventName(LOGOFF);
		q.addVarArg(t);
		q.addGuard(lessOrEqualToDiff(t1, t, 600));
		q.endTransition(5);

		q.addFinalStates(5);
		q.setSkipStates(1, 2, 3, 4, 5);

		QEA qea = q.make();

		qea.record_event_name("repwidraw", 1);
		qea.record_event_name("recvlogoff", 2);

		return qea;
	}

	public QEA makeOneSimple() {

		QEABuilder q = new QEABuilder("SOLOIST_ONE");

		int WITHDRAW = 1;
		int LOGOFF = 2;

		final int t = 1;
		final int set = 2;

		// Max number of withdrawal operations performed within 10 minutes
		// before logoff
		final int LIMIT = 3;

		q.startTransition(1);
		q.eventName(WITHDRAW);
		q.addVarArg(t);
		q.addAssignment(new Assignment("x_" + set + " += x_" + t
				+ "; remove any x_" + t + " from x_" + set
				+ " if it was more than 10m ago") {

			@Override
			public int[] vars() {
				return new int[] { t, set };
			}

			@Override
			public Binding apply(Binding binding, boolean copy) {

				Binding result = copy ? binding.copy() : binding;
				int tVal = binding.getForcedAsInteger(t);
				HashSet<Integer> setVal;
				if (binding.getValue(set) != null) {

					setVal = (HashSet<Integer>) binding.getValue(set);

					// Remove all times that were more than 10 minutes ago
					for (Iterator iterator = setVal.iterator(); iterator
							.hasNext();) {
						Integer time = (Integer) iterator.next();
						if (tVal - time > 600) {
							iterator.remove();
						}
					}
				} else {
					setVal = new HashSet<Integer>();
				}

				// Add the current time to the set
				setVal.add(tVal);
				result.setValue(set, setVal);
				return result;
			}
		});
		q.endTransition(1);

		q.startTransition(1);
		q.eventName(LOGOFF);
		q.addVarArg(t);
		q.addGuard(new Guard("size of x_" + set + " <= " + LIMIT) {

			@Override
			public int[] vars() {
				return new int[] { set };
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

				int count = 0;
				if (binding.getValue(set) != null) {
					HashSet<Integer> setVal = (HashSet<Integer>) binding
							.getValue(set);
					int tVal = binding.getForcedAsInteger(t);

					// Count number of elements within the last 10 minutes
					for (Iterator iterator = setVal.iterator(); iterator
							.hasNext();) {
						int time = (Integer) iterator.next();
						if (tVal - time <= 600) {
							count++;
						}
					}
				}
				return count <= LIMIT;
			}
		});
		q.endTransition(1);

		q.addFinalStates(1);

		QEA qea = q.make();

		qea.record_event_name("repwidraw", 1);
		qea.record_event_name("recvlogoff", 2);

		return qea;
	}

	public QEA makeTwo() {

		QEABuilder q = new QEABuilder("SOLOIST_TWO");
		q.setNegated(true);

		int INVCHECKACCESS_START = 1;
		int INVCHECKACCESS_COMPLETE = 2;

		final int t = 1;
		final int currStartTime = 2;
		final int totalTime = 3;
		final int execCount = 4;
		final int wdwStartTime = 5;

		q.addTransition(1, INVCHECKACCESS_START, new int[] { t }, 1);

		q.startTransition(1);
		q.eventName(INVCHECKACCESS_START);
		q.addVarArg(t);
		q.addAssignment(
				Assignment.list(Assignment.store(currStartTime, t),
								Assignment.set(totalTime,0),
								Assignment.set(execCount, 0),
								Assignment.store(wdwStartTime, t))
				);
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(INVCHECKACCESS_COMPLETE);
		q.addVarArg(t);
		q.addAssignment(
				Assignment.then(Assignment.storeDifference(totalTime, t,currStartTime),
								Assignment.increment(execCount))
		);				
		q.endTransition(3);

		q.startTransition(2);
		q.eventName(INVCHECKACCESS_COMPLETE);
		q.addVarArg(t);
		q.addGuard(new Guard("x_" + t + " - x_" + wdwStartTime
				+ " >= 15m and (x_" + totalTime + " + (x_" + t + " - x_"
				+ currStartTime + ")) / ((x_" + execCount + " + 1) >= 5") {

			public int[] vars() {return new int[] { t, wdwStartTime, totalTime, currStartTime,execCount };}
			public boolean usesQvars() {return false;}
			public boolean check(Binding binding, int qvar, Object firstQval) {return check(binding);}

			@Override
			public boolean check(Binding binding) {

				int tVal = binding.getForcedAsInteger(t);
				double tDoubleVal = tVal;
				int wdwStartTimeVal = binding.getForcedAsInteger(wdwStartTime);
				double totalTimeDoubleVal = binding.getForcedAsInteger(totalTime);
				double currStartTimeDoubleVal = binding.getForcedAsInteger(currStartTime);
				double execCountDoubleVal = binding.getForcedAsInteger(execCount);

				double avg = (totalTimeDoubleVal + (tDoubleVal - currStartTimeDoubleVal)) / (execCountDoubleVal + 1);

				return tVal - wdwStartTimeVal >= 900 && avg >= 5;
			}
		});
		q.addAssignment(Assignment.then(
				Assignment.addDifference(totalTime,t,currStartTime),
				Assignment.increment(execCount)
			));
		q.endTransition(5);

		q.startTransition(2);
		q.eventName(INVCHECKACCESS_COMPLETE);
		q.addVarArg(t);
		q.addGuard(Guard.DifferenceLessThanVal(t, wdwStartTime, 900));
		q.endTransition(4);

		q.startTransition(3);
		q.eventName(INVCHECKACCESS_START);
		q.addVarArg(t);
		q.addGuard(Guard.DifferenceGreaterThanOrEqualToVal(t, wdwStartTime, 900));
		q.addAssignment(Assignment.set(currStartTime, t));
		q.endTransition(2);

		q.startTransition(3);
		q.eventName(INVCHECKACCESS_START);
		q.addVarArg(t);
		q.addGuard(new Guard("x_" + t + " - x_" + wdwStartTime
				+ " >= 15m and x_" + totalTime + "/x_" + execCount + " >= 5") {

			public int[] vars() {return new int[] { t, wdwStartTime, totalTime, execCount };}
			public boolean usesQvars() {return false;}
			public boolean check(Binding binding, int qvar, Object firstQval) {return check(binding);}

			@Override
			public boolean check(Binding binding) {

				int tVal = binding.getForcedAsInteger(t);
				int wdwStartTimeVal = binding.getForcedAsInteger(wdwStartTime);
				double totalTimeDoubleVal = binding.getForcedAsInteger(totalTime);
				double execCountDoubleVal = binding.getForcedAsInteger(execCount);
				double avg = totalTimeDoubleVal / execCountDoubleVal;

				return tVal - wdwStartTimeVal >= 900 && avg >= 5;
			}
		});
		q.addAssignment(Assignment.addDifference(totalTime,t,currStartTime));
		q.endTransition(5);

		q.startTransition(3);
		q.eventName(INVCHECKACCESS_START);
		q.addVarArg(t);
		q.addGuard(Guard.DifferenceGreaterThanOrEqualToVal(t, wdwStartTime, 900));
		q.endTransition(4);

		q.addFinalStates(5);
		q.setSkipStates(1, 2, 3, 4, 5);

		QEA qea = q.make();

		qea.record_event_name("invcheckaccess_start", 1);
		qea.record_event_name("invcheckaccess_complete", 2);

		return qea;
	}

	public QEA makeThree() {

		QEABuilder q = new QEABuilder("SOLOIST_THREE");

		int INVCHECKACCESS_COMPLETE = 1;
		int REPLOGON = 2;

		q.addTransition(1, INVCHECKACCESS_COMPLETE, 2);
		q.addTransition(2, REPLOGON, 1);
		q.addTransition(2, INVCHECKACCESS_COMPLETE, 2);

		q.addFinalStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("invcheckaccess_complete", 1);
		qea.record_event_name("replogon", 2);

		return qea;
	}

	public QEA makeFour() {

		QEABuilder q = new QEABuilder("SOLOIST_FOUR");

		int INVCHECKACCESS_START = 1;
		int INVCHECKACCESS_COMPLETE = 2;

		final int t1 = 1;
		final int t2 = 2;

		q.addTransition(1, INVCHECKACCESS_START, new int[] { t1 }, 2);

		q.startTransition(2);
		q.eventName(INVCHECKACCESS_COMPLETE);
		q.addVarArg(t2);
		q.addGuard(new Guard("x_" + t2 + " - x_" + t1 + " <= 10") {

			@Override
			public int[] vars() {
				return new int[] { t1, t2 };
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
				int t1Val = binding.getForcedAsInteger(t1);
				int t2Val = binding.getForcedAsInteger(t2);
				return t2Val - t1Val <= 10;
			}
		});
		q.endTransition(1);

		q.addFinalStates(1);

		QEA qea = q.make();

		qea.record_event_name("invcheckaccess_start", 1);
		qea.record_event_name("invcheckaccess_complete", 2);

		return qea;
	}
}
