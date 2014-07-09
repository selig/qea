package monitoring.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import monitoring.impl.translators.OfflineTranslator;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import exceptions.ShouldNotHappenException;

/**
 * Provides the base implementation for an offline monitor processing a trace in
 * a file
 * 
 * @author Giles Reger
 * @author Helena Cuenca
 */
public abstract class FileMonitor {

	protected BufferedReader trace;
	protected OfflineTranslator translator;

	private int[] event_lookup;

	// position 0 not used
	private int[][] expansions_int;
	private String[][] expansions_str;

	public FileMonitor(String tracename, QEA qea, OfflineTranslator translator)
			throws FileNotFoundException {

		translator.setMonitor(MonitorFactory.create(qea));
		this.translator = translator;
		trace = new BufferedReader(new FileReader(tracename));

		event_lookup = new int[26];
		int[] starting = new int[26];
		String[] tmp_event_lookup = new String[26];

		// max possible size is number of events, dont' lose much space by using
		// this!
		int events = qea.getEventsAlphabet().length + 1;
		expansions_str = new String[events][];
		expansions_int = new int[events][];
		int expansions = 1;
		for (Map.Entry<String, Integer> entry : qea.get_name_lookup()
				.entrySet()) {
			String string_name = entry.getKey();
			int int_name = entry.getValue(); // assume >0
			char c = string_name.charAt(0); // assume in range 97-122
			if (c < 97 || c > 122) {
				throw new ShouldNotHappenException(
						"This method assumes all event names begin"
								+ " with a lower case letter");
			}
			if (starting[c - 97] == 0) {
				event_lookup[c - 97] = int_name;
				tmp_event_lookup[c - 97] = string_name;
			} else {
				if (starting[c - 97] == 1) {
					// expand
					int tmp = event_lookup[c - 97];
					event_lookup[c - 97] = -expansions;
					expansions_str[expansions] = new String[] {
							tmp_event_lookup[c - 97], string_name };
					expansions_int[expansions] = new int[] { tmp, int_name };
					expansions++;
				} else {
					// add
					int index = -event_lookup[c - 97];
					int prev = expansions_str[index].length;
					int[] nint = new int[prev + 1];
					String[] nstr = new String[prev + 1];
					for (int i = 0; i < prev - 1; i++) {
						nint[i] = expansions_int[index][i];
						nstr[i] = expansions_str[index][i];
					}
					nint[prev] = int_name;
					nstr[prev] = string_name;
					expansions_str[index] = nstr;
					expansions_int[index] = nint;
				}
			}
			starting[c - 97]++;

		}

		if (false) {
			for (int i = 0; i < event_lookup.length; i++) {
				int id = event_lookup[i];
				if (id != 0) {
					System.err.print((char) (i + 97) + "\t" + id);
					if (id < 0) {
						for (int j = 0; j < expansions_int[-id].length; j++) {
							System.err.print("\t" + expansions_int[-id][j]
									+ ":" + expansions_str[-id][j]);
						}
					}
					System.err.println();
				}
			}
		}

	}

	protected int translate(String name) {
		char c = name.charAt(0);
		int event_id = event_lookup[c - 97];
		if (event_id >= 1) {
			return event_id;
		}
		String[] strlookup = expansions_str[-event_id];
		for (int i = 0; i < strlookup.length; i++) {
			if (strlookup[i].equals(name)) {
				return expansions_int[-event_id][i];
			}
		}
		throw new RuntimeException("Could not translate event name " + name);
	}

	public abstract Verdict monitor() throws Exception;

}
