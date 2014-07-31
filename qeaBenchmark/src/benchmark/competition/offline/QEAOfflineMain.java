package benchmark.competition.offline;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import monitoring.impl.CSVFileMonitor;
import monitoring.impl.FileMonitor;
import monitoring.impl.XMLFileMonitorSAX;
import monitoring.impl.translators.OfflineTranslator;

import org.xml.sax.SAXException;

import properties.Property;
import properties.competition.CSRV14PropertyMaker;
import properties.competition.translators.CSRVTranslatorMaker;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import exceptions.XMLFailureException;

public class QEAOfflineMain {

	private static final String XML = "XML";
	private static final String CSV = "CSV";

	private static CSRV14PropertyMaker propMaker = new CSRV14PropertyMaker();
	private static CSRVTranslatorMaker transMaker = new CSRVTranslatorMaker();

	public static void main(String[] args) throws IOException,
			ParserConfigurationException, SAXException {

		long startTime = System.currentTimeMillis();
		String format = checkArgsAndGetTraceFormat(args);
		if (format != null) {

			// Obtain trace and property
			String trace = args[0];
			Property property = Property.valueOf(args[1]);

			// Create property and offline translator
			QEA qea = propMaker.make(property);
			OfflineTranslator trans = transMaker.make(property);
			long beforeMonitoring = 0;

			try {

				Verdict v;
				FileMonitor m;

				// Create monitor according to the format
				if (format.equals(CSV)) { // CSV format
					m = new CSVFileMonitor(trace, qea, trans);
				} else { // XML format
					m = new XMLFileMonitorSAX(trace, qea, trans);
				}

				// Print monitor class
				System.err.println("Running with " + m.getMonitorClass());
				beforeMonitoring = System.currentTimeMillis();

				// Monitor trace
				v = m.monitor();

				// Print verdict and time
				System.err.println(property + ": Verdict was " + v);
				long endTime = System.currentTimeMillis();
				System.err.println(">>Execution time without creation: "
						+ (endTime - beforeMonitoring));
				System.err.println(">>Total execution time : "
						+ (endTime - startTime));

			} catch (XMLFailureException e) {

				// In case of failure in an XML, print verdict and time
				System.err.println(property + ": " + e.getMessage());
				long endTime = System.currentTimeMillis();
				System.err.println(">>Execution time without creation: "
						+ (endTime - beforeMonitoring));
				System.err.println(">>Total execution time : "
						+ (endTime - startTime));

			} catch (FileNotFoundException e) {
				System.err.println("File not found: " + trace);
			}
		}
	}

	public static String checkArgsAndGetTraceFormat(String[] args) {

		// Check the number of arguments is correct
		if (args.length != 2 && args.length != 3) {
			System.err
					.println("Usage: QEAOfflineTrace tracePath propertyName [fileFormat (CSV or XML)]");
			return null;
		}

		// Check the property exists
		try {
			Property.valueOf(args[1]);
		} catch (IllegalArgumentException e) {
			System.err.println("Invalid property name: " + args[1]);
			return null;
		}

		if (args.length == 3) { // Trace format was specified
			if (args[2].equalsIgnoreCase(CSV)) {
				return CSV;
			} else if (args[2].equalsIgnoreCase(XML)) {
				return XML;
			} else {
				System.err.println("Invalid trace format: " + args[2]
						+ ". Only CSV and XML are supported");
				return null;
			}
		} else { // Trace format was not specified

			// Check the extension of the file
			String trace = args[0];
			int dotIdx = trace.lastIndexOf('.');
			if (dotIdx == -1) {
				System.err
						.println("File format couldn't be determined. Please specify CSV or XML as third parameter.");
				return null;
			} else {
				String ext = trace.substring(dotIdx + 1);
				if (ext.equalsIgnoreCase(CSV)) {
					return CSV;
				} else if (ext.equalsIgnoreCase(XML)) {
					return XML;
				} else {
					System.err
							.println("File format couldn't be determined. Please specify CSV or XML as third parameter.");
					return null;
				}
			}
		}
	}
}
