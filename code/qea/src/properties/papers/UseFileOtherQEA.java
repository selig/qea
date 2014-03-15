package properties.papers;

import structure.impl.other.Quantification;
import structure.intf.QEA;
import creation.QEABuilder;

public class UseFileOtherQEA {

	public static QEA makeUseFile() {

		// QVar
		int f = -1;
		// Events
		int open = 1;
		int write = 2;
		int save = 3;
		int close = 4;

		QEABuilder qeaBuilder = new QEABuilder("UseFile");

		qeaBuilder.addQuantification(Quantification.FORALL, f);

		qeaBuilder.addTransition(1, open, new int[] { f }, 2);
		qeaBuilder.addTransition(2, close, new int[] { f }, 1);
		qeaBuilder.addTransition(2, write, new int[] { f }, 3);
		qeaBuilder.addTransition(3, save, new int[] { f }, 4);
		qeaBuilder.addTransition(4, close, new int[] { f }, 1);

		qeaBuilder.addFinalStates(1);

		return qeaBuilder.make();
	}

}
