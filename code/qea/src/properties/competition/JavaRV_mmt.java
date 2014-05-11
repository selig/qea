package properties.competition;

/*
 * Taken from the JavaRV-MMT benchmarks - check if they have non mmt benchmarks too
 */

import static structure.impl.other.Quantification.FORALL;
import creation.QEABuilder;
import properties.Property;
import properties.PropertyMaker;
import structure.intf.Assignment;
import structure.intf.Guard;
import structure.intf.QEA;

public class JavaRV_mmt implements PropertyMaker {

	@Override
	public QEA make(Property property) {
		switch(property){
			case JAVARV_MMT_ONE : return makeOne();
			case JAVARV_MMT_TWO : return makeTwo();
			case JAVARV_MMT_THREE : return makeThree();
			case JAVARV_MMT_FOUR : return makeFour();
			case JAVARV_MMT_FIVE : return makeFive();
		}
		return null;
	}

	public QEA makeOne(){
		QEABuilder q = new QEABuilder("JAVARV_MMT_ONE");

		int STEP = 1;
		int counter_old = 1;
		int counter_new = 2;

		q.addTransition(1, STEP, new int[] { counter_old }, 2);

		q.startTransition(2);
		q.eventName(STEP);
		q.addVarArg(counter_new);
		q.addGuard(Guard.isGreaterThan(counter_new, counter_old));
		q.addAssignment(Assignment.store(counter_old, counter_new));
		q.endTransition(2);
		
		q.addFinalStates(1);

		QEA qea = q.make();

		qea.record_event_name("step", 1);

		return qea;
	}
	
	public QEA makeTwo(){
		QEABuilder q = new QEABuilder("JAVARV_MMT_TWO");
		
		int REQUEST = 1;
		int RESPONSE = 2;
		
		int s = -1;
		int p = 1;
		
		q.addQuantification(FORALL, s);
		
		q.addTransition(1, REQUEST, new int[]{s}, 2);
		q.addTransition(2, RESPONSE, new int[]{s,p}, 1);
		
		q.addFinalStates(1);
		
		QEA qea = q.make();

		qea.record_event_name("request", 1);
		qea.record_event_name("response", 2);

		return qea;		
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
