package properties.papers;

import static structure.impl.other.Quantification.NONE;
import structure.impl.qeas.QVar01_NoFVar_Det_QEA;

/**
 * A QEA giving the very simple UseFile property
 * 
 * This is the propositional version - the only difference being a flag saying
 * it has no quantified variables.
 * 
 * @author Giles Reger
 */

public class UseFilePropositionalQEA extends QVar01_NoFVar_Det_QEA {

	public UseFilePropositionalQEA() {
		// we use two states, three events and 0 as the initial state
		super(2, 3, 1, NONE);

		// define the event names for convenience
		int OPEN = 1;
		int USE = 2;
		int CLOSE = 3;

		// hasnext_true allows us to call next
		addTransition(1, OPEN, 2,true);
		addTransition(2, USE, 2,true);
		addTransition(2, CLOSE, 1,true);

		// only state 1 is final as the file must be closed
		setStateAsFinal(1);
		
		setStateAsStrong(0);
	}
}
