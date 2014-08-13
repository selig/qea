package qea.properties.papers;

import static qea.structure.impl.other.Quantification.FORALL;
import qea.structure.impl.qeas.QVar01_NoFVar_Det_QEA;

/**
 * A QEA giving the very simple UseFile property
 * 
 * This states that a file should be opened before being used and should
 * eventually be closed.
 * 
 * @author Giles Reger
 */

public class UseFileQEA extends QVar01_NoFVar_Det_QEA {

	public UseFileQEA() {
		// we use two states, three events and 0 as the initial state
		super(2, 3, 1, FORALL);

		// define the event names for convenience
		int OPEN = 1;
		int USE = 2;
		int CLOSE = 3;

		// hasnext_true allows us to call next
		addTransition(1, OPEN, 2, false);
		addTransition(2, USE, 2, false);
		addTransition(2, CLOSE, 1, false);

		// only state 1 is final as the file must be closed
		setStateAsFinal(1);

		setStateAsStrong(0);
	}
}
