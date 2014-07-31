package benchmark.rovers;

import properties.rovers.ResourceLifecycle_QVar01_FVar_Det_QEA;
import properties.rovers.ResourceLifecycle_QVar01_FVar_NonDet_QEA;
import properties.rovers.ResourceLifecycle_QVar01_NoFVar_NonDet_QEA;
import properties.rovers.RoverCaseStudy;
import benchmark.rovers.qea.QeaDoEval.QeaDoWork;

public class ResourceLifecycleQeaDoEval extends DoEval {

	@Override
	public DoWork makeWork() {
		return new QeaDoWork();
	}

	public static void main(String[] args) {

		ResourceLifecycleQeaDoEval eval = new ResourceLifecycleQeaDoEval();

		QeaDoWork.print = false;

		// Worst case
		eval.eval_for_ResourceLifecycle(
				RoverCaseStudy.makeResourceLifecycleGeneral(),
				"ResourceLifecycle");

		// Intermediate cases
		eval.eval_for_ResourceLifecycle(
				new ResourceLifecycle_QVar01_FVar_NonDet_QEA(),
				"ResourceLifecycle");

		eval.eval_for_ResourceLifecycle(
				new ResourceLifecycle_QVar01_FVar_Det_QEA(),
				"ResourceLifecycle");

		eval.eval_for_ResourceLifecycle(
				new ResourceLifecycle_QVar01_NoFVar_NonDet_QEA(),
				"ResourceLifecycle");

		// Best case
		eval.eval_for_ResourceLifecycle(RoverCaseStudy.makeResourceLifecycle(),
				"ResourceLifecycle");

	}
}
