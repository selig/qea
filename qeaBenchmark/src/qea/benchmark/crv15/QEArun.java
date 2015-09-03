package qea.benchmark.crv15;

import static qea.properties.crv15.c.QEA_C.makeQueue;
import static qea.properties.crv15.c.QEA_C.makeStack;
import static qea.properties.crv15.java.QEA_Java.makeExamSystem;
import static qea.properties.crv15.java.QEA_Java.makeGraphics;
import static qea.properties.crv15.java.QEA_Java.makeResource;
import static qea.properties.crv15.java.QEA_Java.makeSQLInjection;
import static qea.properties.crv15.java.QEA_Java.makeSingleGrant;
import static qea.properties.crv15.offline.QEA_Offline.makeAuctionBidding;
import static qea.properties.crv15.offline.QEA_Offline.makeCallReturn;
import static qea.properties.crv15.offline.QEA_Offline.makeCandidateSelection;
import static qea.properties.crv15.offline.QEA_Offline.makeClientManagers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import qea.monitoring.impl.CSVFileMonitor;
import qea.monitoring.impl.translators.DefaultTranslator;
import qea.monitoring.impl.translators.OfflineTranslator;
import qea.monitoring.impl.translators.TranslatorFactory;
import static qea.monitoring.impl.translators.TranslatorFactory.*;
import static qea.monitoring.impl.translators.TranslatorFactory.PType.*;
import qea.structure.impl.other.Verdict;
import qea.structure.intf.QEA;

public class QEArun{

	public static void main(String[] args) throws IOException,
			ParserConfigurationException, SAXException {
	
		//runGraphics();
		//runResource();
		//runAuctionBidding();
		//runSQLInjection();
		runClientManagers();
		//runExamSystem();
		//runCallReturn();
		
		//runStack();
		//runQueue();
	}
	
    private static OfflineTranslator makeCandidateSelectionTranslator(){
        return new OfflineTranslator() {

                @Override
                public Verdict translateAndStep(String eventName,
                                String[] paramNames, String[] paramValues) {
                	switch(eventName){
                	case "member" : return monitor.step(1,new Object[]{paramValues[0],paramValues[1]});
                	case "candidate" : return monitor.step(2,new Object[]{paramValues[0],paramValues[1]});
                	case "rank" : return monitor.step(3,new Object[]{paramValues[0],paramValues[1]});
                	default: return null;
                	}
                }

                @Override
                public Verdict translateAndStep(String eventName) {
                        // No event without parameters is relevant for this property
                        return null;
                }
        };
}	
    
    private static OfflineTranslator makeAuctionBiddingTranslator(){
        return new OfflineTranslator() {

        	
                @Override
                public Verdict translateAndStep(String eventName,
                                String[] paramNames, String[] paramValues) {
                	switch(eventName){
                	case "create_auction" : return monitor.step(1,new Object[]{paramValues[0],Integer.parseInt(paramValues[1]),Integer.parseInt(paramValues[2])});
                	case "bid" : return monitor.step(2,new Object[]{paramValues[0],Integer.parseInt(paramValues[1])});
                	case "sold" : return monitor.step(3,new Object[]{paramValues[0]});
                	default: return null;
                	}
                }

                @Override
                public Verdict translateAndStep(String eventName) {
                        if(eventName.equals("endOfDay")) return monitor.step(4);
                        return null;
                }
        };
}	
    private static OfflineTranslator makeQueueTranslator(){
        return new OfflineTranslator() {

        	private Map<String,Integer> imap = new HashMap<>();
        	
        	private Integer getInt(String s){
        		Integer i = imap.get(s);
        		if(i==null){
        			i = Integer.parseInt(s);
        			imap.put(s,i);
        		}
        		return i;
        	}
        	
                @Override
                public Verdict translateAndStep(String eventName,
                                String[] paramNames, String[] paramValues) {
                	switch(eventName){
                	case "insert" : return monitor.step(1,new Object[]{paramValues[0],getInt(paramValues[1])});
                	case "peek" : return monitor.step(2,new Object[]{paramValues[0],getInt(paramValues[1])});
                	
                	default: return null;
                	}
                }

                @Override
                public Verdict translateAndStep(String eventName) {
                        return null;
                }
        };
}
    
    private static OfflineTranslator makeExamSystemTranslator(){
        return new OfflineTranslator() {

        	private Map<String,Integer> imap = new HashMap<>();
        	
        	private Integer getInt(String s){
        		Integer i = imap.get(s);
        		if(i==null){
        			i = Integer.parseInt(s);
        			imap.put(s,i);
        			//System.err.println("Translate "+s+" to "+System.identityHashCode(i));
        		}
        		return i;
        	}
        	
                @Override
                public Verdict translateAndStep(String eventName,
                                String[] paramNames, String[] paramValues) {
                	switch(eventName){
                	case "submit" : return monitor.step(1,new Object[]{paramValues[0],paramValues[1]});
                	case "mark" : return monitor.step(2,new Object[]{paramValues[0],paramValues[1],paramValues[2],paramValues[3]});
                	
                	default: return null;
                	}
                }

                @Override
                public Verdict translateAndStep(String eventName) {
                        return null;
                }
        };
}	    
    
	
	public static void runCandidateSelection() throws IOException {
		
		long startTime = System.currentTimeMillis();
		String trace = "/Users/giles/git/crv15/Offline/cand_sel";
		QEA qea = makeCandidateSelection();
		OfflineTranslator t = makeCandidateSelectionTranslator(); 
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, t);
		System.err.println("Running with " + fm.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();
	
		Verdict v = fm.monitor();
		System.err.println("Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : " + (endTime - startTime));
	}	
	
public static void runClientManagers() throws IOException {
		
		long startTime = System.currentTimeMillis();
		String trace = "/Users/giles/git/crv15/Offline/client_manager_trace";
		QEA qea = makeClientManagers();
		OfflineTranslator t = new DefaultTranslator("createAccount","assign"); 
		t.setNaive();
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, t);
		System.err.println("Running with " + fm.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();
	
		Verdict v = fm.monitor();
		System.err.println("Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : " + (endTime - startTime));
		System.err.println(t.getMonitor());
	}	
		
	
	public static void runCallReturn() throws IOException {
	
		long startTime = System.currentTimeMillis();
		String trace = "/Users/giles/git/crv15_local/Offline/MarQ/Benchmark2/call_trace_invalid";
		QEA qea = makeCallReturn();
		OfflineTranslator t = new DefaultTranslator("call","return");
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, t);
		fm.ignoreHeader();
		System.err.println("Running with " + fm.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();
	
		Verdict v = fm.monitor();
		System.err.println("Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : " + (endTime - startTime));
	}
	
	public static void runAuctionBidding() throws IOException {
		
		long startTime = System.currentTimeMillis();
		String trace = "/Users/giles/git/crv15/Offline/auction_trace_invalid3";
		QEA qea = makeAuctionBidding();
		OfflineTranslator t = makeAuctionBiddingTranslator(); 
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, t);
		System.err.println("Running with " + fm.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();
	
		Verdict v = fm.monitor();
		System.err.println("Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : " + (endTime - startTime));
		System.err.println(t.getMonitor());
	}	
	
	public static void runSQLInjection() throws IOException {
		
		long startTime = System.currentTimeMillis();
		String trace = "/Users/giles/git/crv15/Java/sql/trace";
		QEA qea = makeSQLInjection();
		OfflineTranslator t = new DefaultTranslator("input","derive","sanitise","use");
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, t);
		System.err.println("Running with " + fm.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();
	
		Verdict v = fm.monitor();
		System.err.println("Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : " + (endTime - startTime));
		System.err.println(t.getMonitor());
	}	
	
	public static void runResource() throws IOException {
		
		long startTime = System.currentTimeMillis();
		String trace = "/Users/giles/git/crv15/Java/red_elim/trace";
		QEA qea = makeResource();
		OfflineTranslator t = new DefaultTranslator("request","grant","deny","release");
		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, t);
		System.err.println("Running with " + fm.getMonitorClass());
		long beforeMonitoring = System.currentTimeMillis();
	
		Verdict v = fm.monitor();
		System.err.println("Verdict was " + v);
		long endTime = System.currentTimeMillis();
		System.err.println(">>Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">>Total execution time : " + (endTime - startTime));
		System.err.println(t.getMonitor());
	}	
	
	
public static void runSingleGrant() throws IOException {	
	long startTime = System.currentTimeMillis();
	String trace = "/Users/giles/git/crv15/Java/red_elim/trace";
	QEA qea = makeSingleGrant();
	OfflineTranslator t = new DefaultTranslator("grant","release");
	CSVFileMonitor fm = new CSVFileMonitor(trace, qea, t);
	System.err.println("Running with " + fm.getMonitorClass());
	long beforeMonitoring = System.currentTimeMillis();

	Verdict v = fm.monitor();
	System.err.println("Verdict was " + v);
	long endTime = System.currentTimeMillis();
	System.err.println(">>Execution time without creation: "
			+ (endTime - beforeMonitoring));
	System.err.println(">>Total execution time : " + (endTime - startTime));
	System.err.println(t.getMonitor());
}		

public static void runGraphics() throws IOException {	
	long startTime = System.currentTimeMillis();
	String trace = "/Users/giles/git/crv15/Java/sem_id/graphics_trace";
	QEA qea = makeGraphics();
	OfflineTranslator t = new DefaultTranslator("add");
	CSVFileMonitor fm = new CSVFileMonitor(trace, qea, t);
	System.err.println("Running with " + fm.getMonitorClass());
	long beforeMonitoring = System.currentTimeMillis();

	Verdict v = fm.monitor();
	System.err.println("Verdict was " + v);
	long endTime = System.currentTimeMillis();
	System.err.println(">>Execution time without creation: "
			+ (endTime - beforeMonitoring));
	System.err.println(">>Total execution time : " + (endTime - startTime));
	System.err.println(t.getMonitor());
}		

public static void runStack() throws IOException {	
	long startTime = System.currentTimeMillis();
	String trace = "/Users/giles/git/crv15/C/minisat/simp/vec_trace";
	QEA qea = makeStack();
	OfflineTranslator t = new DefaultTranslator("push","pop");
	CSVFileMonitor fm = new CSVFileMonitor(trace, qea, t);
	System.err.println("Running with " + fm.getMonitorClass());
	long beforeMonitoring = System.currentTimeMillis();

	Verdict v = fm.monitor();
	System.err.println("Verdict was " + v);
	long endTime = System.currentTimeMillis();
	System.err.println(">>Execution time without creation: "
			+ (endTime - beforeMonitoring));
	System.err.println(">>Total execution time : " + (endTime - startTime));
	System.err.println(t.getMonitor());
}	

public static void runQueue() throws IOException {	
	long startTime = System.currentTimeMillis();
	String trace = "/Users/giles/git/crv15/C/minisat/simp/queue_trace";
	QEA qea = makeQueue();
	OfflineTranslator t = TranslatorFactory.makeParsingTranslator(
			event("insert",OBJ,QINT),
			event("peek",OBJ,QINT));
			
	CSVFileMonitor fm = new CSVFileMonitor(trace, qea, t);
	System.err.println("Running with " + fm.getMonitorClass());
	long beforeMonitoring = System.currentTimeMillis();

	Verdict v = fm.monitor();
	System.err.println("Verdict was " + v);
	long endTime = System.currentTimeMillis();
	System.err.println(">>Execution time without creation: "
			+ (endTime - beforeMonitoring));
	System.err.println(">>Total execution time : " + (endTime - startTime));
	System.err.println(t.getMonitor());
}	

public static void runExamSystem() throws IOException {	
	long startTime = System.currentTimeMillis();
	String trace = "/Users/giles/git/crv15/Java/exams/simple";
	QEA qea = makeExamSystem();
	OfflineTranslator t = makeExamSystemTranslator();
	//t.setNaive();
	CSVFileMonitor fm = new CSVFileMonitor(trace, qea, t);
	System.err.println("Running with " + fm.getMonitorClass());
	long beforeMonitoring = System.currentTimeMillis();

	Verdict v = fm.monitor();
	System.err.println("Verdict was " + v);
	long endTime = System.currentTimeMillis();
	System.err.println(">>Execution time without creation: "
			+ (endTime - beforeMonitoring));
	System.err.println(">>Total execution time : " + (endTime - startTime));
	System.err.println(t.getMonitor());
}

}