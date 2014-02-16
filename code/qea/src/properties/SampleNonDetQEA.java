package properties;

import structure.impl.SimpleNonDetQEA;
import static structure.impl.Quantification.*;

public class SampleNonDetQEA extends SimpleNonDetQEA{

	
	public SampleNonDetQEA() {
		super(8, 6, 1, FORALL);
		
		int a = 1;
		int b = 2;
		int c = 3;
		int d = 4;
		int e = 5;
		int f = 6;
		
		addTransition(1,a,new int[]{2,4});
		//TODO it would be nice to have a addTransition(int,int,int) version
		addTransition(2,b,new int[]{2});
		addTransition(2,c,new int[]{3});
		addTransition(4,d,new int[]{5,7});
		addTransition(5,e,new int[]{6});
		addTransition(7,f,new int[]{8});
		
		setStatesAsFinal(3,6,8);
		
	}

}
