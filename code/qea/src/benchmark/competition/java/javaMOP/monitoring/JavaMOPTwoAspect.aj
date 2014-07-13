package benchmark.competition.java.javaMOP.monitoring;

import java.util.concurrent.locks.Lock;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.Property;
import properties.competition.JavaMOP;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import benchmark.competition.java.javaMOP.LockTest;

public aspect JavaMOPTwoAspect {

	private final Monitor monitor;

	private final int LOCK = 1;
	private final int UNLOCK = 2;

	public JavaMOPTwoAspect() {
		QEA qea = new JavaMOP().make(Property.JAVAMOP_TWO);
		monitor = MonitorFactory.create(qea);
	}

	pointcut lockPC(Thread thread, Lock lockObj) : call(void Lock.lock())
		&& target(lockObj) && this(thread);

	pointcut unlockPC(Thread thread, Lock lockObj) : call(void Lock.unlock())
		&& target(lockObj) && this(thread);

	before(Thread thread, Lock lockObj) : lockPC(thread, lockObj) {
		Verdict verdict = null;
		synchronized (monitor) {
			verdict = monitor.step(LOCK, thread, lockObj);
		}
		if (verdict == Verdict.FAILURE) {
			System.err
					.println("Violation in JavaMOP 2. [thread="
							+ thread
							+ ", lock="
							+ lockObj
							+ "]. A thread should release a lock as many times as it acquires the lock.");
		}
	}

	before(Thread thread, Lock lockObj) : unlockPC(thread, lockObj) {
		Verdict verdict = null;
		synchronized (monitor) {
			verdict = monitor.step(UNLOCK, thread, lockObj);
		}
		if (verdict == Verdict.FAILURE) {
			System.err
					.println("Violation in JavaMOP 2. [thread="
							+ thread
							+ ", lock="
							+ lockObj
							+ "]. A thread should release a lock as many times as it acquires the lock.");
		}
	}

	pointcut endOfProgram() : execution(void LockTest.main(String[]));

	after() : endOfProgram() {
		Verdict verdict = monitor.end();
		if (verdict == Verdict.FAILURE || verdict == Verdict.WEAK_FAILURE) {
			System.err
					.println("Violation in JavaMOP 2. A thread should release a lock as many times as it acquires the lock.");
		} else {
			System.err.println("Property JavaMOP 2 satisfied");
		}
	}
}
