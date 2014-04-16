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

public class RespectConflictsTest {

	private Monitor monitorOne;
	private Monitor monitorTwo;
	private QEA qeaOne = RoverCaseStudy.makeRespectConflictsSingle();
	private QEA qeaTwo = RoverCaseStudy.makeRespectConflictsDouble();

	@Before
	public void setup() {
		monitorOne = TestSettings.create(qeaOne);
		monitorTwo = TestSettings.create(qeaTwo);
	}

	int CONFLICT = 1;
	int GRANT = 2;
	int CANCEL = 3;	
	
	public void do_test_one(Monitor monitor){
		
		Object r1 = TestSettings.object("r1");
		Object r2 = TestSettings.object("r2");
		Object r3 = TestSettings.object("r3");
		
		monitor.step(CONFLICT,r1,r2);
		monitor.step(CONFLICT,r1,r3);
		monitor.step(GRANT,r1);
		monitor.step(CANCEL,r1);
		monitor.step(GRANT,r2);
		monitor.step(GRANT,r3);
				
		//System.err.println(monitor);
		
		assertEquals(SUCCESS,monitor.end());
		
	}

	@Test
	public void test_one() {
		do_test_one(monitorOne);
		//do_test_one(monitorTwo);
	}
	public void do_test_two(Monitor monitor){
		
		Object r1 = TestSettings.object("r1");
		Object r2 = TestSettings.object("r2");
		Object r3 = TestSettings.object("r3");
		
		monitor.step(CONFLICT,r1,r2);
		monitor.step(CONFLICT,r1,r3);
		monitor.step(GRANT,r1);
		monitor.step(CANCEL,r1);
		monitor.step(GRANT,r2);
		monitor.step(GRANT,r3);
		monitor.step(GRANT,r1);
		
		//System.err.println(monitor);
		
		assertEquals(FAILURE,monitor.end());
		
	}

	@Test
	public void test_two() {
		do_test_two(monitorOne);
		do_test_two(monitorTwo);
	}	

}
