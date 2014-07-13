package benchmark.competition.java.larva.monitoring;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.Property;
import properties.competition.Larva;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import benchmark.competition.java.larva.transactionsystem.Interface;

public aspect LarvaFourAspect {

	private final Monitor monitor;

	private final int APPROVE = 1;

	public LarvaFourAspect() {
		QEA qea = new Larva().make(Property.LARVA_FOUR);
		monitor = MonitorFactory.create(qea);
	}

	pointcut approveAccount(String accountId) :
		call(void Interface.ADMIN_approveOpenAccount(Integer, String))
		&& args(*, accountId);

	before(String accountId) : approveAccount(accountId) {
		Verdict verdict = monitor.step(APPROVE, accountId);
		if (verdict == Verdict.FAILURE) {
			System.err
					.println("Violation in Larva 4. [accountNumber="
							+ accountId
							+ "]. An account approved by the administrator may not have the same account number as any other already existing account in the system.");
		}
	};
}
