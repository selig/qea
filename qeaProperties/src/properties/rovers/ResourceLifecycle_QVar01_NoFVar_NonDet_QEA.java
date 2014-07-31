package properties.rovers;

import static structure.impl.other.Quantification.FORALL;
import structure.impl.qeas.QVar01_NoFVar_NonDet_QEA;

public class ResourceLifecycle_QVar01_NoFVar_NonDet_QEA extends
		QVar01_NoFVar_NonDet_QEA {

	public ResourceLifecycle_QVar01_NoFVar_NonDet_QEA() {
		super(3, 5, 1, FORALL);
		setName("ResourceLifecycle_QVar01_NoFVar_NonDet_QEA");

		// Events
		int REQUEST = 1;
		int GRANT = 2;
		int DENY = 3;
		int RESCIND = 4;
		int CANCEL = 5;
		// Quantified variable
		int R = -1;

		addTransition(1, REQUEST, 2, false);
		addTransition(2, DENY, 1, false);
		addTransition(2, GRANT, 3, false);
		addTransition(3, RESCIND, 3, false);
		addTransition(3, CANCEL, 1, false);

		// Final states. There are no strong states
		setStatesAsFinal(1, 2);

		record_event_name("request", 1);
		record_event_name("grant", 2);
		record_event_name("deny", 3);
		record_event_name("rescind", 4);
		record_event_name("cancel", 5);
	}

}
