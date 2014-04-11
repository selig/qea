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

	public static IncrementalChecker make(QEntry[] lambda) {
		
		// zeroth place is always empty
		if(lambda.length==0 || lambda.length==1) return new EmptyChecker();
		
		Quantification q= lambda[1].quantification;
		
		for(int i=2;i<lambda.length;i++){
			if(q!=lambda[i].quantification) q=null;
		}
		
		if(q==FORALL) return new AllUniversalChecker(true);
		if(q==NOT_FORALL) return new AllUniversalChecker(false);
		if(q==EXISTS) return new AllExistentialChecker(true);
		if(q==NOT_EXISTS) return new AllExistentialChecker(false);

		throw new RuntimeException("Not implemented for general lambda "+Arrays.toString(lambda));
	}

	public abstract void newBinding(boolean started_final); 
	public abstract Verdict verdict(boolean at_end);
	// Assumption - binding is total
	public abstract void update(QBindingImpl binding, boolean is_final, boolean previous_final);

	
	public static class EmptyChecker extends IncrementalChecker{

		boolean currently_final = false;
		
		@Override
		public void newBinding(boolean started_final) { currently_final = started_final;}

		@Override
		public void update(QBindingImpl binding, boolean is_final,
				boolean previous_final) {
			currently_final = is_final;			
		}		
		
		@Override
		public Verdict verdict(boolean at_end) {
			if(!currently_final){
				if(at_end) return FAILURE;
				else return WEAK_FAILURE;
			}
			if(at_end) return SUCCESS;
			return WEAK_SUCCESS;
		}
		
	}
	
	public static class AllUniversalChecker extends IncrementalChecker{

		private int number_non_final = 0;
		private final boolean negated;
		
		AllUniversalChecker(boolean n){negated=n;}
		
		@Override
		public void newBinding(boolean started_final) {
			if(!started_final) number_non_final++;
		}		
		
		// We can ignore the binding
		@Override
		public void update(QBindingImpl binding, boolean is_final,boolean previous_final) {
			
			if(is_final && !previous_final) number_non_final--;
			else if(!is_final && previous_final) number_non_final++;			
		}

		@Override
		public Verdict verdict(boolean at_end) {
			boolean failing = (number_non_final > 0);
			Verdict result;
			if(failing){
				if(at_end) result=  FAILURE;
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
		
		AllExistentialChecker(boolean n){negated=n;}		
		
		@Override
		public void newBinding(boolean started_final) {
			if(started_final) number_final++;
		}		
		
		// We can ignore the binding
		@Override
		public void update(QBindingImpl binding, boolean is_final,boolean previous_final) {
			
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
			else if(at_end) result=  SUCCESS;
			else result= WEAK_SUCCESS;
			if(negated) result = result.negated();
			return result;
		}
		
	}	
	
}
