package monitoring.impl;

import monitoring.impl.monitors.Incr_QVar0_FVar_Det_QEAMonitor;
import monitoring.impl.monitors.Incr_QVar0_FVar_NonDet_QEAMonitor;
import monitoring.impl.monitors.Incr_QVar0_NoFVar_Det_QEAMonitor;
import monitoring.impl.monitors.Incr_QVar0_NoFVar_NonDet_QEAMonitor;
import monitoring.impl.monitors.Incr_QVar1_FVar_Det_FixedQVar_QEAMonitor;
import monitoring.impl.monitors.Incr_QVar1_FVar_Det_QEAMonitor;
import monitoring.impl.monitors.Incr_QVar1_FVar_NonDet_FixedQVar_QEAMonitor;
import monitoring.impl.monitors.Incr_QVar1_FVar_NonDet_QEAMonitor;
import monitoring.impl.monitors.Incr_QVar1_NoFVar_Det_QEAMonitor;
import monitoring.impl.monitors.Incr_QVar1_NoFVar_NonDet_QEAMonitor;
import monitoring.intf.Monitor;
import structure.impl.QVar01_FVar_Det_QEA;
import structure.impl.QVar01_FVar_NonDet_QEA;
import structure.impl.QVar01_NoFVar_Det_QEA;
import structure.impl.QVar01_NoFVar_NonDet_QEA;
import structure.impl.QVar1_FVar_Det_FixedQVar_QEA;
import structure.impl.QVar1_FVar_NonDet_FixedQVar_QEA;
import structure.intf.QEA;
import exceptions.ShouldNotHappenException;

/**
 * This class should be used to construct Monitors from QEA. The general idea is
 * that the structural properties of the QEA are used to select an appropriate
 * Monitor implementation
 * 
 * @author Giles Reger
 * @author Helena Cuenca
 */
public class MonitorFactory {

	/**
	 * Constructs a Monitor for the specified QEA
	 * 
	 * @param qea
	 *            The QEA property
	 * @return A monitor for the QEA property
	 */
	public static Monitor create(QEA qea) {

		if (qea instanceof QVar01_NoFVar_Det_QEA) {
			if (qea.getLambda().length == 0) {
				return new Incr_QVar0_NoFVar_Det_QEAMonitor(
						(QVar01_NoFVar_Det_QEA) qea);
			} else {
				return new Incr_QVar1_NoFVar_Det_QEAMonitor(
						(QVar01_NoFVar_Det_QEA) qea);
			}
		} else if (qea instanceof QVar01_NoFVar_NonDet_QEA) {
			if (qea.getLambda().length == 0) {
				return new Incr_QVar0_NoFVar_NonDet_QEAMonitor(
						(QVar01_NoFVar_NonDet_QEA) qea);
			} else {
				return new Incr_QVar1_NoFVar_NonDet_QEAMonitor(
						(QVar01_NoFVar_NonDet_QEA) qea);
			}
		} else if (qea instanceof QVar1_FVar_Det_FixedQVar_QEA) {
			// Lambda size is 1
			return new Incr_QVar1_FVar_Det_FixedQVar_QEAMonitor(
					(QVar1_FVar_Det_FixedQVar_QEA) qea);
		} else if (qea instanceof QVar1_FVar_NonDet_FixedQVar_QEA) {
			// Lambda size is 1
			return new Incr_QVar1_FVar_NonDet_FixedQVar_QEAMonitor(
					(QVar1_FVar_NonDet_FixedQVar_QEA) qea);
		} else if (qea instanceof QVar01_FVar_Det_QEA) {
			if (qea.getLambda().length == 0) {
				return new Incr_QVar0_FVar_Det_QEAMonitor(
						(QVar01_FVar_Det_QEA) qea);
			} else {
				return new Incr_QVar1_FVar_Det_QEAMonitor(
						(QVar01_FVar_Det_QEA) qea);
			}
		} else if (qea instanceof QVar01_FVar_NonDet_QEA) {
			if (qea.getLambda().length == 0) {
				return new Incr_QVar0_FVar_NonDet_QEAMonitor(
						(QVar01_FVar_NonDet_QEA) qea);
			} else {
				return new Incr_QVar1_FVar_NonDet_QEAMonitor(
						(QVar01_FVar_NonDet_QEA) qea);
			}
		}
		throw new ShouldNotHappenException("No monitor for " + qea.getClass());
	}

	/**
	 * Determines if the specified QEA is well-formed. This is:
	 * <ul>
	 * <li>All transitions defined for one event name have the same number of
	 * parameters
	 * <li>...
	 * </ul>
	 * 
	 * @param qea
	 *            QEA to be evaluated
	 * @return <code>true</code> if the specified QEA is well-formed as
	 *         described above; <code>false</code> otherwise
	 */
	public static boolean wellFormed(QEA qea) {
		// TODO Implement this method
		return true;
	}
}
