package qea.monitoring.impl.monitors;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import qea.monitoring.impl.GarbageMode;
import qea.monitoring.impl.RestartMode;
import qea.monitoring.impl.configs.NonDetConfig;
import qea.structure.impl.other.Verdict;
import qea.structure.impl.qeas.QVar01_NoFVar_NonDet_QEA;
import qea.util.EagerGarbageHashMap;
import qea.util.IgnoreIdentityWrapper;
import qea.util.IgnoreWrapper;
import qea.util.WeakIdentityHashMap;

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

	public Incr_QVar1_NoFVar_NonDet_QEAMonitor(RestartMode restart,
			GarbageMode garbage, QVar01_NoFVar_NonDet_QEA qea) {
		super(restart, garbage, qea);
		switch (garbage) {
		case UNSAFE_LAZY:
		case OVERSAFE_LAZY:
		case LAZY:
			bindings = new WeakIdentityHashMap<Object,NonDetConfig>();
			break;
		case EAGER:
			bindings = new EagerGarbageHashMap<NonDetConfig>();
			break;
		case NONE:
			bindings = new IdentityHashMap<Object,NonDetConfig>();
		}
		if (restart == RestartMode.IGNORE && garbage != GarbageMode.EAGER) {
			bindings = new IgnoreIdentityWrapper<Object,NonDetConfig>(bindings);
		}
		empty_config = new NonDetConfig(qea.getInitialState(),null);
	}

	@Override
	public Verdict step(int eventName, Object param1) {

		if (saved != null) {
			if (!restart()) {
				return saved;
			}
		}

		boolean existingBinding = false;
		boolean startConfigFinal = false;
		NonDetConfig config;

		// Determine if the value received corresponds to an existing binding
		if (bindings.containsKey(param1)) { // Existing quantified variable
											// binding

			// Get current configuration for the binding
			config = bindings.get(param1);
			// if config=null it means the object is ignored
			// we should stop processing it here
			if (config == null) {
				return computeVerdict(false);
			}

			// Assign flags for counters update
			existingBinding = true;
			startConfigFinal = qea.containsFinalState(config);

		} else { // New quantified variable binding

			// If the global guard is false then we can return now
			// as we can ignore this binding
			if(!qea.checkGlobalGuard(param1)) return computeVerdict(false);			
			
			// If we're using the IGNORE restart strategy make sure we're not ignoring
			if(restart_mode==RestartMode.IGNORE){
				if(((IgnoreWrapper) bindings).isIgnored(param1)){
					return computeVerdict(false);
				}
			}			
			
			// Create configuration for the new binding
			config = empty_config.copyForExtension();
		}

		// Compute next configuration
		config = qea.getNextConfig(config, eventName);

		// Update/add configuration for the binding
		bindings.put(param1, config);

		// Determine if there is a final/non-final strong state
		boolean endConfigFinal = checkFinalAndStrongStates(config, param1);

		// Update counters
		updateCounters(existingBinding, startConfigFinal, endConfigFinal);

		return computeVerdict(false);
	}

	@Override
	public Verdict step(int eventName) {
		Verdict finalVerdict = null;
		empty_config = qea.getNextConfig(empty_config, eventName);
		for (Object bound_object : bindings.keySet()) {
			finalVerdict = step(eventName, bound_object);
		}
		return finalVerdict;
	}

	@Override
	public String getStatus() {
		String ret = "Map:\n";
		Set<Map.Entry<Object, NonDetConfig>> entryset = null;
		if (bindings instanceof EagerGarbageHashMap) {
			entryset = ((EagerGarbageHashMap) bindings).storeEntrySet();
		} else {
			entryset = bindings.entrySet();
		}
		for (Map.Entry<Object, NonDetConfig> entry : entryset) {
			ret += entry.getKey() + "\t->\t" + entry.getValue() + "\n";
		}
		return ret;
	}

	@Override
	protected int removeStrongBindings() {
		int removed = 0;
		for (Object o : strong) {
			NonDetConfig c = bindings.get(o);
			boolean is_final = false;
			for (int s : c.getStates()) {
				is_final |= qea.isStateFinal(s);
			}
			if (is_final == finalStrongState) {
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
		for (Object o : strong) {
			NonDetConfig c = bindings.get(o);
			boolean is_final = false;
			for (int s : c.getStates()) {
				is_final |= qea.isStateFinal(s);
			}
			if (is_final == finalStrongState) {
				bindings.put(o, empty_config.copyForExtension());
				rolled++;
			}
		}
		strong.clear();
		return rolled;
	}

	@Override
	protected int ignoreStrongBindings() {
		int ignored = 0;
		for (Object o : strong) {
			NonDetConfig c = bindings.get(o);
			boolean is_final = false;
			for (int s : c.getStates()) {
				is_final |= qea.isStateFinal(s);
			}
			if (is_final == finalStrongState) {
				((IgnoreWrapper) bindings).ignore(o);
				ignored++;
			}
		}
		strong.clear();
		return ignored;
	}

}
