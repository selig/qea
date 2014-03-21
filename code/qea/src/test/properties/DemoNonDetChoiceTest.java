package test.properties;

import static org.junit.Assert.assertEquals;
import static structure.impl.other.Verdict.*;
import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;

import org.junit.Before;
import org.junit.Test;

import properties.simple.DemoQEAs;
import structure.intf.QEA;

public class DemoNonDetChoiceTest {

	private Monitor monitor;
	private QEA qea = DemoQEAs.makeDemoNonDetChoice();

	static final int e = 1;
	static final int f = 2;

	@Before
	public void setup() {
		monitor = MonitorFactory.create(qea);
	}

	/*
	 * This tests that matching uses the values for quantified variables when
	 * picking transitions
	 */
	@Test
	public void test_one() {
		Object x = new Object(){ public String toString(){return "A";}};
		Object y = new Object(){ public String toString(){return "B";}};
		assertEquals(monitor.step(e, x, y), WEAK_FAILURE);
		System.out.println(monitor);
		assertEquals(monitor.step(f, y), WEAK_FAILURE);
		System.out.println(monitor);
		assertEquals(monitor.step(f, x), WEAK_FAILURE);
		System.out.println(monitor);
	}

}