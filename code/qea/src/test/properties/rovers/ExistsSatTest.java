package test.properties.rovers;

import static org.junit.Assert.assertEquals;
import static structure.impl.other.Verdict.FAILURE;
import static structure.impl.other.Verdict.SUCCESS;
import monitoring.intf.Monitor;

import org.junit.Before;
import org.junit.Test;

import properties.rovers.RoverCaseStudy;
import structure.intf.QEA;
import test.TestSettings;

public class ExistsSatTest {

	private Monitor monitorOne;
	private Monitor monitorTwo;
	private QEA qeaOne = RoverCaseStudy.makeExistsSatelliteSingle();
	private QEA qeaTwo = RoverCaseStudy.makeExistsSatelliteDouble();

	@Before
	public void setup() {
		monitorOne = TestSettings.create(qeaOne);
		monitorTwo = TestSettings.create(qeaTwo);
	}

	int PING = 1;
	int ACK = 2;

	public void do_test_one(Monitor monitor) {
		Object R1 = TestSettings.object("R1");
		Object R2 = TestSettings.object("R2");
		Object S1 = TestSettings.object("S1");
		Object S2 = TestSettings.object("S2");

		monitor.step(PING, R1, S1);
		monitor.step(PING, R1, S2);
		monitor.step(PING, R2, S1);
		monitor.step(PING, R2, S2);
		monitor.step(ACK, S2, R1);
		monitor.step(ACK, S1, R2);

		assertEquals(SUCCESS, monitor.end());

	}

	@Test
	public void test_one() {
		do_test_one(monitorOne);
		do_test_one(monitorTwo);
	}

	public void do_test_two(Monitor monitor) {
		Object R1 = TestSettings.object("R1");
		Object R2 = TestSettings.object("R2");
		Object S1 = TestSettings.object("S1");
		Object S2 = TestSettings.object("S2");

		monitor.step(PING, R1, S1);
		// System.err.println("==========\n"+monitor);
		monitor.step(PING, R1, S2);
		// System.err.println("==========\n"+monitor);
		monitor.step(PING, R2, S1);
		// System.err.println("==========\n"+monitor);
		monitor.step(PING, R2, S2);
		// System.err.println("==========\n"+monitor);
		monitor.step(ACK, S2, R1);
		// System.err.println("==========\n"+monitor);

		assertEquals(FAILURE, monitor.end());
	}

	@Test
	public void test_two() {
		do_test_two(monitorOne);
		do_test_two(monitorTwo);
	}

}
