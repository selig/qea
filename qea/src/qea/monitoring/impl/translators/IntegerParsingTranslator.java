package qea.monitoring.impl.translators;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import qea.exceptions.ShouldNotHappenException;
import qea.structure.impl.other.Verdict;
import qea.structure.intf.QEA;

public class IntegerParsingTranslator extends DefaultTranslator {

	// parsing_mask[e] = null means no parsing
	// parsing_mask[e][i] = 0 means do not parse ith parameter
	// parsing_mask[e][i] = 1 means parse ith parameter
	// parsing_mask[e][i] = 2 means parse ith parameter and intern
	private int[][] parsing_mask;
	
	public void setParse(int event, int p){ setParseStatus(event,p,1);}
	public void setParseInterned(int event, int p){ setParseStatus(event,p,2);}
	public void setParseStatus(int event, int parameter, int status){
		if(parsing_mask[event]==null){
			parsing_mask[event] = new int[parameter+2];
		}
		else if(parsing_mask[event].length <= parameter){
			int[] temp = new int[parameter+2];
			System.arraycopy(parsing_mask[event], 0, temp, 0, parsing_mask[event].length);
			parsing_mask[event]=temp;
		}
		parsing_mask[event][parameter]=status;
	}
	
	public IntegerParsingTranslator(QEA qea){
		super(qea);
		parsing_mask = new int[qea.getEventsAlphabet().length+1][];
	}
	
	public IntegerParsingTranslator(String... names){
		super(names);
		parsing_mask = new int[names.length+1][];
	}	
	
	
	public IntegerParsingTranslator(String[] names, int[] codes){
		super(names,codes);
		parsing_mask = new int[names.length+1][];
	}
	
	/*
	 * For interning integer values
	 */
	private Map<String,Integer> imap = new HashMap<>();	
	private Integer getInternedInt(String s){
		Integer i = imap.get(s);
		if(i==null){
			i = Integer.parseInt(s);
			imap.put(s,i);
		}
		return i;
	}	
	
	@Override
	public Verdict translateAndStep(String eventName, String[] paramNames,
			String[] paramValues) {
		
		int e = translate(eventName);
		if(e == -1){
			return lastVerdict;
		}
		
		int[] mask = parsing_mask[e];
		
		if(mask != null){
		
			//IMPORTANT - if the mask is shorter than paramValues we will drop the end ones
			//				if it is longer than we will have an ArrayOutOfBounds
			Object[] parsed_values = new Object[mask.length];
			System.arraycopy(paramValues, 0, parsed_values, 0, paramValues.length);
			
			for(int i=0;i<parsed_values.length;i++){
				try{				
					if(mask[i]==1){
						parsed_values[i]= Integer.parseInt(paramValues[i].trim());						
					}else if(mask[i]==2){ 
						parsed_values[i] = getInternedInt(paramValues[i].trim()); 
					}
				}catch(NumberFormatException ex){
					throw new ShouldNotHappenException(
							"Event was expecting an integer. Bad event = "+eventName+Arrays.toString(paramValues));
				}
				catch(ArrayIndexOutOfBoundsException ex){
					throw new ShouldNotHappenException(
							"Event had mask that was too long. Bad event = "+eventName+Arrays.toString(paramValues));
				}
			}			
			lastVerdict = monitor.step(e,parsed_values);
			return lastVerdict;
		}	
		lastVerdict = monitor.step(e,paramValues);
		return lastVerdict;
	}	
	
}
