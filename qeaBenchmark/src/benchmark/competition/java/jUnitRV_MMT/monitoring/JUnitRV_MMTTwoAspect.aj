package benchmark.competition.java.jUnitRV_MMT.monitoring;

import properties.Property;
import properties.competition.JavaRV_mmt;
import rvcomp.ExampleRequestResponse;
import structure.impl.other.Verdict;
import benchmark.competition.java.QEAMonitoringAspect;

public aspect JUnitRV_MMTTwoAspect extends QEAMonitoringAspect {

	private final int REQUEST = 1;
	private final int RESPONSE = 2;

	public JUnitRV_MMTTwoAspect() {
		super(new JavaRV_mmt().make(Property.JAVARV_MMT_TWO));
		validationMsg = "Property JUnitRV (MMT) 2 satisfied";
		violationMsg = "Property JUnitRV (MMT) 2 violated. Whenever a service is requested, eventually there is a response for that request from some provider";
	}

	pointcut request(int service) :
		call(void ExampleRequestResponse.request(int)) && args(service);

	pointcut response(int service) :
		call(void ExampleRequestResponse.respond(int, int)) && args(service, *);

	before(int service) : request(service) {
		Verdict verdict = monitor.step(REQUEST, service);
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [service=" + service + "]");
			printTimeAndExit();
		}
	};

	before(int service) : response(service) {
		Verdict verdict = monitor.step(RESPONSE, service);
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [service=" + service + "]");
			printTimeAndExit();
		}
	};
}
