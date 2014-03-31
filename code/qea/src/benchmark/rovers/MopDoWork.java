package benchmark.rovers;

public class MopDoWork extends DoWork<String> {

	@Override
	public void run_with_spec(String spec, String name, int[] args) {
		// Doesn't do anything as we use aspects

	}

	/*
	 * THE EMPTY METHODS THAT THE ASPECTJ GRABS ONTO!!
	 */
	public static String last_event = "";

	@Override
	public void command(int x) {
		last_event = "command";
	}

	@Override
	public void request(Object o) {
		last_event = "request";
	}

	@Override
	public void grant(Object o) {
		last_event = "grant";
	}

	@Override
	public void deny(Object o) {
		last_event = "deny";
	}

	@Override
	public void rescind(Object o) {
		last_event = "rescind";
	}

	@Override
	public void cancel(Object o) {
		last_event = "cancel";
	}

	@Override
	public void succeed(Object o) {
		last_event = "succeed";
	}

	@Override
	public void command(Object o) {
		last_event = "command";
	}

	@Override
	public void set_ack_timeout(int x) {
		last_event = "set_ack_timeout";
	}

	@Override
	public void command(Object a, Object b, Object c, int d) {
		last_event = "command";
	}

	@Override
	public void ack(Object o, int x) {
		last_event = "ack";
	}

	@Override
	public void grant(Object a, Object b) {
		last_event = "grant";
	}

	@Override
	public void cancel(Object a, Object b) {
		last_event = "cancel";
	}

	@Override
	public void schedule(Object a, Object b) {
		last_event = "schedule";
	}

	@Override
	public void finish(Object o) {
		last_event = "finish";
	}

	@Override
	public void conflict(Object a, Object b) {
		last_event = "conflict";
	}

	@Override
	public void ping(Object a, Object b) {
		last_event = "ping";
	}

	@Override
	public void ack(Object a, Object b) {
		last_event = "ack";
	}

	@Override
	public void ack(Object a, Object b, int c) {
		last_event = "ack";
	}

	@Override
	public void send(Object a, Object b, int c) {
		last_event = " send";
	}

}
