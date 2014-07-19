package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import monitoring.impl.translators.TranslatorMaker;
import properties.Property;
import structure.impl.other.Verdict;

public class MonPolyTranslators implements TranslatorMaker {

	@Override
	public OfflineTranslator make(Property property) {
		switch (property) {
		case MONPOLY_ONE:
			return makeOne();
		case MONPOLY_TWO:
			return makeTwo();
		case MONPOLY_THREE:
			return makeThree();
		case MONPOLY_FOUR:
			return makeFour();
		case MONPOLY_FIVE:
			return makeFive();
		}
		return null;
	}

	public OfflineTranslator makeOne() {
		return new OfflineTranslator() {

			private static final int TRANS = 1;
			private static final int REPORT = 2;

			private static final String TRANS_STR = "trans";
			private static final String REPORT_STR = "report";

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {
				switch (eventName) {
				case TRANS_STR:
					return monitor.step(
							TRANS,
							new Object[] { paramValues[3],
									Integer.valueOf(paramValues[4]),
									Integer.valueOf(paramValues[1]) });
				case REPORT_STR:
					return monitor.step(REPORT, new Object[] { paramValues[2],
							Integer.valueOf(paramValues[1]) });
				default:
					return null;
				}
			}

			@Override
			public Verdict translateAndStep(String eventName) {
				// No event without parameters is relevant for this property
				return null;
			}
		};
	}

	public OfflineTranslator makeTwo() {
		return new OfflineTranslator() {

			private static final int AUTH = 1;
			private static final int TRANS = 2;

			private static final String AUTH_STR = "auth";
			private static final String TRANS_STR = "trans";

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {
				switch (eventName) {
				case AUTH_STR:
					return monitor.step(AUTH, new Object[] { paramValues[3],
							Integer.valueOf(paramValues[1]) });
				case TRANS_STR:
					return monitor.step(
							TRANS,
							new Object[] { paramValues[3],
									Integer.valueOf(paramValues[4]),
									Integer.valueOf(paramValues[1]) });
				default:
					return null;
				}
			}

			@Override
			public Verdict translateAndStep(String eventName) {
				// No event without parameters is relevant for this property
				return null;
			}
		};
	}

	public OfflineTranslator makeThree() {
		return new OfflineTranslator() {

			private static final int ACC_S = 1;
			private static final int ACC_F = 2;
			private static final int MGR_S = 3;
			private static final int MGR_F = 4;
			private static final int APPROVE = 5;
			private static final int PUBLISH = 6;

			private static final String ACC_S_STR = "acc_S";
			private static final String ACC_F_STR = "acc_F";
			private static final String MGR_S_STR = "mgr_S";
			private static final String MGR_F_STR = "mgr_F";
			private static final String APPROVE_STR = "approve";
			private static final String PUBLISH_STR = "publish";

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {
				switch (eventName) {

				case ACC_S_STR:
					return monitor.step(ACC_S, new Object[] { paramValues[2] });

				case ACC_F_STR:
					return monitor.step(ACC_F, new Object[] { paramValues[2] });

				case MGR_S_STR:
					return monitor.step(MGR_S, new Object[] { paramValues[2],
							paramValues[3] });

				case MGR_F_STR:
					return monitor.step(MGR_F, new Object[] { paramValues[2],
							paramValues[3] });

				case APPROVE_STR:
					return monitor.step(APPROVE, new Object[] { paramValues[2],
							paramValues[3], Integer.valueOf(paramValues[1]) });

				case PUBLISH_STR:
					return monitor.step(PUBLISH, new Object[] { paramValues[2],
							paramValues[3], Integer.valueOf(paramValues[1]) });

				default:
					return null;
				}
			}

			@Override
			public Verdict translateAndStep(String eventName) {
				// No event without parameters is relevant for this property
				return null;
			}
		};
	}

	public OfflineTranslator makeFour() {
		return new OfflineTranslator() {

			private static final int WITHDRAW = 1;

			private static final String WITHDRAW_STR = "withdraw";

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {
				switch (eventName) {

				case WITHDRAW_STR:
					return monitor.step(WITHDRAW, new Object[] {
							paramValues[2], Integer.valueOf(paramValues[3]),
							Integer.valueOf(paramValues[1]) });
				default:
					return null;
				}
			}

			@Override
			public Verdict translateAndStep(String eventName) {
				// No event without parameters is relevant for this property
				return null;
			}
		};
	}

	public OfflineTranslator makeFive() {
		return new OfflineTranslator() {

			private static final int INSERT_DB2 = 1;
			private static final int INSERT_DB3 = 2;

			private static final String INSERT_STR = "insert";
			private static final String DB_DB2_STR = "db2";
			private static final String DB_DB3_STR = "db3";

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {
				switch (eventName) {

				case INSERT_STR:

					switch (paramValues[3]) {
					case DB_DB2_STR:
						return monitor.step(
								INSERT_DB2,
								new Object[] { paramValues[5],
										Integer.valueOf(paramValues[1]) });

					case DB_DB3_STR:
						return monitor.step(
								INSERT_DB3,
								new Object[] { paramValues[5],
										Integer.valueOf(paramValues[1]) });
					default:
						return null;
					}
				default:
					return null;
				}
			}

			@Override
			public Verdict translateAndStep(String eventName) {
				// No event without parameters is relevant for this property
				return null;
			}
		};
	}
}
