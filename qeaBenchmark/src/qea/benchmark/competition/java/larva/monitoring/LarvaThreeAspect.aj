package qea.benchmark.competition.java.larva.monitoring;

import qea.properties.Property;
import qea.properties.competition.Larva;
import qea.structure.impl.other.Verdict;
import qea.transactionsystem.UserAccount;
import qea.benchmark.competition.java.QEAMonitoringAspect;

public aspect LarvaThreeAspect extends QEAMonitoringAspect {

	private final int TRANSACTION = 1;

	public LarvaThreeAspect() {
		super(new Larva().make(Property.LARVA_THREE));
		validationMsg = "Property Larva 3 satisfied";
		violationMsg = "Property Larva 3 violated. No account may end up with a negative balance after being accessed";
	}

	pointcut transaction(UserAccount account) :
		(call(void UserAccount.withdraw(double)) ||
		call(void UserAccount.deposit(double))) && target(account);

	after(UserAccount account) : transaction(account) {
		Verdict verdict = monitor.step(TRANSACTION, account.getBalance());
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [accountNumber="
					+ account.getAccountNumber() + ", balance="
					+ account.getBalance() + "]");
			printTimeAndExit();
		}
	};
}
