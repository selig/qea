package qea.properties.crv15.offline;

import static qea.structure.impl.other.Quantification.*;
import qea.creation.QEABuilder;
import qea.structure.intf.QEA;

public class RiTHM_2 {

	/*
	 * ForAll pid, ForAll tid : (thcreate IMPLIES EVENTUALLY thrunning) 
	 * For all threads of all process, once the thread is created, it 
	 * eventually goes into state 'thrunning' i.e. it runs
	 */
	public static QEA make_one(){
		QEABuilder b = new QEABuilder("offline_team3_bench1");
		
		// Event names
		int thcreate = 1; int thrunning = 2;
		
		// Quantifications
		int pid = -1; int tid = -12;
		b.addQuantification(FORALL,pid);
		b.addQuantification(FORALL,tid);
		
		//Transitions
		b.addTransition(1,thcreate,new int[]{pid,tid},2);
		b.addTransition(2,thrunning, new int[]{pid,tid},3);
		
		// State 2 is an unsafe 'waiting' state
		b.addFinalStates(1,3);
		
		// All transitions not mentioned are safe
		b.setAllSkipStates();
		
		return b.make();
	}
	
}
