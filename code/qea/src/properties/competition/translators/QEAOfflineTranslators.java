package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import monitoring.impl.translators.TranslatorMaker;
import properties.Property;
import structure.impl.other.Verdict;

public class QEAOfflineTranslators implements TranslatorMaker {

	@Override
	public OfflineTranslator make(Property property) {
		switch (property) {
		case QEA_OFFLINE_ONE:
			return makeOne();
		case QEA_OFFLINE_TWO:
			return makeTwo();
		case QEA_OFFLINE_THREE:
			return makeThree();
		case QEA_OFFLINE_FOUR:
			return makeFour();
		case QEA_OFFLINE_FIVE:
			return makeFive();
		}
		return null;
	}

	public OfflineTranslator makeOne() {
		return new OfflineTranslator() {

			private static final int PING = 1;
			private static final int ACK = 2;

			private static final String PING_STR = "ping";
			private static final String ACK_STR = "ack";

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {
				switch (eventName) {
				case PING_STR:
					return monitor.step(PING, new Object[] { paramValues[0],
							paramValues[1] });
				case ACK_STR:
					return monitor.step(ACK, new Object[] { paramValues[0],
							paramValues[1] });
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

			private static final int GRANT = 1;
			private static final int CANCEL = 2;

			private static final String GRANT_STR = "grant";
			private static final String CANCEL_STR = "cancel";

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {
				switch (eventName) {
				case GRANT_STR:
					return monitor.step(GRANT, new Object[] { paramValues[0],
							paramValues[1] });
				case CANCEL_STR:
					return monitor.step(CANCEL, new Object[] { paramValues[0],
							paramValues[1] });
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

			private static final int COMMAND = 1;
			private static final int SUCCEED = 2;

			private static final String COMMAND_STR = "command";
			private static final String SUCCEED_STR = "succeed";

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {
				switch (eventName) {
				case COMMAND_STR:
					return monitor.step(COMMAND,
							new Object[] { paramValues[0] });
				case SUCCEED_STR:
					return monitor.step(SUCCEED,
							new Object[] { paramValues[0] });
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

			private static final int REQUEST = 1;
			private static final int GRANT = 2;
			private static final int DENY = 3;
			private static final int RESCIND = 4;
			private static final int CANCEL = 5;

			private static final String REQUEST_STR = "request";
			private static final String GRANT_STR = "grant";
			private static final String DENY_STR = "deny";
			private static final String RESCIND_STR = "rescind";
			private static final String CANCEL_STR = "cancel";

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {
				switch (eventName) {
				case REQUEST_STR:
					return monitor.step(REQUEST,
							new Object[] { paramValues[0] });
				case GRANT_STR:
					return monitor.step(GRANT, new Object[] { paramValues[0] });
				case DENY_STR:
					return monitor.step(DENY, new Object[] { paramValues[0] });
				case RESCIND_STR:
					return monitor.step(RESCIND,
							new Object[] { paramValues[0] });
				case CANCEL_STR:
					return monitor
							.step(CANCEL, new Object[] { paramValues[0] });
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

			private static final int CONFLICT = 1;
			private static final int GRANT = 2;
			private static final int CANCEL = 3;

			private static final String CONFLICT_STR = "conflict";
			private static final String GRANT_STR = "grant";
			private static final String CANCEL_STR = "cancel";

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {
				switch (eventName) {
				case CONFLICT_STR:
					return monitor.step(CONFLICT, new Object[] {
							paramValues[0], paramValues[1] });
				case GRANT_STR:
					return monitor.step(GRANT, new Object[] { paramValues[0] });

				case CANCEL_STR:
					return monitor
							.step(CANCEL, new Object[] { paramValues[0] });
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
