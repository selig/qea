package properties;

import structure.impl.SimplestQEA;


/**
 * A QEA giving the canonical HasNext property
 * 
 * @author Giles Reger
 */

public class HasNextQEA extends SimplestQEA {

	
	public HasNextQEA() {
		// we use four states, three events and 0 as the initial state
		super(4,3,0);
		
		// define the event names for convenience
		int NEXT = 0;
		int HASNEXT_TRUE = 1;
		int HASNEXT_FALSE = 2;
		
		// hasnext_true allows us to call next
		addTransition(initialState,HASNEXT_TRUE,1);
		addTransition(1,HASNEXT_TRUE,1);
		addTransition(1,NEXT,initialState);
		
		// hasnext_false means the Iterator is finished
		addTransition(initialState,HASNEXT_FALSE,2);
		addTransition(2,HASNEXT_FALSE,2);
		
		// next from initial state is incorrect
		addTransition(initialState,NEXT,3);
		
		setStateAsFinal(1);
		setStateAsFinal(2);
	}
}
