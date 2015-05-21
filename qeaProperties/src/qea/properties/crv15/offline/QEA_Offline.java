package qea.properties.crv15.offline;

import static qea.structure.impl.other.Quantification.*;
import qea.creation.QEABuilder;
import qea.structure.intf.Assignment;
import qea.structure.intf.Guard;
import qea.structure.intf.QEA;

public class QEA_Offline {

public static QEA makeCandidateSelection(){
		
		QEABuilder q = new QEABuilder("CandidateSelection");

		// Events
		int MEMBER = 1;
		int CANDIDATE = 2;
		int RANK = 3;
		// Quantified variables
		int V = -1;
		int P = -2;
		int C = -3;

		q.addQuantification(FORALL, V);
		q.addQuantification(EXISTS, P);
		q.addQuantification(FORALL, C);

		
		q.addTransition(1, MEMBER, new int[] { V, P }, 2);
		q.addTransition(2, CANDIDATE, new int[] { C, P }, 3);
		q.addTransition(3, RANK, new int[] { V, C }, 4);
		

		q.addFinalStates(2,4);
		q.setSkipStates(1,2,3,4);

		QEA qea = q.make();

		qea.record_event_name("member", 1);
		qea.record_event_name("candidate", 2);
		qea.record_event_name("rank", 3);
		
		return qea;		
		
	}	
	
public static QEA makeClientManagers(){
	
	QEABuilder q = new QEABuilder("ClientManagers");

	// Events
	int CREATE = 1;
	int ASSIGN = 2;
	// Quantified variables
	int C = -1;
	int M1 = -2;
	int M2 = -3;
	int A = -4;

	q.addQuantification(FORALL, C);
	q.addQuantification(EXISTS, M1);
	q.addQuantification(EXISTS, M2);
	q.addQuantification(FORALL, A);

	
	q.addTransition(1, CREATE, new int[] { C, A }, 2);
	q.addTransition(2, ASSIGN, new int[] { A, M1 }, 3);
	q.addTransition(2, ASSIGN, new int[] { A, M2 }, 3);
	
	q.addFinalStates(1,3);
	q.setSkipStates(1,2,3);

	QEA qea = q.make();

	qea.record_event_name("createAccount", 1);
	qea.record_event_name("assign", 2);
	
	return qea;		
	
}	


	public static QEA makeCallReturn(){
		
		QEABuilder q = new QEABuilder("CallReturn");

		// Events
		int CALL = 1;
		int RET = 2;
		// Quantified variables
		int M1 = -1;
		int M2 = -2;

		q.addQuantification(FORALL, M1);
		q.addQuantification(FORALL, M2,Guard.isNotEqual(M1, M2));

		
		q.addTransition(1, CALL, new int[] { M2 }, 1);
		q.addTransition(1, RET, new int[] { M2 }, 1);
		q.addTransition(1, CALL, new int[] { M1 }, 2);
		q.addTransition(2, RET, new int[] { M1 }, 1);
		q.addTransition(2, CALL, new int[] { M2 }, 3);
		q.addTransition(3, RET, new int[] { M2 }, 2);

		q.addFinalStates(1, 2, 3);

		QEA qea = q.make();

		qea.record_event_name("call", 1);
		qea.record_event_name("return", 2);
		
		return qea;		
		
	}
	
public static QEA makeAuctionBidding(){
		
		QEABuilder q = new QEABuilder("AuctionBidding");

		// Events
		int CREATE = 1;
		int BID = 2;
		int SELL = 3;
		int END = 4;
		// Quantified variables
		int I = -1;		
		// Free variables
		int min = 1;
		int period = 2;
		int amount = 3;
		int last_amount = 4;

		q.addQuantification(FORALL, I);
		

		q.addTransition(1, CREATE, new int[] { I, min, period }, 
				Assignment.setVal(last_amount, 0), 2);
		q.addTransition(1, END, 2);
		
		q.addTransition(2, END, Guard.isGreaterThanConstant(period,1), Assignment.decrement(period),2);
		q.addTransition(2, END, Guard.isEqualToConstant(period,1), 4);
		q.addTransition(2, BID, new int[]{ I, amount }, 
				Guard.and(Guard.isGreaterThan(amount,last_amount),
						  Guard.isLessThan(amount,min)),
				Assignment.storeVar(last_amount, amount), 2);
		q.addTransition(2, BID, new int[]{ I, amount }, 
				Guard.and(Guard.isGreaterThan(amount,last_amount),
						  Guard.isGreaterThanOrEqualTo(amount,min)),
				Assignment.storeVar(last_amount, amount), 3);	
		
		
		q.addTransition(3, BID, new int[]{I,amount},Guard.isGreaterThan(amount, last_amount),
													Assignment.storeVar(last_amount,amount), 3);
		q.addTransition(3, SELL, I, 4);
		q.addTransition(3, END, Guard.isGreaterThanConstant(period,1), Assignment.decrement(period),3);

		q.addTransition(4, END, 4);
		
		q.addFinalStates(1, 2, 4);

		QEA qea = q.make();

		qea.record_event_name("call", 1);
		qea.record_event_name("return", 2);
		
		return qea;		
		
	}	
	


}
