package qea.properties.crv15.c;

import static qea.structure.impl.other.Quantification.FORALL;
import qea.creation.QEABuilder;
import qea.structure.intf.Assignment;
import qea.structure.intf.Guard;
import qea.structure.intf.QEA;

public class QEA_C {

public static QEA makeStack(){
	
	QEABuilder q = new QEABuilder("Stack");

	// Events
	int PUSH = 1;
	int POP = 2;
	// Quantified variables
	int S = -1;
	// Free variables
	int size = 1;

	// states
	int empty = 1;
	int nonempty = 2;	
	
	q.addQuantification(FORALL, S);

	
	q.addTransition(empty, PUSH, S, Assignment.setVal(size, 1), nonempty);
	q.addTransition(nonempty, PUSH, S, Assignment.increment(size), nonempty);
	
	q.addTransition(nonempty, POP, S, Guard.isGreaterThanConstant(size, 1),
									  Assignment.decrement(size), nonempty);
	q.addTransition(nonempty, POP, S, Guard.isEqualToConstant(size, 1),
			  Assignment.decrement(size), empty);	


	q.addFinalStates(empty,nonempty);

	QEA qea = q.make();

	qea.record_event_name("push", 1);
	qea.record_event_name("pop", 2);
	
	return qea;		
	
}

public static QEA makeQueue(){
	
	QEABuilder q = new QEABuilder("Queue");

	// Events
	int INSERT = 1;
	int PEEK = 2;
	// Quantified variables
	int Q = -1;
	int E1 = -2;
	int E2 = -3;

	// states
	int start = 1;
	int added1 = 2;	
	int added2 = 3;
	int added1then2 = 4;
	int added2then1 = 5;
	
	q.addQuantification(FORALL, Q);
	q.addQuantification(FORALL, E1);
	q.addQuantification(FORALL, E2,Guard.isGreaterThan(E1, E2));

	
	q.addTransition(start,INSERT,new int[]{Q,E1},added1);
	q.addTransition(start,INSERT,new int[]{Q,E2},added2);
	
	q.addTransition(added1,PEEK, new int[]{Q,E1}, start);	
	q.addTransition(added1,INSERT,new int[]{Q,E2},added1then2);
	
	q.addTransition(added2,PEEK, new int[]{Q,E2}, start);	
	q.addTransition(added2,INSERT,new int[]{Q,E1},added2then1);
	
	q.addTransition(added1then2,PEEK, new int[]{Q,E1}, added2);
	q.addTransition(added2then1,PEEK, new int[]{Q,E2}, added1);
		
	q.addFinalStates(start);

	QEA qea = q.make();

	qea.record_event_name("insert", 1);
	qea.record_event_name("peek", 2);
	
	return qea;		
	
}

public static QEA makeQueueTwo(){
	
	QEABuilder q = new QEABuilder("Queue");

	// Events
	int INSERT = 1;
	int PEEK = 2;
	// Quantified variables
	int Q = -1;
	int E1 = -2;
	int E2 = -3;

	// states
	int start = 1;
	int added = 2;	
	int done = 3;
	
	q.addQuantification(FORALL, Q);
	q.addQuantification(FORALL, E1);

	
	q.addTransition(start,INSERT,new int[]{Q,E1},added);
	q.addTransition(added,PEEK, new int[]{Q,E1}, done);
	
	q.addFinalStates(start,done);

	QEA qea = q.make();

	qea.record_event_name("insert", 1);
	qea.record_event_name("peek", 2);
	
	return qea;		
	
}

}
