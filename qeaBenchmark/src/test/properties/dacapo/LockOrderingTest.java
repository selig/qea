package test.properties.dacapo;

import static org.junit.Assert.assertEquals;
import static structure.impl.other.Verdict.FAILURE;
import static structure.impl.other.Verdict.SUCCESS;
import monitoring.intf.Monitor;

import org.junit.Before;
import org.junit.Test;

import properties.papers.DaCapo;
import structure.intf.QEA;
import test.TestSettings;

public class LockOrderingTest {

	private Monitor monitor;
	private QEA qea = DaCapo.makeLockOrdering();

	@Before
	public void setup() {
		monitor = TestSettings.create(qea);
	}

	int LOCK = 1;
	int UNLOCK = 2;

	@Test
	public void test_one() {

		Object A = TestSettings.object("A");
		Object B = TestSettings.object("B");

		monitor.step(LOCK, A);
		monitor.step(LOCK, B);
		monitor.step(UNLOCK, B);
		monitor.step(UNLOCK, A);

		assertEquals(SUCCESS, monitor.end());

	}

	@Test
	public void test_two() {

		Object A = TestSettings.object("A");

		monitor.step(LOCK, A);
		monitor.step(LOCK, A);
		monitor.step(UNLOCK, A);
		monitor.step(UNLOCK, A);

		assertEquals(SUCCESS, monitor.end());

	}

	@Test
	public void test_three() {

		Object A = TestSettings.object("A");
		Object B = TestSettings.object("B");

		monitor.step(LOCK, A);
		monitor.step(LOCK, B);
		monitor.step(UNLOCK, B);
		monitor.step(UNLOCK, A);
		monitor.step(LOCK, B);
		monitor.step(LOCK, A);
		monitor.step(UNLOCK, A);
		monitor.step(UNLOCK, B);

		assertEquals(FAILURE, monitor.end());

	}

	@Test
	public void test_four() {

		Object A = TestSettings.object("A");
		Object B = TestSettings.object("B");
		Object C = TestSettings.object("C");

		monitor.step(LOCK, A);
		monitor.step(LOCK, B);
		monitor.step(LOCK, C);
		monitor.step(UNLOCK, C);
		monitor.step(UNLOCK, A);
		monitor.step(LOCK, C);
		monitor.step(LOCK, A);

		assertEquals(FAILURE, monitor.end());

	}
}
