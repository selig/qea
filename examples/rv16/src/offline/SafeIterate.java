package offline;

import java.io.IOException;

import qea.monitoring.impl.CSVFileMonitor;
import qea.monitoring.impl.translators.OfflineTranslator;
import qea.monitoring.impl.translators.TranslatorFactory;
import qea.structure.impl.other.Verdict;
import qea.structure.intf.QEA;
import qeaExamples.SafeIterator;
import static qea.monitoring.impl.translators.TranslatorFactory.event;
import static qea.monitoring.impl.translators.TranslatorFactory.PType.*;


public class SafeIterate {

	public static void main(String[] args) throws IOException{
		
		String trace = "traces/safeiter2";
		
		QEA qea = SafeIterator.make();
		OfflineTranslator t = TranslatorFactory.makeParsingTranslator(
				event("iterator",OBJ,INT),
				event("next",OBJ)
		);	

		CSVFileMonitor fm = new CSVFileMonitor(trace, qea, t);
		
		Verdict verdict = fm.monitor();
		
		System.err.println(verdict);
	
	}
	
	
}
