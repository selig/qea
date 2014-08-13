package qea.benchmark.competition.java.larva.monitoring;

import qea.properties.Property;
import qea.properties.competition.Larva;
import qea.structure.impl.other.Verdict;
import qea.transactionsystem.UserSession;
import qea.benchmark.competition.java.QEAMonitoringAspect;

public aspect LarvaTenAspect extends QEAMonitoringAspect {

	private final int OPEN_SESSION = 1;
	private final int CLOSE_SESSION = 2;
	private final int LOG = 3;

	public LarvaTenAspect() {
		super(new Larva().make(Property.LARVA_TEN));
		validationMsg = "Property Larva 10 satisfied";
		violationMsg = "Property Larva 10 violated. Logging can only be made to an active session";
	}

	pointcut openSession(UserSession session) :
		call(void UserSession.openSession()) && target(session);

	pointcut closeSession(UserSession session) :
		call(void UserSession.closeSession()) && target(session);

	pointcut log(UserSession session) :
		call(void UserSession.log(String)) && target(session);

	before(UserSession session) : openSession(session) {
		Verdict verdict = monitor.step(OPEN_SESSION, session.getOwner());
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + "[userId=" + session.getOwner()
					+ ", sid=" + session.getId() + "]");
			printTimeAndExit();
		}
	};

	before(UserSession session) : closeSession(session) {
		Verdict verdict = monitor.step(CLOSE_SESSION, session.getOwner());
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [userId=" + session.getOwner()
					+ ", sid=" + session.getId() + "]");
			printTimeAndExit();
		}
	};

	before(UserSession session) : log(session) {
		Verdict verdict = monitor.step(LOG, session.getOwner());
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [userId=" + session.getOwner()
					+ ", sid=" + session.getId() + "]");
			printTimeAndExit();
		}
	};
}
