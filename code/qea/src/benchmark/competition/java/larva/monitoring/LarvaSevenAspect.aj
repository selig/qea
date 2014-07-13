package benchmark.competition.java.larva.monitoring;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.Property;
import properties.competition.Larva;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import benchmark.competition.java.larva.transactionsystem.UserInfo;

public aspect LarvaSevenAspect {

	private final Monitor monitor;

	private final int NEW_ACCOUNT = 1;

	public LarvaSevenAspect() {
		QEA qea = new Larva().make(Property.LARVA_SEVEN);
		monitor = MonitorFactory.create(qea);
	}

	pointcut newAccount(Integer sid) :
		call(String UserInfo.createAccount(Integer)) && args(sid);

	before(Integer sid) : newAccount(sid) {
//		System.out.println(">> newAccount(" + sid + ")");
		Verdict verdict = monitor.step(NEW_ACCOUNT, sid);
		if (verdict == Verdict.FAILURE) {
			System.err
					.println("Violation in Larva 7. [sid="
							+ sid
							+ "]. No user may not request more than 10 new accounts in a single session.");
		}
	};
}
