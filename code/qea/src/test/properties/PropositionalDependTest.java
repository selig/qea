package test.properties;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.simple.DemoQEAs;
import structure.intf.QEA;
import static structure.impl.Verdict.*;


public class PropositionalDependTest {
	
	private Monitor monitor;
	private QEA qea = DemoQEAs.makePropositionalDepend();
	
	static final int e = 1;
	static final int f = 2;
	
	@Before
	public void setup(){
		monitor = MonitorFactory.create(qea);
	}
	
	/*
	 * 
	 */
	@Test
	public void test_one() {		

		assertEquals(monitor.step(e,5),WEAK_FAILURE);
		System.out.println(monitor);
		assertEquals(monitor.step(f,4),WEAK_FAILURE);
		System.out.println(monitor);
		assertEquals(monitor.step(f,6),WEAK_SUCCESS);
		System.out.println(monitor);
	}

}