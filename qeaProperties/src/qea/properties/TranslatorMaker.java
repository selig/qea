package qea.properties;

import qea.monitoring.impl.translators.OfflineTranslator;

public interface TranslatorMaker {
	
	public OfflineTranslator make(Property property);

}
