package benchmark.competition.java.larva.monitoring;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.Property;
import properties.competition.Larva;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import benchmark.competition.java.larva.transactionsystem.UserInfo;

public aspect LarvaOneAspect {

	private final Monitor monitor;

	private final int MAKE_GOLD_USER = 1;

	public LarvaOneAspect() {
		QEA qea = new Larva().make(Property.LARVA_ONE);
		monitor = MonitorFactory.create(qea);
	}

	pointcut makeGoldUser(UserInfo user) : call(void UserInfo.makeGoldUser())
		&& target(user);

	before(UserInfo user) : makeGoldUser(user) {
		Verdict verdict = monitor.step(MAKE_GOLD_USER, user.getCountry());
		if (verdict == Verdict.FAILURE || verdict == Verdict.WEAK_FAILURE) {
			System.err.println("Violation in Larva 1. [userId=" + user.getId()
					+ "] [country=" + user.getCountry()
					+ "]. Only users based in Argentina can be Gold users.");
		}
	};
}
