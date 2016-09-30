package qea.monitoring.impl;

import java.io.FileNotFoundException;
import java.io.IOException;

import qea.monitoring.impl.translators.OfflineTranslator;
import qea.structure.impl.other.Verdict;
import qea.structure.intf.QEA;

/*
 * We expect events in the form
 * 
 * name,A,B
 * 
 * where we ignore trailing , and skip empyty fields i.e. A,,B
 * 
 * we assume that all event names (in string form) begin with a lower case character
 * 
 */

public class CSVFileMonitor extends FileMonitor {

	public CSVFileMonitor(String tracename, QEA qea,
			OfflineTranslator translator) throws FileNotFoundException {
		super(tracename, qea, translator);
	}

	@Override
	public Verdict monitor() throws IOException {

		String line;
		int events = 0;
		boolean not_stopped = true;
		if(hasHeader){ line=trace.readLine();}
		
		while (not_stopped && (line = trace.readLine()) != null) {
			if(line.isEmpty()) continue;
			events++;
						
			//System.err.println(events+":"+line);
			//System.err.println(translator.getMonitor());
			 //if (events % 100 == 0) {				 
			//	 System.err.println(events);
			 //System.err.println(translator.getMonitor()); //System.exit(0);
			// }			
			 if (events % 10000 == 0) {				 
				 System.err.println(events/10000+" x 10k");
			 //System.err.println(translator.getMonitor()); //System.exit(0);
			 }
			// System.err.println(events+":"+line);
			try{
				Verdict verdict = step(line);	
				//System.err.println("Verdict: "+verdict);
				if (verdict == Verdict.FAILURE) {
					System.err.println("Failure on " + events + ":" + line);
					not_stopped = false;
				}
				if (verdict == Verdict.SUCCESS) {
					System.err.println("SUCCESS on " + events + ":" + line);
					not_stopped = false;
				}
			}catch(Exception e){
				System.err.println("Error on line "+line);
				throw e;
			}
		}
		System.err.println(events + " events");
		//System.err.println(translator.getMonitor().getStatus());
		return translator.getMonitor().end();
	}

	private Verdict step(String line) {

		String[] parts = line.split(",");
		
		if(parts[0].equals("garbage")){
			if(parts.length!=2) throw new RuntimeException("garbage should be used with exactly one value");
			translator.getMonitor().garbage_event(format(parts[1]));
			return null;
		}
		if(translator.hasSingleEvent()){
			return translator.translateAndStep(translator.getSingleEvent(),null,parts);
		}
		
		if(parts.length == 1){
			return translator.translateAndStep(format(parts[0]));
		}
		if (parts.length == 2) {
			return translator.translateAndStep(format(parts[0]),
					null,
					new String[] { format(parts[1]) });
		} else if (parts.length == 3) {
			return translator.translateAndStep(format(parts[0]), null,
					new String[] {format(parts[1]), format(parts[2]) });
		} else {
			int noargs = (parts.length - 1);
			String[] args = new String[noargs];
			for (int i = 0;i<noargs;i++) {
				args[i] = format(parts[i+1]);
			}
			return translator.translateAndStep(format(parts[0]), null, args);
		}
	}
}
