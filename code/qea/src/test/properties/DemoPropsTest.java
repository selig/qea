package test.properties;

import static org.junit.Assert.assertEquals;
import static structure.impl.other.Verdict.*;
import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;

import org.junit.Before;
import org.junit.Test;

import properties.simple.DemoQEAs;
import structure.intf.QEA;

public class DemoPropsTest {

	/*
	 * This tests that matching uses the values for quantified variables when
	 * picking transitions
	 */
	@Test
	public void test_one() {
		
		int e=1;
		int f=2;
		Monitor monitor = MonitorFactory.create(DemoQEAs.makeDemoNonDetChoice());
		
		Object x = new Object(){ public String toString(){return "A";}};
		Object y = new Object(){ public String toString(){return "B";}};
		assertEquals(monitor.step(e, x, y), WEAK_FAILURE);
		assertEquals(monitor.step(f, y), WEAK_FAILURE);
		assertEquals(monitor.step(f, x), WEAK_FAILURE);
	}

	@Test
	public void test_two() {

		int f=1;
		int g=2;
		Monitor monitor = MonitorFactory.create(DemoQEAs.detFreeWithOneProp());		
		
		assertEquals(monitor.step(f), WEAK_FAILURE);
		assertEquals(monitor.step(g, 6), SUCCESS);
	}	
	
	@Test
	public void test_three() {

		int f=1;
		int g=2;
		Monitor monitor = MonitorFactory.create(DemoQEAs.detOneFreeWithOneProp());			
		
		assertEquals(monitor.step(f), WEAK_SUCCESS);
		assertEquals(monitor.step(g, new Object(), 6), WEAK_SUCCESS);
		assertEquals(monitor.step(g, new Object(), 4), WEAK_FAILURE);
	}	
	
	@Test
	public void test_four() {

		int a=1;
		int b=2;
		int c=3;
		Monitor monitor = MonitorFactory.create(DemoQEAs.makeNonDetProp());			
		
		assertEquals(monitor.step(a), WEAK_FAILURE);
		assertEquals(monitor.step(b), WEAK_FAILURE);
		assertEquals(monitor.step(c), WEAK_SUCCESS);
	}	
	
	@Test
	public void test_five() {

		int a=1;
		int b=2;
		int c=3;
		Monitor monitor = MonitorFactory.create(DemoQEAs.makeSimulateQuantification());			
		
		Object o1 = new Object(){ public String toString(){return "o1";}};
		Object o2 = new Object(){ public String toString(){return "o2";}};
		
		assertEquals(monitor.step(a,o1), WEAK_FAILURE);
		assertEquals(monitor.step(a,o2), WEAK_FAILURE);
		assertEquals(monitor.step(b,o1), WEAK_FAILURE);
		assertEquals(monitor.step(b,o2), WEAK_FAILURE);		
		assertEquals(monitor.step(b,o1), SUCCESS);
	}		
	
}