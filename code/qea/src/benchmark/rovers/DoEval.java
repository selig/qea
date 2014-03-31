package benchmark.rovers;

import java.io.PrintStream;
import java.util.Arrays;

abstract class DoEval<S> {

	private static PrintStream old_out = System.out;
	private static PrintStream null_out = null;
	static {
		try {
			new java.io.PrintStream("redirected.txt");
		} catch (Exception e) {
		}
	}

	public void set_runs(int r) {
		runs = r;
	}

	private static int runs = 5;
	private static int warmup = 5;

	public abstract DoWork<S> makeWork();

	public void run_eval(S spec, String name, int[] args, int w) {
		warmup = w;
		run_eval(spec, name, args);
		warmup = 5;
	}

	public void run_eval(S spec, String name, int[] args) {

		System.setOut(null_out);

		for (int i = 0; i < warmup; i++) {
			DoWork<S> work = makeWork();
			work.run_with_spec(spec, name, args);
		}
		old_out.println("==\t" + spec + ":" + Arrays.toString(args) + "\t==");
		for (int i = 0; i < runs; i++) {
			System.setOut(null_out);
			System.gc();
			long start = System.currentTimeMillis();
			DoWork<S> work = makeWork();
			work.run_with_spec(spec, name, args);
			long end = System.currentTimeMillis();
			System.setOut(old_out);
			System.out.println((end - start));

		}

	}

	public void pick_eval(S spec, String name) {

		if (name.equals("IncreasingCommand")) {
			eval_for_IncreasingCommand(spec, name);
		}
		if (name.equals("ResourceLifecycle")) {
			eval_for_ResourceLifecycle(spec, name);
		}
		if (name.equals("ExactlyOneSuccess")) {
			eval_for_ExactlyOneSuccess(spec, name);
		}
		if (name.equals("AcknowledgeCommands")) {
			eval_for_AcknowledgeCommands(spec, name);
		}
		if (name.equals("NestedCommand")) {
			eval_for_NestedCommand(spec, name);
		}
		if (name.equals("GrantCancel")) {
			eval_for_GrantCancel(spec, name);
		}
		if (name.equals("ReleaseResource")) {
			eval_for_ReleaseResource(spec, name);
		}
		if (name.equals("RespectConflicts")) {
			eval_for_RespectConflicts(spec, name);
		}
		if (name.equals("ExistsSatellite")) {
			eval_for_ExistsSatellite(spec, name);
		}
		if (name.equals("ExistsLeader")) {
			eval_for_ExistsLeader(spec, name);
		}
		if (name.equals("MessageHashCorrectInvInt")) {
			eval_for_MessageHashCorrectInvInt(spec, name);
		}

      }

	public void eval_for_IncreasingCommand(S spec, String name) {

		run_eval(spec, name, new int[] { 10000 }); // put this one first to make
													// sure we're nice and warm
		run_eval(spec, name, new int[] { 10 });
		run_eval(spec, name, new int[] { 100 });
		run_eval(spec, name, new int[] { 1000 });
		run_eval(spec, name, new int[] { 10000 }, 0);
		run_eval(spec, name, new int[] { 100000 }, 0);
		run_eval(spec, name, new int[] { 1000000 }, 0);

	}

	public void eval_for_ResourceLifecycle(S spec, String name) {

		run_eval(spec, name, new int[] { 10, 10000 }, 10);
		run_eval(spec, name, new int[] { 100, 10000 });
		run_eval(spec, name, new int[] { 1000, 10000 }, 0);
		run_eval(spec, name, new int[] { 5000, 10000 }, 0);
		run_eval(spec, name, new int[] { 100, 1000000 }, 0);
		run_eval(spec, name, new int[] { 1000, 1000000 }, 0);
		run_eval(spec, name, new int[] { 5000, 1000000 }, 0);

	}

	public void eval_for_ExactlyOneSuccess(S spec, String name) {

		run_eval(spec, name, new int[] { 10 }, 10);
		run_eval(spec, name, new int[] { 100 });
		run_eval(spec, name, new int[] { 1000 }, 0);
		run_eval(spec, name, new int[] { 10000 }, 0);

	}

	public void eval_for_AcknowledgeCommands(S spec, String name) {

		run_eval(spec, name, new int[] { 10 });
		run_eval(spec, name, new int[] { 100 });
		run_eval(spec, name, new int[] { 1000 }, 0);
		run_eval(spec, name, new int[] { 10000 }, 0);
		run_eval(spec, name, new int[] { 100000 }, 0);
		run_eval(spec, name, new int[] { 1000000 }, 0);

	}

	public void eval_for_NestedCommand(S spec, String name) {

		run_eval(spec, name, new int[] { 2, 2, 10 });
		run_eval(spec, name, new int[] { 3, 3, 10 }, 0);
		run_eval(spec, name, new int[] { 2, 2, 100 }, 0);

	}

	public void eval_for_GrantCancel(S spec, String name) {

		run_eval(spec, name, new int[] { 10, 10, 1000 }, 100);
		run_eval(spec, name, new int[] { 10, 10, 10000 });
		run_eval(spec, name, new int[] { 100, 100, 1000 }, 0);
		run_eval(spec, name, new int[] { 100, 100, 10000 }, 0);
		run_eval(spec, name, new int[] { 1000, 1000, 1000 });
		run_eval(spec, name, new int[] { 1000, 1000, 10000 }, 0);

	}

	public void eval_for_ReleaseResource(S spec, String name) {

		run_eval(spec, name, new int[] { 5, 5, 100 });
		run_eval(spec, name, new int[] { 5, 5, 1000 });
		run_eval(spec, name, new int[] { 10, 10, 100 }, 0);
		run_eval(spec, name, new int[] { 10, 10, 1000 }, 0);
		run_eval(spec, name, new int[] { 10, 10, 10000 }, 0);

	}

	public void eval_for_RespectConflicts(S spec, String name) {

		run_eval(spec, name, new int[] { 3, 100, 100 });
		run_eval(spec, name, new int[] { 4, 100, 100 }, 0);
		run_eval(spec, name, new int[] { 5, 100, 100 }, 0);
		run_eval(spec, name, new int[] { 6, 100, 100 }, 0);
		run_eval(spec, name, new int[] { 7, 100, 100 }, 0);
		run_eval(spec, name, new int[] { 3, 1000, 1000 }, 0);
		run_eval(spec, name, new int[] { 4, 1000, 1000 }, 0);
		run_eval(spec, name, new int[] { 5, 1000, 1000 }, 0);

	}

	public void eval_for_ExistsSatellite(S spec, String name) {

		run_eval(spec, name, new int[] { 10, 10 });
		run_eval(spec, name, new int[] { 10, 100 });
		run_eval(spec, name, new int[] { 10, 1000 }, 0);
		run_eval(spec, name, new int[] { 100, 100 }, 0);
		run_eval(spec, name, new int[] { 100, 1000 }, 0);
		run_eval(spec, name, new int[] { 1000, 1000 }, 0);

	}

	public void eval_for_ExistsLeader(S spec, String name) {

		run_eval(spec, name, new int[] { 5 });
		run_eval(spec, name, new int[] { 10 }, 0);
		run_eval(spec, name, new int[] { 15 }, 0);
		run_eval(spec, name, new int[] { 20 }, 0);

	}

	public void eval_for_MessageHashCorrectInvInt(S spec, String name) {

		run_eval(spec, name, new int[] { 100, 100 }, 50);
		run_eval(spec, name, new int[] { 100, 1000 });
		run_eval(spec, name, new int[] { 100, 10000 }, 0);
		run_eval(spec, name, new int[] { 1000, 1000 }, 0);
		run_eval(spec, name, new int[] { 1000, 10000 }, 0);
		run_eval(spec, name, new int[] { 1000, 100000 }, 0);
		run_eval(spec, name, new int[] { 1000, 1000000 }, 0);

	}
}
