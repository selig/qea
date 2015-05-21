package qea.properties.crv15.java;

import static qea.structure.impl.other.Quantification.*;
import qea.creation.QEABuilder;
import qea.structure.intf.Assignment;
import qea.structure.intf.Guard;
import qea.structure.intf.QEA;

public class QEA_Java {


	


public static QEA makeExamSystem(){
	
	QEABuilder q = new QEABuilder("ExamSystem");

	// Events
	int SUBMIT = 1;
	int MARK = 2;
	// Quantified variables
	int P = -1;
	int Q = -2;
	int M = -3;
	int S = -4;
	// Free variables
	int OM = 1;

	q.addQuantification(FORALL, P);
	q.addQuantification(FORALL, Q);
	q.addQuantification(EXISTS, M);
	q.addQuantification(FORALL, S);

	
	q.addTransition(1, SUBMIT, new int[] { S, P }, 2);
	q.addTransition(2, MARK, new int[] { P,Q,S,M }, 3);
	q.addTransition(3, MARK, new int[] { P,Q,S,OM }, 4);
	
	q.addFinalStates(1,3);
	q.setSkipStates(1,2,3,4);

	QEA qea = q.make();

	qea.record_event_name("submit", 1);
	qea.record_event_name("mark", 2);
	
	return qea;		
	
}
	
	
// Actually an online one
public static QEA makeResource(){
	
	QEABuilder q = new QEABuilder("Resource");

	// Events
	int REQUEST = 1;
	int GRANT = 2;
	int DENY = 3;
	int RELEASE = 4;
	// Quantified variables
	int T = -1;
	int C = -2;
	int R = -3;

	// states
	int start = 1;
	int requested = 2;
	int granted = 3;
	
	q.addQuantification(FORALL, T);
	q.addQuantification(FORALL, C);
	q.addQuantification(FORALL, R);

	int[] sig = new int[]{C,T,R};
	
	q.addTransition(start, REQUEST, sig, requested);
	q.addTransition(requested, DENY, sig, start);
	q.addTransition(requested, GRANT, sig, granted);
	q.addTransition(granted, RELEASE, sig, start);
	
	q.addFinalStates(start);

	QEA qea = q.make();

	qea.record_event_name("request", 1);
	qea.record_event_name("grant", 2);
	qea.record_event_name("deny", 3);
	qea.record_event_name("release", 4);
	
	return qea;		
	
}

//Actually an online one
public static QEA makeSingleGrant(){
	
	QEABuilder q = new QEABuilder("SingleGrant");

	// Events
	int GRANT = 1;
	int RELEASE = 2;
	// Quantified variables
	int R = -1;
	// Free variables
	int T1 = 2;
	int T2 = 3;

	// states
	int start = 1;
	int granted = 2;

	
	q.addQuantification(FORALL, R);

	q.addTransition(start, GRANT, new int[]{T1,R}, granted);
	q.addTransition(granted, RELEASE, new int[]{T2,R}, Guard.isEqual(T1,T2), start);
	
	q.addFinalStates(start);

	QEA qea = q.make();

	qea.record_event_name("grant", 1);
	qea.record_event_name("release", 2);
	
	return qea;		
	
}

public static QEA makeSQLInjection(){
	
	QEABuilder q = new QEABuilder("SQLInjection");

	// Events
	int INPUT = 1;
	int DERIVE = 2;
	int SANITISE = 3;
	int USE = 4;
	// Quantified variables
	//int S1 = -1;
	// Free variables
	int S1 = 1;
	int S2 = 2;
	int S3 = 3;

	// states
	int start = 1;
	int dirty = 2;	
	int success = 3;
	int failure = 4;
	
	q.setNegated(true);
	//q.addQuantification(EXISTS, S1);

	
	q.addTransition(start, INPUT, S1, start);
	q.addTransition(start, INPUT, S1, dirty);
	
	q.addTransition(dirty, DERIVE, new int[]{ S2, S3 }, 
			Guard.isEqual(S1, S2), 
			Assignment.storeVar(S1, S3), dirty);
	
	q.addTransition(dirty, DERIVE, new int[]{ S2, S3 },
			Guard.isEqual(S1,S2), dirty);
	
	q.addTransition(dirty, SANITISE, S2, Guard.isEqual(S1, S2), failure);
	q.addTransition(dirty, USE, S2, Guard.isEqual(S1, S2), success);


	q.setSkipStates(start,dirty,success,failure);
	q.addFinalStates(success);

	QEA qea = q.make();

	qea.record_event_name("input", 1);
	qea.record_event_name("derive", 2);
	qea.record_event_name("sanitise", 3);
	qea.record_event_name("use", 4);
	
	return qea;		
	
}

public static QEA makeGraphics(){
	
	QEABuilder q = new QEABuilder("Graphics");

	// Events
	int ADD = 1;
	// Quantified variables
	int C = -1;
	int S = -2;

	// states
	int start = 1;
	int added = 2;	
	

	q.addQuantification(FORALL, C);
	q.addQuantification(FORALL, S);

	
	q.addTransition(start, ADD, new int[]{C,S}, added);


	q.addFinalStates(start,added);

	QEA qea = q.make();

	qea.record_event_name("add", 1);
	
	return qea;		
	
}


}
