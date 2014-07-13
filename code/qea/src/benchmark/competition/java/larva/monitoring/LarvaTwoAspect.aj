package benchmark.competition.java.larva.monitoring;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.Property;
import properties.competition.Larva;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import benchmark.competition.java.larva.transactionsystem.TransactionSystem;
import benchmark.competition.java.larva.transactionsystem.UserInfo;

public aspect LarvaTwoAspect {

	private final Monitor monitor;

	private final int INITIALISE = 1;
	private final int USER_LOGIN = 2;

	public LarvaTwoAspect() {
		QEA qea = new Larva().make(Property.LARVA_TWO);
		monitor = MonitorFactory.create(qea);
	}

	pointcut initialise() : call(void TransactionSystem.initialise());

	pointcut openSession() : call(Integer UserInfo.openSession());

	before() : initialise() {
		Verdict verdict = monitor.step(INITIALISE);
		if (verdict == Verdict.FAILURE) {
			System.err
					.println("Violation in Larva 2. The transaction system must be initialised before any user logs in.");
		}
	};

	before() : openSession() {
		Verdict verdict = monitor.step(USER_LOGIN);
		if (verdict == Verdict.FAILURE) {
			System.err
					.println("Violation in Larva 2. The transaction system must be initialised before any user logs in.");
		}
	};

}
