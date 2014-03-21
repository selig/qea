package test.properties;

import static org.junit.Assert.assertEquals;
import static structure.impl.other.Verdict.WEAK_FAILURE;
import static structure.impl.other.Verdict.WEAK_SUCCESS;
import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;

import org.junit.Before;
import org.junit.Test;

import properties.simple.DemoQEAs;
import structure.intf.QEA;

public class DetFreeWithOnePropTest {

	private Monitor monitor;
	private QEA qea = DemoQEAs.detFreeWithOneProp();

	static final int f = 1;
	static final int g = 2;

	@Before
	public void setup() {
		monitor = MonitorFactory.create(qea);
	}

	/*
	 * 
	 */
	@Test
	public void test_one() {

		assertEquals(monitor.step(f), WEAK_FAILURE);
		System.out.println(monitor);
		assertEquals(monitor.step(g, 6), WEAK_SUCCESS);
		System.out.println(monitor);
	}

}