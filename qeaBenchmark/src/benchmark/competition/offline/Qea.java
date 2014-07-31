package benchmark.competition.offline;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import monitoring.impl.CSVFileMonitor;
import monitoring.impl.translators.OfflineTranslator;

import org.xml.sax.SAXException;

import structure.impl.other.Verdict;
import structure.intf.QEA;

public class Qea {

	static properties.competition.QEAOffline qc = new properties.competition.QEAOffline();
	static properties.competition.translators.QEAOfflineTranslators tm = new properties.competition.translators.QEAOfflineTranslators();

	public static void main(String[] args) throws IOException,
			ParserConfigurationException, SAXException {


		runOne();
		// runTwo();
		 runThree();
		//runFour();
		// runFive();
	}

	public static void runOne() throws IOException {

		long startTime = System.currentTimeMillis();
		// String trace = "/Users/giles/git/csrv14/OFFLINE/Team8/Bench1/ExistsLeader.csv";
		String trace = "traces/ExistsLeader.trace";
		QEA qea = qc.makeOne();
		OfflineTranslator t = tm.makeOne();
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, t);
		System.err.println("Running with " + fm.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();

		Verdict v = fm.monitor();
		System.err.println("QEA 1: Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : " + (endTime - startTime));
	}

	public static void runTwo() throws IOException {

		long startTime = System.currentTimeMillis();
		//String trace = "/Users/giles/git/csrv14/OFFLINE/Team8/Bench2/GrantCancel.csv";
		String trace = "traces/GrantCancel.csv";
		QEA qea = qc.makeTwo();
		OfflineTranslator t = tm.makeTwo();
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, t);
		System.err.println("Running with " + fm.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();


		Verdict v = fm.monitor();
		System.err.println("QEA 2: Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : " + (endTime - startTime));
	}

	public static void runThree() throws IOException {

		long startTime = System.currentTimeMillis();
		//String trace = "/Users/giles/git/csrv14/OFFLINE/Team8/Bench3/NestedCommands.csv";
		String trace = "traces/NestedCommands.csv";
		QEA qea = qc.makeThree();
		OfflineTranslator t = tm.makeThree();
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, t);
		System.err.println("Running with " + fm.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();

		Verdict v = fm.monitor();
		System.err.println("QEA 3: Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : " + (endTime - startTime));
	}

	public static void runFour() throws IOException {

		long startTime = System.currentTimeMillis();
		//String trace = "/Users/giles/git/csrv14/OFFLINE/Team8/Bench4/ResourceLifecycle.csv";
		String trace = "traces/ResourceLifecycle.csv";
		QEA qea = qc.makeFour();
		OfflineTranslator t = tm.makeFour();
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, t);
		System.err.println("Running with " + fm.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();

		Verdict v = fm.monitor();
		System.err.println("QEA 4: Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : " + (endTime - startTime));
	}

	public static void runFive() throws IOException {

		long startTime = System.currentTimeMillis();
		//String trace = "/Users/giles/git/csrv14/OFFLINE/Team8/Bench5/RespectConflicts.csv";
		String trace = "traces/RespectConflicts.csv";
		QEA qea = qc.makeFive();
		OfflineTranslator t = tm.makeFive();
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, t);
		System.err.println("Running with " + fm.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();

		Verdict v = fm.monitor();
		System.err.println("QEA 5: Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : " + (endTime - startTime));
	}

}
