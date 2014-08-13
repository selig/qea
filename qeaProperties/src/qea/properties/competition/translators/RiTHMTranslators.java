package qea.properties.competition.translators;

import qea.monitoring.impl.translators.OfflineTranslator;
import qea.properties.Property;
import qea.properties.TranslatorMaker;
import qea.structure.impl.other.Verdict;

public class RiTHMTranslators implements TranslatorMaker {

	@Override
	public OfflineTranslator make(Property property) {
		switch (property) {
		case RITHM_ONE:
			return makeOne();
		case RITHM_TWO:
			return makeTwo();
		case RITHM_THREE:
			return makeThree();
		case RITHM_FOUR:
			return makeFour();
		case RITHM_FIVE:
			return makeFive();
		}
		return null;
	}

	public OfflineTranslator makeOne() {
		return new OfflineTranslator() {

			private static final int EVENT = 1;

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {
				// The trace only contains events named "event"
				return monitor.step(EVENT,
						new Object[] { Integer.valueOf(paramValues[1]) });
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

			private static final int EVENT = 1;

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {
				// The trace only contains events named "event"
				return monitor.step(EVENT, paramValues[0],
						Integer.valueOf(paramValues[1]));
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

			private static final int EVENT = 1;

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {
				return monitor.step(EVENT, Double.valueOf(paramValues[3]));
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

			private static final int EVENT = 1;

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {
				return monitor.step(EVENT, paramValues[0],
						Double.valueOf(paramValues[3]));
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

			private final int OPEN = 1;
			private final int CLOSE = 2;

			private static final String OPERATION_OPEN_STR = "OPEN";
			private static final String OPERATION_CLOSE_STR = "CLOSE";

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {
				switch (paramValues[2]) {
				case OPERATION_OPEN_STR:
					return monitor.step(OPEN, paramValues[0], paramValues[1]);
				case OPERATION_CLOSE_STR:
					return monitor.step(CLOSE, paramValues[0], paramValues[1]);
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
