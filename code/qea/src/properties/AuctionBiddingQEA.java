package properties;

import static structure.impl.Quantification.FORALL;
import structure.impl.NonSimpleDetQEA;
import structure.impl.TransitionImpl;
import structure.intf.Assignment;
import structure.intf.Guard;

/**
 * A QEA giving the AuctionBidding example
 * 
 * @author Giles Reger
 */

public class AuctionBiddingQEA extends NonSimpleDetQEA {

	public AuctionBiddingQEA() {
		// we use two states, two (or is it one?) events and 1 as the initial state
		super(2, 3, 1, FORALL,2);

		// define the event names for convenience
		int BID = 1;
		// define free var names
		int AMOUNT = 0;
		int NEW_AMOUNT = 1;


		addTransition(1, BID, new TransitionImpl(AMOUNT,2));
		addTransition(1, BID, new TransitionImpl(NEW_AMOUNT,
				Guard.isGreaterThan(NEW_AMOUNT, AMOUNT),
				Assignment.store(AMOUNT, NEW_AMOUNT), 1));


		setStatesAsFinal(1,2);
	}
}
