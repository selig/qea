package test.properties;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;

import org.junit.Before;
import org.junit.Test;

import properties.papers.UseFileOtherQEA;
import structure.intf.QEA;


public class UseFileOtherTest {

	private Monitor monitor;
	private QEA qea = UseFileOtherQEA.makeUseFile();

	static final int f = 1;

	@Before
	public void setup() {
		monitor = MonitorFactory.create(qea);
	}

	@Test
	public void testOne() {

		// Events
		int open = 1;
		int write = 2;
		int save = 3;
		int close = 4;
		int read = 5; // This one is not defined in the QEA!

		Object file1 = new Object();
		Object file2 = new Object();
		Object file3 = new Object();

		monitor.step(open, file1);
		monitor.step(read, file1);
		monitor.step(read, file1);
		monitor.step(open, file2);
		monitor.step(write, file2);
		monitor.step(save, file2);
		monitor.step(read, file2);
		monitor.step(write, file1);
		monitor.step(save, file1);
		monitor.step(close, file1);
		monitor.step(open, file3);
		monitor.step(read, file3);
		monitor.step(write, file2);
		monitor.step(close, file3);
		monitor.step(close, file2);

		System.out.println(monitor);
	}

}
