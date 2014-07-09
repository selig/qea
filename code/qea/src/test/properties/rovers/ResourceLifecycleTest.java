package test.properties.rovers;

import monitoring.intf.Monitor;

import org.junit.Before;
import org.junit.Test;

import properties.rovers.RoverCaseStudy;
import structure.intf.QEA;
import test.TestSettings;

public class ResourceLifecycleTest {

	private Monitor monitor;
	private QEA qea = RoverCaseStudy.makeResourceLifecycle();

	@Before
	public void setup() {
		monitor = TestSettings.create(qea);
	}

	@Test
	public void test_one() {

	}

}
