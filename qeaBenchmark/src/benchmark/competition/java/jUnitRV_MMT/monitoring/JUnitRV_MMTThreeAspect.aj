package benchmark.competition.java.jUnitRV_MMT.monitoring;

import java.util.concurrent.locks.Lock;

import properties.Property;
import properties.competition.JavaRV_mmt;
import rvcomp.ExampleLocking;
import structure.impl.other.Verdict;
import benchmark.competition.java.QEAMonitoringAspect;

public aspect JUnitRV_MMTThreeAspect extends QEAMonitoringAspect {

	private final int RUN = 1;
	private final int LOCK_TRUE = 2;
	private final int UNLOCK = 3;
	private final int ACTION = 4;

	public JUnitRV_MMTThreeAspect() {
		super(new JavaRV_mmt().make(Property.JAVARV_MMT_THREE));
		validationMsg = "Property JUnitRV (MMT) 3 satisfied";
		violationMsg = "Property JUnitRV (MMT) 3 violated. - A thread has to call lock (returning true) to acquire the lock before it may call action - A call to lock only returns true, if no thread is currently holding the lock";
	}

	pointcut run(ExampleLocking thread) : execution(void ExampleLocking.run())
		&& target(thread);

	pointcut action(ExampleLocking thread) : call(void ExampleLocking.action())
		&& target(thread);

	before(ExampleLocking thread) : run(thread) {
		Verdict verdict = null;
		synchronized (monitor) {
			verdict = monitor.step(RUN, thread);
		}
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [thread=" + thread + "]");
			printTimeAndExit();
		}
	};

	boolean around(Lock lockObj, ExampleLocking thread) :
		call(boolean Lock.tryLock()) && target(lockObj) && this(thread) 
		&& within(ExampleLocking) {

		boolean result = false;
		// We perform the lock and the monitor step as an atomic action
		// i.e. whilst holding the lockObj, so that we cannot be interrupted
		synchronized (lockObj) {
			result = proceed(lockObj, thread);
			if (result) {
				Verdict verdict = null;
				// Ordering between lockObj and monitor important
				synchronized (monitor) {
					verdict = monitor.step(LOCK_TRUE, thread);
				}
				if (verdict == Verdict.FAILURE) {
					System.err.println(violationMsg + " [thread=" + thread
							+ "]");
					printTimeAndExit();
				}
			}
		}
		return result;
	};

	void around(Lock lockObj, ExampleLocking thread) :
		call(void Lock.unlock()) && target(lockObj) && this(thread)
		&& within(ExampleLocking) {

		// We perform the unlock and the monitor step as an atomic action
		// i.e. whilst holding the lockObj, so that we cannot be interrupted
		Verdict verdict = null;
		synchronized (lockObj) {
			proceed(lockObj, thread);
			// Ordering between lockObj and monitor important
			synchronized (monitor) {
				verdict = monitor.step(UNLOCK, thread);
			}
		}
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [thread=" + thread + "]");
			printTimeAndExit();
		}
	};

	before(ExampleLocking thread) : action(thread) {
		Verdict verdict = null;
		synchronized (monitor) {
			verdict = monitor.step(ACTION, thread);
		}
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [thread=" + thread + "]");
			printTimeAndExit();
		}
	};
}
