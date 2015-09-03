package qea.properties.crv15.java;

import static qea.structure.impl.other.Quantification.EXISTS;
import qea.creation.QEABuilder;
import qea.structure.intf.QEA;

public class Daniel_4 {

	// 1 is just safeiter as in JavaMOP
	
	public static QEA make_two(){
		QEABuilder b = new QEABuilder("robustSafeIter");
		
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
		int updatedOnce = 4;
		int updatedTwice = 5;
		int error = 6;
		b.addFinalStates(error); // negated QEA
		
		b.addTransition(start, iter, new int[]{c,i}, hasiterator);
		b.addTransition(hasiterator, update, c, updatedOnce);
		b.addTransition(updatedOnce, update, c, updatedTwice);
		b.addTransition(updatedTwice, next, i, error);
		
		b.setSkipStates(start,hasiterator,updatedOnce,updatedTwice);		
		
		return b.make();
	}
	
}
