package qea.test;

import qea.monitoring.impl.MonitorFactory;
import qea.monitoring.intf.Monitor;
import qea.structure.impl.qeas.QEAType;
import qea.structure.intf.QEA;
import qea.creation.QEABuilder;

public class TestSettings {

	public static enum TestMode {
		standard, general
	}

	public static TestMode mode = TestMode.standard;

	public static Monitor create(QEA qea) {

		switch (mode) {
		case standard:
			// Standard operation
			return MonitorFactory.create(qea);

		case general:
			// To test QVarN versions
			try {
				return MonitorFactory.create(QEABuilder.change(qea,
						QEAType.QVARN_NOFVAR_DET_QEA));
			} catch (Exception e1) {
				try {
					return MonitorFactory.create(QEABuilder.change(qea,
							QEAType.QVARN_FVAR_DET_QEA));
				} catch (Exception e2) {
					return MonitorFactory.create(QEABuilder.change(qea,
							QEAType.QVARN_FVAR_NONDET_QEA));
				}
			}
		}
		return null;
	}

	public static Object object(final String string) {
		return new Object() {
			@Override
			public String toString() {
				return string;
			}
		};
	}

}
