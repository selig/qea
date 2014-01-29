package properties;

import structure.impl.SimplestQEA;


/**
 * A QEA giving the canonical HasNext property
 * 
 * @author Giles Reger
 */

public class HasNextQEA extends SimplestQEA {

	
	public HasNextQEA() {
		// we use three states, three events and 0 as the initial state
		super(3,3,1);
		
		// define the event names for convenience
		int NEXT = 1;
		int HASNEXT_TRUE = 2;
		int HASNEXT_FALSE = 3;
		
		// hasnext_true allows us to call next
		addTransition(1,HASNEXT_TRUE,2);
		addTransition(2,HASNEXT_TRUE,2);
		addTransition(2,NEXT,1);
		
		// hasnext_false means the Iterator is finished
		addTransition(1,HASNEXT_FALSE,3);
		addTransition(3,HASNEXT_FALSE,3);
		
		setStatesAsFinal(1,2,3);
	}
}
