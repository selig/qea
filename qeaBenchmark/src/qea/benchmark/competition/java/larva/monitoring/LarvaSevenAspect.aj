package qea.benchmark.competition.java.larva.monitoring;

import qea.properties.Property;
import qea.properties.competition.Larva;
import qea.structure.impl.other.Verdict;
import qea.transactionsystem.UserInfo;
import qea.benchmark.competition.java.QEAMonitoringAspect;

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
