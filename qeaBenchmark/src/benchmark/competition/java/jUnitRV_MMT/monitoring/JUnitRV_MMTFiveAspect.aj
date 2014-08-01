package benchmark.competition.java.jUnitRV_MMT.monitoring;

import properties.Property;
import properties.competition.JavaRV_mmt;
import structure.impl.other.Verdict;
import benchmark.competition.java.QEAMonitoringAspect;
import benchmark.competition.java.jUnitRV_MMT.ExampleRoutes;
import benchmark.competition.java.jUnitRV_MMT.ExampleRoutes.Route;

public aspect JUnitRV_MMTFiveAspect extends QEAMonitoringAspect {

	private final int BLOCK_ROUTE = 1;
	private final int FREE_ROUTE = 2;

	public JUnitRV_MMTFiveAspect() {
		super(new JavaRV_mmt().make(Property.JAVARV_MMT_FIVE));
		validationMsg = "Property JUnitRV (MMT) 5 satisfied";
		violationMsg = "Property JUnitRV (MMT) 5 violated. Blocked routes must not cross";
	}

	pointcut blockRoute(Route route) :
		call(void ExampleRoutes.blockRoute(Route)) && args(route);

	pointcut freeRoute(Route route) : call(void ExampleRoutes.freeRoute(Route))
		&& args(route);

	before(Route route) : blockRoute(route)	 {
		Verdict verdict = monitor.step(BLOCK_ROUTE, route, route.getFromX(),
				route.getFromY(), route.getToX(), route.getToY());
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [route=" + route + "]");
			printTimeAndExit();
		}
	};

	before(Route route) : freeRoute(route) {
		Verdict verdict = monitor.step(FREE_ROUTE, route);
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [route=" + route + "]");
			printTimeAndExit();
		}
	};
}
