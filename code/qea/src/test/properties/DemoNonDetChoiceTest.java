package test.properties;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.simple.DemoQEAs;
import structure.intf.QEA;
import static structure.impl.Verdict.*;


public class DemoNonDetChoiceTest {
	
	private Monitor monitor;
	private QEA qea = DemoQEAs.makeDemoNonDetChoice();
	
	static final int e = 1;
	static final int f = 2;
	
	@Before
	public void setup(){
		monitor = MonitorFactory.create(qea);
	}
	
	/*
	 * This tests that matching uses the values for quantified variables
	 * when picking transitions 
	 */
	@Test
	public void test_one() {		
		Object x = new Object();
		Object y = new Object();
		assertEquals(monitor.step(e,x,y),WEAK_FAILURE);
		assertEquals(monitor.step(f,y),WEAK_FAILURE);
		assertEquals(monitor.step(f,x),WEAK_SUCCESS);
	}

}