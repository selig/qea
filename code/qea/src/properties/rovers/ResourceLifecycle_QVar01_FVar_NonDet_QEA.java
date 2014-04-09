package properties.rovers;

import static structure.impl.other.Quantification.FORALL;
import structure.impl.other.Transition;
import structure.impl.qeas.QVar01_FVar_NonDet_QEA;

public class ResourceLifecycle_QVar01_FVar_NonDet_QEA extends
		QVar01_FVar_NonDet_QEA {

	public ResourceLifecycle_QVar01_FVar_NonDet_QEA() {

		super(3, 5, 1, FORALL, 0);
		setName("ResourceLifecycle_QVar01_FVar_NonDet_QEA");

		// Events
		int REQUEST = 1;
		int GRANT = 2;
		int DENY = 3;
		int RESCIND = 4;
		int CANCEL = 5;
		// Quantified variable
		int R = -1;

		addTransition(1, REQUEST, new Transition(R, 2));
		addTransition(2, DENY, new Transition(R, 1));
		addTransition(2, GRANT, new Transition(R, 3));
		addTransition(3, RESCIND, new Transition(R, 3));
		addTransition(3, CANCEL, new Transition(R, 1));

		// Final states. There are no strong states
		setStatesAsFinal(1, 2);

		record_event_name("request", 1);
		record_event_name("grant", 2);
		record_event_name("deny", 3);
		record_event_name("rescind", 4);
		record_event_name("cancel", 5);
	}
}
