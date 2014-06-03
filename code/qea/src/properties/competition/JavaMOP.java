package properties.competition;

import static structure.impl.other.Quantification.FORALL;
import creation.QEABuilder;
import properties.Property;
import properties.PropertyMaker;
import properties.papers.*;
import structure.intf.Assignment;
import structure.intf.Guard;
import structure.intf.QEA;

public class JavaMOP implements PropertyMaker {

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
		// This is just the HasNext property we know and love
		return new HasNextQEA();
	}
	
	public QEA makeTwo(){
		
		/*
		 * Note - this has a disjoint alphabet, we should implement this optimisation!
		 */
		
		QEABuilder q = new QEABuilder("SafeLock");
		
		int LOCK = 1;
		int UNLOCK = 2;
		int thread = -1;
		int lock = -2;
		int count = 1;
		
		q.addQuantification(FORALL, thread);
		q.addQuantification(FORALL, lock);
		
		q.addTransition(1,LOCK,new int[]{thread,lock},Assignment.set(count, 1),2);
		q.addTransition(2,LOCK,new int[]{thread,lock},Assignment.increment(count),2);
		q.addTransition(2,UNLOCK,new int[]{thread,lock},
				Guard.isGreaterThanConstant(count, 1),Assignment.decrement(count),2);
		q.addTransition(2,UNLOCK,new int[]{thread,lock},
				Guard.isSemEqualToConstant(count,1),Assignment.set(count, 0),1);
		
		q.addFinalStates(1,2);
		
		QEA qea = q.make();
		qea.record_event_name("lock", LOCK);
		qea.record_event_name("unlock", UNLOCK);
		return qea;
	}
	
	public QEA makeThree(){
		//TODO - the A^n B^n C^n one
		return null;
	}
	
	public QEA makeFour(){
		return DaCapo.makeUnsafeMapIter();
	}
	
	public QEA makeFive(){
		// There isn't one
		return null;
	}	
	
}
