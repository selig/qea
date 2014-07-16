package benchmark.competition.offline;

import java.io.FileNotFoundException;
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

	static String[] traces = new String[]{
		"/Users/giles/git/csrv14/OFFLINE/Team1/Bench1/B1.xml",
		"/Users/giles/git/csrv14/OFFLINE/Team1/Bench2/B2.xml",
		"/Users/giles/git/csrv14/OFFLINE/Team1/Bench3/B3.xml",
		"/Users/giles/git/csrv14/OFFLINE/Team1/Bench4/B4.xml",
		"/Users/giles/git/csrv14/OFFLINE/Team1/Bench5/B5.xml"
	};
	
	static properties.competition.Soloist s = new properties.competition.Soloist();
	static properties.competition.translators.SoloistTranslators tm = new properties.competition.translators.SoloistTranslators();
	
	
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException{
		
		runTwo();

		
	}
	
	public static void runTwo() throws IOException, ParserConfigurationException, SAXException{

		QEA qea_one = s.makeTwo();
		System.err.println("Using monitor "+MonitorFactory.create(qea_one).getClass());		
				
		traces = new String[]{ traces[2] };
		
		for(int i=0;i<traces.length;i++){
			String trace = traces[i];
			OfflineTranslator t = tm.makeTwo();
			XMLFileMonitorSAX fm = new XMLFileMonitorSAX(trace, qea_one, t);		
			Verdict v = fm.monitor();		
			System.err.println("Verdict was " + v+" for trace "+(i+1));
		}		
		
	}
	
}
