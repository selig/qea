package qea.benchmark.competition.java.larva.monitoring;

import qea.properties.Property;
import qea.properties.competition.Larva;
import qea.structure.impl.other.Verdict;
import qea.transactionsystem.UserInfo;
import qea.benchmark.competition.java.QEAMonitoringAspect;

public aspect LarvaOneAspect extends QEAMonitoringAspect {

	private final int MAKE_GOLD_USER = 1;

	public LarvaOneAspect() {
		super(new Larva().make(Property.LARVA_ONE));
		validationMsg = "Property Larva 1 satisfied";
		violationMsg = "Property Larva 1 violated. Only users based in Argentina can be Gold users";
	}

	pointcut makeGoldUser(UserInfo user) : call(void UserInfo.makeGoldUser())
		&& target(user);

	before(UserInfo user) : makeGoldUser(user) {
		Verdict verdict = monitor.step(MAKE_GOLD_USER, user.getCountry());
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [userId=" + user.getId()
					+ ", country=" + user.getCountry() + "]");
			printTimeAndExit();
		}
	};
}
