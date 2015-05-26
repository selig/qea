package qea.monitoring.impl.translators;

import java.util.ArrayList;
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
		QINT
	}
	
	public static class EventDescriptor{
		public final String name;
		public final List<PType> ps = new ArrayList<PType>();
		EventDescriptor(String n){ name=n; }
	}
	
	public static EventDescriptor event(String name) { return new EventDescriptor(name); }
	
	public static EventDescriptor event(String name, PType... ps) {
		EventDescriptor e = new EventDescriptor(name);
		for(PType p : ps){
			e.ps.add(p);
		}
		return e;
	}
	
	/*
	 * Currently only support integers
	 */
	public static IntegerParsingTranslator makeParsingTranslator(EventDescriptor... events){
		String[] names = new String[events.length];
		for(int i=0;i<events.length;i++){ names[i] = events[i].name;}
		
		IntegerParsingTranslator t = new IntegerParsingTranslator(names);
		
		for(int i=0;i<events.length;i++){
			for(int j=0;j<events[i].ps.size();j++){
				int status = 0;
				switch(events[i].ps.get(j)){
				case OBJ: status=0; break;
				case INT: status=1; break;
				case QINT: status=2; break;
				}
				t.setParseStatus(i+1, j, status);
			}
		}
		
		return t;
	}
	public static OfflineTranslator makeParsingTranslatorWithIgnore(EventDescriptor... events){
		IntegerParsingTranslator t = makeParsingTranslator(events);
		t.allowUnknown();
		return t;
	}

	
}
