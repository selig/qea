package benchmark.rovers.junitrv;

import benchmark.rovers.DoWork;

public class JunitRVDoWork extends DoWork<String> {

	@Override
	public void run_with_spec(String spec, String name, int[] args) {
		// Doesn't do anything as we use RVunit to instrument
		dowork(name, args);
	}

	private int id(Object o) {
		return System.identityHashCode(o);
	}

	/*
	 * THE EMPTY METHODS THAT THE ASPECTJ GRABS ONTO!!
	 */
	public static String last_event = "";
	public static boolean print_events = true;

	@Override
	public void command_int(int x) {
		last_event = "command";
		if (print_events) {
			System.err.println(last_event);
		}
	}

	@Override
	public void request(Object o) {
		last_event = "request " + id(o);
		if (print_events) {
			System.err.println(last_event);
		}
	}

	public void grant(Object o) {
		last_event = "grant" + id(o);
		if (print_events) {
			System.err.println(last_event);
		}
	}

	@Override
	public void deny(Object o) {
		last_event = "deny" + id(o);
		if (print_events) {
			System.err.println(last_event);
		}
	}

	@Override
	public void rescind(Object o) {
		last_event = "rescind" + id(o);
		if (print_events) {
			System.err.println(last_event);
		}
	}

	public void cancel(Object o) {
		last_event = "cancel" + id(o);
		if (print_events) {
			System.err.println(last_event);
		}
	}

	@Override
	public void succeed(Object o) {
		last_event = "succeed";
		if (print_events) {
			System.err.println(last_event);
		}
	}

	@Override
	public void command(Object o) {
		last_event = "command";
		if (print_events) {
			System.err.println(last_event);
		}
	}

	@Override
	public void set_ack_timeout(int x) {
		last_event = "set_ack_timeout";
		if (print_events) {
			System.err.println(last_event);
		}
	}

	@Override
	public void command(Object a, Object b, Object c, int d) {
		last_event = "command";
		if (print_events) {
			System.err.println(last_event);
		}
	}

	@Override
	public void ack(Object o, int x) {
		last_event = "ack";
		if (print_events) {
			System.err.println(last_event);
		}
	}

	public void grant(Object a, Object b) {
		last_event = "grant";
		if (print_events) {
			System.err.println(last_event);
		}
	}

	public void cancel(Object a, Object b) {
		last_event = "cancel";
		if (print_events) {
			System.err.println(last_event);
		}
	}

	@Override
	public void schedule(Object a, Object b) {
		last_event = "schedule";
		if (print_events) {
			System.err.println(last_event);
		}
	}

	@Override
	public void finish(Object o) {
		last_event = "finish";
		if (print_events) {
			System.err.println(last_event);
		}
	}

	@Override
	public void conflict(Object a, Object b) {
		last_event = "conflict";
		if (print_events) {
			System.err.println(last_event);
		}
	}

	@Override
	public void ping(Object a, Object b) {
		last_event = "ping";
		if (print_events) {
			System.err.println(last_event);
		}
	}

	@Override
	public void ack(Object a, Object b) {
		last_event = "ack";
		if (print_events) {
			System.err.println(last_event);
		}
	}

	@Override
	public void ack(Object a, Object b, int c) {
		last_event = "ack";
		if (print_events) {
			System.err.println(last_event);
		}
	}

	@Override
	public void send(Object a, Object b, int c) {
		last_event = "send";
		if (print_events) {
			System.err.println(last_event);
		}
	}

	@Override
	public void fail(Object o) {
		// TODO Is this method intercepted by AspectJ???
	}

	@Override
	public void priority(Object a, Object b) {
		// TODO Is this method intercepted by AspectJ???
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