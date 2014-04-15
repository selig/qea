package structure.impl.qeas;

import java.util.ArrayList;
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

		throw new RuntimeException("not implemented");

	}

	public void setupMatching() {
		e_sigs = new int[delta[0].length][][];
		for (int e = 1; e < delta[0].length; e++) {
			Set<ArrayList<Integer>> sigset = new HashSet<ArrayList<Integer>>();
			for (int s = 1; s < delta.length; s++) {
				Transition[] ts = delta[s][e];
				for(Transition t : ts) {
                    int[] targs = t.getVariableNames();
                    ArrayList<Integer> itargs = new ArrayList<Integer>();
                    for(int v : targs) itargs.add(v);					
					sigset.add(itargs);
				}
			}
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
