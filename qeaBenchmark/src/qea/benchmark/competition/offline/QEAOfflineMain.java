package qea.benchmark.competition.offline;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import qea.monitoring.impl.CSVFileMonitor;
import qea.monitoring.impl.FileMonitor;
import qea.monitoring.impl.XMLFileMonitorSAX;
import qea.monitoring.impl.translators.OfflineTranslator;

import org.xml.sax.SAXException;

import qea.properties.Property;
import qea.properties.competition.CSRV14PropertyMaker;
import qea.properties.competition.translators.CSRV14TranslatorMaker;
import qea.structure.impl.other.Verdict;
import qea.structure.intf.QEA;
import qea.exceptions.XMLFailureException;

public class QEAOfflineMain {

	private static final String XML = "XML";
	private static final String CSV = "CSV";

	private static CSRV14PropertyMaker propMaker = new CSRV14PropertyMaker();
	private static CSRV14TranslatorMaker transMaker = new CSRV14TranslatorMaker();

	public static void main(String[] args) throws IOException,
			ParserConfigurationException, SAXException {

		long startTime = System.currentTimeMillis();
		String format = checkArgsAndGetTraceFormat(args);
		if (format != null) {

			// Get trace and property
			String trace = args[0];
			Property property = Property.valueOf(args[1]);

			// Create QEA property and offline translator
			QEA qea = propMaker.make(property);
			OfflineTranslator trans = transMaker.make(property);
			long beforeMonitoring = 0;

			try {

				// Create monitor according to the format
				FileMonitor m = format.equals(CSV) ? new CSVFileMonitor(trace,
						qea, trans) : new XMLFileMonitorSAX(trace, qea, trans);

				// Print monitor class
				System.err.println("Running with " + m.getMonitorClass());
				beforeMonitoring = System.currentTimeMillis();

				// Monitor trace
				Verdict v = m.monitor();

				// Print verdict and time
				System.err.println(property + ": Verdict was " + v);
				printTime(startTime, beforeMonitoring);

			} catch (XMLFailureException e) {

				// In case of failure in an XML, print verdict and time
				System.err.println(property + ": Verdict was FAILURE");
				System.err.println(e.getMessage());
				printTime(startTime, beforeMonitoring);

			} catch (FileNotFoundException e) {
				System.err.println("File not found: " + trace);
			}
			catch(Exception e){
				System.err.println("There was an error in monitoring - please report to giles.reger@manchester.ac.uk");
				System.err.println("To help us please include the following stack trace and trace files used");
				e.printStackTrace();
			}
		}
	}

	public static void printTime(long startTime, long beforeMonitoring) {

		long endTime = System.currentTimeMillis();
		System.err.println(">> Execution time without creation: "
				+ (endTime - beforeMonitoring));
		System.err.println(">> Total execution time: " + (endTime - startTime));
	}

	public static String checkArgsAndGetTraceFormat(String[] args) {

		// Check the number of arguments is correct
		if (args.length != 2 && args.length != 3) {
			System.err
					.println("Usage: QEAOfflineMain tracePath propertyName [fileFormat (CSV or XML)]");
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
