package test.properties;

import static org.junit.Assert.assertEquals;
import static structure.impl.other.Verdict.SUCCESS;
import static structure.impl.other.Verdict.WEAK_FAILURE;
import monitoring.intf.Monitor;

import org.junit.Before;
import org.junit.Test;

import properties.simple.DemoQEAs;
import structure.intf.QEA;
import test.TestSettings;

public class PropositionalDependTest {

	private Monitor monitor;
	private QEA qea = DemoQEAs.makePropositionalDepend();

	static final int e = 1;
	static final int f = 2;

	@Before
	public void setup() {
		monitor = TestSettings.create(qea);
	}

	/*
	 * 
	 */
	@Test
	public void test_one() {

		assertEquals(monitor.step(e, 5), WEAK_FAILURE);
		// System.out.println(monitor);
		assertEquals(monitor.step(f, 4), WEAK_FAILURE);
		// System.out.println(monitor);
		assertEquals(monitor.step(f, 6), SUCCESS);
		// System.out.println(monitor);
	}

}