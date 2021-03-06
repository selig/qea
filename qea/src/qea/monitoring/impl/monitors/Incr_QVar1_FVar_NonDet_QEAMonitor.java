package qea.monitoring.impl.monitors;

import java.util.Arrays;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import qea.monitoring.impl.GarbageMode;
import qea.monitoring.impl.RestartMode;
import qea.monitoring.impl.configs.NonDetConfig;
import qea.structure.impl.other.Transition;
import qea.structure.impl.other.Verdict;
import qea.structure.impl.qeas.QVar01_FVar_NonDet_QEA;
import qea.util.EagerGarbageHashMap;
import qea.util.IgnoreIdentityWrapper;
import qea.util.IgnoreWrapper;
import qea.util.WeakIdentityHashMap;

/**
 * An incremental monitor for a non-simple non-deterministic generic QEA
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 * @param <Q>
 */
public class Incr_QVar1_FVar_NonDet_QEAMonitor extends
		Abstr_Incr_QVar1_FVar_QEAMonitor<QVar01_FVar_NonDet_QEA> {

	/**
	 * Maps the current values of the quantified variable to the
	 * non-deterministic configuration for each binding. The configuration
	 * contains the set of states and set of bindings for the free variables
	 */
	private Map<Object, NonDetConfig> bindings;

	/**
	 * Configuration storing the state and free variables binding for events
	 * where the quantified variable is not present
	 */
	private NonDetConfig emptyBindingConfig;

	/**
	 * For each event stores a <code>true</code> indicating it has at least one
	 * signature that only contains free variables as parameters,
	 * <code>false</code> otherwise
	 */
	private boolean[] onlyFVarSignature;

	/**
	 * For each event stores the number of different positions of the quantified
	 * variable in the event signatures
	 */
	private int[] numQVarPositions;

	/**
	 * For each event stores an array showing (with a value <code>true</code>)
	 * the possible different positions of the quantified variables in the
	 * signatures
	 */
	private boolean[][] eventsMasks;

	/**
	 * Creates a <code>IncrementalNonSimpleNonDetGenQEAMonitor</code> to monitor
	 * the specified QEA property
	 * 
	 * @param qea
	 *            QEA property
	 */
	public Incr_QVar1_FVar_NonDet_QEAMonitor(RestartMode restart,
			GarbageMode garbage, QVar01_FVar_NonDet_QEA qea) {
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
		emptyBindingConfig = new NonDetConfig(qea.getInitialState(),
				qea.newBinding(),null);
		buildEventsIndices();
	}

	/**
	 * Initialise the arrays <code>onlyFVarSignature</code>,
	 * <code>numQVarPositions</code> and <code>eventsMasks</code> according to
	 * the events defined in the QEA of this monitor
	 */
	private void buildEventsIndices() {

		int numEvents = qea.getEventsAlphabet().length;
		int[] states = qea.getStates();
		int[] eventsAlphabet = qea.getEventsAlphabet();

		onlyFVarSignature = new boolean[numEvents + 1];
		numQVarPositions = new int[numEvents + 1];
		eventsMasks = new boolean[numEvents + 1][];

		// Iterate over all start states and event names
		for (int state : states) {
			for (int eventName : eventsAlphabet) {

				// Get transitions for the specified start state and event
				Transition[] transitions = qea.getTransitions(state, eventName);
				if (transitions != null) {

					// Iterate over each transition
					for (Transition transition : transitions) {

						Transition transitionImpl = transition;
						boolean onlyFreeVar = true;

						// If needed, initialise array of mask for the event
						if (eventsMasks[eventName] == null) {
							eventsMasks[eventName] = new boolean[transitionImpl
									.getVariableNames().length];
						}

						// Iterate over the variables names of this signature
						int[] varNames = transitionImpl.getVariableNames();
						for (int k = 0; k < varNames.length; k++) {
							if (varNames[k] < 0) { // Quantified variable
								onlyFreeVar = false;
								if (!eventsMasks[eventName][k]) {
									eventsMasks[eventName][k] = true;
									numQVarPositions[eventName]++;
								}
								break; // Exit the loop
							}
						}

						if (onlyFreeVar) {
							// This signature only contains free variables
							onlyFVarSignature[eventName] = true;
						}
					}
				}
			}
		}
	}

	/*
	 * This just removes a binding if it cannot make a move, this is very conservative atm
	 * We remember garbage and try and clear it later
	 */
	private Set<Object> garbage = new HashSet<Object>();
	private Set<Object> garbageToRemove = new HashSet<Object>();
	private boolean inside_bindings =false;
	@Override
	public void garbage_event(Object param){
		NonDetConfig config = bindings.get(param);
		if(config!=null){
			// expensive check for now, replace
			int[] states = config.getStates();
			int[] events = qea.getEventsAlphabet();
			Transition[][][] delta = qea.getDelta();
			for(int state : states){
				for(int event : events){					
					if(delta[state][event]!=null){
						boolean okay=false;
						for(Transition t : delta[state][event]){
							for(int v :t.getVariableNames()){
								if(v == -1) okay = true;
							}
						}
						if(!okay){
							// cannot remove now, but maybe later!
							garbage.add(param);
							return;
						}
					}
					
				}
			}
			if(inside_bindings) garbageToRemove.add(param);
			else bindings.remove(param);
			garbage.remove(param);
		}
	}
	
	@Override
	public Verdict step(int eventName, Object[] args) {

		if(DEBUG){ 
			System.out.println(getStatus());
			System.out.println(">>>"+eventName+Arrays.toString(args));
		}

		if (saved != null) {
			if (!restart()) {
				return saved;
			}
		}

		boolean eventProcessedForAllExistingBindings = false;

		if (onlyFVarSignature[eventName]) {
			inside_bindings=true;

			//if(DEBUG){System.out.println(">>>"+eventName+" has onlyFVar signature");}

			// Update propositional configuration
			emptyBindingConfig = qea.getNextConfig(emptyBindingConfig,
					eventName, args, null, false);

			//if(DEBUG){ System.out.println("New empty is "+emptyBindingConfig); }
			
			// Apply event to all existing bindings
			for (Object binding : bindings.keySet()) {
				stepNoVerdict(eventName, args, binding);
				Verdict verdict = computeVerdict(false);
				if (verdict.isStrong()) {
					return verdict;
				}
			}
			eventProcessedForAllExistingBindings = true;
			inside_bindings=false;
		}

		if (numQVarPositions[eventName] == 1) { // Only one value for the QVar

			//if(DEBUG){System.out.println(">>>"+eventName+" has only one value for QVar");}
			
			Object qVarBinding = getFirstQVarBinding(eventsMasks[eventName],
					args);
			if (!eventProcessedForAllExistingBindings
					|| !bindings.containsKey(qVarBinding)) {
				stepNoVerdict(eventName, args, qVarBinding);
			}
		} else if (numQVarPositions[eventName] > 1) { // Possibly multiple
														// values for the QVar

			Object[] qVarBindings = getDistinctQVarBindings(
					eventsMasks[eventName], args, numQVarPositions[eventName]);
			for (Object qVarBinding : qVarBindings) {
				if (!eventProcessedForAllExistingBindings
						|| !bindings.containsKey(qVarBinding)) {
					stepNoVerdict(eventName, args, qVarBinding);
				}
			}
		}

		if(!garbageToRemove.isEmpty()){
			for(Object o : garbageToRemove) bindings.remove(o);
		}
		
		return computeVerdict(false);
	}

	private final Object[] emptyArgs = new Object[0];

	@Override
	public Verdict step(int eventName) {
		if (saved != null) {
			if (!restart()) {
				return saved;
			}
		}
		
		for (Object bound_object : bindings.keySet()) {
			stepNoVerdict(eventName, emptyArgs, bound_object);
		}
		return computeVerdict(false);
	}

	/**
	 * Processes the specified event with the specified arguments for the
	 * specified binding (quantified variable value) without producing a verdict
	 * 
	 * @param eventName
	 *            Name of the event
	 * @param args
	 *            Array of arguments
	 * @param qVarValue
	 *            Quantified variable value
	 */
	private void stepNoVerdict(int eventName, Object[] args, Object qVarValue) {

		boolean existingBinding = false;
		boolean startConfigFinal = false;
		NonDetConfig config;
		
		// Determine if the value received corresponds to an existing binding
		if (bindings.containsKey(qVarValue)) { // Existing quantified
												// variable binding

			// Get current configuration for the binding
			config = bindings.get(qVarValue);
			// if config=null it means the object is ignored
			// we should stop processing it here
			if (config == null) {
				return;
			}

			// Assign flags for counters update
			existingBinding = true;
			startConfigFinal = qea.containsFinalState(config);

		} else { // New quantified variable binding
			
			// If the global guard is false then we can return now
			// as we can ignore this binding
			if(!qea.checkGlobalGuard(qVarValue)) return;		
			
			// If we're using the IGNORE restart strategy make sure we're not ignoring
			if(restart_mode==RestartMode.IGNORE){
				if(((IgnoreWrapper) bindings).isIgnored(qVarValue)){
					return;
				}
			}			
			
			// Create new configuration with a copy of the propositional conf.
			config = emptyBindingConfig.copyForExtension();
		}

		// Compute next configuration
		config = qea.getNextConfig(config, eventName, args, qVarValue, true);

		if(garbage.contains(qVarValue)){
			garbage_event(qVarValue);
		}
		
		if(existingBinding || !config.equals(emptyBindingConfig)){			
			
			// Update/add configuration for the binding
			bindings.put(qVarValue, config);
	
			// Determine if there is a final/non-final strong state
			boolean endConfigFinal = checkFinalAndStrongStates(config, qVarValue);
	
			// Update counters
			updateCounters(existingBinding, startConfigFinal, endConfigFinal);
		
		}
	}

	@Override
	public String getStatus() {
		String ret = "Empty: " + emptyBindingConfig +"\n";
		ret += "Map ("+bindings.size()+"):\n";
		Set<Map.Entry<Object, NonDetConfig>> entryset = null;
		if (bindings instanceof EagerGarbageHashMap) {
			entryset = ((EagerGarbageHashMap) bindings).storeEntrySet();
		} else {
			entryset = bindings.entrySet();
		}
		for (Map.Entry<Object, NonDetConfig> entry : entryset) {
			int[] states = entry.getValue().getStates();
			//for(int i=0;i<states.length;i++)
			//	if(states[i]==0)
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
				bindings.put(o, emptyBindingConfig.copyForExtension());
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
