package test.properties;

import static org.junit.Assert.assertEquals;
import static structure.impl.other.Verdict.SUCCESS;
import static structure.impl.other.Verdict.WEAK_FAILURE;
import static structure.impl.other.Verdict.WEAK_SUCCESS;
import monitoring.intf.Monitor;

import org.junit.Before;
import org.junit.Test;

import properties.papers.UseFileOtherQEA;
import structure.intf.QEA;
import test.TestSettings;

public class UseFileOtherTest {

	private Monitor monitor;
	private QEA qea = UseFileOtherQEA.makeUseFile();

	static final int f = 1;

	@Before
	public void setup() {
		monitor = TestSettings.create(qea);
	}

	@Test
	public void testOne() {

		// Events
		int open = 1;
		int write = 2;
		int save = 3;
		int close = 4;
		int read = 5; // This one is not defined in the QEA!

		Object file1 = new Object() {
			@Override
			public String toString() {
				return "F1";
			}
		};
		Object file2 = new Object() {
			@Override
			public String toString() {
				return "F2";
			}
		};
		Object file3 = new Object() {
			@Override
			public String toString() {
				return "F3";
			}
		};

		assertEquals(monitor.step(open, file1), WEAK_FAILURE);
		assertEquals(monitor.step(read, file1), WEAK_FAILURE);
		assertEquals(monitor.step(read, file1), WEAK_FAILURE);
		assertEquals(monitor.step(open, file2), WEAK_FAILURE);
		assertEquals(monitor.step(write, file2), WEAK_FAILURE);
		assertEquals(monitor.step(save, file2), WEAK_FAILURE);
		assertEquals(monitor.step(read, file2), WEAK_FAILURE);
		assertEquals(monitor.step(write, file1), WEAK_FAILURE);
		assertEquals(monitor.step(save, file1), WEAK_FAILURE);
		assertEquals(monitor.step(close, file1), WEAK_FAILURE);
		assertEquals(monitor.step(open, file3), WEAK_FAILURE);
		assertEquals(monitor.step(read, file3), WEAK_FAILURE);
		assertEquals(monitor.step(write, file2), WEAK_FAILURE);
		assertEquals(monitor.step(close, file3), WEAK_FAILURE);
		assertEquals(monitor.step(close, file2), WEAK_SUCCESS);

		// System.out.println(monitor);

		assertEquals(monitor.end(), SUCCESS);
	}

}
