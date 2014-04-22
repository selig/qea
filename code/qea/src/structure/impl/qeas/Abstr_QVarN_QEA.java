package structure.impl.qeas;

import java.util.Arrays;

import structure.impl.other.FBindingImpl;
import structure.impl.other.QBindingImpl;
import structure.impl.other.Quantification;
import structure.intf.Binding;
import structure.intf.Guard;
import structure.intf.QEA;
import util.ArrayUtil;

public abstract class Abstr_QVarN_QEA extends QEA {

	protected final int initialState;
	protected final QEntry[] lambda; // indexed from 1, use -qvar
	protected final boolean skipStates[];
	protected final boolean finalStates[];
	protected final boolean strongStates[];	

	protected final int freeVariableCount;
	protected final int quantifiedVariableCount;	
	
	/*
	 * The underlying structure is either Det or NonDet but the contents
	 * can either use free variables or not
	 * 
	 * Both Det and NonDet versions have free and non-free next config options
	 * Therefore, they can have one of two types
	 */
	private final QEAType type;
	public QEAType getQEAType(){ return type;}	
	
	/*
	 * If it is not normal we need to add sub-bindings for
	 * all bindings as *all* bindings must be generated, even
	 * if they are associated with the empty projection
	 */
	protected boolean normal;
	
	public static class QEntry{
		public QEntry(Quantification quantification, int variable, Guard guard) {
			this.quantification = quantification;
			this.variable = variable;
			this.guard = guard;
		}
		public final Quantification quantification;
		public final int variable;
		public final Guard guard;
		
		public String toString(){
			return quantification + " x_"+variable + ((guard!=null) ? guard : "");
		}
		
	}	
	
	public Abstr_QVarN_QEA(int numStates, int numEvents, int initialState,
			int qVariablesCount, int fVariablesCount,QEAType type) {
		this.initialState = initialState;
		lambda = new QEntry[qVariablesCount + 1];
		skipStates = new boolean[numStates + 1];
		finalStates = new boolean[numStates + 1];
		strongStates = new boolean[numStates + 1];
		freeVariableCount = fVariablesCount;
		quantifiedVariableCount = qVariablesCount;
		this.type=type;
	}	
	
	public boolean isNormal(){
		
		//System.err.println(Arrays.toString(lambda));
		if(lambda.length==1) normal=true;
		else{
			boolean innermost_universal = 
					(lambda[lambda.length-1].quantification.isUniversal());
			normal = (innermost_universal == isStateFinal(initialState));
		}
		return normal;
	}
	
	@Override
	public int getInitialState() {
		return initialState;
	}
	/*
	 * Adding stuff
	 */

	public void setStateAsSkip(int state) {
		skipStates[state] = true;
	}

	public void setStatesAsSkip(int... states) {
		for (int state : states) {
			skipStates[state] = true;
		}
	}		
	
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
	public boolean[] getFinalStates(){
		return finalStates;
	}	
	public int getFreeVars(){
		return freeVariableCount;
	}
	
	public boolean[] getStrongStates(){
		return strongStates;
	}
	
	@Override
	public boolean usesFreeVariables() {
		return true;
	}

	@Override
	public boolean isStateSkip(int state) {
		return skipStates[state];
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

	// setupMatching should remove repeated signatures
	protected int[][][] e_sigs;
	public abstract void setupMatching();

	public int[][][] getSigs() {
		return e_sigs;
	}	

	
	public QBindingImpl[] makeBindings(int eventName, Object[] args) {

		/*for(int e = 1; e<e_sigs.length;e++){
			System.out.println(e);
			for(int[] m : e_sigs[e])
				System.out.println(Arrays.toString(m));
		}
		System.out.println(eventName);
		System.exit(0);*/
		
		int[][] sigs = e_sigs[eventName];

		QBindingImpl[] bs = new QBindingImpl[sigs.length];

		if(sigs.length==1){
			int sig[] = sigs[0];
			QBindingImpl qbinding = newQBinding();
			for (int j = 0; j < sig.length; j++) {
				if (sig[j] < 0) {
					qbinding.setValue(sig[j], args[j]);
				}
			}
			bs[0] = qbinding;			
			return bs;			
		}
		// If there are multiple sigs we might need to close
		// There may be repetitions i.e. 
		// sigs = [[-1,1],[1,-1]] for args=[A,A]
		// These should be removed		
		
		int[] usedq = new int[quantifiedVariableCount+1];
		
		int use_next = 0;
		for (int i=0;i<sigs.length;i++) {
			int sig[] = sigs[i];
			QBindingImpl qbinding = newQBinding();
			for (int j = 0; j < sig.length; j++) {
				if (sig[j] < 0) {
					qbinding.setValue(sig[j], args[j]);
					usedq[-sig[j]]++;
				}
			}
			boolean repeated=false;
			for(int j=0;j<use_next;j++){
				Object val = bs[j];
				if(val!=null && val.equals(qbinding)) repeated=true;
			}
			if(!repeated){
				bs[use_next] = qbinding;
				use_next+=1;				
			}
		}
		bs = ArrayUtil.resize(bs, use_next);
		
		if(!normal){
			//if we're not normal then we need to add all submaps
			QBindingImpl first = bs[0];
			QBindingImpl [] subs = first.submaps();
			for(int i=1;i<bs.length;i++){
				QBindingImpl next = bs[i];
				subs = ArrayUtil.concat(subs,next.submaps());				
			}
			bs = ArrayUtil.concat(bs, subs);
		}
		
		//*** Now close ***
		//not necessary if all used same vars i.e. a qvar is either
		// not used are used in all bs
		boolean disjoint = true;
		for(int u : usedq) disjoint &= (u==0 || u==bs.length);
				
		
		if(disjoint) return bs;	
		
		// very expensive, but hopefully not common
		// TODO- there's definitely a better way of doing this
		// TODO - test
		// ^3 in number of bindings
		boolean changing = true;
		while(changing){
			QBindingImpl[] add = new QBindingImpl[0];
			changing=false;
			//consider pair-wise, do +1 extensions per step
			for(QBindingImpl b1 : bs){
				for(QBindingImpl b2 : bs){
					if(!b1.equals(b2)){
						for(int q=1;q<quantifiedVariableCount;q++){
							Object v1 = b1.getValue(-q);
							Object v2 = b2.getValue(-q);
							if(v1!=null || v2!=null){
								if(v1==null){									
									QBindingImpl newone = (QBindingImpl) b1.copy();
									newone.setValue(-q, v2);
									boolean repeated=false;
									for(QBindingImpl other : bs)
										if(other.equals(newone)) repeated=true;
									for(QBindingImpl other : add)
										if(other.equals(newone)) repeated=true;
									if(!repeated){
										changing=true;
										add=ArrayUtil.resize(add, add.length+1);
										add[add.length-1]=newone;
									}
								}
								else if(v2 == null){
									QBindingImpl newone = (QBindingImpl) b2.copy();
									newone.setValue(-q, v1);
									boolean repeated=false;
									for(QBindingImpl other : bs)
										if(other.equals(newone)) repeated=true;
									for(QBindingImpl other : add)
										if(other.equals(newone)) repeated=true;
									if(!repeated){
										changing=true;
										add=ArrayUtil.resize(add, add.length+1);
										add[add.length-1]=newone;
									}

								}
							}
						}
					}
				}
			}
			bs = ArrayUtil.concat(bs,add);
		}
		
		return bs;
	}
	
}
