package qea.properties.crv15.offline;

import qea.creation.QEABuilder;
import static qea.structure.intf.Guard.*;
import static qea.structure.intf.Assignment.*;
import qea.structure.intf.Assignment;
import qea.structure.intf.Guard;
import qea.structure.intf.QEA;
import static qea.structure.impl.other.Quantification.*;

public class RVMonitor_4 {

	public static QEA make_one(){
		QEABuilder b = new QEABuilder("safemapiter");
		
		// Events
		int create = 1; int iter = 2; int use = 3; int update = 4;
		// Quantified variables
		int m = -1; int c = -2; int i = -3;
		b.addQuantification(EXISTS, m);
		b.addQuantification(EXISTS, c);
		b.addQuantification(EXISTS, i);
		b.setNegated(true);
		
		//States
		int start = 1;
		int created = 2;
		int hasiterator = 3;
		int updated = 4;
		int error = 5;
		b.addFinalStates(error); // negated QEA
		
		b.addTransition(start, create, new int[]{m,c}, created);
		b.addTransition(created, iter, new int[]{c,i}, hasiterator);
		b.addTransition(hasiterator, update, m, updated);
		b.addTransition(updated, use, i, error);
		
		b.setSkipStates(start,created,hasiterator,updated);
		
		return b.make();
	}
	
	public static QEA make_two(){
		QEABuilder b = new QEABuilder("safefilewriter");
		
		//Events
		int open=1; int write=2; int close=3;
		// Quantified variables
		int fileWriter = -1;
		b.addQuantification(FORALL,fileWriter);
		
		//States
		int start = 1;
		int lastWasClosed = 2;
		b.addFinalStates(start,lastWasClosed);
		
		// Valid transitions
		b.addTransition(start, open, fileWriter, start);
		b.addTransition(start, write, fileWriter, start);
		b.addTransition(start, close, fileWriter, lastWasClosed);
		
		b.addTransition(lastWasClosed, open, fileWriter, start);
		b.addTransition(lastWasClosed, close, fileWriter, lastWasClosed);		
				
		return b.make();
	}
	
	public static QEA make_three(){
		QEABuilder b = new QEABuilder("hasnext");
		
		// Events
		int hasnext = 1; int next = 2;
		// Free variables
		int ret = 1;
		// Quantified variables
		int i = -1 ;
		b.addQuantification(FORALL, i);
		
		// States
		int unsafe = 1;
		int safe = 2;
		int error = 3;
		
		b.addTransition(unsafe, hasnext, new int[]{i,ret}, isTrue(ret), safe);
		b.addTransition(unsafe, next, i, error);
		b.addTransition(safe,  next, i, unsafe);
		
		b.addFinalStates(unsafe,safe);
		b.setAllSkipStates();
		
		return b.make();
	}
	
	public static QEA make_four(){
		QEABuilder b = new QEABuilder("EqualityCheck");
		
		// Events
		int A = 1; int B = 2; int C = 3; int DONE = 4;
		// Free variables
		int as = 1; int bs = 2; int cs = 3;
		// Quantified variables
		int o = -1;
		b.addQuantification(FORALL, o);

		// State 1 indicates there is an equal number of as, bs and cs
		// State 2 indicates there is an unequal number of as, bs and cs
		// State 3 is an okay state and state 4 an error state
		b.addFinalStates(1,3);
		
		// If we move away from state 1 they are no longer equal
		// Note that ensure sets a variable if it has not been set
		b.addTransition(1,A,o,list(incrementOrSet(as),ensure(bs,0),ensure(cs,0)), 2);
		b.addTransition(1,B,o,list(incrementOrSet(bs),ensure(as,0),ensure(cs,0)), 2);
		b.addTransition(1,C,o,list(incrementOrSet(cs),ensure(as,0),ensure(bs,0)), 2);

		b.addTransition(1,DONE,o,3);
		b.addTransition(2,DONE,o,4);
		
		// If we are one less than the two others then we return to state 1
		// i.e. after this event we will have seen an equal number
		b.addTransition(2,A,o,and(differenceEqualToVal(bs,as,1),differenceEqualToVal(cs,as,1)),
							increment(as), 1);
		b.addTransition(2,B,o,and(differenceEqualToVal(as,bs,1),differenceEqualToVal(cs,bs,1)),
				increment(bs), 1);
		b.addTransition(2,C,o,and(differenceEqualToVal(as,cs,1),differenceEqualToVal(bs,cs,1)),
				increment(cs), 1);		

		// Otherwise we stay still and increment
		b.addTransition(2, A,o, or(differenceNotEqualToVal(bs,as,1),differenceNotEqualToVal(cs,as,1)),
					increment(as), 2);
		b.addTransition(2, B,o, or(differenceNotEqualToVal(as,bs,1),differenceNotEqualToVal(cs,bs,1)),
					increment(bs), 2);
		b.addTransition(2, C,o, or(differenceNotEqualToVal(as,cs,1),differenceNotEqualToVal(bs,cs,1)),
					increment(cs), 2);
						
		return b.make();
	}
	
	
	public static QEA make_five(){
		QEABuilder b = new QEABuilder("SafeLock");

		// Events
		int LOCK = 1; int UNLOCK = 2; int BEGIN = 3; int END = 4;
		// Free variable
		int count = 1;
		int depth = 2;
		// Quantifications
		int thread = -1; int lock = -2;
		b.addQuantification(EXISTS, thread);
		b.addQuantification(EXISTS, lock);
		b.setNegated(true);

		// States
		int outside = 1;
		int inside = 2;
		int lockedInside = 3;
		int lockedOutside = 4;
		int finished = 5;
		int failed = 6;
		
		b.addTransition(outside, BEGIN, thread, setVal(depth,1), inside);
		b.addTransition(outside, BEGIN, thread, outside);
		b.addTransition(outside, LOCK, new int[]{lock,thread}, setVal(count,1), lockedOutside);
		b.addTransition(outside, UNLOCK, new int[]{lock,thread}, failed);
		
		b.addTransition(lockedOutside, BEGIN, thread, setVal(depth,1), lockedInside);
		b.addTransition(lockedOutside, BEGIN, thread, lockedOutside);		
		b.addTransition(lockedOutside, LOCK, new int[]{lock,thread}, increment(count), lockedOutside);
		b.addTransition(lockedOutside, UNLOCK, new int[]{lock,thread},isGreaterThanConstant(count,1),decrement(count),lockedOutside);
		b.addTransition(lockedOutside, UNLOCK, new int[]{lock,thread},isEqualToConstant(count,1),setVal(count,0),outside);
		
		b.addTransition(inside, BEGIN, thread, increment(depth), inside);
		b.addTransition(inside, END, thread, isEqualToConstant(depth, 1), setVal(depth,0), finished);
		b.addTransition(inside, END, thread, isGreaterThanConstant(depth,1), decrement(depth), inside);
		b.addTransition(inside, LOCK, new int[]{lock,thread}, setVal(count,1), lockedInside);
		b.addTransition(inside, UNLOCK, new int[]{lock,thread}, failed);
		
		b.addTransition(lockedInside, LOCK, new int[]{lock,thread}, increment(count), lockedInside);
		b.addTransition(lockedInside, UNLOCK, new int[]{lock,thread}, isEqualToConstant(count,1), setVal(count,0),inside);
		b.addTransition(lockedInside, UNLOCK, new int[]{lock,thread}, isGreaterThanConstant(count,1), decrement(count),lockedInside);
		b.addTransition(lockedInside, BEGIN, thread, increment(depth), lockedInside);
		b.addTransition(lockedInside, END, thread, isGreaterThanConstant(depth,1), decrement(depth),lockedInside);
		b.addTransition(lockedInside, END, thread, isEqualToConstant(depth,1), failed);
		
		b.addFinalStates(inside,lockedInside,lockedOutside,failed);
		b.setAllSkipStates();
		return b.make();
	}
}
