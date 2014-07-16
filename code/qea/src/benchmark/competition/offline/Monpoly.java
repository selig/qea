package benchmark.competition.offline;
import java.io.IOException;

import monitoring.impl.CSVFileMonitor;
import monitoring.impl.MonitorFactory;
import monitoring.impl.translators.OfflineTranslator;
import structure.impl.other.Verdict;
import structure.intf.QEA;

public class Monpoly {

	public static void main(String[] args) throws IOException{
		runFour();
	}
	
	public static void runOne() throws IOException{
		String trace = "/Users/giles/git/csrv14/OFFLINE/Team4/Bench1/trace.csv";		
		QEA qea = (new properties.competition.MonPoly()).makeOne();
		System.err.println("Running with "+MonitorFactory.create(qea).getClass());
		OfflineTranslator t = (new properties.competition.translators.MonPolyTranslators()).makeOne();
		CSVFileMonitor m = new CSVFileMonitor(trace,qea,t);
		Verdict v = m.monitor();
		System.err.println("Verdict for one was "+v);
	}

	public static void runFour() throws IOException{
		String trace = "/Users/giles/git/csrv14/OFFLINE/Team4/Bench4/trace.csv";		
		QEA qea = (new properties.competition.MonPoly()).makeFour();
		System.err.println("Running with "+MonitorFactory.create(qea).getClass());
		OfflineTranslator t = (new properties.competition.translators.MonPolyTranslators()).makeFour();
		CSVFileMonitor m = new CSVFileMonitor(trace,qea,t);
		Verdict v = m.monitor();
		System.err.println("Verdict for four was "+v);
	}	
	
}
