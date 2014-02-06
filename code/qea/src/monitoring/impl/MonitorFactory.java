package monitoring.impl;

import monitoring.intf.Monitor;
import structure.impl.SimplestQEA;
import structure.intf.QEA;

/**
 * This class should be used to construct Monitors from QEA
 * 
 * The general idea is that the structural properties of the QEA are used to
 * select an appropriate Monitor implementation
 * 
 * @author Giles Reger
 * @author Helena Cuenca
 */

public class MonitorFactory {
	
	/**
	 * Constructs a Monitor given a QEA
	 * 
	 * @param qea
	 *            The QEA property
	 * @return A monitor for the QEA property
	 */
	public static Monitor create(QEA qea) {

		//TODO add check for quantified flag
		
		// If the QEA is deterministic, does not use free variables and only has
		// one quantified variable then (currently) use
		// SimplestSmallStepQEAMonitor
		//if (qea.isDeterministic() && !qea.usesFreeVariables()
		//		&& qea.getLambda().length == 1) {
		if(qea instanceof SimplestQEA){
			if(qea.getLambda().length==0) return new SimplestSmallStepQEAMonitor((SimplestQEA) qea);
		//} else if (qea.isDeterministic() && !qea.usesFreeVariables()
		//		&& qea.getLambda().length == 0) {
			else return new SmallStepPropositionalQEAMonitor((SimplestQEA) qea);
			
		} else if (!qea.isDeterministic() && !qea.usesFreeVariables()
				&& qea.getLambda().length == 1) {
			// TODO: New monitor to be created
		} else if (!qea.isDeterministic() && !qea.usesFreeVariables()
				&& qea.getLambda().length == 0) {
			// TODO: New monitor to be created
		}
		return null;
	}
}
