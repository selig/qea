package qea.properties.crv15.offline;

import qea.creation.QEABuilder;
import qea.structure.intf.Assignment;
import qea.structure.intf.Guard;
import qea.structure.intf.QEA;
import static qea.structure.impl.other.Quantification.*;
import static qea.structure.intf.Guard.*;
import static qea.structure.intf.Assignment.*;

public class LogFire_8 {

	public static QEA make_one(){
		QEABuilder b = new QEABuilder("GrantRelease");
		
		//Events
		int Grant = 1; int Release = 2;
		// Free variables
		int task = 1;
		int other_task = 2;
		// Quantified variables
		int resource = -1;
		b.addQuantification(FORALL,resource);
		
		b.addTransition(1, Grant, new int[]{task,resource}, 2);
		b.addTransition(2, Release, new int[]{other_task,resource},isEqual(task,other_task),1);
		
		b.addFinalStates(1);
		
		return b.make();
	}
	
	public static QEA make_four(){
		QEABuilder b = new QEABuilder("ConflictResolution");
		
		// Events
		int CONFLICT = 1; int GRANT = 2; int CANCEL = 3;
		// Quantified variable
		int R1 = -1;
		// Free variables
		int R2 = 1; // Auxiliary variable
		int RS = 2; // Resources that conflict with R1

		b.addQuantification(FORALL, R1);

		// Build up the set RS of conflicts
		b.addTransition(1,CONFLICT,new int[]{R1,R2}, createSetFromElement(RS,R2), 2);
		b.addTransition(1,CONFLICT,new int[]{R2,R1}, createSetFromElement(RS,R2), 2);
		b.addTransition(2,CONFLICT,new int[]{R1,R2}, addElementToSet(RS,R2), 2);
		b.addTransition(2,CONFLICT,new int[]{R2,R1}, addElementToSet(RS,R2), 2);
		
		b.addTransition(2, GRANT, R1, 3);
		b.addTransition(3, CANCEL, R1, 2);
		b.addTransition(3, GRANT, R2, setNotContainsElement(R2,RS), 3);
		
		// Skip transitions required as these events are in the alphabet 
		b.addTransition(1, GRANT, R2, 1);
		b.addTransition(1, CANCEL, R1, 1);
		b.addTransition(2, GRANT, R2, isNotEqual(R1,R2), 2);

		b.addFinalStates(1, 2, 3);

		return b.make();
	}
	
}
