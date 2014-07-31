package properties.rovers;

import static structure.impl.other.Quantification.EXISTS;
import static structure.impl.other.Quantification.FORALL;

import java.util.HashSet;

import structure.intf.Assignment;
import structure.intf.Binding;
import structure.intf.Guard;
import structure.intf.QEA;
import creation.QEABuilder;

/*
 * External
 * - ExactlyOneSuccess
 * - IncreasingCommand 
 * - NestedCommand
 * - AcknowledgeCommand
 * - ExistsSatellite
 * - ExistsLeader
 * - MessageHashCorrect
 * 
 * Internal
 * - GrantCancel
 * - ResourceLifecycle
 * - ReleaseResource
 * - RespectConflicts
 * - RespectPriorities 
 * 
 */

public class RoverCaseStudy {

	public static QEA makeGrantCancelSingleSwitch() {

		QEABuilder q = new QEABuilder("GrantCancelSingleSwitch");

		int GRANT = 1;
		int CANCEL = 2;
		int T1 = 1;
		int T2 = 2;
		int R = -1;

		q.addQuantification(FORALL, R);

		q.addTransition(1, GRANT, new int[] { R, T1 }, 2);
		q.addTransition(2, GRANT, new int[] { R, T2 }, 3);

		q.startTransition(2);
		q.eventName(CANCEL);
		q.addVarArg(R);
		q.addVarArg(T2);
		q.addGuard(Guard.isEqual(T1, T2));
		q.endTransition(1);

		q.addFinalStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("grant", 1);
		qea.record_event_name("cancel", 2);

		return qea;
	}

	public static QEA makeGrantCancelSingle() { // Figure A.25 (2)

		QEABuilder q = new QEABuilder("GrantCancelSingle");

		int GRANT = 1;
		int CANCEL = 2;
		int T1 = 1;
		int T2 = 2;
		int R = -1;

		q.addQuantification(FORALL, R);

		q.addTransition(1, GRANT, new int[] { T1, R }, 2);
		// TODO In the thesis, the parameters are: (-, R)
		q.addTransition(2, GRANT, new int[] { T2, R }, 3);

		q.startTransition(2);
		q.eventName(CANCEL);
		q.addVarArg(T2);
		q.addVarArg(R);
		q.addGuard(Guard.isEqual(T1, T2));
		q.endTransition(1);

		q.addFinalStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("grant", 1);
		qea.record_event_name("cancel", 2);

		return qea;
	}

	public static QEA makeGrantCancelDouble() { // Figure A.25 (1)

		QEABuilder q = new QEABuilder("GrantCancelDouble");

		int GRANT = 1;
		int CANCEL = 2;
		int R = -1;
		int T = -2;
		int TT = 1;

		q.addQuantification(FORALL, R);
		q.addQuantification(FORALL, T);

		q.addTransition(1, GRANT, new int[] { T, R }, 2);
		// TODO In the thesis, the parameters are: (-, R)
		q.addTransition(2, GRANT, new int[] { TT, R }, 3);
		q.addTransition(2, CANCEL, new int[] { T, R }, 1);

		q.addFinalStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("grant", 1);
		qea.record_event_name("cancel", 2);

		return qea;
	}

	public static QEA makeResourceLifecycle() { // Figure A.26

		QEABuilder q = new QEABuilder("ResourceLifecycle");

		int REQUEST = 1;
		int GRANT = 2;
		int DENY = 3;
		int RESCIND = 4;
		int CANCEL = 5;
		int R = -1;

		q.addQuantification(FORALL, R);

		int[] r = new int[] { R };
		q.addTransition(1, REQUEST, r, 2);
		q.addTransition(2, DENY, r, 1);
		q.addTransition(2, GRANT, r, 3);
		q.addTransition(3, RESCIND, r, 3);
		q.addTransition(3, CANCEL, r, 1);

		q.addFinalStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("request", 1);
		qea.record_event_name("grant", 2);
		qea.record_event_name("deny", 3);
		qea.record_event_name("rescind", 4);
		qea.record_event_name("cancel", 5);

		return qea;
	}

	public static QEA makeResourceLifecycleGeneral() { // Figure A.26

		QEABuilder q = new QEABuilder("ResourceLifecycle");

		int REQUEST = 1;
		int GRANT = 2;
		int DENY = 3;
		int RESCIND = 4;
		int CANCEL = 5;
		int R = -1;

		q.addQuantification(FORALL, R);

		int[] r = new int[] { R };
		q.addTransition(1, REQUEST, r, 2);
		q.addTransition(2, DENY, r, 1);
		q.addTransition(2, GRANT, r, 3);
		q.addTransition(3, RESCIND, r, 3);
		q.addTransition(3, CANCEL, r, 1);

		q.addFinalStates(1, 2);

		QEA qea = q.makeMostGeneral();

		qea.record_event_name("request", 1);
		qea.record_event_name("grant", 2);
		qea.record_event_name("deny", 3);
		qea.record_event_name("rescind", 4);
		qea.record_event_name("cancel", 5);

		return qea;
	}

	public static QEA makeReleaseResource() { // Figure A.27

		QEABuilder q = new QEABuilder("ReleaseResource");

		int SCHEDULE = 1;
		int GRANT = 2;
		int CANCEL = 3;
		int FINISH = 4;

		int T = -1;
		int C = -2;
		int R = -3;

		q.addQuantification(FORALL, T);
		q.addQuantification(FORALL, C);
		q.addQuantification(FORALL, R);

		q.addTransition(1, SCHEDULE, new int[] { T, C }, 2);
		q.addTransition(2, GRANT, new int[] { T, R }, 3);
		q.addTransition(3, CANCEL, new int[] { T, R }, 2);
		q.addTransition(3, FINISH, new int[] { C }, 4);

		q.addFinalStates(1, 2);
		q.setSkipStates(1, 2, 3, 4);

		QEA qea = q.make();

		qea.record_event_name("schedule", 1);
		qea.record_event_name("grant", 2);
		qea.record_event_name("cancel", 3);
		qea.record_event_name("finish", 4);

		return qea;
	}

	public static QEA makeRespectConflictsDouble() { // Figure A.28

		QEABuilder q = new QEABuilder("RespectConflictsDouble");

		// Events
		int CONFLICT = 1;
		int GRANT = 2;
		int CANCEL = 3;
		// Quantified variables
		int R1 = -1;
		int R2 = -2;

		q.addQuantification(FORALL, R1);
		q.addQuantification(FORALL, R2);

		q.addTransition(1, CONFLICT, new int[] { R1, R2 }, 2);
		q.addTransition(1, CONFLICT, new int[] { R2, R1 }, 2);
		q.addTransition(2, GRANT, new int[] { R1 }, 3);
		q.addTransition(3, CANCEL, new int[] { R1 }, 2);
		q.addTransition(3, GRANT, new int[] { R2 }, 4);

		q.addFinalStates(1, 2, 3);
		q.setSkipStates(1, 2, 3, 4);

		QEA qea = q.make();

		qea.record_event_name("conflict", 1);
		qea.record_event_name("grant", 2);
		qea.record_event_name("cancel", 3);

		return qea;
	}

	public static QEA makeRespectConflictsSingle() { // Figure A.29

		QEABuilder q = new QEABuilder("RespectConflictsSingle");

		// Events
		int CONFLICT = 1;
		int GRANT = 2;
		int CANCEL = 3;
		// Quantified variable
		int R1 = -1;
		// Free variables
		int R2 = 1;
		int RS = 2;

		q.addQuantification(FORALL, R1);

		q.startTransition(1);
		q.eventName(CONFLICT);
		q.addVarArg(R1);
		q.addVarArg(R2);
		q.addAssignment(Assignment.createSetFromElement(RS, R2));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(CONFLICT);
		q.addVarArg(R1);
		q.addVarArg(R2);
		q.addAssignment(Assignment.addElementToSet(RS, R2));
		q.endTransition(2);

		q.startTransition(1);
		q.eventName(CONFLICT);
		q.addVarArg(R2);
		q.addVarArg(R1);
		q.addAssignment(Assignment.createSetFromElement(RS, R2));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(CONFLICT);
		q.addVarArg(R2);
		q.addVarArg(R1);
		q.addAssignment(Assignment.addElementToSet(RS, R2));
		q.endTransition(2);

		// All conflicts have been given

		// We can grant and cancel
		q.addTransition(2, GRANT, new int[] { R1 }, 3);
		q.addTransition(3, CANCEL, new int[] { R1 }, 2);

		// But if whilst granted we grant a conflicted
		// resource go to a failure state (4)
		// We don't actually need this transition - failure
		// will occur as we can't take the skip transition
		// defined below
		// q.startTransition(3);
		// q.eventName(GRANT);
		// q.addVarArg(R2);
		// q.addGuard(Guard.setContainsElement(R2, RS));
		// q.endTransition(4);

		// Manual skip states
		// q.addTransition(3, CONFLICT, new int[] { R1, R2 }, 3);
		q.addTransition(1, GRANT, new int[] { R2 }, 1);

		// This is tricky - the skip semantics are not
		// as straight-forward as this
		// q.addTransition(2, GRANT, new int[] { R2 }, 2);
		// we need r2!=r1
		q.startTransition(2);
		q.eventName(GRANT);
		q.addVarArg(R2);
		q.addGuard(Guard.isNotEqual(R1, R2));
		q.endTransition(2);

		q.startTransition(3);
		q.eventName(GRANT);
		q.addVarArg(R2);
		q.addGuard(Guard.setNotContainsElement(R2, RS));
		q.endTransition(3);

		// Final and skip states
		q.addFinalStates(1, 2, 3);
		// q.setSkipStates(1, 2, 4);

		QEA qea = q.make();

		qea.record_event_name("conflict", 1);
		qea.record_event_name("grant", 2);
		qea.record_event_name("cancel", 3);

		return qea;
	}

	public static QEA makeRespectConflictsSingleNonDet() {

		QEABuilder q = new QEABuilder("RespectConflictsSingle");

		// Events
		int CONFLICT = 1;
		int GRANT = 2;
		int CANCEL = 3;
		// Quantified variable
		int R1 = -1;
		// Free variables
		final int R2 = 1;
		final int R3 = 2;

		q.addQuantification(FORALL, R1);

		q.addTransition(1, CONFLICT, new int[] { R1, R2 }, 1);
		q.addTransition(1, CONFLICT, new int[] { R2, R1 }, 1);
		q.addTransition(1, CONFLICT, new int[] { R1, R2 }, 2);
		q.addTransition(1, CONFLICT, new int[] { R2, R1 }, 2);

		q.addTransition(2, GRANT, new int[] { R1 }, 3);
		q.addTransition(3, CANCEL, new int[] { R1 }, 2);

		q.startTransition(3);
		q.eventName(GRANT);
		q.addVarArg(R3);
		q.addGuard(Guard.isEqual(R2, R3));
		q.endTransition(4);

		q.addFinalStates(4);
		q.setSkipStates(1, 2, 4);

		QEA qea = q.make();

		qea.record_event_name("conflict", 1);
		qea.record_event_name("grant", 2);
		qea.record_event_name("cancel", 3);

		return qea;
	}

	public static QEA makeRespectPriorities() { // Figure A.30

		QEABuilder q = new QEABuilder("RespectPriorities");

		// Events
		int GRANT = 1;
		int CANCEL = 2;
		int PRIORITY = 3;
		int REQUEST = 4;
		int RESCIND = 5;
		int DENY = 6;
		// Quantified variables
		int R1 = -1;
		int R2 = -2;
		// Free variables
		int R3 = 1;
		int RS = 2;
		int G = 3;

		q.addQuantification(FORALL, R1);
		q.addQuantification(FORALL, R2);

		q.startTransition(1);
		q.eventName(PRIORITY);
		q.addVarArg(R3);
		q.addVarArg(R2);
		q.addAssignment(Assignment.addElementToSet(RS, R3));
		q.endTransition(1);

		q.addTransition(1, PRIORITY, new int[] { R2, R1 }, 2);

		q.startTransition(2);
		q.eventName(PRIORITY);
		q.addVarArg(R3);
		q.addVarArg(R2);
		q.addAssignment(Assignment.addElementToSet(RS, R3));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(GRANT);
		q.addVarArg(R3);
		q.addGuard(Guard.setContainsElement(R3, RS));
		q.addAssignment(Assignment.addElementToSet(G, R3));
		q.endTransition(3);

		q.addTransition(2, GRANT, new int[] { R1 }, 5);

		q.addTransition(2, GRANT, new int[] { R2 }, 10);

		q.startTransition(3);
		q.eventName(CANCEL);
		q.addVarArg(R3);
		q.addGuard(Guard.setContainsOnlyElement(R3, G));
		q.addAssignment(Assignment.removeElementFromSet(G, R3));
		q.endTransition(2);

		q.startTransition(3);
		q.eventName(GRANT);
		q.addVarArg(R3);
		q.addGuard(Guard.setContainsElement(R3, RS));
		q.addAssignment(Assignment.addElementToSet(G, R3));
		q.endTransition(3);

		q.startTransition(3);
		q.eventName(CANCEL);
		q.addVarArg(R3);
		q.addGuard(Guard.setContainsMoreThanElement(R3, G));
		q.addAssignment(Assignment.removeElementFromSet(G, R3));
		q.endTransition(3);

		q.addTransition(3, REQUEST, new int[] { R2 }, 4);

		q.addTransition(4, DENY, new int[] { R2 }, 3);

		q.addTransition(5, CANCEL, new int[] { R1 }, 2);

		q.addTransition(5, REQUEST, new int[] { R2 }, 6);

		q.startTransition(5);
		q.eventName(GRANT);
		q.addVarArg(R3);
		q.addGuard(Guard.setContainsElement(R3, RS));
		q.addAssignment(Assignment.addElementToSet(G, R3));
		q.endTransition(8);

		q.addTransition(6, RESCIND, new int[] { R1 }, 7);

		q.addTransition(7, CANCEL, new int[] { R1 }, 2);

		q.startTransition(8);
		q.eventName(CANCEL);
		q.addVarArg(R3);
		q.addGuard(Guard.setContainsOnlyElement(R3, G));
		q.addAssignment(Assignment.removeElementFromSet(G, R3));
		q.endTransition(5);

		q.startTransition(8);
		q.eventName(GRANT);
		q.addVarArg(R3);
		q.addGuard(Guard.setContainsElement(R3, RS));
		q.addAssignment(Assignment.addElementToSet(G, R3));
		q.endTransition(8);

		q.startTransition(8);
		q.eventName(CANCEL);
		q.addVarArg(R3);
		q.addGuard(Guard.setContainsMoreThanElement(R3, G));
		q.addAssignment(Assignment.removeElementFromSet(G, R3));
		q.endTransition(8);

		q.addTransition(8, REQUEST, new int[] { R2 }, 9);

		q.addTransition(9, DENY, new int[] { R2 }, 8);

		q.addTransition(10, CANCEL, new int[] { R2 }, 2);

		q.startTransition(10);
		q.eventName(GRANT);
		q.addVarArg(R3);
		q.addGuard(Guard.setNotContainsElement(R3, RS));
		q.endTransition(10);

		q.startTransition(10);
		q.eventName(CANCEL);
		q.addVarArg(R3);
		q.addGuard(Guard.setNotContainsElement(R3, RS));
		q.endTransition(10);

		q.addFinalStates(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		q.setSkipStates(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		QEA qea = q.make();

		qea.record_event_name("grant", 1);
		qea.record_event_name("cancel", 2);
		qea.record_event_name("priority", 3);
		qea.record_event_name("request", 4);
		qea.record_event_name("rescind", 5);
		qea.record_event_name("deny", 6);

		return qea;
	}

	public static QEA makeExactlyOneSuccess() { // Figure A.31

		QEABuilder q = new QEABuilder("ExactlyOneSuccess");

		// Events
		int COM = 1;
		int SUC = 2;
		int FAIL = 3;
		// Quantified variable
		int C = -1;

		q.addQuantification(FORALL, C);

		q.addTransition(1, COM, new int[] { C }, 2);
		q.addTransition(2, SUC, new int[] { C }, 3);
		q.addTransition(2, FAIL, new int[] { C }, 4);

		q.addFinalStates(1, 2, 3);

		QEA qea = q.make();

		qea.record_event_name("command", 1);
		qea.record_event_name("succeed", 2);
		qea.record_event_name("fail", 3);

		return qea;
	}

	public static QEA makeIncreasingCommand() { // Figure A.32

		QEABuilder q = new QEABuilder("IncreasingCommand");

		// Event
		int COM = 1;
		// Free variables
		int C1 = 1;
		int C2 = 2;

		q.addTransition(1, COM, new int[] { C1 }, 2);

		q.startTransition(2);
		q.eventName(COM);
		q.addVarArg(C2);
		q.addGuard(Guard.isGreaterThan(C2, C1));
		q.addAssignment(Assignment.storeVar(C1, C2));
		q.endTransition(2);

		q.addFinalStates(1, 2);

		QEA qea = q.make();

		qea.record_event_name("command", 1);

		return qea;
	}

	public static QEA makeAcknowledgeCommands() { // Figure A.33

		QEABuilder q = new QEABuilder("AcknowledgeCommands");

		// Events
		int SET_ACK_TIMEOUT = 1;
		int COM = 2;
		int ACK = 3;
		// Quantified variable
		int C = -1;
		// Free variables
		final int M = 1;
		final int N1 = 2;
		final int P1 = 3;
		final int T1 = 4;
		final int N2 = 5;
		final int P2 = 6;
		final int T2 = 7;
		int CC = 8;

		q.addQuantification(FORALL, C);

		q.addTransition(1, SET_ACK_TIMEOUT, new int[] { M }, 2);
		q.addTransition(2, COM, new int[] { C, N1, P1, T1 }, 3);

		q.startTransition(3);
		q.eventName(ACK);
		q.addVarArg(C);
		q.addVarArg(T2);
		q.addGuard(Guard.isLessThan(T2, M));
		q.endTransition(4);

		q.startTransition(3);
		q.eventName(COM);
		q.addVarArg(CC);
		q.addVarArg(N2);
		q.addVarArg(P2);
		q.addVarArg(T2);
		q.addGuard(new Guard("SpecificGuard") {

			@Override
			public boolean usesQvars() {
				return false;
			}

			@Override
			public int[] vars() {
				return new int[] { N1, N2, P1, P2, T1, T2, M };
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				return check(binding);
			}

			@Override
			public boolean check(Binding binding) {
				Object n1 = binding.getForced(N1);
				Object n2 = binding.getForced(N2);
				Object p1 = binding.getForced(P1);
				Object p2 = binding.getForced(P2);
				int t1 = binding.getForcedAsInteger(T1);
				int t2 = binding.getForcedAsInteger(T2);
				int m = binding.getForcedAsInteger(M);
				return n1 == n2 && p1 == p2 && t2 - t1 < 2 * m;
			}
		});
		q.endTransition(4);

		// Manual skip states for state 3

		q.addTransition(3, SET_ACK_TIMEOUT, new int[] { M }, 3);

		q.startTransition(3);
		q.eventName(ACK);
		q.addVarArg(C);
		q.addVarArg(T2);
		q.addGuard(Guard.isGreaterThanOrEqualTo(T2, M));
		q.endTransition(3);

		q.startTransition(3);
		q.eventName(COM);
		q.addVarArg(CC);
		q.addVarArg(N2);
		q.addVarArg(P2);
		q.addVarArg(T2);
		q.addGuard(new Guard("SpecificInverseGuard") {

			@Override
			public int[] vars() {
				return new int[] { N1, N2, P1, P2, T1, T2, M };
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
				Object n1 = binding.getForced(N1);
				Object n2 = binding.getForced(N2);
				Object p1 = binding.getForced(P1);
				Object p2 = binding.getForced(P2);
				int t1 = binding.getForcedAsInteger(T1);
				int t2 = binding.getForcedAsInteger(T2);
				int m = binding.getForcedAsInteger(M);
				return !(n1 == n2 && p1 == p2 && t2 - t1 < 2 * m);
			}

		});
		q.endTransition(3);

		// Final and skip states
		q.addFinalStates(1, 2, 4);
		q.setSkipStates(1, 2, 4);

		QEA qea = q.make();

		qea.record_event_name("set_ack_timeout", 1);
		qea.record_event_name("command", 2);
		qea.record_event_name("ack", 3);

		return qea;
	}

	public static QEA makeExistsSatelliteDouble() { // Figure A.34

		QEABuilder q = new QEABuilder("ExistsSatelliteDouble");

		// Events
		int PING = 1;
		int ACK = 2;
		// Quantified variables
		int R = -1;
		int S = -2;

		q.addQuantification(FORALL, R);
		q.addQuantification(EXISTS, S);

		q.addTransition(1, PING, new int[] { R, S }, 2);
		q.addTransition(2, ACK, new int[] { S, R }, 3);

		q.addFinalStates(3);
		q.setSkipStates(1, 2, 3);

		QEA qea = q.make();

		qea.record_event_name("ping", 1);
		qea.record_event_name("ack", 2);

		return qea;
	}

	public static QEA makeExistsSatelliteSingle() { // Figure A.35

		QEABuilder q = new QEABuilder("ExistsSatelliteSingle");

		// Events
		int PING = 1;
		int ACK = 2;
		// Quantified variable
		int r = -1;
		// Free variables
		int s = 1;
		int S = 2;

		q.addQuantification(FORALL, r);

		q.startTransition(1);
		q.eventName(PING);
		q.addVarArg(r);
		q.addVarArg(s);
		q.addAssignment(Assignment.createSetFromElement(S, s));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(PING);
		q.addVarArg(r);
		q.addVarArg(s);
		q.addAssignment(Assignment.addElementToSet(S, s));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(ACK);
		q.addVarArg(s);
		q.addVarArg(r);
		q.addGuard(Guard.setContainsElement(s, S));
		q.endTransition(3);

		// Manual skip states for state 2
		q.startTransition(2);
		q.eventName(ACK);
		q.addVarArg(s);
		q.addVarArg(r);
		q.addGuard(Guard.setNotContainsElement(s, S));
		q.endTransition(2);

		// Final and skip states
		q.addFinalStates(1, 3);
		q.setSkipStates(1, 3);

		QEA qea = q.make();

		qea.record_event_name("ping", 1);
		qea.record_event_name("ack", 2);

		return qea;
	}

	public static QEA makeExistsLeader() { // Figure A.36

		QEABuilder q = new QEABuilder("ExistsLeader");

		// System.err.println("*********************");
		// System.err.println("WARNING: Not Normal - not sure if monitors work for this");
		// System.err.println("*********************");

		// Events
		int PING = 1;
		int ACK = 2;
		// Quantified variables
		int R1 = -1;
		int R2 = -2;

		q.addQuantification(EXISTS, R1);
		q.addQuantification(FORALL, R2, Guard.isNotEqual(R1, R2));

		q.addTransition(1, PING, new int[] { R1, R2 }, 2);
		q.addTransition(2, ACK, new int[] { R2, R1 }, 3);

		// Add to alphabet
		q.addTransition(4, PING, new int[] { R2, R1 }, 4);

		q.addFinalStates(3);
		q.setSkipStates(1, 2, 3);

		QEA qea = q.make();

		qea.record_event_name("ping", 1);
		qea.record_event_name("ack", 2);

		return qea;
	}

	public static QEA makeNestedCommand() { // Figure 3.3

		QEABuilder q = new QEABuilder("NestedCommand");

		// Events
		int COM = 1;
		int SUC = 2;
		// Quantified variables
		int X = -1;
		int Y = -2;

		q.addQuantification(FORALL, X);
		q.addQuantification(FORALL, Y);

		q.addTransition(1, COM, new int[] { Y }, 1);
		q.addTransition(1, SUC, new int[] { Y }, 1);
		q.addTransition(1, COM, new int[] { X }, 2);
		q.addTransition(2, SUC, new int[] { X }, 1);
		q.addTransition(2, COM, new int[] { Y }, 3);
		q.addTransition(3, SUC, new int[] { Y }, 2);

		q.addFinalStates(1, 2, 3);

		QEA qea = q.make();

		qea.record_event_name("command", 1);
		qea.record_event_name("succeed", 2);

		return qea;
	}

	public static QEA makeMessageHashCorrect() { // Figure A.37

		QEABuilder q = new QEABuilder("MessageHashCorrect");

		// Events
		int SEND = 1;
		int ACK = 2;
		// Quantified variables
		int X1 = -1;
		int X2 = -2;
		// Free variables
		final int MSG = 1;
		final int H = 2;
		final int M = 3;

		q.addQuantification(FORALL, X1);
		q.addQuantification(FORALL, X2);

		q.startTransition(1);
		q.eventName(SEND);
		q.addVarArg(X1);
		q.addVarArg(X2);
		q.addVarArg(MSG);
		q.addAssignment(Assignment.createSetFromElement(M, MSG));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(SEND);
		q.addVarArg(X1);
		q.addVarArg(X2);
		q.addVarArg(MSG);
		q.addAssignment(Assignment.addElementToSet(M, MSG));
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(ACK);
		q.addVarArg(X2);
		q.addVarArg(X1);
		q.addVarArg(H);
		q.addGuard(new Guard("ExistsMessageWithHashH") {

			@Override
			public int[] vars() {
				return new int[] { M, H };
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
				HashSet<Object> setM = (HashSet<Object>) binding.getForced(M);
				for (Object msg : setM) {
					// TODO What hash function should we use here?
					if (msg.hashCode() == binding.getForcedAsInteger(H)
							.intValue()) {
						return true;
					}
				}
				return false;
			}
		});
		q.addAssignment(new Assignment("RemoveMessagesWithHashH") {

			@Override
			public Binding apply(Binding binding, boolean copy) {
				HashSet<Object> setM = (HashSet<Object>) binding.getForced(M);
				for (Object msg : setM) {
					if (msg.hashCode() == binding.getForcedAsInteger(H)
							.intValue()) {
						setM.remove(msg);
					}
				}
				Binding newBinding = binding;
				if (copy) {
					binding = binding.copy();
				}
				newBinding.setValue(M, setM);
				return newBinding;

			}

			@Override
			public int[] vars() {
				return new int[] { M, H };
			}
		});
		q.endTransition(2);

		q.startTransition(2);
		q.eventName(ACK);
		q.addVarArg(X2);
		q.addVarArg(X1);
		q.addVarArg(H);
		q.addGuard(new Guard("SetContainsOnlyElementWithHashH") {

			@Override
			public int[] vars() {
				return new int[] { M, MSG, H };
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
				HashSet<Object> setM = (HashSet<Object>) binding.getForced(M);
				Object msg = binding.getForced(MSG);
				if (setM.size() == 1
						&& setM.contains(msg)
						&& msg.hashCode() == binding.getForcedAsInteger(H)
								.intValue()) {
					return true;
				}
				return false;
			}
		});
		q.endTransition(1);

		// Manual skip states for state 2
		q.startTransition(2);
		q.eventName(ACK);
		q.addVarArg(X2);
		q.addVarArg(X1);
		q.addVarArg(H);
		// TODO Horrible guard! Can we make it better?
		q.addGuard(new Guard("") {

			@Override
			public int[] vars() {
				return new int[] { M, H, MSG };
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

				boolean existsMsgWithHashH = false;
				HashSet<Object> setM = (HashSet<Object>) binding.getForced(M);
				for (Object msg : setM) {
					if (msg.hashCode() == binding.getForcedAsInteger(H)
							.intValue()) {
						existsMsgWithHashH = true;
					}
				}

				Object msg = binding.getForced(MSG);
				if (!existsMsgWithHashH
						&& !(setM.size() == 1 && setM.contains(msg) && msg
								.hashCode() == binding.getForcedAsInteger(H)
								.intValue())) {
					return true;
				}
				return false;
			}
		});
		q.endTransition(2);

		q.addFinalStates(1);
		q.setSkipStates(1);

		QEA qea = q.make();

		qea.record_event_name("send", 1);
		qea.record_event_name("ack", 2);

		return qea;
	}
}
