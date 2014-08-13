package qea.benchmark.rovers.ruler;

import rules.RuleR;
import rules.RuleSystem.Signal;
import qea.benchmark.rovers.DoEval;
import qea.benchmark.rovers.DoWork;

public class RuleRDoEval extends DoEval {

	@Override
	public DoWork makeWork() {
		return new RuleRDoWork();
	}

	public static void main(String[] args) {

		do_the_eval();

	}

	public static void do_the_eval() {

		RuleRDoEval eval = new RuleRDoEval();
		eval.hardest_only = true;
		eval.csv_mode = true;

		// eval.eval_for_GrantCancel("GrantCancel","GrantCancel");

		// eval.eval_for_ResourceLifecycle("ResourceLifecycle","ResourceLifecycle");

		// eval.eval_for_RespectConflicts("RespectConflicts",
		// "RespectConflicts");

		eval.eval_for_IncreasingCommand("IncreasingCommand",
				"IncreasingCommand");

		eval.eval_for_ExistsSatellite("ExistsSatellite", "ExistsSatellite");

		eval.eval_for_AcknowledgeCommands("AcknowledgeCommands",
				"AcknowledgeCommands");

		eval.eval_for_ReleaseResource("ReleaseResource", "ReleaseResource");

		eval.eval_for_NestedCommand("NestedCommand", "NestedCommand");

		eval.eval_for_ExistsLeader("ExistsLeader", "ExistsLeader");

		eval.eval_for_RespectPriorities("RespectPriorities",
				"RespectPriorities");

	}

	static class RuleRDoWork extends DoWork<String> {

		public static boolean print = false;

		@Override
		public void run_with_spec(String spec_location, String name, int[] args) {

			try {
				monitor = new RuleR("src/benchmark/rovers/ruler/"
						+ spec_location, false);
			} catch (Exception e) {
				monitor = new RuleR("ruler-specs/" + spec_location, false);
			}

			dowork(name, args);
			if (print) {
				System.err.println(monitor);
			}
		}

		private RuleR monitor;

		/**
		 * Prints an error message if the specified verdict is a failure
		 * 
		 * @param verdict
		 *            Verdict
		 */
		public void handle(Signal verdict) {
			if (verdict == Signal.FALSE) {
				System.err.println("warning: failure");
				System.err.println(monitor);
				System.exit(0);
			}
		}

		private Object[] wrap(Object... obs) {
			return obs;
		}

		private static final String command = "command";

		@Override
		public void command_int(int x) {
			handle(monitor.dispatch(command, wrap(x)));
		}

		@Override
		public void command(Object o) {
			handle(monitor.dispatch(command, wrap(o)));
		}

		@Override
		public void command(Object a, Object b, Object c, int d) {
			handle(monitor.dispatch(command, wrap(a, b, c, d)));
		}

		private static final String request = "request";

		@Override
		public void request(Object o) {
			handle(monitor.dispatch(request, wrap(o)));
		}

		private static final String grant = "grant";

		public void grant(Object o) {
			handle(monitor.dispatch(grant, wrap(o)));
		}

		public void grant(Object a, Object b) {
			handle(monitor.dispatch(grant, wrap(a, b)));
		}

		private static final String deny = "deny";

		@Override
		public void deny(Object o) {
			handle(monitor.dispatch(deny, wrap(o)));
		}

		private static final String rescind = "rescind";

		@Override
		public void rescind(Object o) {
			handle(monitor.dispatch(rescind, wrap(o)));
		}

		private static final String cancel = "cancel";

		public void cancel(Object o) {
			handle(monitor.dispatch(cancel, wrap(o)));
		}

		public void cancel(Object a, Object b) {
			handle(monitor.dispatch(cancel, wrap(a, b)));
		}

		private static final String succeed = "succeed";

		@Override
		public void succeed(Object o) {
			handle(monitor.dispatch(succeed, wrap(o)));
		}

		private static final String set_ack = "set_ack";

		@Override
		public void set_ack_timeout(int x) {
			handle(monitor.dispatch(set_ack, wrap(x)));
		}

		private static final String schedule = "schedule";

		@Override
		public void schedule(Object a, Object b) {
			handle(monitor.dispatch(schedule, wrap(a, b)));
		}

		private static final String finish = "finish";

		@Override
		public void finish(Object o) {
			handle(monitor.dispatch(finish, wrap(o)));
		}

		private static final String conflict = "conflict";

		@Override
		public void conflict(Object a, Object b) {
			handle(monitor.dispatch(conflict, wrap(a, b)));
		}

		private static final String ping = "ping";

		@Override
		public void ping(Object a, Object b) {
			handle(monitor.dispatch(ping, wrap(a, b)));
		}

		private static final String send = "send";

		@Override
		public void send(Object a, Object b, int c) {
			handle(monitor.dispatch(send, wrap(a, b, c)));
		}

		private static final String ack = "ack";

		@Override
		public void ack(Object a, Object b) {
			handle(monitor.dispatch(ack, wrap(a, b)));
			;
		}

		@Override
		public void ack(Object a, Object b, int c) {
			handle(monitor.dispatch(ack, wrap(a, b, c)));
		}

		@Override
		public void ack(Object o, int x) {
			handle(monitor.dispatch(ack, wrap(o, x)));
		}

		private static final String fail = "fail";

		@Override
		public void fail(Object o) {
			handle(monitor.dispatch(fail, wrap(o)));

		}

		private static final String priority = "priority";

		@Override
		public void priority(Object a, Object b) {
			handle(monitor.dispatch(priority, wrap(a, b)));
		}

		@Override
		public void grant_rl(Object o) {
			grant(o);
		}

		@Override
		public void grant_rc(Object o) {
			grant(o);
		}

		@Override
		public void grant_rp(Object o) {
			grant(o);
		}

		@Override
		public void cancel_rl(Object o) {
			cancel(o);
		}

		@Override
		public void cancel_rc(Object o) {
			cancel(o);
		}

		@Override
		public void cancel_rp(Object o) {
			cancel(o);
		}

		@Override
		public void grant_gc(Object a, Object b) {
			grant(a, b);
		}

		@Override
		public void grant_rr(Object a, Object b) {
			grant(a, b);
		}

		@Override
		public void cancel_gc(Object a, Object b) {
			cancel(a, b);
		}

		@Override
		public void cancel_rr(Object a, Object b) {
			cancel(a, b);
		}
	}

}
