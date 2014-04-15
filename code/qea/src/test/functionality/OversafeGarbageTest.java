package test.functionality;

import static org.junit.Assert.assertEquals;
import static structure.impl.other.Verdict.FAILURE;
import static structure.impl.other.Verdict.SUCCESS;
import static structure.impl.other.Verdict.WEAK_FAILURE;
import static structure.impl.other.Verdict.WEAK_SUCCESS;
import monitoring.impl.GarbageMode;
import monitoring.impl.MonitorFactory;
import monitoring.impl.RestartMode;
import monitoring.intf.Monitor;

import org.junit.Test;

import properties.rovers.RoverCaseStudy;
import properties.simple.DemoQEAs;

public class OversafeGarbageTest {


	int GRANT = 1;
	int CANCEL = 2;
	
	/*
	 * This tests if we restart after failure 
	 */
	@Test
	public void test_one() {

		Monitor monitor = MonitorFactory
				.create(GarbageMode.OVERSAFE_LAZY,
						RoverCaseStudy.makeGrantCancelSingle());

	
		for(int rep=0;rep<1000;rep++){
			Object task = new Object();
			Object res = new Object();
			assertEquals(WEAK_SUCCESS,monitor.step(GRANT,task,res));
			assertEquals(WEAK_SUCCESS,monitor.step(CANCEL,task,res));
		}
		

		
	}


}
