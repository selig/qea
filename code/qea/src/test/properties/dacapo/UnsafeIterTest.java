package test.properties.dacapo;

import static org.junit.Assert.assertEquals;
import static structure.impl.other.Verdict.FAILURE;
import static structure.impl.other.Verdict.SUCCESS;

import java.util.ArrayList;
import java.util.List;

import monitoring.impl.IncrementalMonitor;
import monitoring.intf.Monitor;

import org.junit.Before;
import org.junit.Test;

import properties.papers.DaCapo;
import structure.intf.QEA;
import test.TestSettings;

public class UnsafeIterTest {

	private Monitor monitor;
	private QEA qea = DaCapo.makeUnsafeIter();

	@Before
	public void setup() {
		monitor = TestSettings.create(qea);
	}

	int ITERATOR = 1;
	int USE = 2;
	int UPDATE = 3;

	@Test
	public void test_one() {
		Object c = TestSettings.object("C");
		Object i = TestSettings.object("I");

		monitor.step(ITERATOR, c, i);
		monitor.step(USE, i);
		monitor.step(UPDATE, c);

		assertEquals(SUCCESS, monitor.end());
	}

	@Test
	public void test_two() {
		Object c = TestSettings.object("C");
		Object i = TestSettings.object("I");

		monitor.step(ITERATOR, c, i);
		monitor.step(USE, i);
		monitor.step(UPDATE, c);
		monitor.step(USE, i);

		assertEquals(FAILURE, monitor.end());
	}

	@Test
	public void test_three() {
		Object c = TestSettings.object("C");
		Object i1 = TestSettings.object("I1");
		Object i2 = TestSettings.object("I2");

		monitor.step(ITERATOR, c, i1);
		monitor.step(USE, i1);
		monitor.step(UPDATE, c);
		monitor.step(ITERATOR, c, i2);
		monitor.step(USE, i2);
		monitor.step(UPDATE, c);

		assertEquals(SUCCESS, monitor.end());
	}

	@Test
	public void test_four() {
		Object c = TestSettings.object("C");
		Object i = TestSettings.object("I");

		monitor.step(USE, i);
		monitor.step(UPDATE, c);
		monitor.step(USE, i);
		monitor.step(UPDATE, c);

		assertEquals(SUCCESS, monitor.end());
	}

	@Test
	public void test_five() {
		List<Object> c = new ArrayList<>();

		monitor.step(UPDATE, c);
		c.add(new Object());
		monitor.step(UPDATE, c);
		c.add(new Object());
		monitor.step(UPDATE, c);

		assertEquals(SUCCESS, monitor.end());
	}

	@Test
	public void test_six() {
		Object c1 = TestSettings.object("C1");
		Object i1 = TestSettings.object("I1");
		Object c2 = TestSettings.object("C2");
		Object i2 = TestSettings.object("I2");

		IncrementalMonitor.use_red = false;

		monitor.step(UPDATE, c1);
		monitor.step(UPDATE, c2);
		monitor.step(ITERATOR, c1, i1);
		monitor.step(ITERATOR, c2, i2);
		monitor.step(USE, i1);
		monitor.step(USE, i2);
		monitor.step(UPDATE, c1);
		monitor.step(USE, i2);

		System.err.println(monitor);

		assertEquals(SUCCESS, monitor.end());
	}
}
