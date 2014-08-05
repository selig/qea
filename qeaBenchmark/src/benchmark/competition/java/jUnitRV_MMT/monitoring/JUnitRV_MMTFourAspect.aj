package benchmark.competition.java.jUnitRV_MMT.monitoring;

import properties.Property;
import properties.competition.JavaRV_mmt;
import rvcomp.ExampleVelocity;
import structure.impl.other.Verdict;
import benchmark.competition.java.QEAMonitoringAspect;

public aspect JUnitRV_MMTFourAspect extends QEAMonitoringAspect {

	private final int STEP = 1;

	public JUnitRV_MMTFourAspect() {
		super(new JavaRV_mmt().make(Property.JAVARV_MMT_FOUR));
		validationMsg = "Property JUnitRV (MMT) 4 satisfied";
		violationMsg = "Property JUnitRV (MMT) 4 violated. The average speed between two observations must never exceed the maximal velocity 10";
	}

	pointcut step(double pos, double time) :
		call(void ExampleVelocity.step(double, double)) && args(pos, time);

	before(double pos, double time) : step(pos, time) {
		Verdict verdict = monitor.step(STEP, pos, time);
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [pos=" + pos + ", time=" + time
					+ "]");
			printTimeAndExit();
		}
	}
}
