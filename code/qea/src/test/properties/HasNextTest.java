package test.properties;

import static org.junit.Assert.assertEquals;
import static structure.impl.other.Verdict.FAILURE;
import static structure.impl.other.Verdict.SUCCESS;
import static structure.impl.other.Verdict.WEAK_FAILURE;
import static structure.impl.other.Verdict.WEAK_SUCCESS;
import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;

import org.junit.Before;
import org.junit.Test;

import properties.papers.HasNextQEA;
import structure.intf.QEA;

public class HasNextTest {

	private Monitor monitor;
	private QEA qea = new HasNextQEA();

	static final int NEXT = 1;
	static final int HASNEXT_TRUE = 2;
	static final int HASNEXT_FALSE = 3;

	@Before
	public void setup() {
		monitor = MonitorFactory.create(qea);
	}

	@Test
	public void test_one() {
		
		// Test correct behaviour with a single iterator
		Object i = new Object();

		assertEquals(monitor.step(HASNEXT_TRUE, i), WEAK_SUCCESS);
		assertEquals(monitor.step(NEXT, i), WEAK_SUCCESS);
		assertEquals(monitor.step(HASNEXT_FALSE, i), WEAK_SUCCESS);
		assertEquals(monitor.end(), SUCCESS);
	}

	@Test
	public void test_two() {
		// Test correct behaviour with two iterators
		Object i1 = new Object();
		Object i2 = new Object();

		assertEquals(monitor.step(HASNEXT_TRUE, i1), WEAK_SUCCESS);
		assertEquals(monitor.step(NEXT, i1), WEAK_SUCCESS);
		assertEquals(monitor.step(HASNEXT_TRUE, i2), WEAK_SUCCESS);
		assertEquals(monitor.step(NEXT, i2), WEAK_SUCCESS);
		assertEquals(monitor.step(HASNEXT_FALSE, i1), WEAK_SUCCESS);
		assertEquals(monitor.step(HASNEXT_FALSE, i2), WEAK_SUCCESS);
		assertEquals(monitor.end(), SUCCESS);
	}

	@Test
	public void test_three() {
		// Test incorrect behaviour with a single iterator
		// two next calls
		Object i = new Object();

		assertEquals(monitor.step(HASNEXT_TRUE, i), WEAK_SUCCESS);
		assertEquals(monitor.step(NEXT, i), WEAK_SUCCESS);
		assertEquals(monitor.step(NEXT, i), FAILURE);
		assertEquals(monitor.step(HASNEXT_FALSE, i), FAILURE);
		assertEquals(monitor.end(), FAILURE);
	}

	@Test
	public void test_four() {
		// Test incorrect behaviour with a single iterator
		// call after hasnext false
		Object i = new Object();

		assertEquals(monitor.step(HASNEXT_TRUE, i), WEAK_SUCCESS);
		assertEquals(monitor.step(NEXT, i), WEAK_SUCCESS);
		assertEquals(monitor.step(HASNEXT_FALSE, i), WEAK_SUCCESS);
		assertEquals(monitor.step(HASNEXT_TRUE, i), FAILURE);
		assertEquals(monitor.end(), FAILURE);
	}

	@Test
	public void test_five() {
		// Test incorrect behaviour with two iterators
		Object i1 = new Object();
		Object i2 = new Object();

		assertEquals(monitor.step(HASNEXT_TRUE, i1), WEAK_SUCCESS);
		assertEquals(monitor.step(NEXT, i1), WEAK_SUCCESS);
		assertEquals(monitor.step(HASNEXT_TRUE, i2), WEAK_SUCCESS);
		assertEquals(monitor.step(NEXT, i2), WEAK_SUCCESS);
		assertEquals(monitor.step(NEXT, i2), FAILURE);
		assertEquals(monitor.step(HASNEXT_FALSE, i1), FAILURE);
		assertEquals(monitor.step(HASNEXT_FALSE, i2), FAILURE);
		assertEquals(monitor.end(), FAILURE);
	}

}
