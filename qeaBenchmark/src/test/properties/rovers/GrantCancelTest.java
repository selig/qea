package test.properties.rovers;

import static org.junit.Assert.assertEquals;
import static structure.impl.other.Verdict.FAILURE;
import static structure.impl.other.Verdict.WEAK_SUCCESS;
import monitoring.intf.Monitor;

import org.junit.Before;
import org.junit.Test;

import properties.rovers.RoverCaseStudy;
import structure.intf.QEA;
import test.TestSettings;

public class GrantCancelTest {

	private Monitor monitorOne;
	private Monitor monitorTwo;
	private QEA qeaOne = RoverCaseStudy.makeGrantCancelSingleSwitch();
	private QEA qeaTwo = RoverCaseStudy.makeGrantCancelSingle();

	@Before
	public void setup() {
		monitorOne = TestSettings.create(qeaOne);
		monitorTwo = TestSettings.create(qeaTwo);
	}

	private static final Object resource_one = new Object();
	private static final Object resource_two = new Object();
	private static final Object task_one = new Object();
	private static final Object task_two = new Object();
	private static final int GRANT = 1;
	private static final int CANCEL = 2;

	@Test
	public void test_one() {
		// for monitor one
		assertEquals(monitorOne.step(GRANT, resource_one, task_one),
				WEAK_SUCCESS);
		assertEquals(monitorOne.step(CANCEL, resource_one, task_one),
				WEAK_SUCCESS);
		assertEquals(monitorOne.step(GRANT, resource_two, task_one),
				WEAK_SUCCESS);
		assertEquals(monitorOne.step(CANCEL, resource_two, task_one),
				WEAK_SUCCESS);

		// for monitor two
		assertEquals(monitorTwo.step(GRANT, task_one, resource_one),
				WEAK_SUCCESS);
		assertEquals(monitorTwo.step(CANCEL, task_one, resource_one),
				WEAK_SUCCESS);
		assertEquals(monitorTwo.step(GRANT, task_one, resource_two),
				WEAK_SUCCESS);
		assertEquals(monitorTwo.step(CANCEL, task_one, resource_two),
				WEAK_SUCCESS);

	}

	@Test
	public void test_two() {
		// for monitor one
		assertEquals(monitorOne.step(GRANT, resource_one, task_one),
				WEAK_SUCCESS);
		assertEquals(monitorOne.step(GRANT, resource_one, task_two), FAILURE);

		// for monitor two
		assertEquals(monitorTwo.step(GRANT, task_one, resource_one),
				WEAK_SUCCESS);
		assertEquals(monitorTwo.step(GRANT, task_two, resource_one), FAILURE);
	}

	@Test
	public void test_three() {
		// for monitor one
		assertEquals(monitorOne.step(GRANT, resource_one, task_one),
				WEAK_SUCCESS);
		assertEquals(monitorOne.step(CANCEL, resource_two, task_two), FAILURE);

		// for monitor two
		assertEquals(monitorTwo.step(GRANT, task_one, resource_one),
				WEAK_SUCCESS);
		assertEquals(monitorTwo.step(CANCEL, task_two, resource_two), FAILURE);
	}

	@Test
	public void test_four() {
		// for monitor one
		assertEquals(monitorOne.step(GRANT, resource_one, task_one),
				WEAK_SUCCESS);
		assertEquals(monitorOne.step(GRANT, resource_two, task_one),
				WEAK_SUCCESS);
		assertEquals(monitorOne.step(CANCEL, resource_two, task_one),
				WEAK_SUCCESS);
		assertEquals(monitorOne.step(CANCEL, resource_one, task_one),
				WEAK_SUCCESS);
		assertEquals(monitorOne.step(GRANT, resource_two, task_one),
				WEAK_SUCCESS);
		assertEquals(monitorOne.step(CANCEL, resource_two, task_one),
				WEAK_SUCCESS);

		// for monitor two
		assertEquals(monitorTwo.step(GRANT, task_one, resource_one),
				WEAK_SUCCESS);
		assertEquals(monitorTwo.step(GRANT, task_one, resource_two),
				WEAK_SUCCESS);
		assertEquals(monitorTwo.step(CANCEL, task_one, resource_two),
				WEAK_SUCCESS);
		assertEquals(monitorTwo.step(CANCEL, task_one, resource_one),
				WEAK_SUCCESS);
		assertEquals(monitorTwo.step(GRANT, task_one, resource_two),
				WEAK_SUCCESS);
		assertEquals(monitorTwo.step(CANCEL, task_one, resource_two),
				WEAK_SUCCESS);

	}

}
