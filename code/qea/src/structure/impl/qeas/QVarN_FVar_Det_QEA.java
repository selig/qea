package structure.impl.qeas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import monitoring.impl.configs.DetConfig;
import structure.impl.other.FullBindingImpl;
import structure.impl.other.QBindingImpl;
import structure.impl.other.Transition;
import structure.intf.Assignment;
import structure.intf.Binding;
import structure.intf.Guard;

/**
 * This class represents the most general deterministic Quantified Event
 * Automaton (QEA)
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class QVarN_FVar_Det_QEA extends Abstr_QVarN_FVar_QEA {

	@Override
	public QEAType getQEAType() {
		return QEAType.QVARN_FVAR_DET_QEA;
	}


	private final Transition[][] delta;


	public QVarN_FVar_Det_QEA(int numStates, int numEvents, int initialState,
			int qVariablesCount, int fVariablesCount) {
		super(numStates,numEvents,initialState,qVariablesCount,fVariablesCount);

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
	
	public void getNextConfig(QBindingImpl qbinding, DetConfig config,
			int eventName, Object[] args) {

		// Remember we're deterministic - this means that we can update the
		// DetConfig

		int start_state = config.getState();
		Binding binding = config.getBinding();
		Transition transition = delta[start_state][eventName];

		if (transition == null) {
			// no transition - fail
			config.setState(0);
			return;
		}

		// if we bind new qvariables or clash with bindings in qbinding
		// then we return without making an update, as we are not relevant
		Binding extend_with = qbinding.extend(transition.getVariableNames(),
				args);
		if (extend_with == null || !qbinding.equals(extend_with)) {
			// not relevant, just return
			System.err.println("Not relevant: "+extend_with);
			return;
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
		// if we cannot go to state 0
		else {
			config.setState(0);
		}

	}


	public void setupMatching() {
		e_sigs = new int[delta[0].length][][];
				
		for (int e = 1; e < delta[0].length; e++) {
			Set<ArrayList<Integer>> sigset = new HashSet<ArrayList<Integer>>();
			for (int s = 1; s < delta.length; s++) {
				Transition t = delta[s][e];
				if (t != null) {
                    int[] targs = t.getVariableNames();
                    ArrayList<Integer> itargs = new ArrayList<Integer>();
                    for(int v : targs) itargs.add(v);					
					sigset.add(itargs);
				}
			}
			e_sigs[e] = new int[sigset.size()][];
            int i=0;
            for(ArrayList<Integer> siglist : sigset){
                 int[] sig = new int[siglist.size()];
                 for(int i1=0;i1<sig.length;i1++) sig[i1]=siglist.get(i1);
                 e_sigs[e][i]=sig;
                 i++;
            }			
		}
		
	}



}
