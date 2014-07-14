package benchmark.competition.java.larva.monitoring;

import monitoring.intf.QEAMonitoringAspect;
import properties.Property;
import properties.competition.Larva;
import structure.impl.other.Verdict;
import benchmark.competition.java.larva.transactionsystem.Interface;

public aspect LarvaFourAspect extends QEAMonitoringAspect {

	private final int APPROVE = 1;

	public LarvaFourAspect() {
		super(new Larva().make(Property.LARVA_FOUR));
		validationMsg = "Property Larva 4 satisfied";
		violationMsg = "Property Larva 4 violated. An account approved by the administrator may not have the same account number as any other already existing account in the system";
	}

	pointcut approveAccount(String accountId) :
		call(void Interface.ADMIN_approveOpenAccount(Integer, String))
		&& args(*, accountId);

	before(String accountId) : approveAccount(accountId) {
		Verdict verdict = monitor.step(APPROVE, accountId);
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [accountNumber=" + accountId
					+ "]");
			printTimeAndExit();
		}
	};
}
