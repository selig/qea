package monitoring.impl.translators;

import properties.Property;

public interface TranslatorMaker {
	
	public OfflineTranslator make(Property property);

}
