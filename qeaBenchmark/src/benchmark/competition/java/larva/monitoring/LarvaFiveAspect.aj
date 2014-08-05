package benchmark.competition.java.larva.monitoring;

import properties.Property;
import properties.competition.Larva;
import structure.impl.other.Verdict;
import transactionsystem.UserInfo;
import benchmark.competition.java.QEAMonitoringAspect;

public aspect LarvaFiveAspect extends QEAMonitoringAspect {

	private final int WITHDRAW = 1;
	private final int DISABLE = 2;
	private final int ACTIVATE = 3;

	public LarvaFiveAspect() {
		super(new Larva().make(Property.LARVA_FIVE));
		validationMsg = "Property Larva 5 satisfied";
		violationMsg = "Property Larva 5 violated. Once a user is disabled, he or she may not withdraw from an account until being made activate again";
	}

	pointcut withdraw(UserInfo user) :
		call(void UserInfo.withdrawFrom(String, double)) && target(user);

	pointcut disable(UserInfo user) : call(void UserInfo.makeDisabled())
		&& target(user);

	pointcut activate(UserInfo user) : call(void UserInfo.makeActive())
		&& target(user);

	before(UserInfo user) : withdraw(user) {
		Verdict verdict = monitor.step(WITHDRAW, user.getId());
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [userId=" + user.getId() + "]");
			printTimeAndExit();
		}
	};

	before(UserInfo user) : disable(user) {
		Verdict verdict = monitor.step(DISABLE, user.getId());
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [userId=" + user.getId() + "]");
			printTimeAndExit();
		}
	};

	before(UserInfo user) : activate(user) {
		Verdict verdict = monitor.step(ACTIVATE, user.getId());
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [userId=" + user.getId() + "]");
			printTimeAndExit();
		}
	};
}
