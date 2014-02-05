package monitoring.impl;

import monitoring.intf.Monitor;
import structure.intf.QEA;

/**
 * This class should be used to construct Monitors from QEA
 * 
 * The general idea is that the structural properties of the QEA are used to select
 * an appropriate Monitor implementation
 * 
 * @author Giles Reger and Helena Cuenca
 */

public class MonitorFactory {

	/**
	 * Constructs a Monitor given a QEA
	 * 
	 * @param qea
	 *            The QEA property
	 * @return A monitor for the QEA property
	 */	
	public static Monitor create(QEA qea){
		
	// If the QEA is deterministic, does not use free variables and only has one
	// quantified variable then (currently) use UnoptSimplestSmallStepQEAMonitor
		if(qea.isDeterministic() && !qea.usesFreeVariables() && qea.getLambda().length==1)
			return new SimplestSmallStepQEAMonitor(qea);
		
		return null;
	}
}
