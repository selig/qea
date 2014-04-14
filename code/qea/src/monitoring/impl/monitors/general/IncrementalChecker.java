package monitoring.impl.monitors.general;

import static structure.impl.other.Quantification.EXISTS;
import static structure.impl.other.Quantification.FORALL;
import static structure.impl.other.Quantification.NOT_EXISTS;
import static structure.impl.other.Quantification.NOT_FORALL;
import static structure.impl.other.Verdict.FAILURE;
import static structure.impl.other.Verdict.SUCCESS;
import static structure.impl.other.Verdict.WEAK_FAILURE;
import static structure.impl.other.Verdict.WEAK_SUCCESS;

import java.util.Arrays;

import structure.impl.other.QBindingImpl;
import structure.impl.other.Quantification;
import structure.impl.other.Verdict;
import structure.impl.qeas.Abstr_QVarN_FVar_QEA.QEntry;


public abstract class IncrementalChecker {

	protected final boolean [] finalStates;	
	protected final boolean[] strongStates;
	
	
	public IncrementalChecker(boolean[] finalStates,boolean[] strongStates) {
		this.finalStates=finalStates;
		this.strongStates=strongStates;
	}

	public static IncrementalChecker make(QEntry[] lambda, 
			boolean[] finalStates,boolean[] strongStates) {
				
		// zeroth place is always empty
		if(lambda.length==0 || lambda.length==1) return new EmptyChecker(finalStates,strongStates);
		
		Quantification q= lambda[1].quantification;
		
		for(int i=2;i<lambda.length;i++){
			if(q!=lambda[i].quantification) q=null;
		}
		
		if(q==FORALL) return new AllUniversalChecker(false,finalStates,strongStates);
		if(q==NOT_FORALL) return new AllUniversalChecker(true,finalStates,strongStates);
		if(q==EXISTS) return new AllExistentialChecker(false,finalStates,strongStates);
		if(q==NOT_EXISTS) return new AllExistentialChecker(true,finalStates,strongStates);

		throw new RuntimeException("Not implemented for general lambda "+Arrays.toString(lambda));
	}

	public abstract Verdict verdict(boolean at_end);
	
	public abstract void newBinding(int start_state); 
	public abstract void newBinding(int[] start_states);
	
	// Assumption - binding is total
	public abstract void update(QBindingImpl binding, int last_state, int next_state);
	public abstract void update(QBindingImpl binding, int[] last_states, int[] next_states);
	
	public static class EmptyChecker extends IncrementalChecker{

		boolean currently_final = false;
		boolean currently_strong = false;
		
		public EmptyChecker(boolean[] finalStates, boolean[] strongStates) {
			super(finalStates,strongStates);
		}

		@Override
		public void newBinding(int state){ currently_final = finalStates[state];}
		public void newBinding(int[] states){
			currently_final=false;
			for(int s : states){
				if(finalStates[s]){
					currently_final=true;
					break;
				}
			}
		}

		@Override
		public void update(QBindingImpl binding,  int last, int next) {
			currently_final = finalStates[next];
			currently_strong = strongStates[next];
		}	
		public void update(QBindingImpl b, int[] lasts, int[] nexts){
			
				boolean all_strong_nf = true;
				currently_final=false;
				for(int s : nexts){
					if(finalStates[s]){
						currently_final=true;
						all_strong_nf=false;
						if(strongStates[s]) currently_strong = true;
					}
					else{
						all_strong_nf &= strongStates[s];
					}
				}
				if(all_strong_nf) currently_strong=true;
		}
		
		@Override
		public Verdict verdict(boolean at_end) {
			if(!currently_final){
				if(at_end || currently_strong) return FAILURE;
				else return WEAK_FAILURE;
			}
			if(at_end || currently_strong) return SUCCESS;
			return WEAK_SUCCESS;
		}
		
	}
	
	public static class AllUniversalChecker extends IncrementalChecker{

		private int number_non_final = 0;
		private final boolean negated;
		private boolean strong_reached;
		
		AllUniversalChecker(boolean n, boolean[] finalStates, boolean[] strongStates) {
			super(finalStates,strongStates);negated=n;}
		
		@Override
		public void newBinding(int state) {
			if(!finalStates[state]) number_non_final++;
		}	
		public void newBinding(int[] states){
			boolean is_final = false;
			for(int s : states){
				if(finalStates[s]){
					is_final=true;
					break;
				}				
			}
			if(!is_final) number_non_final++;
		}		
		
		// We can ignore the binding
		@Override
		public void update(QBindingImpl binding,  int last, int next) {
			boolean is_final = finalStates[next];
			boolean previous_final = finalStates[last];
			if(is_final && !previous_final) number_non_final--;
			else if(!is_final && previous_final) number_non_final++;	
			
			if(strongStates[next] && !is_final) strong_reached=true;
		}
		public void update(QBindingImpl b, int[] lasts, int[] nexts){
			
			boolean all_strong_nf = true;
			boolean is_final=false;
			for(int s : nexts){
				if(finalStates[s]){
					is_final=true;
					all_strong_nf=false;
				}
				else{
					all_strong_nf &= strongStates[s];
				}
			}
			boolean previous_final=false;
			for(int s : nexts){
				if(finalStates[s]){
					previous_final=true;
					break;
				}
			}
			if(is_final && !previous_final) number_non_final--;
			else if(!is_final && previous_final) number_non_final++;			
			
			if(all_strong_nf) strong_reached=true;
	}		

		@Override
		public Verdict verdict(boolean at_end) {
			boolean failing = (number_non_final > 0);
			Verdict result;
			if(failing){
				if(at_end || strong_reached) result=  FAILURE;
				else result=  WEAK_FAILURE;
			}
			else if(at_end) result=  SUCCESS;
			else result= WEAK_SUCCESS;
			if(negated) result = result.negated();
			return result;
		}
		
	}
	
	public static class AllExistentialChecker extends IncrementalChecker{

		private int number_final = 0;		
		private final boolean negated;
		private boolean strong_reached = false;
		
		AllExistentialChecker(boolean n, boolean[] finalStates, boolean[] strongStates) {
			super(finalStates,strongStates);negated=n;}		
		
		@Override
		public void newBinding(int state) {
			if(finalStates[state]) number_final++;
		}		
		public void newBinding(int[] states){
			boolean is_final = false;
			for(int s : states){
				if(finalStates[s]){
					is_final=true;
					break;
				}
			}
			if(is_final) number_final++;
		}		
		
		// We can ignore the binding
		@Override
		public void update(QBindingImpl binding, int last, int next) {
			boolean is_final = finalStates[next];
			boolean previous_final = finalStates[last];
			if(is_final && !previous_final) number_final++;
			else if(!is_final && previous_final) number_final--;	
			
			if(strongStates[next] && is_final) strong_reached=true;			
		}
		public void update(QBindingImpl b, int[] lasts, int[] nexts){
			
			boolean is_final=false;
			for(int s : nexts){
				if(finalStates[s]){
					is_final=true;
					strong_reached=true;
				}
			}
			boolean previous_final=false;
			for(int s : nexts){
				if(finalStates[s]){
					previous_final=true;
					break;
				}
			}
			if(is_final && !previous_final) number_final++;
			else if(!is_final && previous_final) number_final--;


	}				

		@Override
		public Verdict verdict(boolean at_end) {
			boolean failing = (number_final == 0);
			Verdict result;
			if(failing){
				if(at_end) result=  FAILURE;
				else result=  WEAK_FAILURE;
			}
			else if(at_end || strong_reached) result=  SUCCESS;
			else result= WEAK_SUCCESS;
			if(negated) result = result.negated();
			return result;
		}
		
	}	
	
}
