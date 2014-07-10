package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import monitoring.impl.translators.TranslatorMaker;
import properties.Property;
import structure.impl.other.Verdict;

public class SteprTranslators implements TranslatorMaker {

	@Override
	public OfflineTranslator make(Property property) {
		switch (property) {
		case STEPR_ONE:
			return makeOne();
		case STEPR_TWO:
			return makeTwo();
		case STEPR_THREE:
			return makeThree();
		case STEPR_FOUR:
			return makeFour();
		}
		return null;
	}

	public OfflineTranslator makeOne() {
		// TODO Auto-generated method stub
		return null;
	}

	public OfflineTranslator makeTwo() {
		return new OfflineTranslator() {

			private static final int START1 = 1;
			private static final int STOP = 2;

			private static final String START1_STR = "start1";
			private static final String STOP_STR = "stop";

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {
				switch (paramNames[0]) {
				case START1_STR:
					// TODO Should we check the value of the parameter is true?
					return monitor.step(START1, Long.valueOf(paramValues[6]));
				case STOP_STR:
					return monitor.step(STOP, Long.valueOf(paramValues[6]));
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

			private static final int STEP = 1;

			private static final String STEP_STR = "step";

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {

				switch (eventName) {
				case STEP_STR:
					return monitor.step(STEP, paramValues[1], paramValues[2]);

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

			private static final int INIT = 1;
			private static final int RUN = 2;
			private static final int FINISH = 3;
			private static final int GROUP_START = 4;
			private static final int PHASE_START = 5;
			private static final int GROUP_END = 6;

			private static final String GROUP_START_STR = "group_start";
			private static final String PHASE_START_STR = "phase_start";
			private static final String GROUP_END_STR = "group_end";

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {

				if (paramNames.length == 6) { // TODO Can we assume this? Check
												// eventName=step?

					// TODO Should we check the name of the parameter?
					if (!paramValues[2].equals("0")) { // init(p)
						return monitor.step(INIT,
								new Object[] { paramValues[2].intern() });
					}
					if (!paramValues[3].equals("0")) { // run(p)
						return monitor.step(RUN,
								new Object[] { paramValues[3].intern() });
					}
					if (!paramValues[4].equals("0")) { // finish(p)
						return monitor.step(FINISH,
								new Object[] { paramValues[4].intern() });
					}
				}

				switch (paramNames[0]) {

				// TODO Should we check the value of the parameter is true?
				case GROUP_START_STR: // group_start(ts)
					return monitor.step(GROUP_START,
							Long.valueOf(paramValues[6]));

				case GROUP_END_STR: // group_end(ts2)
					return monitor
							.step(GROUP_END, Long.valueOf(paramValues[6]));

				case PHASE_START_STR: // phase_start()
					return monitor.step(PHASE_START);

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
