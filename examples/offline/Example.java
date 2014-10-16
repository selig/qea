/**
 * An Example of how to perform Offline monitoring
 *
 * This gives the five lines required to perform offline monitoring.
 * We also see examples of how to specify a custom translator and a QEA.
 *
 * @author Giles Reger
 */ 

//For specifying the QEA
import static qea.structure.impl.other.Quantification.EXISTS;
import qea.structure.intf.QEA;
import qea.creation.QEABuilder;
import static qea.structure.intf.Guard.*;
import static qea.structure.intf.Assignment.*;

//For performing monitoring
import qea.monitoring.impl.FileMonitor;
import qea.monitoring.impl.CSVFileMonitor;
import qea.monitoring.impl.translators.OfflineTranslator;
import qea.monitoring.impl.translators.DefaultTranslator;
import qea.structure.impl.other.Verdict;

public class Example{


	public static void main(String[] args) throws Exception{

		// see the associated trace to understand the kind of log files the tool can process
		String trace = "trace.csv";
		// see makeQEA for how the QEA is specified
		QEA qea = makeQEA();
		// see makeTranslator to see how a custom translator is given
		OfflineTranslator translator = makeTranslator(); 
		// This will use the MonitorFactory to create a monitor from the qea
		FileMonitor monitor = new CSVFileMonitor(trace, qea, translator);
		// This performs monitoring and will print out a short report of the results
		Verdict verdict = monitor.monitor();

	}

	/**
	 * This makes a translator for the log file trace.csv
	 *
	 * As we have specified our property over a different form of event as we see in the trace we
	 * must translate each event. The abstract event used in the specification (see makeQEA) is
	 *   withdraw(user_id,amount,time_stamp)
	 *
	 * If we inspect the trace we can see these values are in positions 2, 3 and 1 respectively.
	 *
	 * As well as placing the parameter values in the correct places we also transform the values
	 * of the amount and time_stamp into integers.
	 *
	 **/
	private static OfflineTranslator makeTranslator(){
		return new OfflineTranslator() {

			@Override
			public Verdict translateAndStep(String eventName,
					String[] paramNames, String[] paramValues) {
				if(eventName.equals("withdraw")){
					return monitor.step(1,new Object[]{
									paramValues[2],
									Integer.valueOf(paramValues[3]),
									Integer.valueOf(paramValues[1])
								});
				}
				return null;
			}

			@Override
			public Verdict translateAndStep(String eventName) {
				// No event without parameters is relevant for this property
				return null;
			}
		};
	}

	/**
	 * This makes a QEA for the withdrawal property.
	 * The property is that a user cannot withdraw more than 10000 in a 28 day period
	 *
	 * We assume that the log file consists of events of the abstract form
	 *   withdraw(user_id, amount, time_stamp)
	 * where the time_stamp is in seconds
	 *
	 * The property is specified as a negated QEA specifying the existence of a
	 * misbehaving user i.e. one that withdraws more than 10000 in a 28 day period.
	 */
	private static QEA makeQEA(){

	QEABuilder q = new QEABuilder("withdrawal property");

	// First we specify the single event name
	int WITHDRAW = 1;
	// Next we specify the single quantified variable and the quantification
	// Quantified variables need to be consecutively decreasing integers starting from -1
	int user=-1;
	q.setNegated(true);
	q.addQuantification(EXISTS, user);

	// Next we specify the free variables
	// Free variables need to be consecutive integers starting from 1
	int a=1;  int start=2;  int time=3;  int sum=4;

	// We give the time and amount parameters here so that they can easily be changed
	int max_time = 6912000; // 28 days in seconds 
	int max_amount = 10000;

	// Now we specify the transitions

	// This simply creates a transition from state 1 to 1 with the WITHDRAW event and the given parameters
	q.addTransition(1,WITHDRAW, new int[]{user, a, start}, 1);

	// Note that transitions out of state 1 are non-deterministic
	// storeVar is an assignment used to store the value of a into the free variable s
	// For all available assignments see the Assignment class 
	q.addTransition(1,WITHDRAW, new int[]{user, a, start}, storeVar(sum,a),2);
		                          
	// and, differenceLessThanOrEqualTo and sumLessThanOrEqualToVal are all guards
	// For all available guards see the Guard class
	q.addTransition(2,WITHDRAW, new int[]{user, a, time},
		and(differenceLessThanOrEqualToVal(time,start,max_time),sumLessThanOrEqualToVal(sum,a,max_amount)),
    		add(sum,a),
		2);

	q.addTransition(2,WITHDRAW, new int[]{user, a, time},
    		and(differenceLessThanOrEqualToVal(time,start,max_time),sumGreaterThanVal(sum,a,max_amount)), 
		3);

	// We specify the single final state
	q.addFinalStates(3);

	// We specify the skip states
	// A state is
	//  -- a next state if there should be an implicit failure if no transition can be taken
	//  -- a skip state if the event should be skipped if no transition can be taken
	// By default states are next states
	q.setSkipStates(1,3);

	// Running make on the builder causes the QEA to be built
	return q.make();

	}
}
