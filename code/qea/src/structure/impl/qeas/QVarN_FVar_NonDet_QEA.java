package structure.impl.qeas;

import monitoring.impl.configs.NonDetConfig;
import structure.impl.other.FullBindingImpl;
import structure.impl.other.QBindingImpl;
import structure.impl.other.Transition;
import structure.intf.Assignment;
import structure.intf.Binding;
import structure.intf.Guard;
import util.ArrayUtil;

/**
 * This class represents the most general deterministic Quantified Event Automaton (QEA)
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class QVarN_FVar_NonDet_QEA extends Abstr_QVarN_FVar_QEA {

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

	public void getNextConfig(QBindingImpl qbinding, NonDetConfig config,
			int eventName, Object[] args) {


		throw new RuntimeException("not implemented");

	}

	public void setupMatching() {
		e_sigs = new int[delta[0].length][][];
		for (int e = 1; e < delta[0].length; e++) {
			int[][] sigs = new int[0][];

			for (int s = 1; s < delta.length; s++) {
				Transition[] ts = delta[s][e];
				for(Transition t : ts){
						sigs = ArrayUtil.increaseSize(sigs);
						sigs[sigs.length - 1] = t.getVariableNames();
				}
			}
			e_sigs[e] = sigs;
		}
	}	

	
}
