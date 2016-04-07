package qea.monitoring.impl.translators;

import java.util.HashMap;
import java.util.Map;

import qea.exceptions.ShouldNotHappenException;
import qea.structure.impl.other.Verdict;
import qea.structure.intf.QEA;

public class DefaultTranslator extends OfflineTranslator {

	public DefaultTranslator(QEA qea){
		eventLookupInit(qea.get_name_lookup());
	}
	
	public DefaultTranslator(String... names){
		Map<String,Integer> map = new HashMap<String,Integer>();
		for(int i=0;i<names.length;i++){
			map.put(names[i],i+1); // map position 0 in names to 1 etc
		}
		eventLookupInit(map);
	}	
	

	
	public DefaultTranslator(String[] names, int[] codes){
		Map<String,Integer> map = new HashMap<String,Integer>();
		assert(names.length==codes.length);
		for(int i=0;i<names.length;i++){
			map.put(names[i],codes[i]);
		}
		eventLookupInit(map);
	}
	
	protected boolean ignoreUnknown = false;
	public void allowUnknown(){ ignoreUnknown = true; }
	
	@Override
	public Verdict translateAndStep(String eventName, String[] paramNames,
			String[] paramValues) {
		int event = translate(eventName);
		if(event==-1) return lastVerdict;
		lastVerdict = monitor.step(event,paramValues);
		return lastVerdict;
	}

	@Override
	public Verdict translateAndStep(String eventName) {
		int event = translate(eventName);
		if(event==-1) return lastVerdict;
		lastVerdict =  monitor.step(event);
		return lastVerdict;
	}

	protected Verdict lastVerdict = null;
	private int[] event_lookup;

	// position 0 not used
	private int[][] expansions_int;
	private String[][] expansions_str;
	
	private void eventLookupInit(Map<String,Integer> name_lookup) {

		event_lookup = new int[26];
		int[] starting = new int[26];
		String[] tmp_event_lookup = new String[26];

		// max possible size is number of events, dont' lose much space by using
		// this!
		int events = name_lookup.size() + 1;
		expansions_str = new String[events][];
		expansions_int = new int[events][];
		int expansions = 1;
		for (Map.Entry<String, Integer> entry : name_lookup.entrySet()) {
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
		if(singleEvent!=null) return 1;
		char c = name.charAt(0);
		int event_id = event_lookup[c - 97];
		if (event_id >= 1) {
			return event_id;
		}
		String[] strlookup = expansions_str[-event_id];
		if(strlookup == null){
			if(ignoreUnknown) return -1;
			throw new RuntimeException("Could not translate event name " + name);
		}
		for (int i = 0; i < strlookup.length; i++) {
			if (strlookup[i].equals(name)) {
				return expansions_int[-event_id][i];
			}
		}
		if(ignoreUnknown) return -1;
		throw new RuntimeException("Could not translate event name " + name);
	}


	
	
}
