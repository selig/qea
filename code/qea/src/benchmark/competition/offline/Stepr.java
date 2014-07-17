package benchmark.competition.offline;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import monitoring.impl.MonitorFactory;
import monitoring.impl.XMLFileMonitorSAX;
import monitoring.impl.translators.OfflineTranslator;

import org.xml.sax.SAXException;

import structure.impl.other.Verdict;
import structure.intf.QEA;

public class Stepr {

	public static void main(String[] args) throws IOException,
			ParserConfigurationException, SAXException {
		// runOne();
		// runTwo();
		runThree();
		// runFour();
	}

	static String trace = "traces/Team6/log.xml";

	public static void runOne() throws IOException,
			ParserConfigurationException, SAXException {
		QEA qea = (new properties.competition.Stepr()).makeOne();
		System.err.println("Running with "
				+ MonitorFactory.create(qea).getClass());
		OfflineTranslator t = (new properties.competition.translators.SteprTranslators())
				.makeOne();
		XMLFileMonitorSAX m = new XMLFileMonitorSAX(trace, qea, t);
		Verdict v = m.monitor();
		System.err.println("Verdict for one was " + v);
	}

	public static void runTwo() throws IOException,
			ParserConfigurationException, SAXException {
		QEA qea = (new properties.competition.Stepr()).makeTwo();
		System.err.println("Running with "
				+ MonitorFactory.create(qea).getClass());
		OfflineTranslator t = (new properties.competition.translators.SteprTranslators())
				.makeTwo();
		XMLFileMonitorSAX m = new XMLFileMonitorSAX(trace, qea, t);
		Verdict v = m.monitor();
		System.err.println("Verdict for two was " + v);
	}

	public static void runThree() throws IOException,
			ParserConfigurationException, SAXException {
		QEA qea = (new properties.competition.Stepr()).makeThree();
		System.err.println("Running with "
				+ MonitorFactory.create(qea).getClass());
		OfflineTranslator t = (new properties.competition.translators.SteprTranslators())
				.makeThree();
		XMLFileMonitorSAX m = new XMLFileMonitorSAX(trace, qea, t);
		Verdict v = m.monitor();
		System.err.println("Verdict for three was " + v);
	}

	public static void runFour() throws IOException,
			ParserConfigurationException, SAXException {
		QEA qea = (new properties.competition.Stepr()).makeFour();
		System.err.println("Running with "
				+ MonitorFactory.create(qea).getClass());
		OfflineTranslator t = (new properties.competition.translators.SteprTranslators())
				.makeFour();
		XMLFileMonitorSAX m = new XMLFileMonitorSAX(trace, qea, t);
		Verdict v = m.monitor();
		System.err.println("Verdict for four was " + v);
	}

}
