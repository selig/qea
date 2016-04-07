package qea.benchmark.crv15;

import static qea.monitoring.impl.translators.TranslatorFactory.event;
import static qea.monitoring.impl.translators.TranslatorFactory.param;
import static qea.monitoring.impl.translators.TranslatorFactory.PType.*;

import java.io.IOException;

import qea.creation.QEABuilder;
import qea.monitoring.impl.CSVFileMonitor;
import qea.monitoring.impl.translators.OfflineTranslator;
import qea.monitoring.impl.translators.TranslatorFactory;
import qea.properties.crv15.offline.LogFire_8;
import qea.properties.crv15.offline.OCLR_3;
import qea.properties.crv15.offline.RVMonitor_4;
import qea.structure.impl.other.Verdict;
import qea.structure.intf.QEA;
import static qea.structure.intf.Guard.*;

public class OtherRun {

	public static void main(String[] args) throws IOException{
		
		//run_oclr();
		//run_rvmonitor_1();
		//run_logfire_4();
		
		run_breach_2_and_3();
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
		String trace = "/Users/giles/git/crv15_local/DataMill/Offline/traces/rvmonitor/trace1_invalid.csv";
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
		//String trace = "/Users/giles/git/crv15_local/Offline/RVMonitor/Benchmark5/trace5_invalid.csv";
		String trace = "/Users/giles/git/crv15_local/Java/3_Java-MOP/Benchmark1/out";
		QEA qea = RVMonitor_4.make_five();
		OfflineTranslator translator = TranslatorFactory.makeSelectingDefaultTranslator(
				event("acquire",OBJ,OBJ),
				event("release",OBJ,OBJ),
				event("begin",param(1,OBJ)),
				event("end",param(1,OBJ)));	
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, translator);
		System.err.println("Running with " + fm.getMonitorClass());
		//fm.ignoreHeader();
		Verdict v = fm.monitor();
		System.out.println("Verdict :"+v);		
	}
	public static void run_logfire_4() throws IOException{
		String trace = "/Users/giles/git/crv15_local/Offline/8_LogFire/Benchmark4/log4.csv";
		QEA qea = LogFire_8.make_four();
		OfflineTranslator translator = TranslatorFactory.makeSelectingDefaultTranslator(
                event("conflict",param(0,OBJ),param(1,OBJ)),
                event("grant",param(1,OBJ)),
                event("release",param(1,OBJ)));
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, translator);
		fm.ignoreHeader();
		Verdict v = fm.monitor();
		System.out.println("Verdict :"+v);	
		System.out.println(translator.getMonitor());
	}		
	
	public static void run_breach_1() throws IOException {
		String trace = "/Users/giles/git/crv15_local/DataMill/Offline/traces/breach/simple_false.csv";
		OfflineTranslator translator = TranslatorFactory.makeSelectingParsingTranslatorWithSingleEvent(
				event("z",param(0,DOUBLE),param(3,DOUBLE)));
        QEA qea = make_offline_seven_one();
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, translator);
		fm.ignoreHeader();
		Verdict v = fm.monitor();
		System.out.println("Verdict :"+v);	
		System.out.println(translator.getMonitor());        
	}
    public static QEA make_offline_seven_one(){
        QEABuilder b = new QEABuilder("breachOne");
        //Event
        int z = 1;
        // Free variables
        int time = 1; int value = 2;

        b.addTransition(1,z,new int[]{time,value}, isLessThanConstant(value,0.3),1);
        b.addTransition(1,z,new int[]{time,value}, isGreaterThanConstant(time,50.0),2);

        b.addFinalStates(1,2);
        return b.make();
    }	
	public static void run_breach_2_and_3() throws IOException {
		String trace = "/Users/giles/git/crv15_local/DataMill/Offline/traces/breach/what_hill_false.csv";    
    OfflineTranslator translator = TranslatorFactory.makeSelectingParsingTranslatorWithSingleEvent(event("event",
            param(0,DOUBLE),param(21,INT),param(22,INT),param(17,DOUBLE),param(20,DOUBLE),param(18,DOUBLE),param(19,DOUBLE)));
       QEA qea = make_offline_seven_two();
       
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, translator);
		fm.ignoreHeader();
		translator.getMonitor().DEBUG=true;
		Verdict v = fm.monitor();
		System.out.println("Verdict :"+v);	
		System.out.println(translator.getMonitor());        
	}       

    public static QEA make_offline_seven_two(){
        QEABuilder b = new QEABuilder("breachTwo");
        //Events
        int e = 1;
        //States
        int start = 1;
        int error = 2;
        int leftUbad = 3;
        int success = 4;
        // Free variables
        int time = 1;
        int lws = 2;
        int rws = 3;
        int cliff_l = 4;
        int cliff_r = 5;
        int cliff_fl = 6;
        int cliff_fr = 7;

        b.addTransition(start,e,new int[]{time,lws,rws,cliff_l,cliff_r,cliff_fl,cliff_fr},
                        isGreaterThanConstant(time,100.0),error);
        b.addTransition(leftUbad,e,new int[]{time,lws,rws,cliff_l,cliff_r,cliff_fl,cliff_fr},
                        isGreaterThanConstant(time,50.0),error);

        b.addTransition(start,e,new int[]{time,lws,rws,cliff_l,cliff_r,cliff_fl,cliff_fr},
                        or(
                           isLessThanOrEqualToConstant(lws,10),
                           absoluteDifferenceGreaterThanOrEqualToVal(lws,rws,1)
                           ),
                        leftUbad);

        b.addTransition(start,e,new int[]{time,lws,rws,cliff_l,cliff_r,cliff_fl,cliff_fr},
                        or(
                           isGreaterThanConstant(cliff_l,0.5),
                           isGreaterThanConstant(cliff_r,0.5),
                           isGreaterThanConstant(cliff_fl,0.5),
                           isGreaterThanConstant(cliff_fr,0.5)
                           ),success);

        b.addTransition(leftUbad,e,new int[]{time,lws,rws,cliff_l,cliff_r,cliff_fl,cliff_fr},
                        or(
                           isGreaterThanConstant(cliff_l,0.5),
                           isGreaterThanConstant(cliff_r,0.5),
                           isGreaterThanConstant(cliff_fl,0.5),
                           isGreaterThanConstant(cliff_fr,0.5)
                           ),success);

        b.setAllSkipStates();
        b.addFinalStates(start,success);
        return b.make();
    }	
}
