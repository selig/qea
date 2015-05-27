package qea.benchmark.crv15;

import static qea.monitoring.impl.translators.TranslatorFactory.*;
import static qea.monitoring.impl.translators.TranslatorFactory.PType.*;

import java.io.IOException;

import qea.monitoring.impl.CSVFileMonitor;
import qea.monitoring.impl.translators.DefaultTranslator;
import qea.monitoring.impl.translators.OfflineTranslator;
import qea.monitoring.impl.translators.TranslatorFactory;
import qea.properties.crv15.offline.OCLR_3;
import qea.properties.crv15.offline.RVMonitor_4;
import qea.structure.impl.other.Verdict;
import qea.structure.intf.QEA;

public class OtherRun {

	public static void main(String[] args) throws IOException{
		
		//run_oclr();
		run_rvmonitor_5();
		
	}
	
	public static void run_oclr() throws IOException{
		//String trace = "/Users/giles/git/crv15_local/Offline/OCLR/bm1_1000000_globally.csv";
		String trace = "/Users/giles/git/crv15_local/Offline/OCLR/bm5_1000000_between.csv";
		//QEA qea = OCLR_3.make_one();
		QEA qea = OCLR_3.make_five();
		OfflineTranslator t = TranslatorFactory.makeParsingTranslatorWithIgnore(event("a",INT),event("b",INT),event("c",INT));
		//OfflineTranslator t = TranslatorFactory.makeSelectingDefaultTranslator(event("a"),event("b"),event("c"));
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, t);
		fm.ignoreHeader();
		Verdict v = fm.monitor();
		System.out.println("Verdict :"+v);
	}
	public static void run_rvmonitor_1() throws IOException{
		String trace = "/Users/giles/git/crv15_local/Offline/RVMonitor/Bench1/valid1.csv";
		QEA qea = RVMonitor_4.make_one();
		OfflineTranslator translator = TranslatorFactory.makeSelectingDefaultTranslator(
				event("createColl",param(0,OBJ),param(1,OBJ)),
				event("createIter",param(1,OBJ),param(2,OBJ)),
				event("useIter",param(2,OBJ)),
				event("updateMap",param(0,OBJ)));
		
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, translator);
		fm.ignoreHeader();
		Verdict v = fm.monitor();
		System.out.println("Verdict :"+v);		
	}
	public static void run_rvmonitor_2() throws IOException{
		String trace = "/Users/giles/git/crv15_local/Offline/RVMonitor/Bench2/valid3.csv";
		QEA qea = RVMonitor_4.make_two();
		OfflineTranslator translator = TranslatorFactory.makeDefaultTranslator("open","write","close");		
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, translator);
		fm.ignoreHeader();
		Verdict v = fm.monitor();
		System.out.println("Verdict :"+v);		
	}	
	public static void run_rvmonitor_3() throws IOException{
		String trace = "/Users/giles/git/crv15_local/Offline/RVMonitor/Bench3/valid3.csv";
		QEA qea = RVMonitor_4.make_three();
		OfflineTranslator translator = TranslatorFactory.makeParsingTranslatorWithIgnore(
				event("hasNextTrue",OBJ,BOOL),event("next",OBJ));	
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, translator);
		fm.ignoreHeader();
		Verdict v = fm.monitor();
		System.out.println("Verdict :"+v);		
	}	
	public static void run_rvmonitor_4() throws IOException{
		String trace = "/Users/giles/git/crv15_local/Offline/RVMonitor/Bench4/invalid3.csv";
		QEA qea = RVMonitor_4.make_four();
		OfflineTranslator translator = TranslatorFactory.makeDefaultTranslator("a","b","c","done");	
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, translator);
		fm.ignoreHeader();
		Verdict v = fm.monitor();
		System.out.println("Verdict :"+v);		
	}	
	public static void run_rvmonitor_5() throws IOException{
		String trace = "/Users/giles/git/crv15_local/Offline/RVMonitor/Bench5/valid3.csv";
		QEA qea = RVMonitor_4.make_five();
		OfflineTranslator translator = TranslatorFactory.makeSelectingDefaultTranslator(
				event("acquire",OBJ,OBJ),
				event("release",OBJ,OBJ),
				event("begin",param(1,OBJ)),
				event("end",param(1,OBJ)));	
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, translator);
		fm.ignoreHeader();
		Verdict v = fm.monitor();
		System.out.println("Verdict :"+v);		
	}		
}
