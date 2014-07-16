package benchmark.competition.offline;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import monitoring.impl.MonitorFactory;
import monitoring.impl.XMLFileMonitorSAX;
import monitoring.impl.translators.OfflineTranslator;
import monitoring.intf.Monitor;

import org.xml.sax.SAXException;

import structure.impl.other.Verdict;
import structure.intf.QEA;

public class Soloist {

	static String trace_one = "/Users/giles/git/csrv14/OFFLINE/Team1/Bench1/B1.xml";
	
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException{
		
		properties.competition.Soloist s = new properties.competition.Soloist();
		properties.competition.translators.SoloistTranslators tm = new properties.competition.translators.SoloistTranslators();
		
		QEA qea_one = s.makeTwo();
		System.err.println("Using monitor "+MonitorFactory.create(qea_one).getClass());
		
		OfflineTranslator t = tm.makeTwo();
		
		XMLFileMonitorSAX fm = new XMLFileMonitorSAX(trace_one, qea_one, t);
		
		Verdict v = fm.monitor();
		
		System.err.println("Verdict was " + v);
		
	}
	
}
