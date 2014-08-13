package qea.properties.competition.translators;

import qea.monitoring.impl.translators.OfflineTranslator;
import qea.properties.Property;
import qea.properties.TranslatorMaker;
import qea.structure.impl.other.Verdict;

public class SoloistTranslators implements TranslatorMaker {

	@Override
	public OfflineTranslator make(Property property) {

		switch (property) {
		case SOLOIST_ONE:
			return makeOne();
		case SOLOIST_TWO:
			return makeTwo();
		case SOLOIST_THREE:
			return makeThree();
		case SOLOIST_FOUR:
			return makeFour();
		}
		return null;
	}

	public OfflineTranslator makeOne() {
		return new OfflineTranslator() {

			private static final int WITHDRAW = 1;
			private static final int LOGOFF = 2;

			private static final String WITHDRAW_STR = "repwidraw";
			private static final String LOGOFF_STR = "recvlogoff";

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {
				switch (eventName) {
				case WITHDRAW_STR:
					return monitor.step(WITHDRAW,
							Integer.valueOf(paramValues[0]));
				case LOGOFF_STR:
					return monitor
							.step(LOGOFF, Integer.valueOf(paramValues[0]));
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

			private static final int INVCHECKACCESS_START = 1;
			private static final int INVCHECKACCESS_COMPLETE = 2;

			private static final String INVCHECKACCESS_STR = "invcheckaccess";
			private static final String EVENT_TYPE_START_STR = "start";

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {

				switch (eventName) {
				case INVCHECKACCESS_STR:
					if (paramValues[1].equals(EVENT_TYPE_START_STR)) {
						return monitor.step(INVCHECKACCESS_START,
								Integer.valueOf(paramValues[0]));
					}
					return monitor.step(INVCHECKACCESS_COMPLETE,
							Integer.valueOf(paramValues[0]));
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

			private static final int INVCHECKACCESS_COMPLETE = 1;
			private static final int REPLOGON = 2;

			private static final String INVCHECKACCESS_STR = "invcheckaccess";
			private static final String EVENT_TYPE_COMPLETE_STR = "complete";
			private static final String REPLOGON_STR = "replogon";

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {

				switch (eventName) {
				case INVCHECKACCESS_STR:
					switch (paramValues[1]) {
					case EVENT_TYPE_COMPLETE_STR:
						return monitor.step(INVCHECKACCESS_COMPLETE);
					default:
						return null;
					}

				case REPLOGON_STR:
					return monitor.step(REPLOGON);

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

			private static final int INVCHECKACCESS_START = 1;
			private static final int INVCHECKACCESS_COMPLETE = 2;

			private static final String INVCHECKACCESS_STR = "invcheckaccess";
			private static final String EVENT_TYPE_START_STR = "start";
			private static final String EVENT_TYPE_COMPLETE_STR = "complete";

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {
				switch (eventName) {
				case INVCHECKACCESS_STR:
					switch (paramValues[1]) {
					case EVENT_TYPE_START_STR:
						return monitor.step(INVCHECKACCESS_START,
								Integer.valueOf(paramValues[0]));
					case EVENT_TYPE_COMPLETE_STR:
						return monitor.step(INVCHECKACCESS_COMPLETE,
								Integer.valueOf(paramValues[0]));
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
