package qea.monitoring.impl;

import java.io.FileNotFoundException;
import java.io.IOException;

import qea.monitoring.impl.translators.OfflineTranslator;
import qea.structure.impl.other.Verdict;
import qea.structure.intf.QEA;
import qea.util.ArrayUtil;
import qea.exceptions.XMLFailureException;

/**
 * This class processes an XML document representing a trace of events and
 * passes the events to the specified offline translator. The XML format is as
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
 * This parser assumes:
 * <ul>
 * <li>There are no elements other than the specified in the format above
 * <li>There are no comments in the XML document
 * <li>There is always a start tag and an end tag for each element, i.e. there
 * are no elements of the form {@code <element/>}
 * </ul>
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class XMLFileMonitorManual extends FileMonitor {

	/**
	 * Number of events processed
	 */
	private int eventCount = 0;

	private static final int LESS_THAN = '<';
	private static final int GREATER_THAN = '>';
	private static final int MAX_TAG_CONTENT_SIZE = 20;
	/**
	 * Maximum number of fields for any event in the XML document
	 */
	private static final int MAX_FIELD_COUNT = 10;

	/**
	 * Array of field names for the current event
	 */
	private String[] fieldNames;

	/**
	 * Array of field values for the current event
	 */
	private String[] fieldValues;

	private char[] charArray;

	private int fieldCount = 0;

	/**
	 * Creates an XML file monitor for the specified trace, QEA property and
	 * offline translator
	 * 
	 * @param tracename
	 *            Trace file name
	 * @param qea
	 *            QEA property
	 * @param translator
	 *            Translator processing the events and parameters in the trace
	 *            to send them to the monitor
	 * @throws FileNotFoundException
	 */
	public XMLFileMonitorManual(String tracename, QEA qea,
			OfflineTranslator translator) throws FileNotFoundException {
		super(tracename, qea, translator);
		charArray = new char[MAX_TAG_CONTENT_SIZE];
		fieldNames = new String[MAX_FIELD_COUNT];
		fieldValues = new String[MAX_FIELD_COUNT];
	}

	@Override
	public Verdict monitor() throws IOException {

		// Read <log>
		boolean logStartTagFound = false;
		while (!logStartTagFound) {
			skipUntil(LESS_THAN);
			if (trace.read() == 'l') {
				logStartTagFound = true;
				skipUntil(GREATER_THAN);
			}
		}

		// Read <event>...</event>
		boolean moreEvents;
		do {
			moreEvents = readEvent();
		} while (moreEvents);

		// Compute final verdict
		return translator.getMonitor().end();
	}

	private boolean readEvent() throws IOException {

		// Read <event>
		skipUntil(LESS_THAN);
		if (trace.read() != 'e') {
			// No more events
			return false;
		}
		skipUntil(GREATER_THAN);

		// Read <name>
		skipUntil(LESS_THAN);
		skipUntil(GREATER_THAN);

		// Read event name
		String eventName = readUntil(LESS_THAN);

		// Read </name>
		skipUntil(GREATER_THAN);

		// Read <field>...</field>
		fieldCount = 0;
		boolean moreFields;
		do {
			moreFields = readField();
			if (moreFields) {
				fieldCount++;
			}
		} while (moreFields);

		// Send event to the translator
		eventCount++;
		Verdict verdict;
		if (fieldCount == 0) {
			verdict = translator.translateAndStep(eventName);
		} else {
			verdict = translator.translateAndStep(eventName,
					ArrayUtil.resize(fieldNames, fieldCount),
					ArrayUtil.resize(fieldValues, fieldCount));
		}

		if (verdict == Verdict.FAILURE) {
			throw new XMLFailureException(getFailureDetail(eventName));
		}

		return true;
	}

	private String getFailureDetail(String eventName) {
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

	private boolean readField() throws IOException {

		// Read <field>
		skipUntil(LESS_THAN);
		if (trace.read() != 'f') {
			// No more fields
			return false;
		}
		skipUntil(GREATER_THAN);

		// Read <name>
		skipUntil(LESS_THAN);
		skipUntil(GREATER_THAN);

		// Read field name
		fieldNames[fieldCount] = readUntil(LESS_THAN);

		// Read </name>
		skipUntil(GREATER_THAN);

		// Read <value>
		skipUntil(LESS_THAN);
		skipUntil(GREATER_THAN);

		// Read field value
		fieldValues[fieldCount] = readUntil(LESS_THAN);

		// Read </value>
		skipUntil(GREATER_THAN);

		// Read </field>
		skipUntil(LESS_THAN);
		skipUntil(GREATER_THAN);

		return true;
	}

	private void skipUntil(int delimiter) throws IOException {
		int c;
		do {
			c = trace.read();
		} while (c != delimiter);
	}

	private String readUntil(int delimiter) throws IOException {
		int i = 0;
		int c;

		while ((c = trace.read()) != delimiter) {
			charArray[i] = (char) c;
			i++;
		}
		return new String(charArray, 0, i);
	}
}
