package properties;

import static structure.impl.Quantification.FORALL;
import structure.impl.NonSimpleNonDetQEA;
import structure.impl.TransitionImpl;
import structure.intf.Assignment;
import structure.intf.Guard;

/**
 * A QEA giving the AuctionBidding example using nondeterminism
 * 
 * @author Giles Reger
 */

public class AuctionBiddingNDQEA extends NonSimpleNonDetQEA {

	public AuctionBiddingNDQEA() {
		// we use three states, one event and 1 as the initial state
		super(3, 3, 1, FORALL,2);

		// define the event names for convenience
		int BID = 1;
		// define free var names
		int AMOUNT = 0;
		int NEW_AMOUNT = 1;


		addTransition(1, BID, new TransitionImpl(AMOUNT,2));
		addTransition(2, BID, new TransitionImpl(NEW_AMOUNT,
				Guard.isGreaterThan(NEW_AMOUNT, AMOUNT),
				Assignment.store(AMOUNT, NEW_AMOUNT), 2));
		addTransition(2,BID, new TransitionImpl(NEW_AMOUNT,
				Guard.isLessThanOrEqualTo(NEW_AMOUNT,AMOUNT), 3));


		setStatesAsFinal(1,2);
	}
}
