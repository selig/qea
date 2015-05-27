package qea.monitoring.impl.translators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TranslatorFactory {

	public static DefaultTranslator makeDefaultTranslator(String... names){
		return new DefaultTranslator(names);
	}
	
	public static DefaultTranslator makeDefaultTranslatorWithIgnore(String... names){
		DefaultTranslator t = new DefaultTranslator(names);
		t.allowUnknown();
		return t;
	}
	
	public static enum PType {
		OBJ,
		INT,
		QINT,
		BOOL
	}
	public static class Parameter{
		public final int original_place;
		public final PType parsing_type;
		public Parameter(int original_place, PType parsing_type) {
			this.original_place = original_place;
			this.parsing_type = parsing_type;
		}
	}
	
	public static class EventDescriptor{
		public final String name;
		public final List<Parameter> ps = new ArrayList<Parameter>();
		EventDescriptor(String n){ name=n; }
		//IMPORTANT - should be set by constructor, if not set then can cause errors
		public boolean is_reordered=false;
	}
	
	public static EventDescriptor event(String name) { return new EventDescriptor(name); }
	
	public static EventDescriptor event(String name, PType... ps) {
		EventDescriptor e = new EventDescriptor(name);
		for(int i=0;i<ps.length;i++){
			e.ps.add(new Parameter(i,ps[i]));
		}
		return e;
	}
	public static Parameter param(Integer place, PType ty){ return new Parameter(place,ty); }
	public static EventDescriptor event(String name, Parameter... ps){
		EventDescriptor e = new EventDescriptor(name);
		for(int i=0;i<ps.length;i++){
			e.ps.add(ps[i]);
			if(ps[i].original_place!=i) e.is_reordered=true;
		}
		return e;		
	}
	
	/*
	 * Currently only support integers
	 */
	public static OfflineTranslator makeParsingTranslator(EventDescriptor... events){
		String[] names = new String[events.length];
		for(int i=0;i<events.length;i++){ names[i] = events[i].name;}
		
		ParsingTranslator t = new ParsingTranslator(names);
		
		boolean is_reordered=false;
		for(int i=0;i<events.length;i++){
			if(events[i].is_reordered) is_reordered=true;
			for(int j=0;j<events[i].ps.size();j++){
				int status = 0;
				t.setParseStatus(i+1, j, events[i].ps.get(j).parsing_type);
			}
		}
		if(is_reordered){
			SelectingWrapperTranslator<ParsingTranslator> tt = new SelectingWrapperTranslator<ParsingTranslator>(t);
			for(int i=0;i<events.length;i++){
				if(events[i].is_reordered){
					Integer[] order = new Integer[events[i].ps.size()]; 						
					events[i].ps.toArray(order);
					tt.setOrder(names[i], order);
				}
			}
			return tt;
		}
		return t;
	}
	public static OfflineTranslator makeParsingTranslatorWithIgnore(EventDescriptor... events){
		OfflineTranslator t = makeParsingTranslator(events);
		t.allowUnknown();
		return t;
	}

	public static ParsingTranslator makeParsingTranslator(String[] names, PType[] mask){
		EventDescriptor[] events = new EventDescriptor[names.length];
		for(int i=0;i<names.length;i++){
			events[i] = event(names[i],mask);
		}
		return makeParsingTranslator(names,mask);
	}	
	public static ParsingTranslator makeParsingTranslatorWithIgnore(String[] names, PType[] mask){
		EventDescriptor[] events = new EventDescriptor[names.length];
		for(int i=0;i<names.length;i++){
			events[i] = event(names[i],mask);
		}
		return makeParsingTranslatorWithIgnore(names,mask);
	}
	
	public static SelectingWrapperTranslator<DefaultTranslator> makeSelectingDefaultTranslator(EventDescriptor... events){
		String[] names = new String[events.length];
		for(int i=0;i<names.length;i++){
			names[i] = events[i].name;
		}
		
		SelectingWrapperTranslator<DefaultTranslator> t = 
				new SelectingWrapperTranslator<DefaultTranslator>(new DefaultTranslator(names));
		
		for(int i=0;i<events.length;i++){
				Integer[] order = new Integer[events[i].ps.size()]; 						
				for(int j=0;j<events[i].ps.size();j++){
					order[j] = events[i].ps.get(j).original_place;
				}
				//System.err.println("Setting "+names[i]+" "+Arrays.toString(order));
				t.setOrder(names[i], order);
		}		
		
		return t;
	}
	public static SelectingWrapperTranslator<DefaultTranslator> makeSelectingDefaultTranslatorWithIgnore(EventDescriptor... events){
		SelectingWrapperTranslator<DefaultTranslator> t = makeSelectingDefaultTranslator(events);
		t.allowUnknown();
		return t;
	}
	public static SelectingWrapperTranslator<DefaultTranslator> makeSelectingDefaultTranslator(String... names){
		EventDescriptor[] events = new EventDescriptor[names.length];
		for(int i=0;i<names.length;i++){
			events[i] = event(names[i]);
		}
		return makeSelectingDefaultTranslator(events);
	}	
	public static SelectingWrapperTranslator<DefaultTranslator> makeSelectingDefaultTranslatorWithIgnore(String... names){
		SelectingWrapperTranslator<DefaultTranslator> t = makeSelectingDefaultTranslator(names);
		t.allowUnknown();
		return t;
	}	
	
}
