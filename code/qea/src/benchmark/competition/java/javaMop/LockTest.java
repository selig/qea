package benchmark.competition.java.javaMop;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.Property;
import properties.competition.JavaMOP;
import structure.impl.other.Verdict;
import structure.intf.QEA;

// Java - Team4 - Bench2

public class LockTest {

	private static QEA qea;
	private static Monitor monitor;
	public static int LOCK;
	public static int UNLOCK;

	public static int sum = 0;

	public static void main(String[] args) {
		
		setUpQEAMonitoring();

		int depth = Integer.parseInt(args[0]);

		Lock l1 = new ReentrantLock();
		Lock l2 = new ReentrantLock();
		Lock l3 = new ReentrantLock();

		Thread t1 = new CThread(l1, "thread1", 1, depth);
		t1.start();
		Thread t2 = new CThread(l1, "thread2", 2, depth);
		t2.start();
		Thread t3 = new CThread(l1, "thread3", 3, depth);
		t3.start();
		Thread t4 = new WThread1(l2, "thread4", 4, depth);
		t4.start();
		Thread t5 = new CThread(l2, "thread5", 5, depth);
		t5.start();
		Thread t6 = new CThread(l2, "thread6", 6, depth);
		t6.start();

		try {
			t1.join();
			t2.join();
			t3.join();
			t4.join();
			t5.join();
			t6.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("The result is " + sum);

	}

	public static void setUpQEAMonitoring() {
		qea = new JavaMOP().make(Property.JAVAMOP_TWO);
		monitor = MonitorFactory.create(qea);
		LOCK = qea.get_event_id("lock");
		UNLOCK = qea.get_event_id("unlock");
	}

	public static Verdict check(int event, Object[] params) {
		Verdict v = null;
		synchronized (monitor) {
			v = monitor.step(event, params);
		}
		System.out.println(v);
		return v;
	}

}

class CThread extends Thread {

	private Lock lo;
	private int tNum;
	private int depth;

	public CThread(Lock l, String name, int threadNo, int dep) {
		this.lo = l;
		this.setName(name);
		this.tNum = threadNo;
		this.depth = dep;
	}

	public void doSomething() {
		if (depth == 1) {
			try {
				lo.lock();
				LockTest.check(LockTest.LOCK, new Object[] { tNum, lo });
				LockTest.sum += this.tNum;
				Thread.sleep(1000);
			} catch (Exception e) {
				System.err.println(e);
			} finally {
				lo.unlock();
				LockTest.check(LockTest.UNLOCK, new Object[] { tNum, lo });
			}
		} else {
			try {
				depth--;
				lo.lock();
				LockTest.check(LockTest.LOCK, new Object[] { tNum, lo });
				doSomething();
			} catch (Exception e) {
				System.err.println(e);
			} finally {
				lo.unlock();
				LockTest.check(LockTest.UNLOCK, new Object[] { tNum, lo });
			}
		}

	}

	public void run() {
		doSomething();
	}
}

class WThread1 extends Thread {
	private Lock lo;
	private int tNum;
	private int depth;

	public WThread1(Lock l, String name, int threadNo, int dep) {
		this.lo = l;
		this.setName(name);
		this.tNum = threadNo;
		this.depth = dep;
	}

	public void doSomething() {
		if (depth == 1) {
			try {
				lo.lock();
				LockTest.check(LockTest.LOCK, new Object[] { tNum, lo });
				LockTest.sum += this.tNum;
				System.out.println("Oops,thread " + this.tNum
						+ " may cause deadlock!");
				Thread.sleep(1000);
			} catch (Exception e) {
				System.err.println(e);
			}
		} else {
			try {
				depth--;
				lo.lock();
				LockTest.check(LockTest.LOCK, new Object[] { tNum, lo });
				doSomething();
			} catch (Exception e) {
				System.err.println(e);
			} finally {
				lo.unlock();
				LockTest.check(LockTest.UNLOCK, new Object[] { tNum, lo });
			}
		}

	}

	public void run() {
		doSomething();
	}
}
