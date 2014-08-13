package qea.creation;

import static qea.structure.impl.other.Quantification.FORALL;
import qea.structure.intf.QEA;

public class ExampleUsage {

	public static QEA makeHasNext() {

		int i = -1;
		int NEXT = 1;
		int HASNEXT_TRUE = 2;
		int HASNEXT_FALSE = 3;

		QEABuilder qea = new QEABuilder("HasNext");

		qea.addQuantification(FORALL, i);

		// hasnext_true allows us to call next
		qea.addTransition(1, HASNEXT_TRUE, 2);
		qea.addTransition(2, HASNEXT_TRUE, 2);
		qea.addTransition(2, NEXT, 1);

		// hasnext_false means the Iterator is finished
		qea.addTransition(1, HASNEXT_FALSE, 3);
		qea.addTransition(3, HASNEXT_FALSE, 3);

		qea.addFinalStates(1, 2, 3);

		return qea.make();

	}

}
