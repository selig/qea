package benchmark.competition.java.jUnitRV_MMT.monitoring;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.Property;
import properties.competition.JavaRV_mmt;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import benchmark.competition.java.jUnitRV_MMT.ExampleRequestResponse;

public aspect JUnitRV_MMTTwoAspect {

	private final Monitor monitor;

	private final int REQUEST = 1;
	private final int RESPONSE = 2;

	public JUnitRV_MMTTwoAspect() {
		QEA qea = new JavaRV_mmt().make(Property.JAVARV_MMT_TWO);
		monitor = MonitorFactory.create(qea);
	}

	pointcut request(int service) :
		call(void ExampleRequestResponse.request(int)) && args(service);

	pointcut response(int service) :
		call(void ExampleRequestResponse.respond(int, int)) && args(service, *);

	before(int service) : request(service) {
		// System.out.println("request(" + service + ")");
		Verdict verdict = monitor.step(REQUEST, service);
		if (verdict == Verdict.FAILURE) {
			System.err
					.println("Violation in JUnitRV (MMT) 2. [service="
							+ service
							+ "]. Whenever a service is requested, eventually there is a response for that request from some provider.");
		}
	};

	before(int service) : response(service) {
		// System.out.println("response(" + service + ")");
		Verdict verdict = monitor.step(RESPONSE, service);
		if (verdict == Verdict.FAILURE) {
			System.err
					.println("Violation in JUnitRV (MMT) 2. [service="
							+ service
							+ "]. Whenever a service is requested, eventually there is a response for that request from some provider.");
		}
	};

}
