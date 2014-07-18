package benchmark.competition.offline;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import monitoring.impl.CSVFileMonitor;
import monitoring.impl.MonitorFactory;
import monitoring.impl.XMLFileMonitorSAX;
import monitoring.impl.translators.OfflineTranslator;

import org.xml.sax.SAXException;

import structure.impl.other.Verdict;
import structure.intf.QEA;
import exceptions.XMLFailureException;

public class Qea {

	
	static properties.competition.QEAOffline qc = new properties.competition.QEAOffline();
	static properties.competition.translators.QEAOfflineTranslators tm = new properties.competition.translators.QEAOfflineTranslators();

	public static void main(String[] args) throws IOException,
			ParserConfigurationException, SAXException {

		 runOne();
		// runTwo();
		// runThree();
		//runFour();

	}

	public static void runOne() throws IOException {

		long startTime = System.currentTimeMillis();
		QEA qea = qc.makeOne();
		OfflineTranslator t = tm.makeOne();
		System.err.println("Using monitor "
				+ MonitorFactory.create(qea).getClass());
		long beforeMonitoring = System.currentTimeMillis();
		
		//String trace = "/Users/giles/git/csrv14/OFFLINE/Team8/Bench1/ExistsLeader.csv";
		//String trace = "/Users/giles/git/csrv14/OFFLINE/Team8/Bench1/test_trace.csv";
		String trace = "traces/ExistsLeader.trace";

		CSVFileMonitor fm = new CSVFileMonitor(trace,qea,t);

		Verdict v = fm.monitor();
		System.err.println("QEA trace 1: Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : "
				+ (endTime - startTime));
	}

	public static void runTwo() throws IOException {

		long startTime = System.currentTimeMillis();
		QEA qea = qc.makeTwo();
		OfflineTranslator t = tm.makeTwo();
		System.err.println("Using monitor "
				+ MonitorFactory.create(qea).getClass());
		long beforeMonitoring = System.currentTimeMillis();
		String trace = "/Users/giles/git/csrv14/OFFLINE/Team8/Bench2/GrantCancel.csv";

		CSVFileMonitor fm = new CSVFileMonitor(trace,qea,t);

		Verdict v = fm.monitor();
		System.err.println("QEA trace 2: Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : "
				+ (endTime - startTime));
	}

	public static void runThree() throws IOException {

		long startTime = System.currentTimeMillis();
		QEA qea = qc.makeThree();
		OfflineTranslator t = tm.makeThree();
		System.err.println("Using monitor "
				+ MonitorFactory.create(qea).getClass());
		long beforeMonitoring = System.currentTimeMillis();

		String trace = "/Users/giles/git/csrv14/OFFLINE/Team8/Bench3/NestedCommands.csv";

		CSVFileMonitor fm = new CSVFileMonitor(trace,qea,t);

		Verdict v = fm.monitor();
		System.err.println("QEA trace 3: Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : "
				+ (endTime - startTime));
	}

	public static void runFour() throws IOException {

		long startTime = System.currentTimeMillis();
		QEA qea = qc.makeFour();
		OfflineTranslator t = tm.makeFour();
		System.err.println("Using monitor "
				+ MonitorFactory.create(qea).getClass());
		long beforeMonitoring = System.currentTimeMillis();

		String trace = "/Users/giles/git/csrv14/OFFLINE/Team8/Bench4/ResourceLifecycle.csv";

		CSVFileMonitor fm = new CSVFileMonitor(trace,qea,t);

		Verdict v = fm.monitor();
		System.err.println("QEA trace 4: Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : "
				+ (endTime - startTime));
	}
	
	public static void runFive() throws IOException {

		long startTime = System.currentTimeMillis();
		QEA qea = qc.makeFive();
		OfflineTranslator t = tm.makeFive();
		System.err.println("Using monitor "
				+ MonitorFactory.create(qea).getClass());
		long beforeMonitoring = System.currentTimeMillis();

		String trace = "/Users/giles/git/csrv14/OFFLINE/Team8/Bench4/RespectConflicts.csv";

		CSVFileMonitor fm = new CSVFileMonitor(trace,qea,t);

		Verdict v = fm.monitor();
		System.err.println("QEA trace 5: Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : "
				+ (endTime - startTime));
	}	
	
}
