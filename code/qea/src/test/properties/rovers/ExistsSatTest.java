package test.properties.rovers;

import static org.junit.Assert.assertEquals;
import static structure.impl.other.Verdict.FAILURE;
import static structure.impl.other.Verdict.WEAK_SUCCESS;
import monitoring.impl.MonitorFactory;
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


	@Test
	public void test_one() {


	}

}
