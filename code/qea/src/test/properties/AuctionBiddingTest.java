package test.properties;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.AuctionBiddingQEA;
import structure.intf.QEA;
import static structure.impl.Verdict.*;

public class AuctionBiddingTest {

	private Monitor monitor;
	private QEA qea = new AuctionBiddingQEA();
	
	static final int BID = 1;

	
	@Before
	public void setup(){
		monitor = MonitorFactory.create(qea);
	}
	
	@Test
	public void test_one() {
		// Test correct behaviour with a single item
		Object i = new Object();
		
		assertEquals(monitor.step(BID,i,5),WEAK_SUCCESS);
		assertEquals(monitor.step(BID,i,6),WEAK_SUCCESS);
		assertEquals(monitor.step(BID,i,7),WEAK_SUCCESS);		

	}
	
	@Test
	public void test_two() {
		// Test incorrect behaviour with a single item
		Object i = new Object();
		
		assertEquals(monitor.step(BID,i,5),WEAK_SUCCESS);
		assertEquals(monitor.step(BID,i,6),WEAK_SUCCESS);
		assertEquals(monitor.step(BID,i,5),WEAK_FAILURE);		

	}	

	
}
