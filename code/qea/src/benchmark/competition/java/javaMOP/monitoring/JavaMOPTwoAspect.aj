package benchmark.competition.java.javaMOP.monitoring;

import java.util.concurrent.locks.Lock;

import monitoring.intf.QEAMonitoringAspect;
import properties.Property;
import properties.competition.JavaMOP;
import structure.impl.other.Verdict;

public aspect JavaMOPTwoAspect extends QEAMonitoringAspect {

	private final int LOCK = 1;
	private final int UNLOCK = 2;

	public JavaMOPTwoAspect() {
		super(new JavaMOP().make(Property.JAVAMOP_TWO));
		validationMsg = "Property JavaMOP 2 satisfied";
		violationMsg = "Property JavaMOP 2 violated. A thread should release a lock as many times as it acquires the lock";
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
			System.err.println(violationMsg + " [thread=" + thread + ", lock="
					+ lockObj + "]");
			printTimeAndExit();
		}
	}

	before(Thread thread, Lock lockObj) : unlockPC(thread, lockObj) {
		Verdict verdict = null;
		synchronized (monitor) {
			verdict = monitor.step(UNLOCK, thread, lockObj);
		}
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [thread=" + thread + ", lock="
					+ lockObj + "]");
			printTimeAndExit();
		}
	}
}
