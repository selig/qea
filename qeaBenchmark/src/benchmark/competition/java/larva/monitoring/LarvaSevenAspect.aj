package benchmark.competition.java.larva.monitoring;

import monitoring.intf.QEAMonitoringAspect;
import properties.Property;
import properties.competition.Larva;
import structure.impl.other.Verdict;
import benchmark.competition.java.larva.transactionsystem.UserInfo;

public aspect LarvaSevenAspect extends QEAMonitoringAspect {

	private final int NEW_ACCOUNT = 1;

	public LarvaSevenAspect() {
		super(new Larva().make(Property.LARVA_SEVEN));
		validationMsg = "Property Larva 7 satisfied";
		violationMsg = "Property Larva 7 violated. No user may not request more than 10 new accounts in a single session";
	}

	pointcut newAccount(UserInfo user, Integer sid) :
		call(String UserInfo.createAccount(Integer)) && target(user) && args(sid);

	before(UserInfo user, Integer sid) : newAccount(user, sid) {
		Verdict verdict = monitor.step(NEW_ACCOUNT, user, sid);
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [sid=" + sid + "]");
			printTimeAndExit();
		}
	};
}
