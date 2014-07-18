package benchmark.competition.offline;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import monitoring.impl.MonitorFactory;
import monitoring.impl.XMLFileMonitorSAX;
import monitoring.impl.translators.OfflineTranslator;

import org.xml.sax.SAXException;

import structure.impl.other.Verdict;
import structure.intf.QEA;
import exceptions.XMLFailureException;

public class Soloist {

	static String[] traces = new String[]{
		"/Users/giles/git/csrv14/OFFLINE/Team1/Bench1/B1.xml",
		"/Users/giles/git/csrv14/OFFLINE/Team1/Bench2/B2.xml",
		"/Users/giles/git/csrv14/OFFLINE/Team1/Bench3/B3.xml",
		"/Users/giles/git/csrv14/OFFLINE/Team1/Bench4/B4.xml",
		"/Users/giles/git/csrv14/OFFLINE/Team1/Bench5/B5.xml"
	};
	
	static properties.competition.Soloist s = new properties.competition.Soloist();
	static properties.competition.translators.SoloistTranslators tm = new properties.competition.translators.SoloistTranslators();

	public static void main(String[] args) throws IOException,
			ParserConfigurationException, SAXException {

		// runOne();
		 runTwo();
		// runThree();
		//runFour();

	}

	public static void runOne() throws IOException,
			ParserConfigurationException, SAXException {

		long startTime = System.currentTimeMillis();
		QEA qea = s.makeOneNegatedLimitN();
		OfflineTranslator t = tm.makeOne();
		System.err.println("Using monitor "
				+ MonitorFactory.create(qea).getClass());
		long beforeMonitoring = System.currentTimeMillis();
		// traces = new String[] { traces[0] };

		for (int i = 0; i < traces.length; i++) {
			String trace = traces[i];
			XMLFileMonitorSAX fm = new XMLFileMonitorSAX(trace, qea, t);
			try {
				Verdict v = fm.monitor();
				System.err.println("Soloist 1 - Trace " + (i + 1)
						+ ": Verdict was " + v);
				long endTime = System.currentTimeMillis();
				System.err.println(">>Execution time without creation: "
						+ (endTime - beforeMonitoring));
				System.err.println(">>Total execution time : "
						+ (endTime - startTime));
			} catch (XMLFailureException e) {
				System.err.println("Soloist 1 - Trace " + (i + 1) + ": "
						+ e.getMessage());
				long endTime = System.currentTimeMillis();
				System.err.println(">>Execution time without creation: "
						+ (endTime - beforeMonitoring));
				System.err.println(">>Total execution time : "
						+ (endTime - startTime));
			}
		}
	}

	public static void runTwo() throws IOException,
			ParserConfigurationException, SAXException {

		long startTime = System.currentTimeMillis();
		QEA qea = s.makeTwo();
		OfflineTranslator t = tm.makeTwo();
		System.err.println("Using monitor "
				+ MonitorFactory.create(qea).getClass());
		long beforeMonitoring = System.currentTimeMillis();
		 traces = new String[] { traces[2] };

		for (int i = 0; i < traces.length; i++) {
			String trace = traces[i];
			XMLFileMonitorSAX fm = new XMLFileMonitorSAX(trace, qea, t);
			try {
				Verdict v = fm.monitor();
				System.err.println("Soloist 2 - Trace " + (i + 1)
						+ ": Verdict was " + v);
				long endTime = System.currentTimeMillis();
				System.err.println(">>Execution time without creation: "
						+ (endTime - beforeMonitoring));
				System.err.println(">>Total execution time : "
						+ (endTime - startTime));
			} catch (XMLFailureException e) {
				System.err.println("Soloist 2 - Trace " + (i + 1) + ": "
						+ e.getMessage());
				long endTime = System.currentTimeMillis();
				System.err.println(">>Execution time without creation: "
						+ (endTime - beforeMonitoring));
				System.err.println(">>Total execution time : "
						+ (endTime - startTime));
			}
		}
	}

	public static void runThree() throws IOException,
			ParserConfigurationException, SAXException {

		long startTime = System.currentTimeMillis();
		QEA qea = s.makeThree();
		OfflineTranslator t = tm.makeThree();
		System.err.println("Using monitor "
				+ MonitorFactory.create(qea).getClass());
		long beforeMonitoring = System.currentTimeMillis();
		// traces = new String[] { traces[0] };

		for (int i = 0; i < traces.length; i++) {
			String trace = traces[i];
			XMLFileMonitorSAX fm = new XMLFileMonitorSAX(trace, qea, t);
			try {
				Verdict v = fm.monitor();
				System.err.println("Soloist 3 - Trace " + (i + 1)
						+ ": Verdict was " + v);
				long endTime = System.currentTimeMillis();
				System.err.println(">>Execution time without creation: "
						+ (endTime - beforeMonitoring));
				System.err.println(">>Total execution time : "
						+ (endTime - startTime));
			} catch (XMLFailureException e) {
				System.err.println("Soloist 3 - Trace " + (i + 1) + ": "
						+ e.getMessage());
				long endTime = System.currentTimeMillis();
				System.err.println(">>Execution time without creation: "
						+ (endTime - beforeMonitoring));
				System.err.println(">>Total execution time : "
						+ (endTime - startTime));
			}
		}
	}

	public static void runFour() throws IOException,
			ParserConfigurationException, SAXException {

		long startTime = System.currentTimeMillis();
		QEA qea = s.makeFour();
		OfflineTranslator t = tm.makeFour();
		System.err.println("Using monitor "
				+ MonitorFactory.create(qea).getClass());
		long beforeMonitoring = System.currentTimeMillis();
		// traces = new String[] { traces[0] };

		for (int i = 0; i < traces.length; i++) {
			String trace = traces[i];
			XMLFileMonitorSAX fm = new XMLFileMonitorSAX(trace, qea, t);
			try {
				Verdict v = fm.monitor();
				System.err.println("Soloist 4 - Trace " + (i + 1)
						+ ": Verdict was " + v);
				long endTime = System.currentTimeMillis();
				System.err.println(">>Execution time without creation: "
						+ (endTime - beforeMonitoring));
				System.err.println(">>Total execution time : "
						+ (endTime - startTime));
			} catch (XMLFailureException e) {
				System.err.println("Soloist 4 - Trace " + (i + 1) + ": "
						+ e.getMessage());
				long endTime = System.currentTimeMillis();
				System.err.println(">>Execution time without creation: "
						+ (endTime - beforeMonitoring));
				System.err.println(">>Total execution time : "
						+ (endTime - startTime));
			}
		}
	}
}
