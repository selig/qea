package monitoring.impl;

import monitoring.impl.monitors.IncrementalNSPropositionalNonDetQEAMonitor;
import monitoring.impl.monitors.IncrementalNSPropositionalQEAMonitor;
import monitoring.impl.monitors.IncrementalNonSimpleDetQEAMonitor;
import monitoring.impl.monitors.IncrementalNonSimpleNonDetQEAMonitor;
import monitoring.impl.monitors.IncrementalPropositionalNonDetQEAMonitor;
import monitoring.impl.monitors.IncrementalPropositionalQEAMonitor;
import monitoring.impl.monitors.IncrementalSimpleDetQEAMonitor;
import monitoring.impl.monitors.IncrementalSimpleNonDetQEAMonitor;
import monitoring.intf.Monitor;
import structure.impl.NonSimpleDetQEA;
import structure.impl.NonSimpleNonDetQEA;
import structure.impl.SimpleDetQEA;
import structure.impl.SimpleNonDetQEA;
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

		if (qea instanceof SimpleDetQEA) {
			if (qea.getLambda().length == 0)
				return new IncrementalPropositionalQEAMonitor(
						(SimpleDetQEA) qea);
			else
				return new IncrementalSimpleDetQEAMonitor((SimpleDetQEA) qea);

		} else if (qea instanceof SimpleNonDetQEA) {
			if (qea.getLambda().length == 0)
				return new IncrementalPropositionalNonDetQEAMonitor(
						(SimpleNonDetQEA) qea);
			else
				return new IncrementalSimpleNonDetQEAMonitor(
						(SimpleNonDetQEA) qea);
		} else if (qea instanceof NonSimpleDetQEA) {
			if (qea.getLambda().length == 0) {
				return new IncrementalNSPropositionalQEAMonitor(
						(NonSimpleDetQEA) qea);
			} else {
				return new IncrementalNonSimpleDetQEAMonitor(
						(NonSimpleDetQEA) qea);
			}
		} else if (qea instanceof NonSimpleNonDetQEA) {
			if (qea.getLambda().length == 0) {
				return new IncrementalNSPropositionalNonDetQEAMonitor(
						(NonSimpleNonDetQEA) qea);
			} else {
				return new IncrementalNonSimpleNonDetQEAMonitor(
						(NonSimpleNonDetQEA) qea);
			}

		} else if (!qea.isDeterministic() && !qea.usesFreeVariables()
				&& qea.getLambda().length == 0) {
			// TODO: New monitor to be created
		}
		return null;
	}
}
