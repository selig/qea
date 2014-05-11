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

public class ReleaseResourceTest {

	private Monitor monitor;
	private QEA qea = RoverCaseStudy.makeReleaseResource();

	@Before
	public void setup() {
		monitor = TestSettings.create(qea);
	}

	int SCHEDULE = 1;
	int GRANT = 2;
	int CANCEL = 3;
	int FINISH = 4;

	@Test
	public void test_one() {

		Object task = TestSettings.object("task");
		Object command = TestSettings.object("command");
		Object resource = TestSettings.object("resource");

		monitor.step(SCHEDULE,task,command);
		monitor.step(GRANT,task,resource);
		monitor.step(CANCEL,task,resource);
		monitor.step(FINISH,command);
		
		assertEquals(SUCCESS,monitor.end());
		
	}
	@Test
	public void test_two() {

		Object task = TestSettings.object("task");
		Object command = TestSettings.object("command");
		Object resource = TestSettings.object("resource");

		monitor.step(SCHEDULE,task,command);
		monitor.step(GRANT,task,resource);
		monitor.step(FINISH,command);
		
		assertEquals(FAILURE,monitor.end());
		
	}	
	@Test
	public void test_three() {

		Object task = TestSettings.object("task");
		Object command1 = TestSettings.object("command1");
		Object resource1 = TestSettings.object("resource1");
		Object command2 = TestSettings.object("command2");
		Object resource2 = TestSettings.object("resource2");
		
		monitor.step(SCHEDULE,task,command1);
		monitor.step(GRANT,task,resource1);
		monitor.step(GRANT,task,resource2);
		monitor.step(SCHEDULE,task,command2);
		monitor.step(CANCEL,task,resource1);
		monitor.step(CANCEL,task,resource2);
		monitor.step(FINISH,command1);
		monitor.step(GRANT,task,resource1);
		monitor.step(CANCEL,task,resource1);				
		monitor.step(FINISH,command2);
		
		assertEquals(SUCCESS,monitor.end());
		
	}	
	@Test
	public void test_four() {

		Object task = TestSettings.object("task");
		Object command = TestSettings.object("command");
		Object resource = TestSettings.object("resource");

		monitor.step(SCHEDULE,task,command);
		monitor.step(GRANT,task,resource);
		monitor.step(CANCEL,task,resource);
		
		assertEquals(SUCCESS,monitor.end());
		
	}
	@Test
	public void test_five() {

		Object task = TestSettings.object("task");
		Object command = TestSettings.object("command");
		Object resource = TestSettings.object("resource");

		monitor.step(GRANT,task,resource);
		monitor.step(SCHEDULE,task,command);
		monitor.step(FINISH,command);
		monitor.step(CANCEL,task,resource);
		
		assertEquals(SUCCESS,monitor.end());
		
	}	
	@Test
	public void test_six() {

		Object task1 = TestSettings.object("task1");
		Object command1 = TestSettings.object("command1");
		Object resource1 = TestSettings.object("resource1");
		Object task2 = TestSettings.object("task2");
		Object command2 = TestSettings.object("command2");
		Object resource2 = TestSettings.object("resource2");
		
		monitor.step(SCHEDULE,task1,command1);
		monitor.step(GRANT,task2,resource2);
		monitor.step(SCHEDULE,task2,command2);
		monitor.step(GRANT,task2,resource1);
		monitor.step(CANCEL,task1,resource1);
		monitor.step(FINISH,command2);
		monitor.step(CANCEL,task1,resource1);
		
		assertEquals(FAILURE,monitor.end());
		
	}		
}
