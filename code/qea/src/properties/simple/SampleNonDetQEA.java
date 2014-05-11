package properties.simple;

import static structure.impl.other.Quantification.FORALL;
import structure.impl.qeas.QVar01_NoFVar_NonDet_QEA;

public class SampleNonDetQEA extends QVar01_NoFVar_NonDet_QEA {

	public SampleNonDetQEA() {
		super(8, 6, 1, FORALL);

		int a = 1;
		int b = 2;
		int c = 3;
		int d = 4;
		int e = 5;
		int f = 6;

		addTransitions(1, a, new int[] { 2, 4 },false);
		addTransition(2, b, 2,false);
		addTransition(2, c, 3,false);
		addTransitions(4, d, new int[] { 5, 7 },false);
		addTransition(5, e, 6,false);
		addTransition(7, f, 8,false);

		setStatesAsFinal(3, 6, 8);

	}

}
