package offline;


import java.io.IOException;

import qea.monitoring.impl.CSVFileMonitor;
import qea.monitoring.impl.translators.DefaultTranslator;
import qea.monitoring.impl.translators.OfflineTranslator;
import qea.monitoring.impl.translators.TranslatorFactory;
import qea.structure.impl.other.Verdict;
import qea.structure.intf.QEA;
import qeaExamples.PublisherSubscriber;
import static qea.monitoring.impl.translators.TranslatorFactory.event;

public class PublishSubscribe {

	public static void main(String[] args) throws IOException{
		
		String trace = "traces/pubsub2";
		
		QEA qea = PublisherSubscriber.make();
		//OfflineTranslator t = new DefaultTranslator("send","ack");
		
		OfflineTranslator t = TranslatorFactory.makeSelectingDefaultTranslatorWithIgnore(
				event("send",1,3),
				event("ack",1,3)
		);
		
		t.printEvents();

		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, t);
		
		Verdict verdict = fm.monitor();
		
		System.err.println(verdict);
		
	
	}
	
}
