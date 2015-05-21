package qea.properties.crv15.offline;

import qea.creation.QEABuilder;
import static qea.structure.intf.Guard.*;
import static qea.structure.intf.Assignment.*;
import qea.structure.intf.Guard;
import qea.structure.intf.QEA;

public class OCLR_3 {

	/*
	 * globally c preceding at least 100 tu d;
	 * 
	 * This property can be expressed using the globally scope and the precedence 
	 * pattern. The globally pattern corresponds to the entire trace. The pattern 
	 * states that whenever event d occurs, event c must have occurred before and 
	 * the time distance from the occurrence of event c must be at least 100 time 
	 * units.
	 */
	public static QEA make_one(){
		QEABuilder b = new QEABuilder("offline_team3_bench1");
		
		// Event names
		int C = 1; int D = 2;
		
		// Free variables
		int t1 = 1; int t2 = 2;
		
		//Transitions
		b.addTransition(1,D,t1,3);
		// In state 2 we have seen a C and the last time we saw one was at t1
		b.addTransition(1, C, t1, 2);
		b.addTransition(2, C, t1, 2);
		// From state we go to the failing state 3
		// when we see D with t2 more than 100 greater than t2
		b.addTransition(2, D, t2, differenceGreaterThanVal(t1, t2, 100), 3);
		
		// state 3 is not accepting, the others are
		b.addFinalStates(1,2);
		
		// All transitions not mentioned are safe
		b.setAllSkipStates();
		
		return b.make();
	}
	
	/*
	 * before c at least 1000 tu b responding at most 1000 tu a;
	 * 
	 * This property can be expressed using the before scope and the 
	 * response pattern. The scope of this expression selects the trace 
	 * segment from the beginning to the instant (included) 1000 time 
	 * units before the first occurrence of event c from the trace. 
	 * The pattern states that within the segment, whenever event a 
	 * occurs, event b should occur within 1000 time units (included) 
	 * after its occurrence.
	 */
	public static QEA make_two(){
		QEABuilder b = new QEABuilder("offline_team3_benchmark2");
		
		// Event names
		int A=1; int B=2; int C=3;
		// Free variables
		int t1=1; int t2=2;
		
		//States
		int start    = 1;
		int finished = 2; // after seeing a C
		int waiting  = 3; // seen an A, waiting for a B
		int failed   = 4;
		
		//Transitions
		
		// Seeing C takes us to a 'finished' accepting state
		b.addTransition(start, C, t1, finished);
		
		// Seeing an A starts a period
		b.addTransition(start, A, t1, waiting);
		// To allow future periods we must also stay in the start state
		b.addTransition(start, A, t1, start);
		
		// If a C occurs such that the A was not in scope we cancel the waiting
		b.addTransition(waiting, C, t2, differenceGreaterThanVal(t1,t2,1000), finished);
		
		// If a waited-for B occurs too late we fail
		b.addTransition(waiting,B,t2,differenceGreaterThanVal(t1,t2,1000),failed);
		// If a waited-for B occurs within time then that period finishes
		b.addTransition(waiting,B,t2,differenceLessThanOrEqualToVal(t1,t2,1000),finished);
		
		// If we detect the waiting period has expired through an A or C event then we fail
		b.addTransition(waiting,A,t2,differenceGreaterThanVal(t1,t2,1000),failed);
		b.addTransition(waiting,C,t2,differenceGreaterThanVal(t1,t2,1000),failed);
		
		// Due to the non-determinism of this specification and the acceptance
		// condition of QEA we need to 'negate' the QEA and set the bad states
		// to accepting. This is because the acceptance condition is related to
		// a single path existing to an accepting state, whereas here we have
		// captured failure as a single path existing to a non-accepting state.
		b.setNegated(true);
		b.addFinalStates(waiting,failed);
		
		// Non-mentioned transitions are safe
		b.setAllSkipStates();
		
		return b.make();
	}
	/*
	 * after 2 b never c;
	 * 
	 * This property can be expressed using the after scope and the absence 
	 * pattern. The scope of this expression selects the trace segment from 
	 * the second occurrence of event b (not included) to the end of the trace. 
	 * The pattern states that within the segment, no occurrence of event c 
	 * should be found.
	 */
	public static QEA make_three(){
		QEABuilder b = new QEABuilder("offline_team3_benchmark3");
		
		// Events
		int B = 1; int C = 2;
	
		// Transitions
		b.addTransition(1,B,2);
		b.addTransition(2,B,3);
		b.addTransition(3,C,4);
		
		// If we reach state 4 we have seen two B's then a C so fail
		b.addFinalStates(1,2,3);
		
		// Other B and C events can be safely ignored
		b.setAllSkipStates();
				
		return b.make();
	}
	
	/*
	 * between a and c always b;
	 * 
	 * This property can be expressed by the between-and scope and the 
	 * universality pattern. The scope of this expression selects all 
	 * the trace segments delimited by event a (not included) and event 
	 * c (not included). The pattern states that within the segment, 
	 * every occurrence of an event should be event b.
	 */
	public static QEA make_four(){
		QEABuilder b = new QEABuilder("offline_team3_benchmark4");
		
		// Events
		int A = 1; int B = 2; int C = 3;
	
		// Transitions
		b.addTransition(1,A,2);
		b.addTransition(2,C,1);
		b.addTransition(2,B,2);
		
		b.addFinalStates(1,2);
				
		return b.make();
	}
	
	/*
	 *  between 2 a and c at least 100 tu eventually at most 5 b;
	 *  
	 *  his property can be expressed by the between-and scope and the 
	 *  existence pattern. The scope of this expression selects the trace 
	 *  segment from the second occurrence of event a (not included) and 
	 *  the instant that is at least 100 time units (included) before the 
	 *  next occurrence of event c. The pattern states that within the 
	 *  segment, the number of occurrences of event b should not exceeds 5.
	 */
	public static QEA make_five(){
		QEABuilder b = new QEABuilder("offline_team3_benchmark5");
		
		//Events
		int A=1; int B=2; int C=3;
		
		// Free variables
		int t1=1; int t2=2; int c=3;
		
		// States
		int start  = 1;
		int seenA  = 2;
		int seenAA = 3; // after two A events
		int finished = 4; // after two A events and a C
		int counting = 5;
		int bad_count = 6;
		int failed = 7;
		
		// Transitions
		b.addTransition(start, A, t1, seenA);
		b.addTransition(seenA, A, t1, seenAA);
		b.addTransition(seenAA, C, t1, finished);
		
		// Start counting on a B
		b.addTransition(seenAA, B, t1, setVal(c, 1), counting);
		
		// Do the counting
		b.addTransition(counting, B, t1, isLessThanConstant(c, 4), increment(c), counting);
		b.addTransition(counting, B, t1, isGreaterThanConstant(c, 3), increment(c), bad_count);
		// Stop counting on a C
		b.addTransition(counting, C, t2,finished);
		
		// If we see a C after a bad count then we need to check to see if the badness happened
		// more than 100 units of time ago, if so we fail, otherwise we finish
		b.addTransition(bad_count, C, t2, differenceLessThanOrEqualToVal(t1,t2,100),finished);
		b.addTransition(bad_count, C, t2, differenceGreaterThanVal(t1,t2,100),failed);
		
		b.addFinalStates(start,seenA,seenAA,counting,finished);
		b.setAllSkipStates();
		
		return b.make();
	}

	
}
