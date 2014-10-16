package qea.properties.competition.translators;

import qea.monitoring.impl.translators.DefaultTranslator;
import qea.monitoring.impl.translators.OfflineTranslator;
import qea.properties.Property;
import qea.properties.TranslatorMaker;
import qea.structure.impl.other.Verdict;

public class QEAOfflineTranslators implements TranslatorMaker {

	@Override
	public OfflineTranslator make(Property property) {
		switch (property) {
		case QEA_OFFLINE_ONE:
			return makeOne();
		case QEA_OFFLINE_TWO:
			return makeTwo();
		case QEA_OFFLINE_THREE:
			return makeThree();
		case QEA_OFFLINE_FOUR:
			return makeFour();
		case QEA_OFFLINE_FIVE:
			return makeFive();
		}
		return null;
	}

	public OfflineTranslator makeOne() {
		return new DefaultTranslator("ping","ack");
		
	}

	public OfflineTranslator makeTwo() {
		return new DefaultTranslator("grant","cancel");
		
	}

	public OfflineTranslator makeThree() {
		return new DefaultTranslator("command","succeed");		
	}

	public OfflineTranslator makeFour() {
		return new DefaultTranslator("request","grant","deny","rescind","cancel");		
	}

	public OfflineTranslator makeFive() {
		return new DefaultTranslator("conflict","grant","cancel");		
	}
}
