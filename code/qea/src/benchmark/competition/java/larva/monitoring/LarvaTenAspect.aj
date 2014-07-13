package benchmark.competition.java.larva.monitoring;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.Property;
import properties.competition.Larva;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import benchmark.competition.java.larva.transactionsystem.Main;
import benchmark.competition.java.larva.transactionsystem.UserSession;

public aspect LarvaTenAspect {

	private final Monitor monitor;

	private final int OPEN_SESSION = 1;
	private final int CLOSE_SESSION = 2;
	private final int LOG = 3;

	public LarvaTenAspect() {
		QEA qea = new Larva().make(Property.LARVA_TEN);
		monitor = MonitorFactory.create(qea);
	}

	pointcut openSession(UserSession session) :
		call(void UserSession.openSession()) && target(session);

	pointcut closeSession(UserSession session) :
		call(void UserSession.closeSession()) && target(session);

	pointcut log(UserSession session) :
		call(void UserSession.log(String)) && target(session);

	before(UserSession session) : openSession(session) {
//		System.out.println(">> openSession(" + session.getOwner() + ")");
		Verdict verdict = monitor.step(OPEN_SESSION, session.getOwner());
		if (verdict == Verdict.FAILURE) {
			System.err.println("Violation in Larva 10. [userId="
					+ session.getOwner() + "] [sid=" + session.getId()
					+ "]. Logging can only be made to an active session.");
			System.exit(0);
		}
	};

	before(UserSession session) : closeSession(session) {
//		System.out.println(">> closeSession(" + session.getOwner() + ")");
		Verdict verdict = monitor.step(CLOSE_SESSION, session.getOwner());
		if (verdict == Verdict.FAILURE) {
			System.err.println("Violation in Larva 10. [userId="
					+ session.getOwner() + "] [sid=" + session.getId()
					+ "]. Logging can only be made to an active session.");
			System.exit(0);
		}
	};

	before(UserSession session) : log(session) {
//		System.out.println(">> log(" + session.getOwner() + ")");
		Verdict verdict = monitor.step(LOG, session.getOwner());
		if (verdict == Verdict.FAILURE) {
			System.err.println("Violation in Larva 10. [userId="
					+ session.getOwner() + "] [sid=" + session.getId()
					+ "]. Logging can only be made to an active session.");
			System.exit(0);
		}
	};
	
	pointcut endOfProgram() : execution(void Main.main(String[]));

	after() : endOfProgram() {
		Verdict verdict = monitor.end();
		if (verdict == Verdict.FAILURE || verdict == Verdict.WEAK_FAILURE) {
			System.err
					.println("Violation in Larva 10. Logging can only be made to an active session.");
		} else {
			System.err.println("Property Larva 10 satisfied");
		}
	}
}
