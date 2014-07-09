package test.properties;

import static org.junit.Assert.assertEquals;
import static structure.impl.other.Verdict.FAILURE;
import static structure.impl.other.Verdict.SUCCESS;
import static structure.impl.other.Verdict.WEAK_FAILURE;
import static structure.impl.other.Verdict.WEAK_SUCCESS;
import monitoring.intf.Monitor;

import org.junit.Test;

import properties.simple.DemoQEAs;
import test.TestSettings;

public class DemoPropsTest {

	/*
	 * This tests that matching uses the values for quantified variables when
	 * picking transitions
	 */
	@Test
	public void test_one() {

		int e = 1;
		int f = 2;
		Monitor monitor = TestSettings.create(DemoQEAs.makeDemoNonDetChoice());

		Object x = new Object() {
			@Override
			public String toString() {
				return "A";
			}
		};
		Object y = new Object() {
			@Override
			public String toString() {
				return "B";
			}
		};

		assertEquals(monitor.step(e, x, y), FAILURE);
		assertEquals(monitor.step(f, y), FAILURE);
		assertEquals(monitor.step(f, x), FAILURE);
	}

	@Test
	public void test_two() {

		int f = 1;
		int g = 2;
		Monitor monitor = TestSettings.create(DemoQEAs.detFreeWithOneProp());

		assertEquals(monitor.step(f), WEAK_FAILURE);
		assertEquals(monitor.step(g, 6), SUCCESS);
	}

	@Test
	public void test_three() {

		int f = 1;
		int g = 2;
		Monitor monitor = TestSettings.create(DemoQEAs.detOneFreeWithOneProp());

		assertEquals(monitor.step(f), WEAK_SUCCESS);
		assertEquals(monitor.step(g, new Object(), 6), WEAK_SUCCESS);
		assertEquals(monitor.step(g, new Object(), 4), FAILURE);
	}

	@Test
	public void test_four() {

		int a = 1;
		int b = 2;
		int c = 3;
		Monitor monitor = TestSettings.create(DemoQEAs.makeNonDetProp());

		assertEquals(monitor.step(a), WEAK_FAILURE);
		assertEquals(monitor.step(b), WEAK_FAILURE);
		assertEquals(monitor.step(c), WEAK_SUCCESS);
	}

	@Test
	public void test_five() {

		int a = 1;
		int b = 2;
		int c = 3;
		Monitor monitor = TestSettings.create(DemoQEAs
				.makeSimulateQuantification());

		Object o1 = TestSettings.object("o1");
		Object o2 = TestSettings.object("o2");

		assertEquals(monitor.step(a, o1), WEAK_FAILURE);
		assertEquals(monitor.step(a, o2), WEAK_FAILURE);
		assertEquals(monitor.step(b, o1), WEAK_FAILURE);
		assertEquals(monitor.step(b, o2), WEAK_FAILURE);
		assertEquals(monitor.step(b, o1), SUCCESS);
	}

	@Test
	public void test_six() {

		int f = 1;
		int g = 2;
		int h = 3;
		Monitor monitor = TestSettings.create(DemoQEAs
				.detSingleNonFixedWithProp());

		// System.err.println(monitor.getClass());

		Object x = new Object();
		assertEquals(monitor.step(f, 1, x), WEAK_FAILURE);
		// System.err.println(monitor.getStatus());
		assertEquals(monitor.step(g), WEAK_FAILURE);
		// System.err.println(monitor.getStatus());
		assertEquals(monitor.step(h, x, 6), WEAK_SUCCESS);
		// System.err.println(monitor.getStatus());
	}

	@Test
	public void test_seven() {

		int f = 1;
		int g = 2;
		int h = 3;
		Monitor monitor = TestSettings.create(DemoQEAs.detSingleWithPropNoF());

		Object x = new Object();
		assertEquals(monitor.step(f, x), WEAK_FAILURE);
		assertEquals(monitor.step(g), WEAK_FAILURE);
		assertEquals(monitor.step(h, x), WEAK_SUCCESS);
	}
}