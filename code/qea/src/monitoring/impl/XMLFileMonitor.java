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
import properties.competition.Soloist;
import properties.competition.translators.OfflineTranslator_SOLOIST_FOUR;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import util.ArrayUtil;
import exceptions.ShouldNotHappenException;

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
public class XMLFileMonitor extends FileMonitor implements ContentHandler {

	public static void main(String[] args) throws IOException,
			ParserConfigurationException, SAXException {
		XMLFileMonitor fileMonitor = new XMLFileMonitor("traces/Team1/B5.xml",
				new Soloist().make(Property.SOLOIST_FOUR),
				new OfflineTranslator_SOLOIST_FOUR());
		System.out.println(fileMonitor.monitor());
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
	 * Creates an XML file monitor for the specified trace and QEA property
	 * 
	 * @param traceFileName
	 *            Trace file name
	 * @param qea
	 *            QEA property
	 * @throws FileNotFoundException
	 */
	public XMLFileMonitor(String traceFileName, QEA qea,
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
			if (step() == Verdict.FAILURE) {
				System.out.print("Failure on event #" + eventCount + ": "
						+ eventName + "(");
				for (int i = 0; i < fieldCount; i++) {
					if (i > 0) {
						System.out.print(",");
					}
					System.out.print(fieldNames[i] + "=" + fieldValues[i]);
				}
				System.out.println(")");
			}
			break;

		default:
			break;
		}
	}

	private Verdict step() {

		// int event = translate(eventName);
		if (fieldCount == 0) {
			return translator.translateAndStep(eventName);
		} else {
			return translator.translateAndStep(eventName,
					ArrayUtil.resize(fieldNames, fieldCount),
					ArrayUtil.resize(fieldValues, fieldCount));
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		switch (status) {

		case EXPECTING_EVENT_NAME:
			eventName = new String(ch, start, length);
			break;

		case EXPECTING_FIELD_NAME:
			fieldNames[fieldCount] = new String(ch, start, length);
			break;

		case EXPECTING_FIELD_VALUE:
			fieldValues[fieldCount] = new String(ch, start, length);
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
