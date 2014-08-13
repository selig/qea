package qea.test.properties;

import static org.junit.Assert.assertEquals;
import static qea.structure.impl.other.Verdict.FAILURE;
import static qea.structure.impl.other.Verdict.SUCCESS;
import static qea.structure.impl.other.Verdict.WEAK_FAILURE;
import static qea.structure.impl.other.Verdict.WEAK_SUCCESS;
import qea.monitoring.intf.Monitor;

import org.junit.Before;
import org.junit.Test;

import qea.properties.papers.UseFileQEA;
import qea.structure.intf.QEA;
import qea.test.TestSettings;

public class UseFileTest {

	private Monitor monitor;
	private QEA qea = new UseFileQEA();

	static final int OPEN = 1;
	static final int USE = 2;
	static final int CLOSE = 3;

	@Before
	public void setup() {
		monitor = TestSettings.create(qea);
	}

	@Test
	public void test_one() {
		// Test correct behaviour with a single file
		Object f = new Object();

		assertEquals(monitor.step(OPEN, f), WEAK_FAILURE);
		assertEquals(monitor.step(USE, f), WEAK_FAILURE);
		assertEquals(monitor.step(CLOSE, f), WEAK_SUCCESS);
		assertEquals(monitor.end(), SUCCESS);
	}

	@Test
	public void test_two() {
		// Test correct behaviour with two files
		Object f1 = new Object();
		Object f2 = new Object();

		assertEquals(monitor.step(OPEN, f1), WEAK_FAILURE);
		assertEquals(monitor.step(OPEN, f2), WEAK_FAILURE);
		assertEquals(monitor.step(USE, f1), WEAK_FAILURE);
		assertEquals(monitor.step(CLOSE, f1), WEAK_FAILURE);
		assertEquals(monitor.step(USE, f2), WEAK_FAILURE);
		assertEquals(monitor.step(OPEN, f1), WEAK_FAILURE);
		assertEquals(monitor.step(USE, f1), WEAK_FAILURE);
		assertEquals(monitor.step(CLOSE, f1), WEAK_FAILURE);
		assertEquals(monitor.step(CLOSE, f2), WEAK_SUCCESS);
		assertEquals(monitor.step(OPEN, f2), WEAK_FAILURE);
		assertEquals(monitor.step(USE, f2), WEAK_FAILURE);
		assertEquals(monitor.step(CLOSE, f2), WEAK_SUCCESS);
		assertEquals(monitor.end(), SUCCESS);
	}

	@Test
	public void test_three() {
		// Test incorrect behaviour with a single file
		// use before opening
		Object f = new Object();

		assertEquals(monitor.step(USE, f), FAILURE);
		assertEquals(monitor.end(), FAILURE);
	}

	@Test
	public void test_four() {
		// Test incorrect behaviour with a single file
		// not closing
		Object f = new Object();

		assertEquals(monitor.step(OPEN, f), WEAK_FAILURE);
		assertEquals(monitor.step(USE, f), WEAK_FAILURE);
		assertEquals(monitor.end(), FAILURE);
	}

	@Test
	public void test_five() {
		// Test incorrect behaviour with a single file
		// close twice
		Object f = new Object();

		assertEquals(monitor.step(OPEN, f), WEAK_FAILURE);
		assertEquals(monitor.step(USE, f), WEAK_FAILURE);
		assertEquals(monitor.step(CLOSE, f), WEAK_SUCCESS);
		assertEquals(monitor.step(CLOSE, f), FAILURE);
		assertEquals(monitor.end(), FAILURE);
	}

}
