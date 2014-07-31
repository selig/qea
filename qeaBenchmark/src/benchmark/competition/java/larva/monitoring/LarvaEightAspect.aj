package benchmark.competition.java.larva.monitoring;

import monitoring.intf.QEAMonitoringAspect;
import properties.Property;
import properties.competition.Larva;
import structure.impl.other.Verdict;
import benchmark.competition.java.larva.transactionsystem.Interface;

public aspect LarvaEightAspect extends QEAMonitoringAspect {

	private final int TRANSFER = 1;
	private final int RECONCILE = 2;

	public LarvaEightAspect() {
		super(new Larva().make(Property.LARVA_EIGHT));
		validationMsg = "Property Larva 8 satisfied";
		violationMsg = "Property Larva 8 violated. The administrator must reconcile accounts every 1000 attempted external money transfers or an aggregate total of one million dollars in attempted external transfers";
	}

	pointcut transfer(double amount) :
		(call(void Interface.USER_depositFromExternal(Integer, Integer, String,
				double)) && args(*, *, *, amount))
		|| (call(boolean Interface.USER_payToExternal(Integer, Integer, String,
				double)) && args(*, *, *, amount));

	pointcut reconcile() : call(void Interface.ADMIN_reconcile());

	before(double amount) : transfer(amount) {
		Verdict verdict = monitor.step(TRANSFER, amount);
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [amount=" + amount + "]");
			printTimeAndExit();
		}
	};

	before() : reconcile() {
		Verdict verdict = monitor.step(RECONCILE);
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg);
			printTimeAndExit();
		}
	};
}
