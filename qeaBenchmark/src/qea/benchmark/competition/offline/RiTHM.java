package qea.benchmark.competition.offline;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import qea.monitoring.impl.XMLFileMonitorSAX;
import qea.monitoring.impl.translators.OfflineTranslator;

import org.xml.sax.SAXException;

import qea.properties.competition.translators.RiTHMTranslators;
import qea.structure.impl.other.Verdict;
import qea.structure.intf.QEA;
import qea.exceptions.XMLFailureException;

public class RiTHM {

	static qea.properties.competition.RiTHM r = new qea.properties.competition.RiTHM();
	static RiTHMTranslators tm = new RiTHMTranslators();

	public static void main(String[] args) throws IOException,
			ParserConfigurationException, SAXException {
		// runOne();
		// runTwo();
		// runThree();
		// runFour();
		runFive();
	}

	public static void runOne() throws IOException,
			ParserConfigurationException, SAXException {

		String trace = "/home/mbaxkhc3/git/csrv14/OFFLINE/Team3/Bench1/Debug/home1_dropbox.xml";
		long startTime = System.currentTimeMillis();
		QEA qea = r.makeOne();
		OfflineTranslator t = tm.makeOne();
		XMLFileMonitorSAX m = new XMLFileMonitorSAX(trace, qea, t);
		System.err.println("Running with " + m.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();

		try {
			Verdict v = m.monitor();
			System.err.println("RiTHM 1: Verdict was " + v);
			long endTime = System.currentTimeMillis();
			System.err.println(">>Execution time without creation: "
					+ (endTime - beforeMonitoring));
			System.err.println(">>Total execution time : "
					+ (endTime - startTime));
		} catch (XMLFailureException e) {
			System.err.println("RiTHM 1: " + e.getMessage());
			long endTime = System.currentTimeMillis();
			System.err.println(">>Execution time without creation: "
					+ (endTime - beforeMonitoring));
			System.err.println(">>Total execution time : "
					+ (endTime - startTime));
		}
	}

	public static void runTwo() throws IOException,
			ParserConfigurationException, SAXException {

		String trace = "/home/mbaxkhc3/git/csrv14/OFFLINE/Team3/Bench1/Debug/home1_dropbox.xml";
		long startTime = System.currentTimeMillis();
		QEA qea = r.makeTwo();
		OfflineTranslator t = tm.makeTwo();
		XMLFileMonitorSAX m = new XMLFileMonitorSAX(trace, qea, t);
		System.err.println("Running with " + m.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();

		try {
			Verdict v = m.monitor();
			System.err.println("RiTHM 2: Verdict was " + v);
			long endTime = System.currentTimeMillis();
			System.err.println(">>Execution time without creation: "
					+ (endTime - beforeMonitoring));
			System.err.println(">>Total execution time : "
					+ (endTime - startTime));
		} catch (XMLFailureException e) {
			System.err.println("RiTHM 2: " + e.getMessage());
			long endTime = System.currentTimeMillis();
			System.err.println(">>Execution time without creation: "
					+ (endTime - beforeMonitoring));
			System.err.println(">>Total execution time : "
					+ (endTime - startTime));
		}
	}

	public static void runThree() throws IOException,
			ParserConfigurationException, SAXException {

		String trace = "/home/mbaxkhc3/git/csrv14/OFFLINE/Team3/Bench3/Debug/flows.0607.xml";
		long startTime = System.currentTimeMillis();
		QEA qea = r.makeThree();
		OfflineTranslator t = tm.makeThree();
		XMLFileMonitorSAX m = new XMLFileMonitorSAX(trace, qea, t);
		System.err.println("Running with " + m.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();

		try {
			Verdict v = m.monitor();
			System.err.println("RiTHM 3: Verdict was " + v);
			long endTime = System.currentTimeMillis();
			System.err.println(">>Execution time without creation: "
					+ (endTime - beforeMonitoring));
			System.err.println(">>Total execution time : "
					+ (endTime - startTime));
		} catch (XMLFailureException e) {
			System.err.println("RiTHM 3: " + e.getMessage());
			long endTime = System.currentTimeMillis();
			System.err.println(">>Execution time without creation: "
					+ (endTime - beforeMonitoring));
			System.err.println(">>Total execution time : "
					+ (endTime - startTime));
		}
	}

	public static void runFour() throws IOException,
			ParserConfigurationException, SAXException {

		String trace = "/home/mbaxkhc3/git/csrv14/OFFLINE/Team3/Bench3/Debug/flows.0607.xml";
		long startTime = System.currentTimeMillis();
		QEA qea = r.makeFour();
		OfflineTranslator t = tm.makeFour();
		XMLFileMonitorSAX m = new XMLFileMonitorSAX(trace, qea, t);
		System.err.println("Running with " + m.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();

		try {
			Verdict v = m.monitor();
			System.err.println("RiTHM 4: Verdict was " + v);
			long endTime = System.currentTimeMillis();
			System.err.println(">>Execution time without creation: "
					+ (endTime - beforeMonitoring));
			System.err.println(">>Total execution time : "
					+ (endTime - startTime));
		} catch (XMLFailureException e) {
			System.err.println("RiTHM 4: " + e.getMessage());
			long endTime = System.currentTimeMillis();
			System.err.println(">>Execution time without creation: "
					+ (endTime - beforeMonitoring));
			System.err.println(">>Total execution time : "
					+ (endTime - startTime));
		}
	}

	public static void runFive() throws IOException,
			ParserConfigurationException, SAXException {

		String trace = "/home/mbaxkhc3/git/csrv14/OFFLINE/Team3/Bench5/Debug/strace_xml.mxl";
		long startTime = System.currentTimeMillis();
		QEA qea = r.makeFive();
		OfflineTranslator t = tm.makeFive();
		XMLFileMonitorSAX m = new XMLFileMonitorSAX(trace, qea, t);
		System.err.println("Running with " + m.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();

		try {
			Verdict v = m.monitor();
			System.err.println("RiTHM 5: Verdict was " + v);
			long endTime = System.currentTimeMillis();
			System.err.println(">>Execution time without creation: "
					+ (endTime - beforeMonitoring));
			System.err.println(">>Total execution time : "
					+ (endTime - startTime));
		} catch (XMLFailureException e) {
			System.err.println("RiTHM 5: " + e.getMessage());
			long endTime = System.currentTimeMillis();
			System.err.println(">>Execution time without creation: "
					+ (endTime - beforeMonitoring));
			System.err.println(">>Total execution time : "
					+ (endTime - startTime));
		}
	}
}
