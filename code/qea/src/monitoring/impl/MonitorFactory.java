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
import monitoring.impl.monitors.general.Incr_Naive_NonDet_Monitor;
import monitoring.impl.monitors.general.Incr_QVarN_FVar_Det_QEAMonitor;
import monitoring.impl.monitors.general.Incr_QVarN_FVar_NonDet_QEAMonitor;
import monitoring.intf.Monitor;
import structure.impl.qeas.QVar01_FVar_Det_QEA;
import structure.impl.qeas.QVar01_FVar_NonDet_QEA;
import structure.impl.qeas.QVar01_NoFVar_Det_QEA;
import structure.impl.qeas.QVar01_NoFVar_NonDet_QEA;
import structure.impl.qeas.QVar1_FVar_Det_FixedQVar_QEA;
import structure.impl.qeas.QVar1_FVar_NonDet_FixedQVar_QEA;
import structure.impl.qeas.QVarN_FVar_Det_QEA;
import structure.impl.qeas.QVarN_FVar_NonDet_QEA;
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

	
	public static Monitor create(QEA qea){
		return create(qea,RestartMode.NONE,GarbageMode.NONE);
	}
	public static Monitor create(RestartMode restart, QEA qea) {
		return create(qea,restart,GarbageMode.NONE);
	}	
	public static Monitor create(GarbageMode garbage,QEA qea){
			return create(qea,RestartMode.NONE,garbage);
	}
	
	
	/**
	 * Constructs a Monitor for the specified QEA
	 * 
	 * @param qea
	 *            The QEA property
	 * @return A monitor for the QEA property
	 */
	@SuppressWarnings("rawtypes")
	public static Monitor create(QEA qea, RestartMode restart, GarbageMode garbage) {

		if(qea.getLambda().length==0)
			if(restart==RestartMode.REMOVE || restart==RestartMode.IGNORE)
				System.err.println("WARNING: restart mode"+restart+" not applicable for zero quantified variables");
		
		switch (qea.getQEAType()) {

		case QVAR01_NOFVAR_DET_QEA:
			if (qea.getLambda().length == 0) {
				return new Incr_QVar0_NoFVar_Det_QEAMonitor(restart,garbage,
						(QVar01_NoFVar_Det_QEA) qea);
			} else {
				return new Incr_QVar1_NoFVar_Det_QEAMonitor(restart,garbage,
						(QVar01_NoFVar_Det_QEA) qea);
			}

		case QVAR01_NOFVAR_NONDET_QEA:
			if (qea.getLambda().length == 0) {
				return new Incr_QVar0_NoFVar_NonDet_QEAMonitor(restart,garbage,
						(QVar01_NoFVar_NonDet_QEA) qea);
			} else {
				return new Incr_QVar1_NoFVar_NonDet_QEAMonitor(restart,garbage,
						(QVar01_NoFVar_NonDet_QEA) qea);
			}

		case QVAR1_FVAR_DET_FIXEDQVAR_QEA:
			return new Incr_QVar1_FVar_Det_FixedQVar_QEAMonitor(restart,garbage,
					(QVar1_FVar_Det_FixedQVar_QEA) qea);

		case QVAR01_FVAR_DET_QEA:
			if (qea.getLambda().length == 0) {
				return new Incr_QVar0_FVar_Det_QEAMonitor(restart,garbage,
						(QVar01_FVar_Det_QEA) qea);
			} else {
				return new Incr_QVar1_FVar_Det_QEAMonitor(restart,garbage,
						(QVar01_FVar_Det_QEA) qea);
			}

		case QVAR1_FVAR_NONDET_FIXEDQVAR_QEA:
			return new Incr_QVar1_FVar_NonDet_FixedQVar_QEAMonitor(restart,garbage,
					(QVar1_FVar_NonDet_FixedQVar_QEA) qea);
		case QVAR01_FVAR_NONDET_QEA:
			if (qea.getLambda().length == 0) {
				return new Incr_QVar0_FVar_NonDet_QEAMonitor(restart,garbage,
						(QVar01_FVar_NonDet_QEA) qea);
			} else {
				return new Incr_QVar1_FVar_NonDet_QEAMonitor(restart,garbage,
						(QVar01_FVar_NonDet_QEA) qea);
			}

		case QVARN_FVAR_DET_QEA:
				return new Incr_QVarN_FVar_Det_QEAMonitor(restart,garbage,
						(QVarN_FVar_Det_QEA) qea);
			
		case QVARN_FVAR_NONDET_QEA:		
				return new Incr_QVarN_FVar_NonDet_QEAMonitor(restart,garbage,
						(QVarN_FVar_NonDet_QEA) qea);
					
				
		default:
			throw new ShouldNotHappenException("No monitor for "
					+ qea.getClass());
		}
	}

	public static Monitor createNaive(QEA qea){
		switch(qea.getQEAType()){
		case QVARN_FVAR_DET_QEA:
			return new Incr_Naive_Det_Monitor( // naive does not use restart or garbage
					(QVarN_FVar_Det_QEA) qea);
		case QVARN_FVAR_NONDET_QEA:
			return new Incr_Naive_NonDet_Monitor( // naive does not use restart or garbage
					(QVarN_FVar_NonDet_QEA) qea);	

		default:
			throw new ShouldNotHappenException("No monitor for "
					+ qea.getClass());
		}
	}
	
}
