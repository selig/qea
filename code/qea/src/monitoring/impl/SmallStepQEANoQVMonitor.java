package monitoring.impl;

import monitoring.intf.Monitor;
import structure.impl.Verdict;
import structure.intf.QEA;

/**
 * A small-step monitor for a simple QEA with no quantified variables
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class SmallStepQEANoQVMonitor extends Monitor {
	
	private int currentState;
	
	private QEA qea;
	
	public SmallStepQEANoQVMonitor(QEA qea) {
		this.qea = qea;
	}

	@Override
	public Verdict step(int eventName, Object[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Verdict step(int eventName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Verdict end() {
		// TODO Auto-generated method stub
		return null;
	}

}
