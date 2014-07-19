package monitoring.impl;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import monitoring.impl.translators.OfflineTranslator;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import properties.Property;
import properties.competition.Stepr;
import properties.competition.translators.SteprTranslators;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import util.ArrayUtil;
import exceptions.ShouldNotHappenException;
import exceptions.XMLFailureException;

/**
 * Content Handler that processes an XML document representing a trace of events
 * and passes the events to the specified QEA monitor. The XML format is as
 * follows:
 * 
 * {@code
 * <log>
	<event>
		<name>...</name>
		<field>
			<name>...</name>
			<value>...</value>
		</field>
		<field>
			...
		</field>
	</event>
	<event>
		...
	</event>
</log>
 * }
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class XMLFileMonitorSAX extends FileMonitor implements ContentHandler {

	public static void main(String[] args) throws IOException,
			ParserConfigurationException, SAXException {

		Property property = Property.STEPR_TWO;

		XMLFileMonitorSAX fileMonitor = new XMLFileMonitorSAX(
				"traces/Team6/log.xml", new Stepr().make(property),
				new SteprTranslators().make(property));
		long start = System.currentTimeMillis();
		System.out.println(fileMonitor.monitor());
		System.out.println("Took: " + (System.currentTimeMillis() - start));
	}

	/**
	 * Name of the event that is being processed
	 */
	private String eventName;

	/**
	 * Number of events processed
	 */
	private int eventCount;

	/**
	 * Array of field names for the current event
	 */
	private String[] fieldNames;

	/**
	 * Array of field values for the current event
	 */
	private String[] fieldValues;

	/**
	 * Count of fields for the current event
	 */
	private int fieldCount;

	/**
	 * Maximum number of fields for any event in the XML document
	 */
	private final int MAX_FIELD_COUNT = 10;

	/**
	 * Enumeration representing the possible stages of XML processing
	 */
	private enum Status {
		START, INSIDE_EVENT, EXPECTING_EVENT_NAME, INSIDE_FIELD, EXPECTING_FIELD_NAME, EXPECTING_FIELD_VALUE
	}

	/**
	 * Stores the current processing status
	 */
	private Status status = Status.START;

	/**
	 * Creates an XML file monitor for the specified trace, QEA property and
	 * offline translator
	 * 
	 * @param traceFileName
	 *            Trace file name
	 * @param qea
	 *            QEA property
	 * @param translator
	 *            Translator processing the events and parameters in the trace
	 *            to send them to the monitor
	 * @throws FileNotFoundException
	 */
	public XMLFileMonitorSAX(String traceFileName, QEA qea,
			OfflineTranslator translator) throws FileNotFoundException {

		super(traceFileName, qea, translator);
		fieldNames = new String[MAX_FIELD_COUNT];
		fieldValues = new String[MAX_FIELD_COUNT];
	}

	@Override
	public Verdict monitor() throws IOException, ParserConfigurationException,
			SAXException {

		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		xmlReader.setContentHandler(this);
		xmlReader.parse(new InputSource(trace));
		System.err.println(eventCount + " events");
		return translator.getMonitor().end();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		switch (qName) {

		case "log":
			break;

		case "field":
			status = Status.INSIDE_FIELD;
			break;

		case "event":
			status = Status.INSIDE_EVENT;
			fieldCount = 0;
			break;

		case "name":
			if (status == Status.INSIDE_EVENT) {
				status = Status.EXPECTING_EVENT_NAME;
			} else {
				status = Status.EXPECTING_FIELD_NAME;
			}
			break;

		case "value":
			status = Status.EXPECTING_FIELD_VALUE;
			break;

		default:
			throw new ShouldNotHappenException("Unrecognised element: " + qName);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		switch (qName) {
		case "name":
			if (status == Status.EXPECTING_EVENT_NAME) {
				status = Status.INSIDE_EVENT;
			} else {
				status = Status.INSIDE_FIELD;
			}
			break;

		case "value":
			status = Status.INSIDE_FIELD;
			break;

		case "field":
			status = Status.INSIDE_EVENT;
			break;

		case "event":

			eventCount++;
			Verdict verdict = step();
			if (verdict == Verdict.FAILURE) {
				throw new XMLFailureException(getFailureDetail());
			}
			break;

		default:
			break;
		}
	}

	private Verdict step() {

		if (fieldCount == 0) {
			return translator.translateAndStep(eventName);
		} else {
			return translator.translateAndStep(eventName,
					ArrayUtil.resize(fieldNames, fieldCount),
					ArrayUtil.resize(fieldValues, fieldCount));
		}
	}

	private String getFailureDetail() {
		String failureDetail = "Failure on event #" + eventCount + ": "
				+ eventName + "(";
		for (int i = 0; i < fieldCount; i++) {
			if (i > 0) {
				failureDetail += ",";
			}
			failureDetail += fieldNames[i] + "=" + fieldValues[i];
		}
		failureDetail += ")";
		return failureDetail;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		switch (status) {

		case EXPECTING_EVENT_NAME:
			eventName = new String(ch, start, length);
			break;

		case EXPECTING_FIELD_NAME:
			fieldNames[fieldCount] = format(new String(ch, start, length));
			break;

		case EXPECTING_FIELD_VALUE:
			fieldValues[fieldCount] = format(new String(ch, start, length));
			fieldCount++;
			break;

		default:
			break;
		}
	}

	@Override
	public void setDocumentLocator(Locator locator) {
	}

	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
	}

}
