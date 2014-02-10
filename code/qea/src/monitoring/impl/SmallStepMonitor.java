package monitoring.impl;

import monitoring.intf.Monitor;
import structure.impl.Verdict;
import structure.intf.QEA;

/**
 * All incremental monitors implement the trace method using the step method.
 * 
 * TODO: Consider using "incremental" instead of "smallstep"
 * 
 * @author Giles Reger
 * @author Helena Cuenca
 */
public abstract class SmallStepMonitor<Q extends QEA> extends Monitor<Q> {

	public SmallStepMonitor(Q q) {
		super(q);
	}

	@Override
	public Verdict trace(int[] eventNames, Object[][] args) {
		for (int i = 0; i < eventNames.length; i++) {
			switch (args[i].length) {
			case 0:
				step(eventNames[i]);
				break;
			case 1:
				step(eventNames[i], args[i][0]);
				break;
			case 2:
				step(eventNames[i], args[i][0], args[i][1]);
				break;
			case 3:
				step(eventNames[i], args[i][0], args[i][1], args[i][2]);
				break;
			case 4:
				step(eventNames[i], args[i][0], args[i][1], args[i][2],
						args[i][3]);
				break;
			case 5:
				step(eventNames[i], args[i][0], args[i][1], args[i][2],
						args[i][3], args[i][4]);
				break;
			default:
				step(eventNames[i], args[i]);
				break;
			}
			step(eventNames[i], args[i]);
		}
		return null; // TODO: What's the return value here?
	}

}
