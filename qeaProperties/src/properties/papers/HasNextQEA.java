package properties.papers;

import static structure.impl.other.Quantification.FORALL;
import structure.impl.qeas.QVar01_NoFVar_Det_QEA;

/**
 * A QEA giving the canonical HasNext property
 * 
 * This property is about the usage of java.util.Iterator and is used a lot as
 * an example in monitoring
 * 
 * The behaviour is that if we call next then hasnext must have previously
 * returned true since the last call of next. I extend this so that if hasnext
 * returns false then it cannot return true later.
 * 
 * This is described further here -
 * http://en.wikipedia.org/wiki/Runtime_verification#HasNext But I think this
 * implementation of the property is more accurate
 * 
 * Note that the failure/error state is implicit
 * 
 * @author Giles Reger
 */

public class HasNextQEA extends QVar01_NoFVar_Det_QEA {

	public HasNextQEA() {
		// we use three states, three events and 0 as the initial state
		super(3, 3, 1, FORALL);

		// define the event names for convenience
		int NEXT = 1;
		int HASNEXT_TRUE = 2;
		int HASNEXT_FALSE = 3;

		// hasnext_true allows us to call next
		addTransition(1, HASNEXT_TRUE, 2, false);
		addTransition(2, HASNEXT_TRUE, 2, false);
		addTransition(2, NEXT, 1, false);

		// hasnext_false means the Iterator is finished
		addTransition(1, HASNEXT_FALSE, 3, false);
		addTransition(3, HASNEXT_FALSE, 3, false);

		setStatesAsFinal(1, 2, 3);

		setStatesAsStrong(0);
	}
}
