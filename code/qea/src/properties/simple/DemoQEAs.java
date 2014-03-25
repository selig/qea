package properties.simple;

import static structure.impl.other.Quantification.EXISTS;
import static structure.impl.other.Quantification.FORALL;
import structure.intf.Guard;
import structure.intf.QEA;
import creation.QEABuilder;

public class DemoQEAs {

	public static QEA detSingleWithPropNoF(){
		
		int y = -1;
		int F = 1;
		int G = 2;
		int H = 3;
		
		QEABuilder qea = new QEABuilder("detSingleNonFixedWithPropNoF");
		
		qea.addQuantification(FORALL, y);
		
		qea.addTransition(1, F, new int[]{y}, 2);
		qea.addTransition(2, G, 3);
		qea.addTransition(3, H, new int[]{y}, 4);
		
		qea.addFinalStates(4);
		
		return qea.make();
	}			
	
	public static QEA detSingleNonFixedWithProp(){
		
		int y = -1;
		int x = 1;
		int F = 1;
		int G = 2;
		int H = 3;
		
		QEABuilder qea = new QEABuilder("detSingleNonFixedWithProp");
		
		qea.addQuantification(FORALL, y);
		
		qea.addTransition(1, F, new int[]{x,y}, 2);
		qea.addTransition(2, G, 3);
		
		qea.startTransition(3);
		qea.eventName(H);
		qea.addVarArg(y);
		qea.addVarArg(x);
		qea.addGuard(Guard.isGreaterThanConstant(x,5));
		qea.endTransition(4);
		
		qea.addFinalStates(4);
		
		return qea.make();
	}		
	
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
		qea.setSkipStates(3);
		
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

	public static QEA makeNonDetProp(){
		
		QEABuilder qea = new QEABuilder("NonDetProp");
		
		int A = 1;
		int B = 2;
		int C = 3;
		
		qea.addTransition(1,A,2);
		qea.addTransition(1,A,1);
		qea.addTransition(1,B,3);
		qea.addTransition(1,B,1);
		qea.addTransition(1,C,4);
		qea.addTransition(1,C,1);		
		
		qea.addFinalStates(4);
		
		return qea.make();
	}
	
	public static QEA makeSimulateQuantification(){
		
		QEABuilder qea = new QEABuilder("SimulateQuantification");
		
		int A = 1;
		int B = 2;
		int x = 1;
		int y = 2;
				
		qea.addTransition(1,A, new int[]{x}, 1);
		qea.addTransition(1,B, new int[]{x}, 1);
		
		qea.addTransition(1,A, new int[]{x}, 2);
		
		qea.startTransition(2);
		qea.eventName(B);
		qea.addVarArg(y);
		qea.addGuard(Guard.isEqual(x, y));
		qea.endTransition(3);
		
		qea.startTransition(2);
		qea.eventName(B);
		qea.addVarArg(y);
		qea.addGuard(Guard.isNotEqual(x, y));
		qea.endTransition(2);		
		
		qea.startTransition(2);
		qea.eventName(A);
		qea.addVarArg(y);
		qea.addGuard(Guard.isEqual(x,y));
		qea.endTransition(4);
				
		qea.startTransition(2);
		qea.eventName(A);
		qea.addVarArg(y);
		qea.addGuard(Guard.isNotEqual(x,y));
		qea.endTransition(2);		
		
		qea.startTransition(3);
		qea.eventName(A);
		qea.addVarArg(y);
		qea.addGuard(Guard.isEqual(x,y));
		qea.endTransition(4);
		
		qea.startTransition(3);
		qea.eventName(B);
		qea.addVarArg(y);
		qea.addGuard(Guard.isEqual(x,y));
		qea.endTransition(4);		
		
		qea.startTransition(3);
		qea.eventName(A);
		qea.addVarArg(y);
		qea.addGuard(Guard.isNotEqual(x,y));
		qea.endTransition(3);
		
		qea.startTransition(3);
		qea.eventName(B);
		qea.addVarArg(y);
		qea.addGuard(Guard.isNotEqual(x,y));
		qea.endTransition(3);		
		
		qea.addFinalStates(4);		
		qea.setSkipStates(4);
		
		return qea.make();
	}	
	
}
