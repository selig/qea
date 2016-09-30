package qeaExamples;

import qea.creation.QEABuilder;
import static qea.structure.impl.other.Quantification.EXISTS;
import qea.structure.intf.QEA;

public class UnsafeMapIter {

	/*
	 * The UnsafeMapIter property states that
	 * There does not exist a Map m, Collection c and Iterator i such that when c is created from m
	 * and i is created from c, m is updated then i is used.
	 * 
	 * Note: this negative formulation matches the common matching-failure formulation
	 * 
	 * There are four events:
	 * 		- create(m,c)
	 * 		- iterator(c,i)
	 * 		- update(m)
	 * 		- use(i)
	 * 
	 */
	
	public static QEA make(){
		
		// Create Builder
		QEABuilder builder = new QEABuilder("UnsafeMapIter");
		
		// Declare variables (quantified so negative)
		int m = -1;
		int c = -2;
		int i = -3;
				
		// Add quantifications	not Exists m, Exists c, Exists i
		builder.negate();
		builder.addQuantification(EXISTS, m);
		builder.addQuantification(EXISTS, c);
		builder.addQuantification(EXISTS, i);
				
		// Declare events
		int create = 1;
		int iterator = 2;
		int update  = 3;
		int use     = 4;		
		
		// Declare states
		int start = 1;
		int createdC = 2;
		int createdI = 3;
		int updated = 4;
		int failure = 5;				
		
		// Add transitions
		builder.addTransition(start, create, new int[]{m, c}, createdC);
		builder.addTransition(createdC, iterator, new int[]{c,i}, createdI);
		builder.addTransition(createdI, update, m, updated);
		builder.addTransition(updated, use, i, failure);	
		
		// Define acceptance
		builder.addFinalStates(failure);
		builder.setAllSkipStates(); // needs to happen here
		
		return builder.make();
		
	}
	
}
