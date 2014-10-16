package qea.benchmark.competition.offline;

import java.io.File;
import java.io.IOException;

import qea.monitoring.impl.CSVFileMonitor;
import qea.monitoring.impl.translators.OfflineTranslator;
import qea.properties.competition.translators.MonPolyTranslators;
import qea.structure.impl.other.Verdict;
import qea.structure.intf.QEA;

public class Monpoly {

	static qea.properties.competition.MonPoly m = new qea.properties.competition.MonPoly();
	static MonPolyTranslators tm = new MonPolyTranslators();

	public static void main(String[] args) throws IOException {
		// runOne();
		// runTwo();
		// runThree();
		 runFour();
		
		
		//runFive();
	}

	public static void runOne() throws IOException {

		long startTime = System.currentTimeMillis();
		// String trace = "/Users/giles/git/csrv14/OFFLINE/Team4/Bench1/trace.csv";
		String trace = "traces/Team4/B1_trace.csv";
		QEA qea = m.makeOne();
		OfflineTranslator t = tm.makeOne();
		CSVFileMonitor m = new CSVFileMonitor(trace, qea, t);
		System.err.println("Running with " + m.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();

		Verdict v = m.monitor();
		System.err.println("MonPoly 1: Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : " + (endTime - startTime));
	}

	public static void runTwo() throws IOException {

		long startTime = System.currentTimeMillis();
		String trace = "traces/Team4/B2_trace.csv";
		QEA qea = m.makeTwo();
		OfflineTranslator t = tm.makeTwo();
		CSVFileMonitor m = new CSVFileMonitor(trace, qea, t);
		System.err.println("Running with " + m.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();

		Verdict v = m.monitor();
		System.err.println("MonPoly 2: Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : " + (endTime - startTime));
	}

	public static void runThree() throws IOException {

		long startTime = System.currentTimeMillis();
		String trace = "traces/Team4/B3_trace.csv";
		QEA qea = m.makeThree();
		OfflineTranslator t = tm.makeThree();
		CSVFileMonitor m = new CSVFileMonitor(trace, qea, t);
		System.err.println("Running with " + m.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();

		Verdict v = m.monitor();
		System.err.println("MonPoly 3: Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : " + (endTime - startTime));
	}

	public static void runFour() throws IOException {

		long startTime = System.currentTimeMillis();
		 String trace = "/Users/giles/git/csrv14/OFFLINE/Team4/Bench4/trace.csv";
		//String trace = "traces/Team4/B4_trace.csv";
		QEA qea = m.makeFour();
		OfflineTranslator t = tm.makeFour();
		CSVFileMonitor m = new CSVFileMonitor(trace, qea, t);
		System.err.println("Running with " + m.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();

		Verdict v = m.monitor();
		System.err.println("MonPoly 4: Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : " + (endTime - startTime));
	}

	public static void runFive() throws IOException {

		long startTime = System.currentTimeMillis();

		//String trace = "/Users/giles/git/csrv14/OFFLINE/Team4/Bench5/ldcc/ldcc.csv";
		String trace = "traces/Team4/ldcc.csv";
		QEA qea = m.makeFive();
		OfflineTranslator t = tm.makeFive();
		CSVFileMonitor m = new CSVFileMonitor(trace, qea, t);
		System.err.println("Running with " + m.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();

		Verdict v = m.monitor();
		System.err.println("MonPoly 5: Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : " + (endTime - startTime));
	}
}
