package benchmark.rovers;

import properties.rovers.RoverCaseStudy;

public class OutputDoEval extends DoEval {

	@Override
	public DoWork makeWork() {
		return new OutputDoWork();
	}

	public static void main(String[] args) {

		OutputDoEval eval = new OutputDoEval();
		
		eval.set_runs(1);
		
		eval.run_eval("GrantCancel","GrantCancel", new int[] { 1000, 1000, 1000000 }, 0);
		
		//eval.run_eval("ResourceLifecycle", "ResourceLifecycle", new int[] { 5000, 1000000 }, 0);
		
		//eval.run_eval("ExistsLeader", "ExistsLeader", new int[] { 100 }, 0);
		
		//eval.run_eval("RespectConflicts","RespectConflicts",new int[]{5, 1000, 1000},0);
		
		//eval.run_eval("NestedCommand","NestedCommand",new int[]{2, 2, 100},0);

	}

}
