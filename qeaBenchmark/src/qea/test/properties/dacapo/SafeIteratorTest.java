package qea.test.properties.dacapo;

import static org.junit.Assert.assertEquals;
import static qea.structure.impl.other.Verdict.FAILURE;
import static qea.structure.impl.other.Verdict.SUCCESS;
import qea.monitoring.intf.Monitor;

import org.junit.Before;
import org.junit.Test;

import qea.properties.papers.DaCapo;
import qea.structure.intf.QEA;
import qea.test.TestSettings;

public class SafeIteratorTest {

	private Monitor monitor;
	private QEA qea = DaCapo.makeSafeIterator();

	@Before
	public void setup() {
		monitor = TestSettings.create(qea);
	}

	int ITERATOR = 1;
	int NEXT = 2;

	@Test
	public void test_one() {

		Object i = TestSettings.object("i");

		monitor.step(ITERATOR, i, 5);
		monitor.step(NEXT, i);
		monitor.step(NEXT, i);
		monitor.step(NEXT, i);
		monitor.step(NEXT, i);
		monitor.step(NEXT, i);

		assertEquals(SUCCESS, monitor.end());

	}

	@Test
	public void test_two() {

		Object i = TestSettings.object("i");

		monitor.step(ITERATOR, i, 4);
		monitor.step(NEXT, i);
		monitor.step(NEXT, i);
		monitor.step(NEXT, i);
		monitor.step(NEXT, i);
		monitor.step(NEXT, i);

		assertEquals(FAILURE, monitor.end());

	}

	@Test
	public void test_three() {

		Object i1 = TestSettings.object("i1");
		Object i2 = TestSettings.object("i2");

		monitor.step(ITERATOR, i1, 2);
		monitor.step(ITERATOR, i2, 2);
		monitor.step(NEXT, i1);
		monitor.step(NEXT, i1);
		monitor.step(NEXT, i2);
		monitor.step(NEXT, i2);

		assertEquals(SUCCESS, monitor.end());

	}

	@Test
	public void test_four() {

		Object i1 = TestSettings.object("i1");
		Object i2 = TestSettings.object("i2");

		monitor.step(ITERATOR, i1, 0);
		monitor.step(ITERATOR, i2, 2);
		monitor.step(NEXT, i1);
		monitor.step(NEXT, i1);
		monitor.step(NEXT, i2);
		monitor.step(NEXT, i2);

		assertEquals(FAILURE, monitor.end());

	}

}
