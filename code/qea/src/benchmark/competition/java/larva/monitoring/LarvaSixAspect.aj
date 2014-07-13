package benchmark.competition.java.larva.monitoring;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.Property;
import properties.competition.Larva;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import benchmark.competition.java.larva.transactionsystem.UserInfo;

public aspect LarvaSixAspect {

	private final Monitor monitor;

	private final int TRANSFER = 1;
	private final int GREY_LIST = 2;
	private final int WHITE_LIST = 3;

	public LarvaSixAspect() {
		QEA qea = new Larva().make(Property.LARVA_SIX);
		monitor = MonitorFactory.create(qea);
	}

	pointcut transfer(UserInfo user) :
		call(void UserInfo.depositTo(String, double)) && target(user);

	pointcut greyList(UserInfo user) : call(void UserInfo.greylist())
		&& target(user);

	pointcut whiteList(UserInfo user) : call(void UserInfo.whitelist())
		&& target(user);

	before(UserInfo user) : transfer(user) {
		Verdict verdict = monitor.step(TRANSFER, user.getId());
		if (verdict == Verdict.FAILURE) {
			System.err
					.println("Violation in Larva 6. [userId="
							+ user.getId()
							+ "]. Once greylisted, a user must perform at least three incoming transfers before being whitelisted.");
		}
	};

	before(UserInfo user) : greyList(user) {
		Verdict verdict = monitor.step(GREY_LIST, user.getId());
		if (verdict == Verdict.FAILURE) {
			System.err
					.println("Violation in Larva 6. [userId="
							+ user.getId()
							+ "]. Once greylisted, a user must perform at least three incoming transfers before being whitelisted.");
		}
	};

	before(UserInfo user) : whiteList(user) {
		Verdict verdict = monitor.step(WHITE_LIST, user.getId());
		if (verdict == Verdict.FAILURE) {
			System.err
					.println("Violation in Larva 6. [userId="
							+ user.getId()
							+ "]. Once greylisted, a user must perform at least three incoming transfers before being whitelisted.");
		}
	};
}
