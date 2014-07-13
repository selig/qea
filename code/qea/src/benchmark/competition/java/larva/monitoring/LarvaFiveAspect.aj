package benchmark.competition.java.larva.monitoring;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.Property;
import properties.competition.Larva;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import benchmark.competition.java.larva.transactionsystem.Main;
import benchmark.competition.java.larva.transactionsystem.UserInfo;

public aspect LarvaFiveAspect {

	private final Monitor monitor;

	private final int WITHDRAW = 1;
	private final int DISABLE = 2;
	private final int ACTIVATE = 3;

	public LarvaFiveAspect() {
		QEA qea = new Larva().make(Property.LARVA_FIVE);
		monitor = MonitorFactory.create(qea);
	}

	pointcut withdraw(UserInfo user) :
		call(void UserInfo.withdrawFrom(String, double))
		&& target(user);

	pointcut disable(UserInfo user) : call(void UserInfo.makeDisabled())
	&& target(user);

	pointcut activate(UserInfo user) : call(void UserInfo.makeActive())
	&& target(user);

	before(UserInfo user) : withdraw(user) {
		Verdict verdict = monitor.step(WITHDRAW, user.getId());
		if (verdict == Verdict.FAILURE) {
			System.err
					.println("Violation in Larva 5. [userId="
							+ user.getId()
							+ "]. Once a user is disabled, he or she may not withdraw from an account until being made activate again.");
			System.exit(0);
		}
	};

	before(UserInfo user) : disable(user) {
		Verdict verdict = monitor.step(DISABLE, user.getId());
		if (verdict == Verdict.FAILURE) {
			System.err
					.println("Violation in Larva 5. [userId="
							+ user.getId()
							+ "]. Once a user is disabled, he or she may not withdraw from an account until being made activate again.");
			System.exit(0);
		}
	};

	before(UserInfo user) : activate(user) {
		Verdict verdict = monitor.step(ACTIVATE, user.getId());
		if (verdict == Verdict.FAILURE) {
			System.err
					.println("Violation in Larva 5. [userId="
							+ user.getId()
							+ "]. Once a user is disabled, he or she may not withdraw from an account until being made activate again.");
			System.exit(0);
		}
	};

	pointcut endOfProgram() : execution(void Main.main(String[]));

	after() : endOfProgram() {
		Verdict verdict = monitor.end();
		if (verdict == Verdict.FAILURE || verdict == Verdict.WEAK_FAILURE) {
			System.err
					.println("Violation in Larva 5. Once a user is disabled, he or she may not withdraw from an account until being made activate again.");
		} else {
			System.err.println("Property Larva 5 satisfied");
		}
	}

}
