package qea.benchmark.competition.java.larva.monitoring;

import qea.properties.Property;
import qea.properties.competition.Larva;
import qea.structure.impl.other.Verdict;
import qea.transactionsystem.TransactionSystem;
import qea.transactionsystem.UserInfo;
import qea.benchmark.competition.java.QEAMonitoringAspect;

public aspect LarvaTwoAspect extends QEAMonitoringAspect {

	private final int INITIALISE = 1;
	private final int USER_LOGIN = 2;

	public LarvaTwoAspect() {
		super(new Larva().make(Property.LARVA_TWO));
		validationMsg = "Property Larva 2 satisfied";
		violationMsg = "Property Larva 2 violated. The transaction system must be initialised before any user logs in";
	}

	pointcut initialise() : call(void TransactionSystem.initialise());

	pointcut openSession() : call(Integer UserInfo.openSession());

	before() : initialise() {
		Verdict verdict = monitor.step(INITIALISE);
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg);
			printTimeAndExit();
		}
	};

	before() : openSession() {
		Verdict verdict = monitor.step(USER_LOGIN);
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg);
			printTimeAndExit();
		}
	};
}
