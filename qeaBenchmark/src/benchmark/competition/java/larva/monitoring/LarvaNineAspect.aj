package benchmark.competition.java.larva.monitoring;

import monitoring.intf.QEAMonitoringAspect;
import properties.Property;
import properties.competition.Larva;
import structure.impl.other.Verdict;
import benchmark.competition.java.larva.transactionsystem.UserInfo;

public aspect LarvaNineAspect extends QEAMonitoringAspect {

	private final int OPEN_SESSION = 1;
	private int CLOSE_SESSION = 2;

	public LarvaNineAspect() {
		super(new Larva().make(Property.LARVA_NINE));
		validationMsg = "Property Larva 9 satisfied";
		violationMsg = "Property Larva 9 violated. A user may not have more than 3 active sessions at once";
	}

	pointcut openSession(UserInfo user) : call(Integer UserInfo.openSession())
		&& target(user);

	pointcut closeSession(UserInfo user) :
		call(void UserInfo.closeSession(Integer)) && target(user);

	before(UserInfo user) : openSession(user) {
		Verdict verdict = monitor.step(OPEN_SESSION, user.getId());
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [userId=" + user.getId() + "]");
			printTimeAndExit();
		}
	};

	before(UserInfo user) : closeSession(user) {
		Verdict verdict = monitor.step(CLOSE_SESSION, user.getId());
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [userId=" + user.getId() + "]");
			printTimeAndExit();
		}
	};
}
