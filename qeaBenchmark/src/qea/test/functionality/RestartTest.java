package qea.test.functionality;

import static org.junit.Assert.assertEquals;
import static qea.structure.impl.other.Verdict.FAILURE;
import static qea.structure.impl.other.Verdict.SUCCESS;
import static qea.structure.impl.other.Verdict.WEAK_FAILURE;
import static qea.structure.impl.other.Verdict.WEAK_SUCCESS;
import qea.monitoring.impl.MonitorFactory;
import qea.monitoring.impl.RestartMode;
import qea.monitoring.intf.Monitor;

import org.junit.Test;

import qea.properties.rovers.RoverCaseStudy;
import qea.properties.simple.DemoQEAs;
import qea.structure.impl.qeas.QEAType;
import qea.structure.intf.QEA;
import qea.creation.QEABuilder;

public class RestartTest {

	private static final Object resource_one = new Object() {
		@Override
		public String toString() {
			return "r1";
		}
	};
	private static final Object resource_two = new Object() {
		@Override
		public String toString() {
			return "r2";
		}
	};
	private static final Object task_one = new Object() {
		@Override
		public String toString() {
			return "t1";
		}
	};
	private static final Object task_two = new Object() {
		@Override
		public String toString() {
			return "t2";
		}
	};
	private static final int GRANT = 1;
	private static final int CANCEL = 2;
	static final int e = 1;
	static final int f = 2;

	/*
	 * This tests if we restart after failure
	 */
	@Test
	public void test_one() {
		QEA qea = QEABuilder.change(RoverCaseStudy.makeGrantCancelSingle(),
				QEAType.QVARN_FVAR_NONDET_QEA);
		Monitor monitor = MonitorFactory.create(RestartMode.REMOVE, qea);

		assertEquals(monitor.step(GRANT, task_one, resource_one), WEAK_SUCCESS);
		assertEquals(monitor.step(GRANT, task_two, resource_one), FAILURE);

		assertEquals(monitor.step(GRANT, task_one, resource_one), WEAK_SUCCESS);
		assertEquals(monitor.step(GRANT, task_two, resource_one), FAILURE);

	}

	/*
	 * This tests if we restart after failure (with a repeat failure in there)
	 */
	@Test
	public void test_two() {

		QEA qea = QEABuilder.change(RoverCaseStudy.makeGrantCancelSingle(),
				QEAType.QVARN_FVAR_NONDET_QEA);
		Monitor monitor = MonitorFactory.create(RestartMode.REMOVE, qea);

		assertEquals(monitor.step(GRANT, task_one, resource_one), WEAK_SUCCESS);
		assertEquals(monitor.step(CANCEL, task_two, resource_two), FAILURE);

		assertEquals(monitor.step(GRANT, task_one, resource_one), FAILURE);
		assertEquals(monitor.step(GRANT, task_two, resource_two), WEAK_SUCCESS);

	}

	/*
	 * This tests if we restart after success
	 */
	@Test
	public void test_three() {

		QEA qea = QEABuilder.change(DemoQEAs.makePropositionalDepend(),
				QEAType.QVARN_FVAR_NONDET_QEA);
		Monitor monitor = MonitorFactory.create(RestartMode.REMOVE, qea);

		assertEquals(monitor.step(e, 5), WEAK_FAILURE);
		// System.out.println(monitor);
		assertEquals(monitor.step(f, 4), WEAK_FAILURE);
		// System.out.println(monitor);
		assertEquals(monitor.step(f, 6), SUCCESS);
		// System.out.println(monitor);
		assertEquals(monitor.step(f, 3), WEAK_FAILURE);
		// System.out.println(monitor);
		assertEquals(monitor.step(f, 7), SUCCESS);
		// System.out.println(monitor);

	}

}
