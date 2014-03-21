package properties.simple;

import static structure.impl.other.Quantification.EXISTS;
import static structure.impl.other.Quantification.FORALL;
import structure.intf.Guard;
import structure.intf.QEA;
import creation.QEABuilder;

public class DemoQEAs {

	
	public static QEA detOneFreeWithOneProp(){
		
		int y = -1;
		int x = 1;
		int F = 1;
		int G = 2;
		
		QEABuilder qea = new QEABuilder("detOneFreeWithOneProp");
		
		qea.addQuantification(FORALL, y);
		
		qea.addTransition(1, F, 2);
		
		qea.startTransition(2);
		qea.eventName(G);
		qea.addVarArg(y);
		qea.addVarArg(x);
		qea.addGuard(Guard.isGreaterThanConstant(x,5));
		qea.endTransition(3);
		
		qea.addFinalStates(3);
		
		return qea.make();
	}	
	
	public static QEA detFreeWithOneProp(){
		
		int x = 1;
		int F = 1;
		int G = 2;
		
		QEABuilder qea = new QEABuilder("detFreeWithOneProp");
		
		qea.addTransition(1, F, 2);
		
		qea.startTransition(2);
		qea.eventName(G);
		qea.addVarArg(x);
		qea.addGuard(Guard.isGreaterThanConstant(x,5));
		qea.endTransition(3);
		
		qea.addFinalStates(3);
		
		return qea.make();
	}
	
	public static QEA makePropositionalDepend() {

		int x = -1;
		int y = 1;
		int E = 1;
		int F = 2;

		QEABuilder qea = new QEABuilder("propositionalDepend");

		qea.addQuantification(EXISTS, x);

		qea.addTransition(1, E, new int[] { y }, 2);

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

		qea.addQuantification(FORALL, x);

		qea.addTransition(1, E, new int[] { x, y }, 2);
		qea.addTransition(1, E, new int[] { y, x }, 3);
		qea.addTransition(2, F, new int[] { x }, 4);

		qea.addTransition(3, F, new int[] { x }, 3);

		qea.addFinalStates(1, 3);

		return qea.make();

	}

}
