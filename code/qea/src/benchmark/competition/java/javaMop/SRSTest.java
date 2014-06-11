package benchmark.competition.java.javaMop;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.Property;
import properties.competition.JavaMOP;
import structure.impl.other.Verdict;
import structure.intf.QEA;

// Java - Team4 - Bench3

public class SRSTest {

	private static QEA qea;
	private static Monitor monitor;
	public static int A;
	public static int B;
	public static int C;

	public static void main(String[] args) {

		setUpQEAMonitoring();

		int times = Integer.parseInt(args[0]);

		Thread t1 = new CThread2("thread1", "A", times);
		t1.start();
		Thread t2 = new CThread2("thread2", "B", times);
		t2.start();
		Thread t3 = new CThread2("thread3", "C", times);
		t3.start();

		try {
			t1.join();
			t2.join();
			t3.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setUpQEAMonitoring() {
		qea = new JavaMOP().make(Property.JAVAMOP_THREE);
		monitor = MonitorFactory.create(qea);
		A = qea.get_event_id("a");
		B = qea.get_event_id("b");
		C = qea.get_event_id("c");
	}

	public static Verdict check(int event) {
		Verdict v = null;
		synchronized (monitor) {
			v = monitor.step(event);
		}
		System.out.println(v);
		return v;
	}
}

class CThread2 extends Thread {

	private int times;
	private String func;

	public CThread2(String name, String fun, int t) {
		this.setName(name);
		this.func = fun;
		this.times = t;
	}

	public void run() {
		if (func.equals("A")) {
			for (int i = 0; i < times; i++) {
				try {
					A();
					Thread.sleep(500);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (func.equals("B")) {
			for (int i = 0; i < times - 1; i++) {
				try {
					B();
					Thread.sleep(500);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (func.equals("C")) {
			for (int i = 0; i < times; i++) {
				try {
					C();
					Thread.sleep(500);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void A() {
		System.out.println("A()");
		SRSTest.check(SRSTest.A);
	}

	public void B() {
		System.out.println("B()");
		SRSTest.check(SRSTest.B);
	}

	public void C() {
		System.out.println("C()");
		SRSTest.check(SRSTest.C);
	}

}
