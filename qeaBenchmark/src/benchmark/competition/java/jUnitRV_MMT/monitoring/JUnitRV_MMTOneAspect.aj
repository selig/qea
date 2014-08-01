package benchmark.competition.java.jUnitRV_MMT.monitoring;

import properties.Property;
import properties.competition.JavaRV_mmt;
import structure.impl.other.Verdict;
import benchmark.competition.java.QEAMonitoringAspect;
import benchmark.competition.java.jUnitRV_MMT.ExampleCounting;

public aspect JUnitRV_MMTOneAspect extends QEAMonitoringAspect {

	private final int STEP = 1;

	public JUnitRV_MMTOneAspect() {
		super(new JavaRV_mmt().make(Property.JAVARV_MMT_ONE));
		validationMsg = "Property JUnitRV (MMT) 1 satisfied";
		violationMsg = "Property JUnitRV (MMT) 1 violated. The parameter counter of method step has to increase by 1 with each call";
	}

	pointcut step(int counter) : call(void ExampleCounting.step(int))
		&& args(counter);

	before(int counter) : step(counter) {
		Verdict verdict = monitor.step(STEP, counter);
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [counter=" + counter + "]");
			printTimeAndExit();
		}
	}
}
