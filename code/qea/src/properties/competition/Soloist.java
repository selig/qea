package properties.competition;

import properties.Property;
import properties.PropertyMaker;
import structure.intf.QEA;

public class Soloist implements PropertyMaker {

	@Override
	public QEA make(Property property) {
		switch(property){
			case SOLOIST_ONE : return makeOne();
			case SOLOIST_TWO : return makeTwo();
			case SOLOIST_THREE : return makeThree();
			case SOLOIST_FOUR : return makeFour();
			case SOLOIST_FIVE : return makeFive();
		}
		return null;
	}

	public QEA makeOne(){
		return null;
	}
	
	public QEA makeTwo(){
		return null;
	}
	
	public QEA makeThree(){
		return null;
	}
	
	public QEA makeFour(){
		return null;
	}
	
	public QEA makeFive(){
		return null;
	}	
	
}
