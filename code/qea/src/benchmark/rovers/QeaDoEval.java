package benchmark.rovers;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.rovers.RoverCaseStudy;
import structure.impl.other.Verdict;
import structure.impl.qeas.QEAType;
import structure.intf.QEA;
import creation.QEABuilder;

public class QeaDoEval extends DoEval {

	@Override
	public DoWork makeWork() {
		return new QeaDoWork();
	}

	public static void main(String[] args) {


		QeaDoWork.print = false;
		
		/*
		 * Questions to answer/fix
		 * - Why is RespectConflicts slower for Symbol than Naive?
		 */
		
		QeaDoEval eval = new QeaDoEval();
		eval.hardest_only=true;			
		//eval.output=true;
		eval.csv_mode=true;
		
		//QeaDoWork.category=Category.NAIVE;
		//System.err.println("Category: "+QeaDoWork.category);
		// eval.eval_for_RespectConflicts(
		// RoverCaseStudy.makeRespectConflictsSingle(), "RespectConflicts");		
		//	QeaDoWork.category=Category.SYMBOL;
		//	System.err.println("Category: "+QeaDoWork.category);
		//	for(int i=0;i<100;i++)
		//	 eval.eval_for_RespectConflicts(
		//	 RoverCaseStudy.makeRespectConflictsSingle(), "RespectConflicts");		 
		
		eval.eval_for_RespectPriorities(RoverCaseStudy.makeRespectPriorities(),
				 "RespectPriorities");	
		
		System.exit(0);
		
		
		//Do the whole thing 5 times!
		for(int i=0;i<5;i++){
			//Force naive monitors
			QeaDoWork.category=Category.NAIVE;
			System.err.println("Category: "+QeaDoWork.category);
			do_the_eval();
			//Force symbol-indexing
			QeaDoWork.category=Category.SYMBOL;
			System.err.println("Category: "+QeaDoWork.category);
			do_the_eval();
			//Force most general structured
			QeaDoWork.category=Category.GENERAL_STRUCT;
			System.err.println("Category: "+QeaDoWork.category);
			do_the_eval();
			//Use default monitors
			QeaDoWork.category=Category.NORMAL;
			System.err.println("Category: "+QeaDoWork.category);
			do_the_eval();
		}
		
	}	
		
	public static void do_the_eval(){	
		
		QeaDoEval eval = new QeaDoEval();
		eval.hardest_only=true;
		eval.csv_mode=true;

		// Internal Properties

		eval.eval_for_GrantCancel(RoverCaseStudy.makeGrantCancelSingle(),
				"GrantCancel");

		eval.eval_for_ResourceLifecycle(RoverCaseStudy.makeResourceLifecycle(),
				"ResourceLifecycle");

		//System.err.println("Turn respect conflicts back on");
		 eval.eval_for_RespectConflicts(
		 RoverCaseStudy.makeRespectConflictsSingle(), "RespectConflicts");		
		
		
			eval.eval_for_IncreasingCommand(RoverCaseStudy.makeIncreasingCommand(),
					"IncreasingCommand");		 
		
			//System.err.println("Turn exists sat back on");
			 eval.eval_for_ExistsSatellite(
					 RoverCaseStudy.makeExistsSatelliteSingle(), "ExistsSatellite");


		if(QeaDoWork.category.isGeneral()){

			 eval.eval_for_AcknowledgeCommands(
			 RoverCaseStudy.makeAcknowledgeCommands(), "AcknowledgeCommands");		 
			 
			 eval.eval_for_ReleaseResource(RoverCaseStudy.makeReleaseResource(),
			 "ReleaseResource");		 
			 
			eval.eval_for_NestedCommand(RoverCaseStudy.makeNestedCommand(),
			 "NestedCommand");
	
			eval.eval_for_ExistsLeader(RoverCaseStudy.makeExistsLeader(),
			 "ExistsLeader");
			
			eval.eval_for_RespectPriorities(RoverCaseStudy.makeRespectPriorities(),
					 "RespectPriorities");	
		}

	}

	public static enum Category{
		NORMAL,
		NAIVE,
		SYMBOL,
		GENERAL_STRUCT;

		public boolean isGeneral() {
			switch(this){
			case SYMBOL: return true;
			case NAIVE: return true;
			}
			return false;
		}
	}
	
	static class QeaDoWork extends DoWork<QEA> {

		public static boolean print = false;
		public static Category category = Category.NORMAL;

		@Override
		public void run_with_spec(QEA qea, String name, int[] args) {

			switch(category){
			case NORMAL : break;
			case GENERAL_STRUCT : 
				qea = QEABuilder.change(qea, QEAType.QVAR01_FVAR_NONDET_QEA); break;
			case NAIVE:
			case SYMBOL:
				try{
					qea = QEABuilder.change(qea, QEAType.QVARN_FVAR_DET_QEA); break;
				}catch(Exception e){
					qea = QEABuilder.change(qea, QEAType.QVARN_FVAR_NONDET_QEA); break;
				}
			}
			
			setup(qea);
			dowork(name, args);
			if (print) {
				System.err.println(monitor);
			}
		}

		private Monitor monitor;
		private int[] events;

		/**
		 * Creates a monitor for the specified QEA property and initialises the
		 * array of events, this is for each event name obtains the ID of the event
		 * in the QEA
		 * 
		 * @param qea
		 *            QEA property
		 */
		private void setup(QEA qea) {
			if(category==Category.NAIVE)
				monitor = MonitorFactory.createNaive(qea);
			else
				monitor = MonitorFactory.create(qea);
			events = new int[16];

			events[0] = qea.get_event_id("command");
			events[1] = qea.get_event_id("request");
			events[2] = qea.get_event_id("grant");
			events[3] = qea.get_event_id("deny");
			events[4] = qea.get_event_id("rescind");
			events[5] = qea.get_event_id("cancel");
			events[6] = qea.get_event_id("succeed");
			events[7] = qea.get_event_id("set_ack_timeout");
			events[8] = qea.get_event_id("schedule");
			events[9] = qea.get_event_id("finish");
			events[10] = qea.get_event_id("conflict");
			events[11] = qea.get_event_id("ping");
			events[12] = qea.get_event_id("send");
			events[13] = qea.get_event_id("ack");
			events[14] = qea.get_event_id("fail");
			events[15] = qea.get_event_id("priority");
		}

		/**
		 * Prints an error message if the specified verdict is a failure
		 * 
		 * @param verdict
		 *            Verdict
		 */
		public void handle(Verdict verdict) {
			if (verdict == Verdict.FAILURE) {
				System.err.println("warning: failure");
				System.err.println(monitor);				
				System.exit(0);
			}
		}

		@Override
		public void command(int x) {
			handle(monitor.step(events[0], x));
		}

		@Override
		public void command(Object o) {
			handle(monitor.step(events[0], o));
		}

		@Override
		public void command(Object a, Object b, Object c, int d) {
			handle(monitor.step(events[0], a, b, c, d));
		}

		@Override
		public void request(Object o) {
			handle(monitor.step(events[1], o));
		}

		@Override
		public void grant(Object o) {
			handle(monitor.step(events[2], o));
		}

		@Override
		public void grant(Object a, Object b) {
			handle(monitor.step(events[2], a, b));
		}

		@Override
		public void deny(Object o) {
			handle(monitor.step(events[3], o));
		}

		@Override
		public void rescind(Object o) {
			handle(monitor.step(events[4], o));
		}

		@Override
		public void cancel(Object o) {
			handle(monitor.step(events[5], o));
		}

		@Override
		public void cancel(Object a, Object b) {
			handle(monitor.step(events[5], a, b));
		}

		@Override
		public void succeed(Object o) {
			handle(monitor.step(events[6], o));
		}

		@Override
		public void set_ack_timeout(int x) {
			handle(monitor.step(events[7], x));
		}

		@Override
		public void schedule(Object a, Object b) {
			handle(monitor.step(events[8], a, b));
		}

		@Override
		public void finish(Object o) {
			handle(monitor.step(events[9], o));
		}

		@Override
		public void conflict(Object a, Object b) {
			handle(monitor.step(events[10], a, b));
		}

		@Override
		public void ping(Object a, Object b) {
			handle(monitor.step(events[11], a, b));
		}

		@Override
		public void send(Object a, Object b, int c) {
			handle(monitor.step(events[12], a, b, c));
		}

		@Override
		public void ack(Object a, Object b) {
			handle(monitor.step(events[13], a, b));
			;
		}

		@Override
		public void ack(Object a, Object b, int c) {
			handle(monitor.step(events[13], a, b, c));
		}

		@Override
		public void ack(Object o, int x) {
			handle(monitor.step(events[13], o, x));
		}

		@Override
		public void fail(Object o) {
			handle(monitor.step(events[14], o));

		}

		@Override
		public void priority(Object a, Object b) {
			handle(monitor.step(events[15], a, b));
		}
	}

	
}
