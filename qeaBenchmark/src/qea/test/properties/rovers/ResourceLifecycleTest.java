package qea.test.properties.rovers;

import qea.monitoring.intf.Monitor;

import org.junit.Before;
import org.junit.Test;

import qea.properties.rovers.RoverCaseStudy;
import qea.structure.intf.QEA;
import qea.test.TestSettings;

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
