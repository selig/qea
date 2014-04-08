package structure.impl.qeas;

import java.util.HashSet;
import java.util.Set;

import monitoring.impl.configs.DetConfig;
import structure.impl.other.FBindingImpl;
import structure.impl.other.FullBindingImpl;
import structure.impl.other.QBindingImpl;
import structure.impl.other.Quantification;
import structure.impl.other.Transition;
import structure.intf.Assignment;
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
public class QVarN_FVar_Det_QEA extends QEA {

	public QEAType getQEAType(){ return QEAType.QVARN_FVAR_DET_QEA;}
	
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
		
		public String toString(){ return quantification+" "+variable;}
	}
	
	private final int initialState;
	public final QEntry[] lambda; // indexed from 1, use -qvar
	private final Transition[][] delta;
	private final boolean finalStates[];
	private final boolean strongStates[];
	
	private final int freeVariableCount;
	private final int quantifiedVariableCount;
	
	public QVarN_FVar_Det_QEA(int numStates, int numEvents,
			int initialState, int qVariablesCount, int fVariablesCount) {
		this.initialState = initialState;
		this.lambda = new QEntry[qVariablesCount+1];
		this.delta = new Transition[numStates+1][numEvents+1];
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
	public void addTransition(int startState, int event, Transition transition) {
		delta[startState][event] = transition;
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

        public Transition[][] getTransitions(){
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

                //Remember we're deterministic - this means that we can update the DetConfig

                int start_state = config.getState();
                Binding binding = config.getBinding();
                Transition transition = delta[start_state][eventName];

                if(transition==null){
                	// no transition - fail
                	config.setState(0);
                	return;
                }
                
                // if we bind new qvariables or clash with bindings in qbinding
                // then we return without making an update, as we are not relevant
                Binding extend_with = qbinding.extend(transition.getVariableNames(),args);
                if(extend_with == null || !qbinding.equals(extend_with)){
                	//not relevant, just return 
                	System.err.println(extend_with);
                	return;
                }
                
                // update binding based on args
                binding.update(transition.getVariableNames(),args);                
                
                // check guard
                Guard guard = transition.getGuard();
                Binding extended_guard = binding;
                if(guard !=null && guard.usesQvars()) extended_guard = new FullBindingImpl(binding,qbinding);
                if(guard==null || guard.check(extended_guard)){
                     config.setState(transition.getEndState());
                     Assignment assignment = transition.getAssignment();
                    // in det case we just update the binding, no copy
                     if(assignment!=null) assignment.apply(binding,false); 
                }
                //if we cannot go to state 0
                else config.setState(0);

	}


	@Override
	public boolean isStateStrong(int state) {
		// TODO Auto-generated method stub
		return false;
	}

	public Binding newFBinding() {
		return new FBindingImpl(freeVariableCount);
	}
	
	public QBindingImpl newQBinding() {
		return new QBindingImpl(quantifiedVariableCount);
	}


	public void setupMatching(){
		e_sigs = new int[finalStates.length][][];
		for(int e=1;e<delta[0].length;e++){
			int[][] sigs = new int[0][];

			for(int s = 1;s<delta.length;s++){
				Transition t = delta[s][e];
				if(t!=null){
					sigs = ArrayUtil.increaseSize(sigs);
					sigs[sigs.length-1] = t.getVariableNames();
				}
			}			
			e_sigs[e] = sigs;
		}
	}
	
	int[][][] e_sigs;
	
	// use a set to remove redundancies - a linear removal approach might be better
	// given the size of the set
	//TODO check
	public Set<QBindingImpl> makeBindings(int eventName, Object[] args) {
		
		int[][] sigs = e_sigs[eventName];
		
		Set<QBindingImpl> bs = new HashSet<QBindingImpl>();
		
		for(int i=0;i<sigs.length;i++){
			int[] sig = sigs[i];
			QBindingImpl qbinding = newQBinding();
			for(int j=0;j<sig.length;j++){
				if(sig[j]<0){
					qbinding.setValue(sig[j], args[j]);
				}
			}
			bs.add(qbinding);
		}
		
		//TODO need to close bindings
		
		return bs;
	}


	public int getInitialState() {
		return initialState;
	}
	
}
