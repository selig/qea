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
import monitoring.impl.monitors.general.Incr_Naive_Det_Monitor;
import monitoring.intf.Monitor;
import structure.impl.qeas.QVar01_FVar_Det_QEA;
import structure.impl.qeas.QVar01_FVar_NonDet_QEA;
import structure.impl.qeas.QVar01_NoFVar_Det_QEA;
import structure.impl.qeas.QVar01_NoFVar_NonDet_QEA;
import structure.impl.qeas.QVar1_FVar_Det_FixedQVar_QEA;
import structure.impl.qeas.QVar1_FVar_NonDet_FixedQVar_QEA;
import structure.impl.qeas.QVarN_FVar_Det_QEA;
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
	@SuppressWarnings("rawtypes")
	public static Monitor create(QEA qea) {

		switch (qea.getQEAType()) {

		case QVAR01_NOFVAR_DET_QEA:
			if (qea.getLambda().length == 0) {
				return new Incr_QVar0_NoFVar_Det_QEAMonitor(
						(QVar01_NoFVar_Det_QEA) qea);
			} else {
				return new Incr_QVar1_NoFVar_Det_QEAMonitor(
						(QVar01_NoFVar_Det_QEA) qea);
			}

		case QVAR01_NOFVAR_NONDET_QEA:
			if (qea.getLambda().length == 0) {
				return new Incr_QVar0_NoFVar_NonDet_QEAMonitor(
						(QVar01_NoFVar_NonDet_QEA) qea);
			} else {
				return new Incr_QVar1_NoFVar_NonDet_QEAMonitor(
						(QVar01_NoFVar_NonDet_QEA) qea);
			}

		case QVAR1_FVAR_DET_FIXEDQVAR_QEA:
			return new Incr_QVar1_FVar_Det_FixedQVar_QEAMonitor(
					(QVar1_FVar_Det_FixedQVar_QEA) qea);

		case QVAR01_FVAR_DET_QEA:
			if (qea.getLambda().length == 0) {
				return new Incr_QVar0_FVar_Det_QEAMonitor(
						(QVar01_FVar_Det_QEA) qea);
			} else {
				return new Incr_QVar1_FVar_Det_QEAMonitor(
						(QVar01_FVar_Det_QEA) qea);
			}

		case QVAR1_FVAR_NONDET_FIXEDQVAR_QEA:
			return new Incr_QVar1_FVar_NonDet_FixedQVar_QEAMonitor(
					(QVar1_FVar_NonDet_FixedQVar_QEA) qea);
		case QVAR01_FVAR_NONDET_QEA:
			if (qea.getLambda().length == 0) {
				return new Incr_QVar0_FVar_NonDet_QEAMonitor(
						(QVar01_FVar_NonDet_QEA) qea);
			} else {
				return new Incr_QVar1_FVar_NonDet_QEAMonitor(
						(QVar01_FVar_NonDet_QEA) qea);
			}

		case QVARN_FVAR_DET_QEA:
			if(true) // replace with flag
				return new Incr_Naive_Det_Monitor((QVarN_FVar_Det_QEA) qea);
			
		case QVARN_FVAR_NONDET_QEA:			
			
		default:
			throw new ShouldNotHappenException("No monitor for "
					+ qea.getClass());
		}
	}
}
