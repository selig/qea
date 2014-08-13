package qea.properties.papers;

import static qea.structure.impl.other.Quantification.FORALL;
import qea.structure.impl.other.Transition;
import qea.structure.impl.qeas.QVar1_FVar_NonDet_FixedQVar_QEA;
import qea.structure.intf.Assignment;
import qea.structure.intf.Guard;

/**
 * A QEA giving the AuctionBidding example using nondeterminism
 * 
 * @author Giles Reger
 */

public class AuctionBiddingNDQEA extends QVar1_FVar_NonDet_FixedQVar_QEA {

	public AuctionBiddingNDQEA() {
		// we use three states, one event and 1 as the initial state
		super(3, 3, 1, FORALL, 2);

		// define the event names for convenience
		int BID = 1;
		// define quantified var name
		int ITEM = -1;
		// define free var names
		int AMOUNT = 1;
		int NEW_AMOUNT = 2;

		addTransition(1, BID, new Transition(ITEM, AMOUNT, 2));
		addTransition(
				2,
				BID,
				new Transition(ITEM, NEW_AMOUNT, Guard.isGreaterThan(
						NEW_AMOUNT, AMOUNT), Assignment.storeVar(AMOUNT,
						NEW_AMOUNT), 2));
		addTransition(
				2,
				BID,
				new Transition(ITEM, NEW_AMOUNT, Guard.isLessThanOrEqualTo(
						NEW_AMOUNT, AMOUNT), 3));

		setStatesAsFinal(1, 2);
		setStatesAsStrong(0, 3);
	}
}
