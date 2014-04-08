package structure.impl.qeas;

import monitoring.impl.configs.DetConfig;
import structure.impl.other.QBindingImpl;
import structure.impl.other.Quantification;
import structure.impl.other.Transition;
import structure.impl.qeas.QVarN_FVar_Det_QEA.QEntry;
import structure.intf.Binding;
import structure.intf.Guard;
import structure.intf.QEA;
import util.ArrayUtil;

/**
 * This class represents the most general deterministic Quantified Event Automaton (QEA)
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class QVarN_FVar_NonDet_QEA extends QEA {

	public QEAType getQEAType(){ return QEAType.QVARN_FVAR_NONDET_QEA;}	
	
	/*
	 * Contents
	 */
	public static class QEntry{
		public QEntry(Quantification quantification, int variable, Guard guard) {
			this.quantification = quantification;
			this.variable = variable;
			this.guard = guard;
		}
		public final Quantification quantification;
		public final int variable;
		public final Guard guard;
	}
	
	private final int initialState;
	public final QEntry[] lambda; // indexed from 1, use -qvar
	private final Transition[][][] delta;
	private final boolean finalStates[];
	private final boolean strongStates[];	
	
	private final int freeVariableCount;
	private final int quantifiedVariableCount;
	
	public QVarN_FVar_NonDet_QEA(int numStates, int numEvents,
			int initialState, int qVariablesCount, int fVariablesCount) {
		this.initialState = initialState;
		this.lambda = new QEntry[qVariablesCount+1];
		this.delta = new Transition[numStates+1][numEvents+1][];
		this.finalStates = new boolean[numStates+1];
		this.strongStates = new boolean[numStates+1];
		this.freeVariableCount = fVariablesCount;
		this.quantifiedVariableCount = qVariablesCount;
	}

	
	/*
	 * Adding stuff
	 */
	
	public void setStatesAsFinal(int... states) {
		for (int state : states) {
			finalStates[state] = true;
		}
	}
	public void setStatesAsStrong(int... states) {
		for (int state : states) {
			strongStates[state] = true;
		}
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
	// guard can be null
	public void addQuantification(int var,Quantification quantification,Guard guard){
		assert(var<0 && (-var)<=lambda.length);
		lambda[-var] = new QEntry(quantification,var,guard);
	}
	
	
	
	
	/*
	 * Boring helper methods
	 */
	@Override
	public int[] getStates() {
		int[] q = new int[finalStates.length - 1];
		for (int i = 0; i < q.length; i++) {
			q[i] = i + 1;
		}
		return q;
	}

	@Override
	public int[] getEventsAlphabet() {
		int[] a = new int[delta[0].length - 1];
		for (int i = 0; i < a.length; i++) {
			a[i] = i + 1;
		}
		return a;
	}

	@Override
	public int[] getLambda() {
		int[] l = new int[lambda.length];
		for(int i=0;i<l.length;i++)
			l[i]=lambda[i].variable;
		return l;
	}

    public Transition[][][] getTransitions(){
               return delta;
    }

	@Override
	public boolean isDeterministic() {
		return true;
	}

	@Override
	public boolean usesFreeVariables() {
		return true;
	}

	@Override
	public boolean isStateFinal(int state) {
		return finalStates[state];
	}


	public void getNextConfig(QBindingImpl qbinding, DetConfig config,
			int eventName, Object[] args) {

                //Remember we're determinstic - this means that we can update the DetConfig instead
                // of creating a new one
                // TODO - check that other Det approaches do this!

                int start_state = config.getState();
                Binding binding = config.getBinding();
                Transition[] transitions = delta[start_state][eventName];

                // update binding based on args

                throw new RuntimeException("not implemented");

	}


	@Override
	public boolean isStateStrong(int state) {
		// TODO Auto-generated method stub
		return false;
	}


	
}
