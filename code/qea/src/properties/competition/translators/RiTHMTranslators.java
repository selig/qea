package properties.competition.translators;

import monitoring.impl.translators.OfflineTranslator;
import monitoring.impl.translators.TranslatorMaker;
import properties.Property;

public class RiTHMTranslators implements TranslatorMaker {

	@Override
	public OfflineTranslator make(Property property) {
		switch (property) {
		case RITHM_ONE:
			return makeOne();
		case RITHM_TWO:
			return makeTwo();
		case RITHM_THREE:
			return makeThree();
		case RITHM_FOUR:
			return makeFour();
		case RITHM_FIVE:
			return makeFive();
		}
		return null;
	}

	public OfflineTranslator makeOne() {
		// TODO Auto-generated method stub
		return null;
	}

	public OfflineTranslator makeTwo() {
		// TODO Auto-generated method stub
		return null;
	}

	public OfflineTranslator makeThree() {
		// TODO Auto-generated method stub
		return null;
	}

	public OfflineTranslator makeFour() {
		// TODO Auto-generated method stub
		return null;
	}

	public OfflineTranslator makeFive() {
		// TODO Auto-generated method stub
		return null;
	}

}
