package monitoring.impl;

import java.util.HashSet;

import monitoring.impl.configs.NonDetConfig;
import monitoring.intf.Monitor;
import structure.impl.other.Verdict;
import structure.intf.QEA;

/**
 * This class provides implementation for the <code>trace</code> method using
 * the <code>step</code> method, as well as some utility methods to be used by
 * the subclasses
 * 
 * @author Giles Reger
 * @author Helena Cuenca
 */
public abstract class IncrementalMonitor<Q extends QEA> extends Monitor<Q> {

	/**
	 * Number of bindings for this monitor that are in a final state
	 */
	protected int bindingsInFinalStateCount;

	/**
	 * Number of bindings for this monitor that are in a non-final state
	 */
	protected int bindingsInNonFinalStateCount;

	/**
	 * We have seen a final strong state
	 */
	protected boolean finalStrongState = false;

	/**
	 * We have seen a non-final strong state
	 */
	protected boolean nonFinalStrongState = false;

	/**
	 * The previous verdict, only save if it was strong, used in restart
	 */
	protected Verdict saved = null;
	protected final HashSet<Object> strong;	
	
	/**
	 * Garbage and restart modes
	 */
	protected final RestartMode restart_mode;
	protected final GarbageMode garbage_mode;
	
	/**
	 * Class constructor specifying the QEA to be monitored. For invocation by
	 * subclass constructors
	 * 
	 * @param qea
	 *            QEA property
	 */
	protected IncrementalMonitor(RestartMode restart, GarbageMode garbage,Q qea) {
		super(qea);
		restart_mode = restart;
		garbage_mode = garbage;
		strong = new HashSet<Object>();
	}

	@Override
	public Verdict trace(int[] eventNames, Object[][] args) {

		Verdict finalVerdict = null;

		for (int i = 0; i < eventNames.length; i++) {
			switch (args[i].length) {
			case 0:
				finalVerdict = step(eventNames[i]);
				break;
			case 1:
				finalVerdict = step(eventNames[i], args[i][0]);
				break;
			case 2:
				finalVerdict = step(eventNames[i], args[i][0], args[i][1]);
				break;
			case 3:
				finalVerdict = step(eventNames[i], args[i][0], args[i][1],
						args[i][2]);
				break;
			case 4:
				finalVerdict = step(eventNames[i], args[i][0], args[i][1],
						args[i][2], args[i][3]);
				break;
			case 5:
				finalVerdict = step(eventNames[i], args[i][0], args[i][1],
						args[i][2], args[i][3], args[i][4]);
				break;
			default:
				finalVerdict = step(eventNames[i], args[i]);
				break;
			}
		}
		return finalVerdict;
	}

	/**
	 * Determines if all bindings in this monitor are in a final state
	 * 
	 * @return <code>true</code> if all bindings in this monitor are in a final
	 *         state; <code>false</code> otherwise
	 */
	protected boolean allBindingsInFinalState() {
		if (bindingsInNonFinalStateCount == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Determines if there is at least one binding in a final state in this
	 * monitor
	 * 
	 * @return <code>true</code> if at least one binding in this monitor is in
	 *         final state; <code>false</code> otherwise
	 */
	protected boolean existsOneBindingInFinalState() {
		if (bindingsInFinalStateCount > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Updates the counters for the bindings in final and non-final state after
	 * a binding has been added/modified
	 * 
	 * @param existingBinding
	 *            Indicates if a binding was modified (<code>true</code>) or if
	 *            a new binding was added (<code>false</code>)
	 * @param startConfigFinal
	 *            Indicates if the initial configuration of the binding was in a
	 *            final state
	 * @param endConfigFinal
	 *            Indicates if the final configuration of the binding was in a
	 *            final state
	 */
	protected void updateCounters(boolean existingBinding,
			boolean startConfigFinal, boolean endConfigFinal) {
		if (existingBinding) {
			if (startConfigFinal && !endConfigFinal) {
				bindingsInNonFinalStateCount++;
				bindingsInFinalStateCount--;
			} else if (!startConfigFinal && endConfigFinal) {
				bindingsInNonFinalStateCount--;
				bindingsInFinalStateCount++;
			}
		} else {
			if (endConfigFinal) {
				bindingsInFinalStateCount++;
			} else {
				bindingsInNonFinalStateCount++;
			}
		}
	}

	/**
	 * Determines if the specified non-deterministic configuration contains a
	 * final strong state or a non-final strong state and sets the corresponding
	 * flags of this monitor accordingly.
	 * 
	 * @param config
	 *            Non-deterministic configuration containing a set of states to
	 *            be checked
	 * @param qVarValue 
	 * @return <code>true</code> if the specified configuration contains at
	 *         least one final state (not necessarily strong);
	 *         <code>false</code> otherwise
	 */
	protected boolean checkFinalAndStrongStates(NonDetConfig config, Object qVarValue) {
		
		boolean endConfigFinal = false;
		boolean allNonFinalStrongState = true;
		int[] configStates = config.getStates();
		for (int s : configStates) {
			if (qea.isStateFinal(s)) {
				endConfigFinal = true;
				allNonFinalStrongState=false;
				if (qea.isStateStrong(s)) {
					if(restart_mode.on()) strong.add(qVarValue);
					finalStrongState = true;
				}
			} else if (!qea.isStateStrong(s)) {
				allNonFinalStrongState = false;				
			}
		}
		if(allNonFinalStrongState){
			if(restart_mode.on()) strong.add(qVarValue);
			nonFinalStrongState = true;
		}
		return endConfigFinal;
	}

	/**
	 * 
	 */
	protected boolean restart(){
		switch(restart_mode){
			case NONE:
				return false;
			
			case REMOVE:
				// remove the offending binding
				tidyUpOnRestart(removeStrongBindings(),true);
				return true;
			case ROLLBACK:
				//rollback the offending bindings to initial state
				tidyUpOnRestart(rollbackStrongBindings(),false);
				return true;
			case IGNORE:
				//set the offending bindings to be ignored in the future
				tidyUpOnRestart(ignoreStrongBindings(),true);
				return true;
		}
		return false;
	}
	private void tidyUpOnRestart(int strong_bindings,boolean remove){
		if(finalStrongState){
			finalStrongState=false;
			// if removed or initial state is not final then remove those as final states
			if(remove || !qea.isStateFinal(qea.getInitialState())){
				bindingsInFinalStateCount -= strong_bindings;
				if(!remove) bindingsInNonFinalStateCount += strong_bindings;
			}
		}
		else{
			nonFinalStrongState=false;
			// if removed of initial state is final them remove those as nonfinal states
			if(remove || qea.isStateFinal(qea.getInitialState())){
				bindingsInNonFinalStateCount -= strong_bindings;
				if(!remove) bindingsInFinalStateCount += strong_bindings;
			}
		}
		saved=null;	
	}
	/*
	 * Respectively remove and rollback all strong bindings
	 * return the number of bindings removed or rolled back
	 */
	protected abstract int removeStrongBindings();
	protected abstract int rollbackStrongBindings();
	protected abstract int ignoreStrongBindings();
}
