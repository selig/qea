package qea.monitoring.impl.translators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import qea.exceptions.ShouldNotHappenException;
import qea.monitoring.impl.translators.TranslatorFactory.PType;
import qea.structure.impl.other.Verdict;
import qea.structure.intf.QEA;

public class ParsingTranslator extends DefaultTranslator {

	// parsing_mask[e] = null means no parsing
	// parsing_mask[e][i] = 0 means do not parse ith parameter
	// parsing_mask[e][i] = 1 means parse ith parameter
	// parsing_mask[e][i] = 2 means parse ith parameter and intern
	private PType[][] parsing_mask;
	
	public void setParseStatus(int event, int parameter, PType parsing_type){
		if(parsing_mask[event]==null){
			parsing_mask[event] = new PType[parameter+1];
		}
		else if(parsing_mask[event].length <= parameter){
			PType[] temp = new PType[parameter+1];
			System.arraycopy(parsing_mask[event], 0, temp, 0, parsing_mask[event].length);
			parsing_mask[event]=temp;
		}
		parsing_mask[event][parameter]=parsing_type;
	}
	
	public ParsingTranslator(QEA qea){
		super(qea);
		parsing_mask = new PType[qea.getEventsAlphabet().length+1][];
	}
	
	public ParsingTranslator(String... names){
		super(names);
		parsing_mask = new PType[names.length+1][];
	}	
	
	
	public ParsingTranslator(String[] names, int[] codes){
		super(names,codes);
		parsing_mask = new PType[names.length+1][];
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
		
		PType[] mask = parsing_mask[e];
		
		if(mask != null){
		
			if(mask.length==0){
				lastVerdict = monitor.step(e);
				return lastVerdict;
			}
			
			//IMPORTANT - if the mask is shorter than paramValues we will drop the end ones
			//				if it is longer than we will have an ArrayOutOfBounds
			Object[] parsed_values = new Object[mask.length];
			System.arraycopy(paramValues, 0, parsed_values, 0, mask.length);
			
			for(int i=0;i<parsed_values.length;i++){
				try{			
					switch(mask[i]){
					  case INT : 
						  parsed_values[i]= Integer.parseInt(paramValues[i].trim()); 
						  break;
					  case QINT : 
						  parsed_values[i] = getInternedInt(paramValues[i].trim()); 
						  break;
					  case BOOL : 
						  parsed_values[i] = Boolean.parseBoolean(paramValues[i].trim());
						  break;
					  case DOUBLE : 
						  parsed_values[i] = Double.parseDouble(paramValues[i].trim()); 
						  break;
					  case LONG :
						  parsed_values[i] = Long.parseLong(paramValues[i].trim());
						  break;
					  case LIST:
					  {
						  String s = paramValues[i].trim();
						  List list = new ArrayList();
						  // We assume list is of the form [1;2;3] with skippable whitespace
						  if(s.charAt(0)!='[' || s.charAt(s.length()-1) != ']'){
							  throw new RuntimeException(s+" is not a LIST");
						  }
						  s = s.substring(1,s.length()-2);
						  String[] parts = s.split(";");
						  for(String p : parts){ list.add(s.trim());}
						  parsed_values[i] = list;
						  break;
					  }
					  default : // skip OBJ
					}					
				}catch(NumberFormatException ex){
					throw new ShouldNotHappenException(
							"Event was expecting an value or a different type.\n"+
							"Kind of param type expected = "+mask[i]+"\n"+
							"Bad index = "+i+"\n"+
							"Bad event = "+eventName+Arrays.toString(paramValues));
				}
				catch(ArrayIndexOutOfBoundsException ex){
					throw new ShouldNotHappenException(
							"Event had mask that was too long. Bad event = "+eventName+Arrays.toString(paramValues));
				}
			}	
			//System.err.println(e+Arrays.toString(parsed_values));
			lastVerdict = monitor.step(e,parsed_values);
			return lastVerdict;
		}	
		if(printEvents){
			System.err.println(eventName+Arrays.toString(paramValues));
		}
		lastVerdict = monitor.step(e,paramValues);
		return lastVerdict;
	}	
	
}
