package qea.test.properties;

import static org.junit.Assert.assertEquals;
import static qea.structure.impl.other.Verdict.FAILURE;
import static qea.structure.impl.other.Verdict.SUCCESS;
import static qea.structure.impl.other.Verdict.WEAK_FAILURE;
import static qea.structure.impl.other.Verdict.WEAK_SUCCESS;
import qea.monitoring.intf.Monitor;

import org.junit.Before;
import org.junit.Test;

import qea.properties.papers.UseFilePropositionalQEA;
import qea.structure.intf.QEA;
import qea.test.TestSettings;

public class UseFilePropositionalTest {

	private Monitor monitor;
	private QEA qea = new UseFilePropositionalQEA();

	static final int OPEN = 1;
	static final int USE = 2;
	static final int CLOSE = 3;

	@Before
	public void setup() {
		monitor = TestSettings.create(qea);
	}

	@Test
	public void test_one() {
		// Test correct propositional behaviour with a single file

		assertEquals(monitor.step(OPEN), WEAK_FAILURE);
		assertEquals(monitor.step(USE), WEAK_FAILURE);
		assertEquals(monitor.step(CLOSE), WEAK_SUCCESS);
		assertEquals(monitor.end(), SUCCESS);
	}

	@Test
	public void test_two() {
		// Previous behavoiur with two files should now be incorrect
		// if considered propositionally

		assertEquals(monitor.step(OPEN), WEAK_FAILURE);
		assertEquals(monitor.step(OPEN), FAILURE); // failure happens here
													// - two opens
		assertEquals(monitor.step(USE), FAILURE);
		assertEquals(monitor.step(CLOSE), FAILURE);
		assertEquals(monitor.step(USE), FAILURE);
		assertEquals(monitor.step(OPEN), FAILURE);
		assertEquals(monitor.step(USE), FAILURE);
		assertEquals(monitor.step(CLOSE), FAILURE);
		assertEquals(monitor.step(CLOSE), FAILURE);
		assertEquals(monitor.step(OPEN), FAILURE);
		assertEquals(monitor.step(USE), FAILURE);
		assertEquals(monitor.step(CLOSE), FAILURE);
		assertEquals(monitor.end(), FAILURE);
	}

	@Test
	public void test_three() {
		// Test incorrect behaviour
		// use before opening

		assertEquals(monitor.step(USE), FAILURE);
		assertEquals(monitor.end(), FAILURE);
	}

	@Test
	public void test_four() {
		// Test incorrect behaviour
		// not closing
		Object f = new Object();

		assertEquals(monitor.step(OPEN), WEAK_FAILURE);
		assertEquals(monitor.step(USE), WEAK_FAILURE);
		assertEquals(monitor.end(), FAILURE);
	}

	@Test
	public void test_five() {
		// Test incorrect behaviour
		// close twice
		Object f = new Object();

		assertEquals(monitor.step(OPEN), WEAK_FAILURE);
		assertEquals(monitor.step(USE), WEAK_FAILURE);
		assertEquals(monitor.step(CLOSE), WEAK_SUCCESS);
		assertEquals(monitor.step(CLOSE), FAILURE);
		assertEquals(monitor.end(), FAILURE);
	}

}
