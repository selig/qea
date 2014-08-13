package qea.properties.papers;

import qea.structure.impl.other.Quantification;
import qea.structure.intf.QEA;
import qea.creation.QEABuilder;

public class UseFileOtherQEA {

	public static QEA makeUseFile() {

		// QVar
		int f = -1;
		// Events
		int open = 1;
		int write = 2;
		int save = 3;
		int close = 4;
		int read = 5;

		QEABuilder qeaBuilder = new QEABuilder("UseFile");

		qeaBuilder.addQuantification(Quantification.FORALL, f);

		qeaBuilder.addTransition(1, open, new int[] { f }, 2);
		qeaBuilder.addTransition(2, close, new int[] { f }, 1);
		qeaBuilder.addTransition(2, write, new int[] { f }, 3);
		qeaBuilder.addTransition(3, save, new int[] { f }, 4);
		qeaBuilder.addTransition(4, close, new int[] { f }, 1);

		qeaBuilder.addTransition(1, read, new int[] { f }, 5);

		qeaBuilder.setSkipStates(1, 2, 3, 4);

		qeaBuilder.addFinalStates(1);

		return qeaBuilder.make();
	}

}
