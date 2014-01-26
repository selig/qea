package test.properties;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.HasNextQEA;
import structure.intf.QEA;
import static structure.impl.Verdict.*;

public class HasNextTest {

	private Monitor monitor;
	private QEA qea = new HasNextQEA();
	
	@Before
	public void setup(){
		monitor = MonitorFactory.create(qea);
	}
	
	@Test
	public void test_one() {
		// Test with a single iterator
		
		// cannot write test until we define what an event is
		//assertEquals(monitor.step(event),WEAK_SUCCESS);
	}

}
