package properties.simple;

import static structure.impl.Quantification.FORALL;
import structure.impl.QVar01_NoFVar_NonDet_QEA;

public class SampleNonDetQEA extends QVar01_NoFVar_NonDet_QEA {

	public SampleNonDetQEA() {
		super(8, 6, 1, FORALL);

		int a = 1;
		int b = 2;
		int c = 3;
		int d = 4;
		int e = 5;
		int f = 6;

		addTransitions(1, a, new int[] { 2, 4 });
		addTransition(2, b, 2);
		addTransition(2, c, 3);
		addTransitions(4, d, new int[] { 5, 7 });
		addTransition(5, e, 6);
		addTransition(7, f, 8);

		setStatesAsFinal(3, 6, 8);

	}

}
