package qea.test.properties;

import static org.junit.Assert.assertEquals;
import static qea.structure.impl.other.Verdict.FAILURE;
import static qea.structure.impl.other.Verdict.WEAK_SUCCESS;
import qea.monitoring.intf.Monitor;

import org.junit.Before;
import org.junit.Test;

import qea.properties.papers.AuctionBiddingNDQEA;
import qea.properties.papers.AuctionBiddingQEA;
import qea.structure.intf.QEA;
import qea.test.TestSettings;

public class AuctionBiddingTest {

	private Monitor monitorD;
	private Monitor monitorND;
	private QEA qeaD = new AuctionBiddingQEA();
	private QEA qeaND = new AuctionBiddingNDQEA();

	static final int BID = 1;

	@Before
	public void setup() {
		monitorD = TestSettings.create(qeaD);
		monitorND = TestSettings.create(qeaND);
	}

	@Test
	public void test_one_d() {
		// Test correct behaviour with a single item
		Object i = new Object();

		assertEquals(monitorD.step(BID, i, 5), WEAK_SUCCESS);
		assertEquals(monitorD.step(BID, i, 6), WEAK_SUCCESS);
		assertEquals(monitorD.step(BID, i, 7), WEAK_SUCCESS);

	}

	@Test
	public void test_one_nd() {
		// Test correct behaviour with a single item
		Object i = new Object();

		assertEquals(monitorND.step(BID, i, 5), WEAK_SUCCESS);
		assertEquals(monitorND.step(BID, i, 6), WEAK_SUCCESS);
		assertEquals(monitorND.step(BID, i, 7), WEAK_SUCCESS);

	}

	@Test
	public void test_two_d() {
		// Test incorrect behaviour with a single item
		Object i = new Object();

		assertEquals(monitorD.step(BID, i, 5), WEAK_SUCCESS);
		assertEquals(monitorD.step(BID, i, 6), WEAK_SUCCESS);
		assertEquals(monitorD.step(BID, i, 5), FAILURE);

	}

	@Test
	public void test_two_nd() {
		// Test incorrect behaviour with a single item
		Object i = new Object();

		assertEquals(monitorND.step(BID, i, 5), WEAK_SUCCESS);
		assertEquals(monitorND.step(BID, i, 6), WEAK_SUCCESS);
		assertEquals(monitorND.step(BID, i, 5), FAILURE);

	}

}
