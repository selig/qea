package qeaExamples;

import qea.creation.QEABuilder;
import qea.structure.intf.QEA;
import static qea.structure.impl.other.Quantification.FORALL;
import static qea.structure.intf.Assignment.*;
import static qea.structure.intf.Guard.*;

public class SafeIterator {

	/*
	 * The SafeIterator property states that
	 * If an iterator i is created from a collection of size s then s is an upper bound
	 * on the number of times next can be called on i
	 * 
	 * There are two events:
	 * 		- iterator(i,size)
	 * 		- next(i)
	 */
	
	public static QEA make(){
		
		QEABuilder builder = new QEABuilder("SafeIterator");
		
		// Declare variables
		int i = -1;		// quantified need to be negative
		int size = 1;	// free variables need to be positive
		
		// Declare events
		int iterator = 1;
		int next = 2;
		
		// Declare states
		int start = 1;
		int created = 2;
		
		// Define quantification
		builder.addQuantification(FORALL,i);
		
		// Define transitions
		builder.addTransition(start,iterator,new int[]{i,size},created);
		builder.addTransition(created,next,i,isGreaterThanConstant(size,0),decrement(size),created);
		
		// Define acceptance
		builder.addFinalStates(start,created);
		
		return builder.make();
	}
	
	
	public static QEA oneImadeEarlier(){
		
		// Create Builder
		QEABuilder builder = new QEABuilder("SafeIterator");
		
		// Declare variables
		int i = -1;
		int size = 1;
		
		// Add Quantifications
		builder.addQuantification(FORALL, i);
		
		// Declare events
		int iterator = 1;
		int next = 2;
		
		// Declare states
		int start = 1;
		int created = 2;
		builder.setSkipStates(start);
		builder.addFinalStates(start,created);
		
		// Add transitions
		builder.addTransition(start, iterator, new int[]{i,size},created);
		builder.addTransition(
				created, next, i, 
				isGreaterThanConstant(size,0),
				decrement(size),
				created);

		// Return the QEA
		return builder.make();		
	}
	
}
