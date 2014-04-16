package benchmark.rovers;

import properties.rovers.RoverCaseStudy;

public class QeaDoEval extends DoEval {

	@Override
	public DoWork makeWork() {
		return new QeaDoWork();
	}

	public static void main(String[] args) {

		QeaDoEval eval = new QeaDoEval();

		QeaDoWork.print = false;

		// Internal Properties

		eval.eval_for_GrantCancel(RoverCaseStudy.makeGrantCancelSingle(),
				"GrantCancel");

		eval.eval_for_ResourceLifecycle(RoverCaseStudy.makeResourceLifecycle(),
				"ResourceLifecycle");

		 eval.eval_for_ReleaseResource(RoverCaseStudy.makeReleaseResource(),
		 "ReleaseResource");


		 eval.eval_for_RespectConflicts(
		 RoverCaseStudy.makeRespectConflictsSingle(), "RespectConflicts");

		// TODO Implementation of eval_for_RespectPriorities and
		// work_for_RespectPriorities is missing
		// eval.eval_for_RespectPriorities(RoverCaseStudy.makeRespectPriorities(),
		// "RespectPriorities");

		// External properties

		eval.eval_for_ExactlyOneSuccess(RoverCaseStudy.makeExactlyOneSuccess(),
				"ExactlyOneSuccess");

		eval.eval_for_IncreasingCommand(RoverCaseStudy.makeIncreasingCommand(),
				"IncreasingCommand");

		eval.eval_for_NestedCommand(RoverCaseStudy.makeNestedCommand(),
		 "NestedCommand");

		
		 eval.eval_for_AcknowledgeCommands(
		 RoverCaseStudy.makeAcknowledgeCommands(), "AcknowledgeCommands");
	
		 eval.eval_for_ExistsSatellite(
		 RoverCaseStudy.makeExistsSatelliteSingle(), "ExistsSatellite");

		//TODO - check, this qea is not Normal
		// eval.eval_for_ExistsLeader(RoverCaseStudy.makeExistsLeader(),
		// "ExistsLeader");

		// TODO Multiple quantified QEA not implemented yet
		// eval.eval_for_MessageHashCorrectInvInt(
		// RoverCaseStudy.makeMessageHashCorrect(),
		// "MessageHashCorrectInvInt");
	}
}
