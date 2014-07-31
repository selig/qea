package properties;

import monitoring.impl.translators.OfflineTranslator;

public interface TranslatorMaker {
	
	public OfflineTranslator make(Property property);

}
