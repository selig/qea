package test.properties.rovers;

import static org.junit.Assert.assertEquals;
import static structure.impl.other.Verdict.*;
import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;

import org.junit.Before;
import org.junit.Test;

import properties.rovers.RoverCaseStudy;
import structure.intf.QEA;
import test.TestSettings;

public class RespectPrioritiesTest {

	private Monitor monitor;
	private QEA qea = RoverCaseStudy.makeRespectPriorities();

	int GRANT = 1;
	int CANCEL = 2;
	int PRIORITY = 3;
	int REQUEST = 4;
	int RESCIND = 5;
	int DENY = 6;	
	
	@Before
	public void setup() {
		monitor = TestSettings.create(qea);
		
		Object r1 = TestSettings.object("r1");
		Object r2 = TestSettings.object("r2");
		Object r3 = TestSettings.object("r3");
		
		monitor.step(PRIORITY,r1,r2);
		monitor.step(PRIORITY,r1,r3);
		monitor.step(REQUEST,r1);
		monitor.step(GRANT,r1);
		monitor.step(REQUEST,r2);
		monitor.step(DENY,r2);
		monitor.step(CANCEL,r1);
		monitor.step(REQUEST,r3);
		monitor.step(GRANT,r3);
		monitor.step(CANCEL,r3);
		
		//System.err.println(monitor);
		
		assertEquals(SUCCESS,monitor.end());
		
	}


	@Test
	public void test_one() {


	}

}
