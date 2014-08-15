package qea.monitoring.impl.monitors;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import qea.monitoring.impl.GarbageMode;
import qea.monitoring.impl.RestartMode;
import qea.monitoring.impl.configs.DetConfig;
import qea.structure.impl.other.Verdict;
import qea.structure.impl.qeas.QVar1_FVar_Det_FixedQVar_QEA;
import qea.util.EagerGarbageHashMap;
import qea.util.IgnoreIdentityWrapper;
import qea.util.IgnoreWrapper;
import qea.util.WeakIdentityHashMap;

/**
 * An incremental monitor for a non-simple deterministic QEA
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class Incr_QVar1_FVar_Det_FixedQVar_QEAMonitor extends
		Abstr_Incr_QVar1_FVar_QEAMonitor<QVar1_FVar_Det_FixedQVar_QEA> {

	/**
	 * Maps the current values of the quantified variable to the deterministic
	 * configuration for each binding. The configuration contains the state and
	 * the bindings for the free variables
	 */
	private Map<Object, DetConfig> bindings;

	/**
	 * Creates an IncrementalNonSimpleDetQEAMonitor for the specified QEA
	 * 
	 * @param qea
	 *            QEA
	 */
	public Incr_QVar1_FVar_Det_FixedQVar_QEAMonitor(RestartMode restart,
			GarbageMode garbage, QVar1_FVar_Det_FixedQVar_QEA qea) {
		super(restart, garbage, qea);
		switch (garbage) {
		case UNSAFE_LAZY:
		case OVERSAFE_LAZY:
		case LAZY:
			bindings = new WeakIdentityHashMap<Object,DetConfig>();
			break;
		case EAGER:
			bindings = new EagerGarbageHashMap<DetConfig>();
			break;
		case NONE:
		default:
			bindings = new IdentityHashMap<Object,DetConfig>();
		}
		if (restart == RestartMode.IGNORE && garbage != GarbageMode.EAGER) {
			bindings = new IgnoreIdentityWrapper<Object,DetConfig>(bindings);
		}
	}

	@Override
	public Verdict step(int eventName, Object[] args) {

		if (saved != null) {
			if (!restart()) {
				return saved;
			}
		}

		boolean existingBinding = false;
		boolean startConfigFinal = false;
		DetConfig config;

		// Obtain the value for the quantified variable
		Object qVarValue = args[0];

		// Determine if the value received corresponds to an existing binding
		if (bindings.containsKey(qVarValue)) { // Existing quantified
												// variable binding

			// Get current configuration for the binding
			config = bindings.get(qVarValue);
			// if config=null it means the object is ignored
			// we should stop processing it here
			if (config == null) {
				return computeVerdict(false);
			}

			// Assign flags for counters update
			existingBinding = true;
			startConfigFinal = qea.isStateFinal(config.getState());

		} else { // New quantified variable binding

			// If the global guard is false then we can return now
			// as we can ignore this binding
			if(!qea.checkGlobalGuard(qVarValue)) return computeVerdict(false);			
			
			// If we're using the IGNORE restart strategy make sure we're not ignoring
			if(restart_mode==RestartMode.IGNORE){
				if(((IgnoreWrapper) bindings).isIgnored(qVarValue)){
					return computeVerdict(false);
				}
			}			
			
			// Create configuration for the new binding
			config = new DetConfig(qea.getInitialState(), qea.newBinding());
		}

		// Compute next configuration
		config = qea.getNextConfig(config, eventName, args, qVarValue);

		// Update/add configuration for the binding
		bindings.put(qVarValue, config);

		// Flag needed to update counters later
		boolean endConfigFinal = qea.isStateFinal(config.getState());

		// Determine if there is a final/non-final strong state
		if (qea.isStateStrong(config.getState())) {
			if (restart_mode.on()) {
				strong.add(qVarValue);
			}
			if (endConfigFinal) {
				finalStrongState = true;
			} else {
				nonFinalStrongState = true;
			}
		}

		// Update counters
		updateCounters(existingBinding, startConfigFinal, endConfigFinal);

		return computeVerdict(false);
	}

	private static final Object[] dummyArgs = new Object[] {};

	@Override
	public Verdict step(int eventName) {
		Verdict finalVerdict = null;
		for (Object binding : bindings.keySet()) {
			throw new RuntimeException("Not implemented properly");
			// finalVerdict = step(eventName, dummyArgs);
		}
		return finalVerdict;
	}

	@Override
	public String toString() {
		String ret = "Map:\n";
		for (IdentityHashMap.Entry<Object, DetConfig> entry : bindings
				.entrySet()) {
			ret += entry.getKey() + "\t->\t" + entry.getValue() + "\n";
		}
		return ret;
	}

	@Override
	public String getStatus() {
		String ret = "Map:\n";
		Set<Map.Entry<Object, DetConfig>> entryset = null;
		if (bindings instanceof EagerGarbageHashMap) {
			entryset = ((EagerGarbageHashMap) bindings).storeEntrySet();
		} else {
			entryset = bindings.entrySet();
		}
		for (Map.Entry<Object, DetConfig> entry : entryset) {
			ret += entry.getKey() + "\t->\t" + entry.getValue() + "\n";
		}
		return ret;
	}

	@Override
	protected int removeStrongBindings() {
		int removed = 0;
		for (Object o : strong) {
			int state = bindings.get(o).getState();
			if (qea.isStateFinal(state) == finalStrongState) {
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
		for (Object o : strong) {
			int state = bindings.get(o).getState();
			if (qea.isStateFinal(state) == finalStrongState) {
				DetConfig c = bindings.get(o);
				c.setState(qea.getInitialState());
				c.getBinding().setEmpty();
				rolled++;
			}
		}
		strong.clear();
		return rolled;
	}

	// TODO - fixed an issue encounted for DaCapo - need to fix this
	// across all monitors. The issue is that strong bindings may
	// have become garbage so are no longer in bindings
	@Override
	protected int ignoreStrongBindings() {
		int ignored = 0;
		for (Object o : strong) {
			DetConfig config = bindings.get(o);
			if (config != null) {
				int state = config.getState();
				if (qea.isStateFinal(state) == finalStrongState) {
					((IgnoreWrapper) bindings).ignore(o);
					ignored++;
				}
			} else {
				ignored++; // we still want to remove it
			}
		}
		strong.clear();
		return ignored;
	}
}
