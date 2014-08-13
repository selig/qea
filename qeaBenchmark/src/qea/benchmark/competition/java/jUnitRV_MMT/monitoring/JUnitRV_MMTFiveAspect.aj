package qea.benchmark.competition.java.jUnitRV_MMT.monitoring;

import qea.properties.Property;
import qea.properties.competition.JavaRV_mmt;
import qea.rvcomp.ExampleRoutes;
import qea.rvcomp.ExampleRoutes.Route;
import qea.structure.impl.other.Verdict;
import qea.benchmark.competition.java.QEAMonitoringAspect;

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
