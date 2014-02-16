package monitoring.impl;

import java.util.Arrays;

import monitoring.impl.monitors.*;
import monitoring.intf.Monitor;
import structure.impl.*;
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
		
		if(qea instanceof SimplestQEA){
			if(qea.getLambda().length==0) return new SmallStepPropositionalQEAMonitor((SimplestQEA) qea);
			else return new SimplestIncrementalQEAMonitor((SimplestQEA) qea);
			
		} else if (qea instanceof SimpleNonDeterministicQEA){
			if(qea.getLambda().length==0) return null; // TODO: New monitor to be created
			else return new IncrementalNonDetQEAMonitor((SimpleNonDeterministicQEA) qea);
		} else if (!qea.isDeterministic() && !qea.usesFreeVariables()
				&& qea.getLambda().length == 0) {
			// TODO: New monitor to be created
		}
		return null;
	}
}
