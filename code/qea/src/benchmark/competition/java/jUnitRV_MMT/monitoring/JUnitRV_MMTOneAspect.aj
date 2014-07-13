package benchmark.competition.java.jUnitRV_MMT.monitoring;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.Property;
import properties.competition.JavaRV_mmt;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import benchmark.competition.java.jUnitRV_MMT.ExampleCounting;

public aspect JUnitRV_MMTOneAspect {

	private final Monitor monitor;

	private final int STEP = 1;

	public JUnitRV_MMTOneAspect() {
		QEA qea = new JavaRV_mmt().make(Property.JAVARV_MMT_ONE);
		monitor = MonitorFactory.create(qea);
	}

	pointcut step(int counter) : call(void ExampleCounting.step(int))
		&& args(counter);

	before(int counter) : step(counter) {
		// System.out.println(">> step(" + counter + ")");
		Verdict verdict = monitor.step(STEP, counter);
		if (verdict == Verdict.FAILURE) {
			System.err
					.println("Violation in JUnitRV (MMT) 1. [counter="
							+ counter
							+ "]. The parameter counter of method step has to increase by 1 with each call.");
			System.exit(0);
		}
	}

	pointcut endOfProgram() : execution(void ExampleCounting.main(String...));

	after() : endOfProgram() {
		Verdict verdict = monitor.end();
		if (verdict == Verdict.FAILURE || verdict == Verdict.WEAK_FAILURE) {
			System.err
					.println("Violation in JUnitRV (MMT) 1. The parameter counter of method step has to increase by 1 with each call.");
		} else {
			System.err.println("Property JUnitRV (MMT) 1 satisfied");
		}
	}
	
}
