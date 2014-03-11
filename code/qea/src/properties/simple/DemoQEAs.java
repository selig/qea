package properties.simple;

import static structure.impl.Quantification.*;
import creation.QEABuilder;
import structure.intf.Guard;
import structure.intf.QEA;

public class DemoQEAs {

public static QEA makePropositionalDepend() {
		
		int x = -1;
		int y = 1;
		int E = 1;
		int F = 2;
		
		QEABuilder qea = new QEABuilder("propositionalDepend");
		
		qea.addQuantification(EXISTS,x);
		
		qea.addTransition(1,E, new int[]{y},2);
		
		qea.startTransition(2);
		qea.eventName(F);
		qea.addVarArg(x);
		qea.addGuard(Guard.isGreaterThan(x, y));
		qea.endTransition(3); 
		
		qea.addFinalStates(3);
		
		return qea.make();
		
	}
	
public static QEA makeDemoNonDetChoice() {
		
		int x = -1;
		int y = 1;
		int E = 1;
		int F = 2;
		
		QEABuilder qea = new QEABuilder("DemoNonDetChoiceQEA");
		
		qea.addQuantification(FORALL,x);
		
		qea.addTransition(1,E, new int[]{x,y},2);
		qea.addTransition(1,E, new int[]{y,x},3);
		qea.addTransition(2,F,new int[]{x},4);
		
		qea.addTransition(3,F,new int[]{x},3);
		
		qea.addFinalStates(1,4);
		
		return qea.make();
		
	}

}
