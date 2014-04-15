package structure.impl.qeas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import monitoring.impl.configs.NonDetConfig;
import structure.impl.other.FullBindingImpl;
import structure.impl.other.QBindingImpl;
import structure.impl.other.Transition;
import structure.intf.Assignment;
import structure.intf.Binding;
import structure.intf.Guard;
import structure.intf.QEA_nondet_free;
import util.ArrayUtil;

/**
 * This class represents the most general deterministic Quantified Event Automaton (QEA)
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class QVarN_FVar_NonDet_QEA extends Abstr_QVarN_FVar_QEA implements QEA_nondet_free {

	public QEAType getQEAType(){ return QEAType.QVARN_FVAR_NONDET_QEA;}	
	

	private final Transition[][][] delta;

	
	public QVarN_FVar_NonDet_QEA(int numStates, int numEvents,
			int initialState, int qVariablesCount, int fVariablesCount) {
		super(numStates,numEvents,initialState,qVariablesCount,fVariablesCount);
		
		this.delta = new Transition[numStates+1][numEvents+1][];

	}

	public void addTransitions(int startState, int event, Transition[] transitions) {
		if (delta[startState][event] == null) {
			delta[startState][event] = transitions;
		} else {
			// Add new transitions
			delta[startState][event] = ArrayUtil.concat(
					delta[startState][event], transitions);
		}
	}


	@Override
	public int[] getEventsAlphabet() {
		int[] a = new int[delta[0].length - 1];
		for (int i = 0; i < a.length; i++) {
			a[i] = i + 1;
		}
		return a;
	}

    public Transition[][][] getTransitions(){
               return delta;
    }

	@Override
	public boolean isDeterministic() {
		return false;
	}

	public NonDetConfig getNextConfig(QBindingImpl qbinding, NonDetConfig config,
			int eventName, Object[] args) {

		//This *must* copy config
		
		int[] start_states = config.getStates();
		Binding[] start_bindings = config.getBindings();

		int[] end_states = null;
		Binding[] end_bindings = null;
		int end_count=0;
		
		for(int si = 0; si< start_states.length;si++){
			int start_state = start_states[si];
			Binding binding = start_bindings[si];						
			
			Transition[] transitions = delta[start_state][eventName];
	
			if (transitions == null) {
				// no transition - fail and remove i.e.
				continue;
			}
			if(end_states==null){
				end_states = new int[transitions.length];
				end_bindings = new Binding[transitions.length];
			}else{
				int new_size = end_count+transitions.length;
				if(new_size > end_states.length){
					end_states = ArrayUtil.resize(end_states,new_size);
					end_bindings = ArrayUtil.resize(end_bindings,new_size);
				}
			}
			
			for(Transition transition : transitions){
	
				// if we bind new qvariables or clash with bindings in qbinding
				// e are not relevant
				Binding extend_with = qbinding.extend(transition.getVariableNames(),
						args);
				if (extend_with == null || !qbinding.equals(extend_with)) {
					// not relevant, just return
					continue;
				}
		
				// update binding based on args
				Binding next_binding = binding.extend(transition.getVariableNames(), args);
		

				// check guard
				Guard guard = transition.getGuard();
				Binding extended_guard = next_binding;
				if (guard != null && guard.usesQvars()) {
					extended_guard = new FullBindingImpl(next_binding, qbinding);
				}
				if (guard == null || guard.check(extended_guard)) {
					int next_state = transition.getEndState();
					Assignment assignment = transition.getAssignment();
					// we've copied the binding, so the assignment doesn't
					// need to
					if (assignment != null) {
						assignment.apply(next_binding, false);
					}
					end_states[end_count] = next_state;
					end_bindings[end_count] = next_binding;
					end_count++;
				}
				//else don't add anything, as it would be
				//the failing state
			}
		}
		
		//if everything has been zero
		if(end_count==0)
			return new NonDetConfig(0,newFBinding());		
		
		end_states = ArrayUtil.resize(end_states, end_count);
		end_bindings = ArrayUtil.resize(end_bindings,end_count);
		
		NonDetConfig next_config = new NonDetConfig(end_states, end_bindings);
		
		//System.err.println(config+" --"+eventName+Arrays.toString(args)+"--> "+next_config+" for "+qbinding);
		
		return next_config;
	}

	public void setupMatching() {
		e_sigs = new int[delta[0].length][][];
		for (int e = 1; e < delta[0].length; e++) {
			Set<ArrayList<Integer>> sigset = new HashSet<ArrayList<Integer>>();
			for (int s = 1; s < delta.length; s++) {
				Transition[] ts = delta[s][e];
				if(ts!=null)
				for(Transition t : ts) {
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

	@Override
	public Transition[][][] getDelta() {
		return delta;
	}	

	
}
