package structure.impl.qeas;

import java.util.HashSet;
import java.util.Set;

import structure.impl.other.FBindingImpl;
import structure.impl.other.QBindingImpl;
import structure.impl.other.Quantification;
import structure.impl.other.Transition;
import structure.intf.Binding;
import structure.intf.Guard;
import structure.intf.QEA;
import util.ArrayUtil;

public abstract class Abstr_QVarN_FVar_QEA extends QEA {

	protected final int initialState;
	protected final QEntry[] lambda; // indexed from 1, use -qvar
	protected final boolean finalStates[];
	protected final boolean strongStates[];	

	protected final int freeVariableCount;
	protected final int quantifiedVariableCount;	
	
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
	
	public Abstr_QVarN_FVar_QEA(int numStates, int numEvents, int initialState,
			int qVariablesCount, int fVariablesCount) {
		this.initialState = initialState;
		lambda = new QEntry[qVariablesCount + 1];
		finalStates = new boolean[numStates + 1];
		strongStates = new boolean[numStates + 1];
		freeVariableCount = fVariablesCount;
		quantifiedVariableCount = qVariablesCount;
	}	
	
	@Override
	public int getInitialState() {
		return initialState;
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

	// guard can be null
	public void addQuantification(int var, Quantification quantification,
			Guard guard) {
		assert (var < 0 && (-var) <= lambda.length);
		lambda[-var] = new QEntry(quantification, var, guard);
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
	public int[] getLambda() {
		int[] l = new int[lambda.length];
		for (int i = 0; i < l.length; i++) {
			if (lambda[i] != null) {
				l[i] = lambda[i].variable;
			}
		}

		return l;
	}

	public QEntry[] getFullLambda(){
		return lambda;
	}
	
	@Override
	public boolean usesFreeVariables() {
		return true;
	}

	@Override
	public boolean isStateFinal(int state) {
		return finalStates[state];
	}
	@Override
	public boolean isStateStrong(int state) {
		return strongStates[state];
	}
	
	public Binding newFBinding() {
		return new FBindingImpl(freeVariableCount);
	}

	public QBindingImpl newQBinding() {
		return new QBindingImpl(quantifiedVariableCount);
	}	

	/*
	 * Matching
	 */

	protected int[][][] e_sigs;
	public abstract void setupMatching();


	
	public QBindingImpl[] makeBindings(int eventName, Object[] args) {

		int[][] sigs = e_sigs[eventName];

		QBindingImpl[] bs = new QBindingImpl[sigs.length];

		for (int i=0;i<sigs.length;i++) {
			int sig[] = sigs[i];
			QBindingImpl qbinding = newQBinding();
			for (int j = 0; j < sig.length; j++) {
				if (sig[j] < 0) {
					qbinding.setValue(sig[j], args[j]);
				}
			}
			bs[i] = qbinding;
		}

		if(sigs.length>1){
			//TODO need to close bindings
			//TODO remove repetitions
			// Idea - track usage of qvars when building bindings to see if we need to do this			
		}
		
		return bs;
	}
	
}
