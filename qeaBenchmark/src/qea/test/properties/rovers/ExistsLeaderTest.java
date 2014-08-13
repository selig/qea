package qea.test.properties.rovers;

import static org.junit.Assert.assertEquals;
import static qea.structure.impl.other.Verdict.FAILURE;
import static qea.structure.impl.other.Verdict.SUCCESS;
import qea.monitoring.intf.Monitor;

import org.junit.Before;
import org.junit.Test;

import qea.properties.rovers.RoverCaseStudy;
import qea.structure.intf.QEA;
import qea.test.TestSettings;

public class ExistsLeaderTest {

	private Monitor monitor;
	private QEA qea = RoverCaseStudy.makeExistsLeader();

	@Before
	public void setup() {
		monitor = TestSettings.create(qea);
	}

	static int PING = 1;
	static int ACK = 2;

	/*
	 * Successful test -
	 */
	@Test
	public void test_one() {
		Object A = TestSettings.object("A");
		Object B = TestSettings.object("B");
		Object C = TestSettings.object("C");

		monitor.step(PING, A, B);
		// System.err.println("======================\n"+monitor);
		monitor.step(PING, A, C);
		// System.err.println("======================\n"+monitor);
		monitor.step(PING, A, A);
		// System.err.println("======================\n"+monitor);
		monitor.step(ACK, B, A);
		// System.err.println("======================\n"+monitor);
		monitor.step(ACK, A, A);
		// System.err.println("======================\n"+monitor);
		monitor.step(ACK, C, A);
		// System.err.println("======================\n"+monitor);

		assertEquals(SUCCESS, monitor.end());

	}

	/*
	 * Check that x and y have same domain
	 */
	@Test
	public void test_two() {

		Object A = TestSettings.object("A");
		Object B = TestSettings.object("B");
		Object C = TestSettings.object("C");

		monitor.step(PING, A, B);
		monitor.step(PING, C, B);
		monitor.step(ACK, B, A);

		assertEquals(FAILURE, monitor.end());

	}
}
