package benchmark.competition.java.jUnitRV_MMT.monitoring;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.Property;
import properties.competition.JavaRV_mmt;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import benchmark.competition.java.jUnitRV_MMT.ExampleVelocity;

public aspect JUnitRV_MMTFourAspect {

	private final Monitor monitor;

	private final int STEP = 1;

	public JUnitRV_MMTFourAspect() {
		QEA qea = new JavaRV_mmt().make(Property.JAVARV_MMT_FOUR);
		monitor = MonitorFactory.create(qea);
	}

	pointcut step(double pos, double time) :
		call(void ExampleVelocity.step(double, double)) && args(pos, time);

	before(double pos, double time) : step(pos, time) {
		// System.out.println(">> step(" + pos + "," + time + ")");
		Verdict verdict = monitor.step(STEP, pos, time);
		if (verdict == Verdict.FAILURE || verdict == Verdict.WEAK_FAILURE) {
			System.err
					.println("Violation in JUnitRV (MMT) 4. [pos="
							+ pos
							+ "] [time="
							+ time
							+ "]. The average speed between two observations must never exceed the maximal velocity 10.");
		}
	}

}
