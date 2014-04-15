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
import structure.impl.qeas.Abstr_QVarN_QEA.QEntry;
import structure.intf.Guard;


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
		
		if(q==FORALL) 
			return new AllUniversalChecker(false,finalStates,strongStates,lambda[1].guard);
		if(q==NOT_FORALL) 
			return new AllUniversalChecker(true,finalStates,strongStates,lambda[1].guard);
		if(q==EXISTS) 
			return new AllExistentialChecker(false,finalStates,strongStates,lambda[1].guard);
		if(q==NOT_EXISTS) 
			return new AllExistentialChecker(true,finalStates,strongStates,lambda[1].guard);

		throw new RuntimeException("Not implemented for general lambda "+Arrays.toString(lambda));
	}

	public abstract Verdict verdict(boolean at_end);
	
	public abstract void newBinding(QBindingImpl binding, int start_state); 
	public abstract void newBinding(QBindingImpl binding, int[] start_states);
	
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
		public void newBinding(QBindingImpl binding, int state){ currently_final = finalStates[state];}
		public void newBinding(QBindingImpl binding, int[] states){
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
		private final Guard guard;
		
		AllUniversalChecker(boolean n, boolean[] finalStates, boolean[] strongStates,Guard g) {
			super(finalStates,strongStates);
			negated=n;
			guard=g;
		}
		
		@Override
		public void newBinding(QBindingImpl binding, int state) {
			if(guard==null || guard.check(binding))
				if(!finalStates[state]) number_non_final++;
		}	
		public void newBinding(QBindingImpl binding, int[] states){
			if(guard==null || guard.check(binding)){
				boolean is_final = false;
				for(int s : states){
					if(finalStates[s]){
						is_final=true;
						break;
					}				
				}
				if(!is_final) number_non_final++;
			}
		}		
		
		// We can ignore the binding
		@Override
		public void update(QBindingImpl binding,  int last, int next) {
			if(guard==null || guard.check(binding)){
				boolean is_final = finalStates[next];
				boolean previous_final = finalStates[last];
				if(is_final && !previous_final) number_non_final--;
				else if(!is_final && previous_final) number_non_final++;	
				
				if(strongStates[next] && !is_final) strong_reached=true;
			}
		}
		public void update(QBindingImpl binding, int[] lasts, int[] nexts){			
			if(guard==null || guard.check(binding)){
				boolean is_final=false;
				boolean all_strong_nf=true;
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
				for(int s : lasts){
					if(finalStates[s]){
						previous_final=true;
						break;
					}
				}
				if(is_final && !previous_final) number_non_final--;
				else if(!is_final && previous_final) number_non_final++;
				
				if(all_strong_nf) strong_reached=true;
			}
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
		private final Guard guard;
		
		AllExistentialChecker(boolean n, boolean[] finalStates, boolean[] strongStates,Guard g) {
			super(finalStates,strongStates);
			negated=n;
			guard=g;
		}		
		
		@Override
		public void newBinding(QBindingImpl binding, int state) {
			if(guard==null || guard.check(binding))
				if(finalStates[state]) number_final++;
		}		
		public void newBinding(QBindingImpl binding, int[] states){
			if(guard==null || guard.check(binding)){
				boolean is_final = false;
				for(int s : states){
					if(finalStates[s]){
						is_final=true;
						break;
					}
				}
				if(is_final) number_final++;
			}
		}		
		
		// We can ignore the binding
		@Override
		public void update(QBindingImpl binding, int last, int next) {
			if(guard==null || guard.check(binding)){
				boolean is_final = finalStates[next];
				boolean previous_final = finalStates[last];
				if(is_final && !previous_final) number_final++;
				else if(!is_final && previous_final) number_final--;	
				
				if(strongStates[next] && is_final) strong_reached=true;	
			}
		}
		public void update(QBindingImpl binding, int[] lasts, int[] nexts){
			if(guard==null || guard.check(binding)){
				boolean is_final=false;
				for(int s : nexts){
					if(finalStates[s]){
						is_final=true;
						if(strongStates[s]) strong_reached=true;
					}
				}
				boolean previous_final=false;
				for(int s : lasts){
					if(finalStates[s]){
						previous_final=true;
						break;
					}
				}
				if(is_final && !previous_final) number_final++;
				else if(!is_final && previous_final) number_final--;	
			}
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
	
	public static class OneAlternationChecker extends IncrementalChecker{

		private final boolean q1;
		private final boolean q2;
		private final boolean negated1;
		private final boolean negated2;
		private final Guard g1;
		private final Guard g2;
		
		public OneAlternationChecker(boolean[] finalStates,boolean[] strongStates,
				QEntry one, QEntry two) {
			super(finalStates, strongStates);
			switch(one.quantification){
			case FORALL : q1=true;negated1=false;break;
			case NOT_FORALL : q1=true;negated1=true;break;
			case EXISTS: q1=false;negated1=false;break;
			case NOT_EXISTS: q1=false;negated1=true;break;
			default : throw new RuntimeException("Unexpected quantification "+one.quantification);
			}
			switch(two.quantification){
			case FORALL : q2=true;negated2=false;break;
			case NOT_FORALL : q2=true;negated2=true;break;
			case EXISTS: q2=false;negated2=false;break;
			case NOT_EXISTS: q2=false;negated2=true;break;
			default : throw new RuntimeException("Unexpected quantification "+two.quantification);
			}			
			g1=one.guard;
			g2=two.guard;
		}

		@Override
		public Verdict verdict(boolean at_end) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void newBinding(QBindingImpl binding, int start_state) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void newBinding(QBindingImpl binding, int[] start_states) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void update(QBindingImpl binding, int last_state, int next_state) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void update(QBindingImpl binding, int[] last_states,
				int[] next_states) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
