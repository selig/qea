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

public class ExistsLeaderTest {

	private Monitor monitor;
	private QEA qea = RoverCaseStudy.makeExistsLeader();

	@Before
	public void setup() {
		monitor = TestSettings.create(qea);
	}


	@Test
	public void test_one() {


	}

}
