package benchmark.competition.java.larva.monitoring;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.Property;
import properties.competition.Larva;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import benchmark.competition.java.larva.transactionsystem.Interface;

public aspect LarvaEightAspect {

	private final Monitor monitor;

	private final int TRANSFER = 1;
	private final int RECONCILE = 2;

	public LarvaEightAspect() {
		QEA qea = new Larva().make(Property.LARVA_EIGHT);
		monitor = MonitorFactory.create(qea);
	}

	pointcut transfer(double amount) :
		(call(void Interface.USER_depositFromExternal(Integer, Integer, String,
				double)) && args(*, *, *, amount))
		|| (call(boolean Interface.USER_payToExternal(Integer, Integer, String,
				double)) && args(*, *, *, amount));

	pointcut reconcile() : call(void Interface.ADMIN_reconcile());

	before(double amount) : transfer(amount) {
//		System.out.println(">> transfer(" + amount + ")");
		Verdict verdict = monitor.step(TRANSFER, amount);
		if (verdict == Verdict.FAILURE || verdict == Verdict.WEAK_FAILURE) {
			System.err
					.println("Violation in Larva 8. [amount="
							+ amount
							+ "]. The administrator must reconcile accounts every 1000 attempted external money transfers or an aggregate total of one million dollars in attempted external transfers");
		}
	};

	before() : reconcile() {
//		System.out.println(">> reconcile()");
		Verdict verdict = monitor.step(RECONCILE);
		if (verdict == Verdict.FAILURE || verdict == Verdict.WEAK_FAILURE) {
			System.err
					.println("Violation in Larva 8. The administrator must reconcile accounts every 1000 attempted external money transfers or an aggregate total of one million dollars in attempted external transfers");
		}
	};
}
