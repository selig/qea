package benchmark.competition.java.jUnitRV_MMT.monitoring;

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

	pointcut run(ExampleLocking t) : call(void *.run())
		&& target(t);

	pointcut lockEvent(ExampleLocking t) : call(boolean ExampleLocking.lock())
		&& target(t);

	pointcut unlockEvent(ExampleLocking t) :
		call(void ExampleLocking.unlock()) && target(t);

	pointcut action(ExampleLocking t) : call(void ExampleLocking.action())
		&& target(t);

	before(ExampleLocking t) : run(t) {
		Verdict verdict = null;
		synchronized (monitor) {
			verdict = monitor.step(RUN, t.getId());
		}
		if (verdict == Verdict.FAILURE || verdict == Verdict.WEAK_FAILURE) {
			System.err
					.println("Violation in JUnitRV (MMT) 3. [id="
							+ t.getId()
							+ "]. - A thread has to call lock (returning true) to acquire the lock before it may call action - A call to lock only returns true, if no thread is currently holding the lock.");
		}
	};

	after(ExampleLocking t) returning(boolean result) : lockEvent(t) {
		if (result) {
			Verdict verdict = null;
			synchronized (monitor) {
				verdict = monitor.step(LOCK_TRUE, t.getId());
			}
			if (verdict == Verdict.FAILURE || verdict == Verdict.WEAK_FAILURE) {
				System.err
						.println("Violation in JUnitRV (MMT) 3. [id="
								+ t.getId()
								+ "]. - A thread has to call lock (returning true) to acquire the lock before it may call action - A call to lock only returns true, if no thread is currently holding the lock.");
			}
		}
	};

	before(ExampleLocking t) : unlockEvent(t) {
		Verdict verdict = null;
		synchronized (monitor) {
			verdict = monitor.step(UNLOCK, t.getId());
		}
		if (verdict == Verdict.FAILURE || verdict == Verdict.WEAK_FAILURE) {
			System.err
					.println("Violation in JUnitRV (MMT) 3. [id="
							+ t.getId()
							+ "]. - A thread has to call lock (returning true) to acquire the lock before it may call action - A call to lock only returns true, if no thread is currently holding the lock.");
		}
	};

	before(ExampleLocking t) : action(t) {
		Verdict verdict = null;
		synchronized (monitor) {
			verdict = monitor.step(ACTION, t.getId());
		}
		if (verdict == Verdict.FAILURE || verdict == Verdict.WEAK_FAILURE) {
			System.err
					.println("Violation in JUnitRV (MMT) 3. [id="
							+ t.getId()
							+ "]. - A thread has to call lock (returning true) to acquire the lock before it may call action - A call to lock only returns true, if no thread is currently holding the lock.");
		}
	};

}
