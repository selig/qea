package benchmark.competition.offline;

import java.io.IOException;

import monitoring.impl.CSVFileMonitor;
import monitoring.impl.MonitorFactory;
import monitoring.impl.translators.OfflineTranslator;
import structure.impl.other.Verdict;
import structure.intf.QEA;

public class Monpoly {

	public static void main(String[] args) throws IOException {
		runFive();
	}

	public static void runOne() throws IOException {
		long startTime = System.currentTimeMillis();
		// String trace = "/Users/giles/git/csrv14/OFFLINE/Team4/Bench1/trace.csv";
		String trace = "traces/Team4/B1_trace.csv";
		QEA qea = (new properties.competition.MonPoly()).makeOne();
		System.err.println("Using monitor "
				+ MonitorFactory.create(qea).getClass());
		OfflineTranslator t = (new properties.competition.translators.MonPolyTranslators())
				.makeOne();
		CSVFileMonitor m = new CSVFileMonitor(trace, qea, t);
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
		QEA qea = (new properties.competition.MonPoly()).makeTwo();
		System.err.println("Using monitor "
				+ MonitorFactory.create(qea).getClass());
		OfflineTranslator t = (new properties.competition.translators.MonPolyTranslators())
				.makeTwo();
		CSVFileMonitor m = new CSVFileMonitor(trace, qea, t);
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
		QEA qea = (new properties.competition.MonPoly()).makeThree();
		System.err.println("Using monitor "
				+ MonitorFactory.create(qea).getClass());
		OfflineTranslator t = (new properties.competition.translators.MonPolyTranslators())
				.makeThree();
		CSVFileMonitor m = new CSVFileMonitor(trace, qea, t);
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
		// String trace = "/Users/giles/git/csrv14/OFFLINE/Team4/Bench4/trace.csv";
		String trace = "traces/Team4/B4_trace.csv";
		QEA qea = (new properties.competition.MonPoly()).makeFour();
		System.err.println("Using monitor "
				+ MonitorFactory.create(qea).getClass());
		OfflineTranslator t = (new properties.competition.translators.MonPolyTranslators())
				.makeFour();
		CSVFileMonitor m = new CSVFileMonitor(trace, qea, t);
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
		String trace = "/Users/giles/git/csrv14/OFFLINE/Team4/Bench5/ldcc/ldcc.csv";
		QEA qea = (new properties.competition.MonPoly()).makeFive();
		System.err.println("Using monitor "
				+ MonitorFactory.create(qea).getClass());
		OfflineTranslator t = (new properties.competition.translators.MonPolyTranslators())
				.makeFive();
		CSVFileMonitor m = new CSVFileMonitor(trace, qea, t);
		long beforeMonitoring = System.currentTimeMillis();

		Verdict v = m.monitor();
		System.err.println("MonPoly 5: Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : " + (endTime - startTime));
	}	
}
