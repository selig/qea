package qea.structure.impl.qeas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import qea.monitoring.impl.configs.DetConfig;
import qea.structure.impl.other.FullBindingImpl;
import qea.structure.impl.other.QBindingImpl;
import qea.structure.impl.other.Transition;
import qea.structure.intf.Assignment;
import qea.structure.intf.Binding;
import qea.structure.intf.Guard;
import qea.structure.intf.QEA_det_free;
import qea.exceptions.NotRelevantException;

/**
 * This class represents the most general deterministic Quantified Event
 * Automaton (QEA)
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class QVarN_Det_QEA extends Abstr_QVarN_QEA implements QEA_det_free {

	private final Transition[][] delta;

	public QVarN_Det_QEA(int numStates, int numEvents, int initialState,
			int qVariablesCount, int fVariablesCount, QEAType type) {
		super(numStates, numEvents, initialState, qVariablesCount,
				fVariablesCount, type);

		delta = new Transition[numStates + 1][numEvents + 1];

	}

	public Transition[][] getTransitions() {
		return delta;
	}

	@Override
	public boolean isDeterministic() {
		return true;
	}

	public void addTransition(int startState, int event, Transition transition) {
		delta[startState][event] = transition;
	}

	@Override
	public int[] getEventsAlphabet() {
		int[] a = new int[delta[0].length - 1];
		for (int i = 0; i < a.length; i++) {
			a[i] = i + 1;
		}
		return a;
	}

	/*
	 * MUST check for null - instead of NotRelevantException
	 */
	public Integer getNextConfig(QBindingImpl qbinding, Integer start_state,
			int eventName, Object[] args) {

		Transition transition = delta[start_state][eventName];

		if (transition == null) {
			// no transition - fail if not a skip state
			if (!skipStates[start_state]) {
				return 0;
			}
			return start_state;
		}

		// if we bind new qvariables or clash with bindings in qbinding
		// then we return without making an update, as we are not relevant
		Binding extend_with = qbinding.extend(transition.getVariableNames(),
				args);
		if (extend_with == null || !qbinding.equals(extend_with)) {
			// not relevant, just return null
			// MUST check for this
			return null;
		}

		// No guards or assignments - just return the end state
		return transition.getEndState();

	}

	public void getNextConfig(QBindingImpl qbinding, DetConfig config,
			int eventName, Object[] args) throws NotRelevantException {

		// Remember we're deterministic - this means that we can update the
		// DetConfig

		//System.err.println("getNextConfig");
		
		int start_state = config.getState();
		Binding binding = config.getBinding();
		Transition transition = delta[start_state][eventName];

		//boolean no_transitions=false;
		if (transition == null) {
			// no transition - fail if not a skip state
			if (!skipStates[start_state]) {
				config.setState(0);
			}
			//Should check for relevance here
			// not relevant if there is no transition anywhere such that the below relevance check would pass
			boolean relevant_transition = false;
			for(int s=0;s<delta.length;s++){
				transition = delta[s][eventName];
				if(transition!=null){
					Binding extend_with = qbinding.extend(transition.getVariableNames(),args);
					if(!(extend_with == null || !qbinding.equals(extend_with))){
						//System.err.println(transition+" is relevant "+extend_with);
						relevant_transition=true;
					}
				}
			}
			if(!relevant_transition) throw new NotRelevantException();
			
			return;
		}

		// if we bind new qvariables or clash with bindings in qbinding
		// then we return without making an update, as we are not relevant
		Binding extend_with = qbinding.extend(transition.getVariableNames(),args);
		if (extend_with == null || !qbinding.equals(extend_with)) {
			// not relevant, just return
			 //System.err.println("Not relevant: "+qbinding+" for "+eventName+Arrays.toString(args));
			throw new NotRelevantException();
		}

		// update binding based on args
		binding.update(transition.getVariableNames(), args);

		// check guard
		Guard guard = transition.getGuard();
		Binding extended_guard = binding;
		if (guard != null && guard.usesQvars()) {
			extended_guard = new FullBindingImpl(binding, qbinding);
		}
		if (guard == null || guard.check(extended_guard)) {
			config.setState(transition.getEndState());
			Assignment assignment = transition.getAssignment();
			// in det case we just update the binding, no copy
			if (assignment != null) {
				assignment.apply(binding, false);
			}
		}
		// if we cannot go to state 0 unless a skip state
		else {
			if (!skipStates[start_state]) {
				config.setState(0);
			}
		}

	}

	@Override
	public void setupMatching() {
		e_sigs = new int[delta[0].length][][];

		for (int e = 1; e < delta[0].length; e++) {
			Set<ArrayList<Integer>> sigset = new HashSet<ArrayList<Integer>>();
			for (int s = 1; s < delta.length; s++) {
				Transition t = delta[s][e];
				if (t != null) {
					int[] targs = t.getVariableNames();
					ArrayList<Integer> itargs = new ArrayList<Integer>();
					for (int v : targs) {
						itargs.add(v);
					}
					sigset.add(itargs);
				}
			}
			
			e_sigs[e] = new int[sigset.size()][];
			int i = 0;
			for (ArrayList<Integer> siglist : sigset) {
				int[] sig = new int[siglist.size()];
				for (int i1 = 0; i1 < sig.length; i1++) {
					sig[i1] = siglist.get(i1);
				}
				e_sigs[e][i] = sig;
				i++;
			}
		}

	}

	@Override
	public Transition[][] getDelta() {
		return delta;
	}

	@Override
	public boolean can_leave(int state, int event) {
		if (isNormal()) {
			Transition t = delta[state][event];
			// if no transition then this depends on whether state is skip
			if (t == null) {
				return !skipStates[state];
			}
			// if t has any fvars then it's true as the config can get updated
			for (int v : t.getVariableNames()) {
				if (v > 0) {
					return true;
				}
			}
			// lastly check if we loop
			return t.getEndState() != state;
		} else {
			return true;
		}
	}

}
