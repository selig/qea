package monitoring.impl;

import structure.impl.SimplestQEA;
import structure.impl.Verdict;
import structure.intf.QEA;

/**
 * A small-step monitor for a simple QEA with no quantified variables
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class SmallStepPropositionalQEAMonitor extends SmallStepMonitor<SimplestQEA> {
	
	private int currentState;
	
	public SmallStepPropositionalQEAMonitor(SimplestQEA qea) {
		super(qea);
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
