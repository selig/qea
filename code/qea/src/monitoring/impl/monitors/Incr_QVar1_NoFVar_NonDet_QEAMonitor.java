package monitoring.impl.monitors;

import java.util.IdentityHashMap;
import java.util.Map;

import monitoring.impl.GarbageMode;
import monitoring.impl.RestartMode;
import monitoring.impl.configs.NonDetConfig;
import structure.impl.other.Verdict;
import structure.impl.qeas.QVar01_NoFVar_NonDet_QEA;
import util.WeakIdentityHashMap;

/**
 * A small-step monitor for a non-deterministic simple QEA
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class Incr_QVar1_NoFVar_NonDet_QEAMonitor extends
		Abstr_Incr_QVar1_NoFVar_QEAMonitor<QVar01_NoFVar_NonDet_QEA> {

	private Map<Object, NonDetConfig> bindings;

	private NonDetConfig empty_config;
	
	public Incr_QVar1_NoFVar_NonDet_QEAMonitor(RestartMode restart, GarbageMode garbage, QVar01_NoFVar_NonDet_QEA qea) {
		super(restart,garbage,qea);
		if(garbage==GarbageMode.LAZY)
			bindings = new WeakIdentityHashMap<>();
		else
			bindings = new IdentityHashMap<>();
		empty_config = new NonDetConfig(qea.getInitialState());
	}

	@Override
	public Verdict step(int eventName, Object param1) {

		if(saved!=null){
			if(!restart()) return saved;
		}		
		
		boolean existingBinding = false;
		boolean startConfigFinal = false;
		NonDetConfig config;

		// Determine if the value received corresponds to an existing binding
		if (bindings.containsKey(param1)) { // Existing quantified variable
											// binding

			// Get current configuration for the binding
			config = bindings.get(param1);

			// Assign flags for counters update
			existingBinding = true;
			startConfigFinal = qea.containsFinalState(config);

		} else { // New quantified variable binding

			// Create configuration for the new binding
			config = empty_config.copy();
		}

		// Compute next configuration
		config = qea.getNextConfig(config, eventName);

		// Update/add configuration for the binding
		bindings.put(param1, config);

		// Determine if there is a final/non-final strong state
		boolean endConfigFinal = checkFinalAndStrongStates(config,param1);

		// Update counters
		updateCounters(existingBinding, startConfigFinal, endConfigFinal);

		return computeVerdict(false);
	}

	@Override
	public Verdict step(int eventName) {		
		Verdict finalVerdict = null;
		empty_config = qea.getNextConfig(empty_config,eventName);
		for (Object bound_object : bindings.keySet()) {			
			finalVerdict = step(eventName, bound_object);
		}
		return finalVerdict;
	}

	@Override
	public String getStatus() {
		String ret = "Map:\n";
		for (IdentityHashMap.Entry<Object, NonDetConfig> entry : bindings
				.entrySet()) {
			ret += entry.getKey() + "\t->\t" + entry.getValue() + "\n";
		}
		return ret;
	}

	@Override
	protected int removeStrongBindings() {
		int removed = 0;
		for(Object o : strong){	
			NonDetConfig c = bindings.get(o);
			boolean is_final = false;
			for(int s : c.getStates()) is_final |= qea.isStateFinal(s);
			if(is_final==finalStrongState){
				bindings.remove(o);
				removed++;
			}
		}
		strong.clear();
		return removed;
	}

	@Override
	protected int rollbackStrongBindings() {
		int rolled = 0;
		for(Object o : strong){
			NonDetConfig c = bindings.get(o);
			boolean is_final = false;
			for(int s : c.getStates()) is_final |= qea.isStateFinal(s);
			if(is_final==finalStrongState){
				bindings.put(o,empty_config.copy());
				rolled++;
			}						
		}
		strong.clear();
		return rolled;
	}	
	
}
