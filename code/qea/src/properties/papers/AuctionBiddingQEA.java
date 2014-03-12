package properties.papers;

import static structure.impl.Quantification.FORALL;
import structure.impl.QVar01_FVar_Det_FixedQVar_QEA;
import structure.impl.TransitionImpl;
import structure.intf.Assignment;
import structure.intf.Guard;

/**
 * A QEA giving the AuctionBidding example
 * 
 * @author Giles Reger
 */

public class AuctionBiddingQEA extends QVar01_FVar_Det_FixedQVar_QEA {

	public AuctionBiddingQEA() {
		// we use two states, one event and 1 as the initial state
		super(2, 3, 1, FORALL, 2);

		// define the event names for convenience
		int BID = 1;
		// define quantified var name
		int ITEM = -1;
		// define free var names
		int AMOUNT = 0;
		int NEW_AMOUNT = 1;

		addTransition(1, BID, new TransitionImpl(ITEM, AMOUNT, 2));
		addTransition(
				2,
				BID,
				new TransitionImpl(ITEM, NEW_AMOUNT, Guard.isGreaterThan(
						NEW_AMOUNT, AMOUNT), Assignment.store(AMOUNT,
						NEW_AMOUNT), 2));

		setStatesAsFinal(1, 2);
	}
}