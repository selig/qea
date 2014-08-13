package qea.test.functionality;

import static org.junit.Assert.assertEquals;
import static qea.structure.impl.other.Verdict.WEAK_SUCCESS;
import qea.monitoring.impl.GarbageMode;
import qea.monitoring.impl.MonitorFactory;
import qea.monitoring.intf.Monitor;

import org.junit.Test;

import qea.properties.papers.DaCapo;
import qea.properties.rovers.RoverCaseStudy;
import qea.structure.impl.qeas.QEAType;
import qea.structure.intf.QEA;
import qea.test.TestSettings;
import qea.creation.QEABuilder;

public class OversafeGarbageTest {

	int GRANT = 1;
	int CANCEL = 2;
	int ITERATOR = 1;
	int USE = 2;
	int UPDATE = 3;

	@Test
	public void test_one() {

		QEA qea = RoverCaseStudy.makeGrantCancelSingle();
		qea = QEABuilder.deconstruct(qea).make(QEAType.QVARN_FVAR_DET_QEA);

		Monitor monitor = MonitorFactory.create(GarbageMode.UNSAFE_LAZY, qea);

		for (int rep = 0; rep < 1000000; rep++) {
			Object task = new Object();
			Object res = new Object();
			assertEquals(WEAK_SUCCESS, monitor.step(GRANT, task, res));
			assertEquals(WEAK_SUCCESS, monitor.step(CANCEL, task, res));
			if (rep % 999 == 0) {
				System.gc();
			}

		}
	}

	@Test
	public void test_two() {

		QEA qea = DaCapo.makeUnsafeIter();
		// qea = QEABuilder.deconstruct(qea).make(QEAType.QVARN_FVAR_DET_QEA);

		Monitor monitor = MonitorFactory.create(GarbageMode.UNSAFE_LAZY, qea);

		for (int rep = 0; rep < 1000000; rep++) {
			Object c = TestSettings.object("C");

			for (int irep = 0; irep < 1000; irep++) {
				Object i = TestSettings.object("I");

				assertEquals(WEAK_SUCCESS, monitor.step(ITERATOR, c, i));
				assertEquals(WEAK_SUCCESS, monitor.step(USE, i));
				assertEquals(WEAK_SUCCESS, monitor.step(UPDATE, c));
			}
			if (rep % 99 == 0) {
				System.gc();
			}

		}
	}

}
