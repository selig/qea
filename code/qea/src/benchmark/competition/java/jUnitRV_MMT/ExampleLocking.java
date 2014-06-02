package benchmark.competition.java.jUnitRV_MMT;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Java - Team3 - Bench3

public class ExampleLocking implements Runnable {

	public static void main(String... argv) throws ExecutionException,
			InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(5);
		int n = 5;
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
		for (int i = 0; i < 1000;) {
			if (lock()) {
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
	}

	public void unlock() {
		lock.unlock();
	}

	private static final Lock lock = new ReentrantLock();

	private final int id;

}