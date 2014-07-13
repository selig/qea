package benchmark.competition.java.jUnitRV_MMT.monitoring;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.Property;
import properties.competition.JavaRV_mmt;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import benchmark.competition.java.jUnitRV_MMT.ExampleRoutes;
import benchmark.competition.java.jUnitRV_MMT.ExampleRoutes.Route;

public aspect JUnitRV_MMTFiveAspect {

	private final Monitor monitor;

	private final int BLOCK_ROUTE = 1;
	private final int FREE_ROUTE = 2;

	public JUnitRV_MMTFiveAspect() {
		QEA qea = new JavaRV_mmt().make(Property.JAVARV_MMT_FIVE);
		monitor = MonitorFactory.create(qea);
	}

	pointcut blockRoute(Route route) :
		call(void ExampleRoutes.blockRoute(Route)) && args(route);

	pointcut freeRoute(Route route) : call(void ExampleRoutes.freeRoute(Route))
		&& args(route);

	before(Route route) : blockRoute(route)	 {
		Verdict verdict = monitor.step(BLOCK_ROUTE, route, route.getFromX(),
				route.getFromY(), route.getToX(), route.getToY());
		if (verdict == Verdict.FAILURE) {
			System.err.println("Violation in JUnitRV (MMT) 5. [route=" + route
					+ "]. Blocked routes must not cross.");
		}

	};

	before(Route route) : freeRoute(route) {
		Verdict verdict = monitor.step(FREE_ROUTE, route);
		if (verdict == Verdict.FAILURE) {
			System.err.println("Violation in JUnitRV (MMT) 5. [route=" + route
					+ "]. Blocked routes must not cross.");
		}
	};
}
