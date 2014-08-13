package qea.benchmark.rovers;

import java.io.PrintStream;
import java.util.Arrays;

public abstract class DoEval<S> {

	public static boolean hardest_only = false;
	public static boolean output = false;
	public static boolean csv_mode = false;

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

	/**
	 * Runs the evaluation for the specified property with the specified
	 * arguments and warm-up repetitions. After the evaluation, the warm-up
	 * repetitions are set to the default value
	 * 
	 * @param spec
	 *            QEA property
	 * @param name
	 *            Name of the property
	 * @param args
	 *            Arguments for the evaluation. The number and meaning varies
	 *            from property to property
	 * @param w
	 *            Warm-up repetitions
	 */
	public void run_eval(S spec, String name, int[] args, int w) {
		warmup = w;
		try {
			run_eval(spec, name, args);
		} catch (Exception e) {
			System.err.println("There was a problem:");
			e.printStackTrace();
		}
		warmup = 5;
	}

	/**
	 * Runs the evaluation for the specified property with the specified
	 * arguments
	 * 
	 * @param spec
	 *            QEA property
	 * @param name
	 *            Name of the property
	 * @param args
	 *            Arguments for the evaluation. The number and meaning varies
	 *            from property to property
	 */
	public void run_eval(S spec, String name, int[] args) {

		// Set standard output to null
		System.setOut(null_out);

		// Execute warm-up repetitions
		for (int i = 0; i < warmup; i++) {
			DoWork<S> work = makeWork();
			work.run_with_spec(spec, name, args);
		}

		// Print name of the property and arguments
		if (csv_mode) {
			old_out.print(spec + ":" + Arrays.toString(args).replace(',', ':')
					+ ",");
		} else {
			old_out.println("==\t" + spec + ":" + Arrays.toString(args)
					+ "\t==");
		}

		// Execute runs printing the time for each one
		long[] times = new long[runs];
		for (int i = 0; i < runs; i++) {
			System.setOut(null_out);
			System.gc();
			long start = System.currentTimeMillis();
			DoWork<S> work = makeWork();
			work.run_with_spec(spec, name, args);
			long end = System.currentTimeMillis();
			System.setOut(old_out);
			long time = end - start;
			if (output && !csv_mode) {
				System.out.println(i + " took " + time);
			}
			if (csv_mode) {
				System.out.print(time + ",");
			}
			times[i] = time;
		}
		// compute average time
		long total = 0;
		for (long time : times) {
			total += time;
		}
		double average = (double) total / runs;
		System.out.println(average);

	}

	public void pick_eval(S spec, String name) {

		if (name.equals("IncreasingCommand")) {
			eval_for_IncreasingCommand(spec, name);
		} else if (name.equals("ResourceLifecycle")) {
			eval_for_ResourceLifecycle(spec, name);
		} else if (name.equals("ExactlyOneSuccess")) {
			eval_for_ExactlyOneSuccess(spec, name);
		} else if (name.equals("AcknowledgeCommands")) {
			eval_for_AcknowledgeCommands(spec, name);
		} else if (name.equals("NestedCommand")) {
			eval_for_NestedCommand(spec, name);
		} else if (name.equals("GrantCancel")) {
			eval_for_GrantCancel(spec, name);
		} else if (name.equals("ReleaseResource")) {
			eval_for_ReleaseResource(spec, name);
		} else if (name.equals("RespectConflicts")) {
			eval_for_RespectConflicts(spec, name);
		} else if (name.equals("ExistsSatellite")) {
			eval_for_ExistsSatellite(spec, name);
		} else if (name.equals("ExistsLeader")) {
			eval_for_ExistsLeader(spec, name);
		} else if (name.equals("MessageHashCorrectInvInt")) {
			eval_for_MessageHashCorrectInvInt(spec, name);
		}
	}

	public void eval_for_IncreasingCommand(S spec, String name) {

		if (!hardest_only) {
			run_eval(spec, name, new int[] { 10000 }); // put this one first to
														// make
														// sure we're nice and
														// warm
			run_eval(spec, name, new int[] { 10 });
			run_eval(spec, name, new int[] { 100 });
			run_eval(spec, name, new int[] { 1000 });
			run_eval(spec, name, new int[] { 10000 }, 0);
			run_eval(spec, name, new int[] { 100000 }, 0);
		}
		run_eval(spec, name, new int[] { 1000000 }, 0);

	}

	public void eval_for_ResourceLifecycle(S spec, String name) {

		if (!hardest_only) {
			run_eval(spec, name, new int[] { 10, 10000 }, 10);
			run_eval(spec, name, new int[] { 100, 10000 });
			run_eval(spec, name, new int[] { 1000, 10000 }, 0);
			run_eval(spec, name, new int[] { 5000, 10000 }, 0);
			run_eval(spec, name, new int[] { 100, 1000000 }, 0);
			run_eval(spec, name, new int[] { 1000, 1000000 }, 0);
		}
		run_eval(spec, name, new int[] { 5000, 1000000 }, 0);

	}

	public void eval_for_ExactlyOneSuccess(S spec, String name) {

		if (!hardest_only) {
			run_eval(spec, name, new int[] { 10 }, 10);
			run_eval(spec, name, new int[] { 100 });
			run_eval(spec, name, new int[] { 1000 }, 0);
		}
		run_eval(spec, name, new int[] { 10000 }, 0);

	}

	public void eval_for_AcknowledgeCommands(S spec, String name) {

		if (!hardest_only) {
			run_eval(spec, name, new int[] { 10 });
			run_eval(spec, name, new int[] { 100 });
			run_eval(spec, name, new int[] { 1000 }, 0);
			run_eval(spec, name, new int[] { 10000 }, 0);
			run_eval(spec, name, new int[] { 100000 }, 0);
		}
		// System.err.println("AcknowledgeCommands turned down");
		run_eval(spec, name, new int[] { 10000 }, 0);

	}

	public void eval_for_NestedCommand(S spec, String name) {

		if (!hardest_only) {
			run_eval(spec, name, new int[] { 2, 2, 10 });
			run_eval(spec, name, new int[] { 3, 3, 10 }, 0);
			run_eval(spec, name, new int[] { 2, 2, 100 }, 0);
		}
		run_eval(spec, name, new int[] { 2, 2, 100 }, 0);

	}

	/**
	 * Runs a benchmark for the property GrantCancel
	 * 
	 * @param spec
	 *            QEA
	 * 
	 * @param name
	 *            String with the name of the property. Should be "GrantCancel"
	 */
	public void eval_for_GrantCancel(S spec, String name) {

		if (!hardest_only) {
			run_eval(spec, name, new int[] { 10, 10, 1000 }, 100);
			run_eval(spec, name, new int[] { 10, 10, 10000 });
			run_eval(spec, name, new int[] { 100, 100, 1000 }, 0);
			run_eval(spec, name, new int[] { 100, 100, 10000 }, 0);
			run_eval(spec, name, new int[] { 1000, 1000, 1000 });
			run_eval(spec, name, new int[] { 1000, 1000, 10000 }, 0);
		}
		run_eval(spec, name, new int[] { 1000, 1000, 100000 }, 0);

	}

	public void eval_for_ReleaseResource(S spec, String name) {

		if (!hardest_only) {
			run_eval(spec, name, new int[] { 5, 5, 100 });
			run_eval(spec, name, new int[] { 5, 5, 1000 });
			run_eval(spec, name, new int[] { 10, 10, 100 }, 0);
			run_eval(spec, name, new int[] { 10, 10, 1000 }, 0);
		}
		run_eval(spec, name, new int[] { 10, 10, 10000 }, 0);

	}

	public void eval_for_RespectConflicts(S spec, String name) {

		if (!hardest_only) {
			run_eval(spec, name, new int[] { 3, 100, 100 });
			run_eval(spec, name, new int[] { 4, 100, 100 }, 0);
			run_eval(spec, name, new int[] { 5, 100, 100 }, 0);
			run_eval(spec, name, new int[] { 6, 100, 100 }, 0);
			run_eval(spec, name, new int[] { 7, 100, 100 }, 0);
			run_eval(spec, name, new int[] { 3, 1000, 1000 }, 0);
			run_eval(spec, name, new int[] { 4, 1000, 1000 }, 0);
		}
		run_eval(spec, name, new int[] { 5, 1000, 1000 }, 0);

	}

	public void eval_for_ExistsSatellite(S spec, String name) {

		if (!hardest_only) {
			run_eval(spec, name, new int[] { 10, 10 });
			run_eval(spec, name, new int[] { 10, 100 });
			run_eval(spec, name, new int[] { 10, 1000 }, 0);
			run_eval(spec, name, new int[] { 100, 100 }, 0);
			run_eval(spec, name, new int[] { 100, 1000 }, 0);
		}
		run_eval(spec, name, new int[] { 1000, 1000 }, 0);

	}

	public void eval_for_ExistsLeader(S spec, String name) {

		if (!hardest_only) {
			run_eval(spec, name, new int[] { 5 });
			run_eval(spec, name, new int[] { 10 }, 0);
			run_eval(spec, name, new int[] { 15 }, 0);
			run_eval(spec, name, new int[] { 20 }, 0);
		}
		run_eval(spec, name, new int[] { 30 }, 0);

	}

	public void eval_for_MessageHashCorrectInvInt(S spec, String name) {

		if (!hardest_only) {
			run_eval(spec, name, new int[] { 100, 100 }, 50);
			run_eval(spec, name, new int[] { 100, 1000 });
			run_eval(spec, name, new int[] { 100, 10000 }, 0);
			run_eval(spec, name, new int[] { 1000, 1000 }, 0);
			run_eval(spec, name, new int[] { 1000, 10000 }, 0);
			run_eval(spec, name, new int[] { 1000, 100000 }, 0);
		}
		run_eval(spec, name, new int[] { 1000, 1000000 }, 0);

	}

	public void eval_for_RespectPriorities(S spec, String name) {
		if (!hardest_only) {
			// none here!
		}
		// run_eval(spec, name, new int[]{5,100});
		run_eval(spec, name, new int[] { 20, 100000 });
	}
}
