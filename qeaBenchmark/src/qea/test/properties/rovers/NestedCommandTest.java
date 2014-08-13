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

public class NestedCommandTest {

	private Monitor monitor;
	private QEA qea = RoverCaseStudy.makeNestedCommand();

	@Before
	public void setup() {
		monitor = TestSettings.create(qea);
	}

	int COM = 1;
	int SUC = 2;

	@Test
	public void test_one() {
		Object C1 = TestSettings.object("C1");
		Object C2 = TestSettings.object("C2");
		Object C3 = TestSettings.object("C3");

		monitor.step(COM, C1);
		monitor.step(COM, C2);
		monitor.step(COM, C3);
		monitor.step(SUC, C3);
		monitor.step(SUC, C2);
		monitor.step(SUC, C1);

		assertEquals(SUCCESS, monitor.end());
	}

	@Test
	public void test_two() {
		Object C1 = TestSettings.object("C1");
		Object C2 = TestSettings.object("C2");
		Object C3 = TestSettings.object("C3");

		monitor.step(COM, C1);
		monitor.step(COM, C2);
		monitor.step(COM, C3);
		monitor.step(SUC, C3);
		monitor.step(SUC, C1);
		monitor.step(SUC, C2);

		assertEquals(FAILURE, monitor.end());
	}

	@Test
	public void test_three() {
		Object C1 = TestSettings.object("C1");
		Object C2 = TestSettings.object("C2");
		Object C3 = TestSettings.object("C3");

		monitor.step(COM, C1);
		monitor.step(COM, C2);
		monitor.step(COM, C3);
		monitor.step(SUC, C3);
		monitor.step(COM, C3);
		monitor.step(SUC, C3);
		monitor.step(SUC, C2);
		monitor.step(COM, C2);
		monitor.step(SUC, C2);
		monitor.step(SUC, C1);
		monitor.step(COM, C3);
		monitor.step(SUC, C3);

		assertEquals(SUCCESS, monitor.end());
	}

	@Test
	public void test_four() {
		Object C1 = TestSettings.object("C1");
		Object C2 = TestSettings.object("C2");
		Object C3 = TestSettings.object("C3");

		monitor.step(COM, C1);
		monitor.step(COM, C2);
		monitor.step(SUC, C3);
		monitor.step(SUC, C2);
		monitor.step(SUC, C1);

		assertEquals(FAILURE, monitor.end());
	}
}
