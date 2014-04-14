package properties.competition;

import properties.Property;
import properties.PropertyMaker;
import structure.intf.QEA;

public class Stepr implements PropertyMaker {

	@Override
	public QEA make(Property property) {
		switch(property){
			case STEPR_ONE : return makeOne();
			case STEPR_TWO : return makeTwo();
			case STEPR_THREE : return makeThree();
			case STEPR_FOUR : return makeFour();
			case STEPR_FIVE : return makeFive();
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
