package benchmark.competition.java.jUnitRV_MMT.monitoring;

import java.util.concurrent.locks.Lock;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.Property;
import properties.competition.JavaRV_mmt;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import benchmark.competition.java.jUnitRV_MMT.ExampleLocking;

public aspect JUnitRV_MMTThreeAspect {

	private final Monitor monitor;

	private final int RUN = 1;
	private final int LOCK_TRUE = 2;
	private final int UNLOCK = 3;
	private final int ACTION = 4;

	public JUnitRV_MMTThreeAspect() {
		QEA qea = new JavaRV_mmt().make(Property.JAVARV_MMT_THREE);
		monitor = MonitorFactory.create(qea);
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
			System.err
					.println("Violation in JUnitRV (MMT) 3. [thread="
							+ thread
							+ "]. - A thread has to call lock (returning true) to acquire the lock before it may call action - A call to lock only returns true, if no thread is currently holding the lock.");
			System.exit(0);
		}
	};

	boolean around(Lock lockObj, ExampleLocking thread) :
			call(boolean Lock.tryLock())
			&& cflow(call(boolean ExampleLocking.lock())) && target(lockObj)
			&& this(thread) {

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
					System.err
							.println("Violation in JUnitRV (MMT) 3. [thread="
									+ thread
									+ "]. - A thread has to call lock (returning true) to acquire the lock before it may call action - A call to lock only returns true, if no thread is currently holding the lock.");
					System.exit(0);
				}
	
			}
		}
		return result;
	};

	void around(Lock lockObj, ExampleLocking thread) :
		call(void Lock.unlock()) && cflow(call(void ExampleLocking.unlock()))
		&& target(lockObj) && this(thread) {

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
			System.err
					.println("Violation in JUnitRV (MMT) 3. [thread="
							+ thread
							+ "]. - A thread has to call lock (returning true) to acquire the lock before it may call action - A call to lock only returns true, if no thread is currently holding the lock.");
			System.exit(0);
		}
	};

	before(ExampleLocking thread) : action(thread) {
		Verdict verdict = null;
		synchronized (monitor) {
			verdict = monitor.step(ACTION, thread);
		}
		if (verdict == Verdict.FAILURE) {
			System.err
					.println("Violation in JUnitRV (MMT) 3. [id="
							+ thread
							+ "]. - A thread has to call lock (returning true) to acquire the lock before it may call action - A call to lock only returns true, if no thread is currently holding the lock.");
			System.exit(0);
		}
	};
	
	pointcut endOfProgram() : execution(void ExampleLocking.main(String...));

	after() : endOfProgram() {
		Verdict verdict = monitor.end();
		if (verdict == Verdict.FAILURE || verdict == Verdict.WEAK_FAILURE) {
			System.err
					.println("Violation in JUnitRV (MMT) 3. - A thread has to call lock (returning true) to acquire the lock before it may call action - A call to lock only returns true, if no thread is currently holding the lock.");
		} else {
			System.err.println("Property JUnitRV (MMT) 3 satisfied");
		}
	}

}
