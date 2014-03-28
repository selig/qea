package benchmark.rovers;

import properties.rovers.RoverCaseStudy;
import structure.intf.QEA;

public class QeaDoEval extends DoEval {

	@Override
	public DoWork makeWork() {
		return new QeaDoWork();
	}

	public static void main(String[] args){
		
		QeaDoEval eval = new QeaDoEval();
		
		QeaDoWork.print=false;
		eval.eval_for_GrantCancel(RoverCaseStudy.makeGrantCancelSingle(),"GrantCancel");
		
		eval.eval_for_ResourceLifecycle(RoverCaseStudy.makeResourceLifecycle(), "ResourceLifecycle");
	}
	
}
