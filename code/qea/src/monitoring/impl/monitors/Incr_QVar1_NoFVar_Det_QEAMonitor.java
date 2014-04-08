package monitoring.impl.monitors;

import java.util.IdentityHashMap;
import java.util.Map;

import monitoring.impl.GarbageMode;
import monitoring.impl.RestartMode;
import structure.impl.other.Verdict;
import structure.impl.qeas.QVar01_NoFVar_Det_QEA;
import util.WeakIdentityHashMap;
import exceptions.ShouldNotHappenException;

/**
 * A small-step monitor for the Simplest QEA
 * 
 * @author Giles Reger
 * @author Helena Cuenca
 */
public class Incr_QVar1_NoFVar_Det_QEAMonitor extends
		Abstr_Incr_QVar1_NoFVar_QEAMonitor<QVar01_NoFVar_Det_QEA> {

	private Map<Object, Integer> bindings;
	private int empty_state;

	/**
	 * Creates an IncrementalSimpleDetQEAMonitor for the specified QEA
	 * 
	 * @param qea
	 *            QEA
	 */
	public Incr_QVar1_NoFVar_Det_QEAMonitor(RestartMode restart, GarbageMode garbage, QVar01_NoFVar_Det_QEA qea) {
		super(restart,garbage,qea);
		if(garbage==GarbageMode.LAZY)
			bindings = new WeakIdentityHashMap<>();
		else
			bindings = new IdentityHashMap<>();
		empty_state = qea.getInitialState();
	}

	@Override
	public Verdict step(int eventName, Object[] args) {
		if (args.length > 1) {
			throw new ShouldNotHappenException(
					"Was only expecting one parameter");
		}
		return step(eventName, args[0]);
	}

	@Override
	public Verdict step(int eventName, Object param1) {

		if(saved!=null){
			if(!restart()) return saved;
		}		
		
		boolean existingBinding = false;
		int startState;

		// Determine if the value received corresponds to an existing binding
		if (bindings.containsKey(param1)) { // Existing binding

			// Get current state for the binding
			startState = bindings.get(param1);

			// Assign flag for counters update
			existingBinding = true;

		} else { // New binding
			startState = empty_state;
		}

		// Compute next state
		int endState = qea.getNextState(startState, eventName);

		// Update/add state for the binding
		bindings.put(param1, endState);

		// Flag needed to update counters later
		boolean endStateFinal = qea.isStateFinal(endState);

		// Determine if there is a final/non-final strong state
		if (qea.isStateStrong(endState)) {
			if(restart_mode.on()) strong.add(param1);
			if (endStateFinal) {
				finalStrongState = true;
			} else {
				nonFinalStrongState = true;
			}
		}

		// Update counters
		updateCounters(existingBinding, qea.isStateFinal(startState),
				endStateFinal);

		return computeVerdict(false);
	}

	private final static Object[] dummyArgs = new Object[]{};
	@Override
	public Verdict step(int eventName) {
		empty_state = qea.getNextState(empty_state, eventName);
		Verdict finalVerdict = null;
		for (Object bound_object : bindings.keySet()) {
			finalVerdict = step(eventName, bound_object);
		}
		return finalVerdict;
	}

	@Override
	public String getStatus() {
		String ret = "Map:\n";
		for (IdentityHashMap.Entry<Object, Integer> entry : bindings.entrySet()) {
			ret += entry.getKey() + "\t->\t" + entry.getValue() + "\n";
		}
		return ret;
	}

	@Override
	protected int removeStrongBindings() {
		int removed = 0;
		for(Object o : strong){
			int state = bindings.get(o);
			if(qea.isStateFinal(state)==finalStrongState){
				removed++;
				bindings.remove(o);
			}
			
		}
		strong.clear();
		return removed;
	}

	@Override
	protected int rollbackStrongBindings() {
		int rolled = 0;
		for(Object o : strong){
			int state = bindings.get(o);
			if(qea.isStateFinal(state)==finalStrongState){			
				bindings.put(o,empty_state);
				rolled++;
			}
		}
		strong.clear();
		return rolled;
	}	
	
}
