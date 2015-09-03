package qea.properties.crv15.java;

import static qea.structure.impl.other.Quantification.EXISTS;
import qea.creation.QEABuilder;
import qea.structure.intf.QEA;

public class JavaMOP_3 {

	// 1-3 are the same as in RVMonitor_4
	
	public static QEA make_four(){
		QEABuilder b = new QEABuilder("safeiter");
		
		// Events
		int iter = 1; int next = 2; int update = 3;
		// Quantified variables
		int c = -1; int i = -2;
		b.addQuantification(EXISTS, c);
		b.addQuantification(EXISTS, i);
		b.setNegated(true);
		
		//States
		int start = 1;
		int hasiterator = 2;
		int updated = 4;
		int error = 5;
		b.addFinalStates(error); // negated QEA
		
		b.addTransition(start, iter, new int[]{c,i}, hasiterator);
		b.addTransition(hasiterator, update, c, updated);
		b.addTransition(updated, next, i, error);
		
		b.setSkipStates(start,hasiterator,updated);
		
		return b.make();
	}
	
}
