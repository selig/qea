package monitoring.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import properties.rovers.RoverCaseStudy;

import monitoring.intf.Monitor;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import exceptions.ShouldNotHappenException;

/*
 * We expect events in the form
 * 
 * name,arg0=A,arg1=B
 * 
 * where we ignore the names ar0,arg1.. so this becomes name(A,B)
 * 
 * we assume that all event names (in string form) begin with a lower case character
 * 
 */

public class CSVFileMonitor {

	private Monitor monitor;
	private BufferedReader trace;
	   
	
	private int[] event_lookup;
	
	//position 0 not used
	private int[][] expansions_int;
	private String[][] expansions_str;
	
	public CSVFileMonitor(String tracename, QEA qea) throws FileNotFoundException {
		monitor = MonitorFactory.create(qea);
		trace = new BufferedReader(new FileReader(tracename));
		
		event_lookup = new int[26];
		int[] starting = new int[26];
		String[] tmp_event_lookup = new String[26];
		
		// max possible size is number of events, dont' lose much space by using this!
		int events = qea.getEventsAlphabet().length+1;
		expansions_str = new String[events][];
		expansions_int = new int[events][];
		int expansions=1;
		for(Map.Entry<String,Integer> entry : qea.get_name_lookup().entrySet()){
			String string_name = entry.getKey();
			int int_name = entry.getValue(); // assume >0
			char c = string_name.charAt(0); // assume in range 97-122
			if(c <97 || c> 122){
				throw new ShouldNotHappenException("This method assumes all event names begin"+
						" with a lower case letter");
			}
			if(starting[c-97]==0){
				event_lookup[c-97] = int_name;	
				tmp_event_lookup[c-97] = string_name;
			}else{
				if(starting[c-97]==1){
					//expand
					int tmp = event_lookup[c-97];
					event_lookup[c-97]=(-expansions);
					expansions_str[expansions] = new String[]{tmp_event_lookup[c-97],string_name};
					expansions_int[expansions] = new int[]{tmp,int_name};
					expansions++;
				}
				else{
					//add
					int index = -(event_lookup[c-97]);
					int prev = expansions_str[index].length;
					int[] nint = new int[prev+1];
					String[] nstr = new String[prev+1];
					for(int i=0;i<prev-1;i++){
						nint[i]=expansions_int[index][i];
						nstr[i]=expansions_str[index][i];
					}
					nint[prev]=int_name;
					nstr[prev]=string_name;
					expansions_str[index]=nstr;
					expansions_int[index]=nint;
				}
			}
			starting[c-97]++;
			
			
		}
		
		if(false){
			for(int i=0;i<event_lookup.length;i++){
				int id = event_lookup[i];
				if(id!=0){
					System.err.print(((char)(i+97))+"\t"+id);
					if(id<0){
						for(int j=0;j<expansions_int[-id].length;j++){
							System.err.print("\t"+expansions_int[-id][j]+":"+expansions_str[-id][j]);
						}
					}
					System.err.println();
				}
			}
		}
		
	}
	
	private Verdict monitor() throws IOException{
			
		String line;
		int events=0;
		while((line=trace.readLine())!=null){
			events++;
			if(events%100==0){
				System.err.println(events);
				//System.err.println(monitor);
			}
			//System.err.println(events+":"+line);
			if(step(line)==Verdict.FAILURE){
				System.err.println("Failure on "+events+":"+line);
			}
		}
		System.err.println(events+" events");
		return monitor.end();
	}
	
	private Verdict step(String line){
		
		String[] parts = line.split(",|=");
		int name = translate(parts[0]);
		if(parts.length==3){
			return monitor.step(name,format(parts[2]));
		}
		else if(parts.length==5){
			return monitor.step(name,format(parts[2]),format(parts[4]));
		}
		else{
			int noargs = (parts.length-1)/2;
			Object[] args = new Object[noargs];
			for(int i=1;i<noargs;i+=2) args[i] = format(parts[i]);
			return monitor.step(name,args);
		}
	}
	
	private int translate(String name){
		char c = name.charAt(0);
		int event_id = event_lookup[c-97];
		if(event_id >= 1) return event_id;
		String[] strlookup = expansions_str[-event_id];
		for(int i=0;i<strlookup.length;i++){
			if(strlookup[i].equals(name)){
					return expansions_int[-event_id][i];
			}
		}
		throw new RuntimeException("Could not translate event name "+name);
	}
	
	private Object format(String arg){
		return arg.intern();
	}
	
	//A test
	public static void main(String[] args) throws IOException{
		
		CSVFileMonitor f = new CSVFileMonitor("traces/RespectConflicts.trace",
										RoverCaseStudy.makeRespectConflictsSingle());
		
		long start = System.currentTimeMillis();
		System.err.println(f.monitor());
		System.err.println("Took: "+(System.currentTimeMillis()-start));
		
	}
	
}
