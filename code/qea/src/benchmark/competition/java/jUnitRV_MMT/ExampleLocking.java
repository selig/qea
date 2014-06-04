package benchmark.competition.java.jUnitRV_MMT;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.Property;
import properties.competition.JavaRV_mmt;
import structure.intf.QEA;

// Java - Team3 - Bench3

public class ExampleLocking implements Runnable {

	private static QEA qea;
	private static Monitor monitor;
	private static int RUN;
	private static int LOCK_TRUE;
	private static int UNLOCK;
	private static int ACTION;

	public static void main(String... argv) throws ExecutionException,
			InterruptedException {

		setUpQEAMonitoring();
//		int n = 5;
		int n = 2;
		ExecutorService executor = Executors.newFixedThreadPool(n);
		Future<?>[] futures = new Future<?>[n];
		for (int i = 0; i < n; ++i) {
			futures[i] = executor.submit(new ExampleLocking(i));
		}

		for (int i = 0; i < n; ++i) {
			futures[i].get();
		}
		executor.shutdown();
	}

	public ExampleLocking(int id) {
		this.id = id;
	}

	@Override
	public void run() {
		System.out.println(this.id + " " + monitor.step(RUN, this.id));
		for (int i = 0; i < 1000;) {
			if (lock()) {
				System.out.println(this.id + " " + monitor.step(LOCK_TRUE, this.id));
				try {
					action();
					Thread.sleep(5);
					++i;
					unlock();
					Thread.sleep(5);
				} catch (InterruptedException ex) {
				}
			}
		}
	}

	public boolean lock() {
		return lock.tryLock();
	}

	public void action() {
		System.out.println(id);
		System.out.println(this.id + " " + monitor.step(ACTION, this.id));
	}

	public void unlock() {
		lock.unlock();
		System.out.println(this.id + " " + monitor.step(UNLOCK, this.id));
	}

	private static final Lock lock = new ReentrantLock();

	private final int id;

	public static void setUpQEAMonitoring() {
		qea = new JavaRV_mmt().make(Property.JAVARV_MMT_THREE);
		monitor = MonitorFactory.create(qea);
		RUN = qea.get_event_id("run");
		LOCK_TRUE = qea.get_event_id("lock_true");
		UNLOCK = qea.get_event_id("unlock");
		ACTION = qea.get_event_id("action");
	}

}