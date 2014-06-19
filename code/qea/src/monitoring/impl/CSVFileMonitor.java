package monitoring.impl;

import java.io.FileNotFoundException;
import java.io.IOException;

import properties.rovers.RoverCaseStudy;
import structure.impl.other.Verdict;
import structure.intf.QEA;

/*
 * We expect events in the form
 * 
 * name,arg0=A,arg1=B
 * 
 * where we ignore the names ar0,arg1.. so this becomes name(A,B)
 * 
 * we assume that all event names (in string form) begin with a lower case character
 * 
 */

public class CSVFileMonitor extends FileMonitor {

	public CSVFileMonitor(String tracename, QEA qea)
			throws FileNotFoundException {
		super(tracename, qea);
	}

	@Override
	public Verdict monitor() throws IOException {

		String line;
		int events = 0;
		while ((line = trace.readLine()) != null) {
			events++;
			if (events % 100 == 0) {
				System.err.println(events);
				// System.err.println(monitor);
			}
			// System.err.println(events+":"+line);
			if (step(line) == Verdict.FAILURE) {
				System.err.println("Failure on " + events + ":" + line);
			}
		}
		System.err.println(events + " events");
		return monitor.end();
	}

	private Verdict step(String line) {

		String[] parts = line.split(",|=");
		int name = translate(parts[0]);
		if (parts.length == 3) {
			return monitor.step(name, format(parts[2]));
		} else if (parts.length == 5) {
			return monitor.step(name, format(parts[2]), format(parts[4]));
		} else {
			int noargs = (parts.length - 1) / 2;
			Object[] args = new Object[noargs];
			for (int i = 1; i < noargs; i += 2) {
				args[i] = format(parts[i]);
			}
			return monitor.step(name, args);
		}
	}

	private Object format(String arg) {
		return arg.intern();
	}

	// A test
	public static void main(String[] args) throws IOException {

		CSVFileMonitor f = new CSVFileMonitor("traces/RespectConflicts.trace",
				RoverCaseStudy.makeRespectConflictsSingle());

		long start = System.currentTimeMillis();
		System.err.println(f.monitor());
		System.err.println("Took: " + (System.currentTimeMillis() - start));

	}

}
