package qea.monitoring.impl.monitors.general;

import static qea.structure.impl.other.Quantification.*;
import static qea.structure.impl.other.Verdict.FAILURE;
import static qea.structure.impl.other.Verdict.SUCCESS;
import static qea.structure.impl.other.Verdict.WEAK_FAILURE;
import static qea.structure.impl.other.Verdict.WEAK_SUCCESS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import qea.structure.impl.other.QBindingImpl;
import qea.structure.impl.other.Quantification;
import qea.structure.impl.other.Verdict;
import qea.structure.impl.qeas.Abstr_QVarN_QEA.QEntry;
import qea.structure.intf.Guard;
import qea.util.OurWeakHashMap;

public abstract class IncrementalChecker {

	boolean DEBUG = false;
	
	protected final boolean[] finalStates;
	protected final boolean[] strongStates;

	/*
	 * Should probably make it weak!
	 */
	protected Set<QBindingImpl> strong_bindings = Collections
			.newSetFromMap(new OurWeakHashMap<QBindingImpl, Boolean>());

	public Set<QBindingImpl> getStrongBindings() {
		return strong_bindings;
	}

	// Used to update counts
	public abstract void removeStrong(QBindingImpl binding);

	public IncrementalChecker(boolean[] finalStates, boolean[] strongStates) {
		this.finalStates = finalStates;
		this.strongStates = strongStates;
	}

	// Returns true if state could effect the outcome i.e.
	// if quantification is all universal then a state is active
	// iff it is non-final
	public abstract boolean isActive(int state);
	
	public boolean isActive(int[] states){
		for(int i=0;i<states.length;i++)
			if(!isActive(states[i])) return false;
		return true;
	}

	public static IncrementalChecker make(QEntry[] lambda, boolean negated,
			boolean[] finalStates, boolean[] strongStates) {

		// zeroth place is always empty
		if (lambda.length == 0 || lambda.length == 1) {
			return new EmptyChecker(negated,finalStates, strongStates);
		}

		Quantification q = lambda[1].quantification;
		Guard all_guards = lambda[1].guard;

		for (int i = 2; i < lambda.length; i++) {
			if (q != lambda[i].quantification) {
				q = null;
			}
			if (lambda[i].guard != null) {
				if (all_guards == null) {
					all_guards = lambda[i].guard;
				} else {
					all_guards = Guard.and(all_guards, lambda[i].guard);
				}
			}
		}

		if (q == FORALL) {
			return new AllUniversalChecker(negated,finalStates, strongStates,
					all_guards);
		}
		if (q == EXISTS) {
			return new AllExistentialChecker(negated,finalStates, strongStates,
					all_guards);
		}

		if (lambda.length == 3) {
			return new OneAlternationChecker(finalStates, strongStates,
					lambda[1], lambda[2],negated);
		}

		//System.out.println(Arrays.toString(lambda));
		
		return new GeneralChecker(negated,lambda,finalStates,strongStates);
		
		//throw new RuntimeException("Not implemented for general lambda "
		//		+ Arrays.toString(lambda));
	}

	public abstract Verdict verdict(boolean at_end);

	public abstract boolean relevantBinding(QBindingImpl binding);
	
	public abstract void newBinding(QBindingImpl binding, int start_state);

	public abstract void newBinding(QBindingImpl binding, int[] start_states);

	// Assumption - binding is total
	public abstract void update(QBindingImpl binding, int last_state,
			int next_state);

	public abstract void update(QBindingImpl binding, int[] last_states,
			int[] next_states);

	public static class EmptyChecker extends IncrementalChecker {

		boolean currently_final = false;
		boolean currently_strong = false;
		private final boolean negated;

		public EmptyChecker(boolean negated, boolean[] finalStates, boolean[] strongStates) {
			super(finalStates, strongStates);
			this.negated=negated;
		}

		@Override
		public void newBinding(QBindingImpl binding, int state) {
			currently_final = finalStates[state];
		}

		@Override
		public void newBinding(QBindingImpl binding, int[] states) {
			currently_final = false;
			for (int s : states) {
				if (finalStates[s]) {
					currently_final = true;
					break;
				}
			}
		}

		@Override
		public void update(QBindingImpl binding, int last, int next) {
			currently_final = finalStates[next];
			currently_strong = strongStates[next];
		}

		@Override
		public void update(QBindingImpl b, int[] lasts, int[] nexts) {

			boolean all_strong_nf = true;
			currently_final = false;
			for (int s : nexts) {
				if (finalStates[s]) {
					currently_final = true;
					all_strong_nf = false;
					if (strongStates[s]) {
						currently_strong = true;
					}
				} else {
					all_strong_nf &= strongStates[s];
				}
			}
			if (all_strong_nf) {
				currently_strong = true;
			}
		}

		@Override
		public Verdict verdict(boolean at_end) {
			Verdict v;
			if (!currently_final) {
				if (at_end || currently_strong) {
					v= FAILURE;
				} else {
					v= WEAK_FAILURE;
				}
			}
			else{ 
				if (at_end || currently_strong) {
					v= SUCCESS;
				}
				else{
					v= WEAK_SUCCESS;
				}
			}
			
			if(negated) return v.negated();
			else return v;
		}

		@Override
		public void removeStrong(QBindingImpl binding) {
			// Does nothing as cannot have a strong binding
		}

		@Override
		public boolean isActive(int state) {
			return false;
		}

		@Override
		public boolean relevantBinding(QBindingImpl binding) {
			return false;
		}

	}

	public static class AllUniversalChecker extends IncrementalChecker {

		private int number_non_final = 0;
		private final boolean negated;
		private boolean strong_reached;
		private final Guard guard;

		AllUniversalChecker(boolean n, boolean[] finalStates,
				boolean[] strongStates, Guard g) {
			super(finalStates, strongStates);
			negated = n;
			guard = g;
		}

		@Override
		public void newBinding(QBindingImpl binding, int state) {
			if (guard == null || guard.check(binding)) {
				if (!finalStates[state]) {
					number_non_final++;
				}
			}
		}

		@Override
		public void newBinding(QBindingImpl binding, int[] states) {
			if (guard == null || guard.check(binding)) {
				boolean is_final = false;
				for (int s : states) {
					if (finalStates[s]) {
						is_final = true;
						break;
					}
				}
				if (!is_final) {
					number_non_final++;
				}
			}
		}

		// We can ignore the binding
		@Override
		public void update(QBindingImpl binding, int last, int next) {
			if (guard == null || guard.check(binding)) {
				boolean is_final = finalStates[next];
				boolean previous_final = finalStates[last];
				if (is_final && !previous_final) {
					number_non_final--;
				} else if (!is_final && previous_final) {
					number_non_final++;
				}

				if (strongStates[next] && !is_final) {
					strong_reached = true;
					strong_bindings.add(binding);
				}
			}
		}

		@Override
		public void update(QBindingImpl binding, int[] lasts, int[] nexts) {
			if (guard == null || guard.check(binding)) {
				boolean is_final = false;
				boolean all_strong_nf = true;
				for (int s : nexts) {
					if (finalStates[s]) {
						is_final = true;
						all_strong_nf = false;
					} else {
						all_strong_nf &= strongStates[s];
					}
				}
				boolean previous_final = false;
				for (int s : lasts) {
					if (finalStates[s]) {
						previous_final = true;
						break;
					}
				}
				if (is_final && !previous_final) {
					number_non_final--;
				} else if (!is_final && previous_final) {
					number_non_final++;
				}

				if (all_strong_nf) {
					strong_reached = true;
					strong_bindings.add(binding);
				}
			}
		}

		@Override
		public Verdict verdict(boolean at_end) {
			boolean failing = number_non_final > 0;
			Verdict result;
			if (failing) {
				if (at_end || strong_reached) {
					result = FAILURE;
				} else {
					result = WEAK_FAILURE;
				}
			} else if (at_end) {
				result = SUCCESS;
			} else {
				result = WEAK_SUCCESS;
			}
			if (negated) {
				result = result.negated();
			}
			return result;
		}

		@Override
		public void removeStrong(QBindingImpl binding) {
			strong_reached = false; // Assumption - all strongs will
									// be being removed!
			number_non_final--; // binding must have been non-final
								// to be strong

		}

		@Override
		public boolean isActive(int state) {
			return !finalStates[state];
		}

		@Override
		public boolean relevantBinding(QBindingImpl binding) {
			return guard==null || guard.check(binding);
		}

	}

	public static class AllExistentialChecker extends IncrementalChecker {

		private int number_final = 0;
		private final boolean negated;
		private boolean strong_reached = false;
		private final Guard guard;

		AllExistentialChecker(boolean n, boolean[] finalStates,
				boolean[] strongStates, Guard g) {
			super(finalStates, strongStates);
			negated = n;
			guard = g;
		}

		@Override
		public void newBinding(QBindingImpl binding, int state) {
			if (guard == null || guard.check(binding)) {
				if (finalStates[state]) {
					number_final++;
				}
			}
		}

		@Override
		public void newBinding(QBindingImpl binding, int[] states) {
			if (guard == null || guard.check(binding)) {
				boolean is_final = false;
				for (int s : states) {
					if (finalStates[s]) {
						is_final = true;
						break;
					}
				}
				if (is_final) {
					number_final++;
				}
			}
		}

		// We can ignore the binding
		@Override
		public void update(QBindingImpl binding, int last, int next) {
			if (guard == null || guard.check(binding)) {
				boolean is_final = finalStates[next];
				boolean previous_final = finalStates[last];
				if (is_final && !previous_final) {
					number_final++;
				} else if (!is_final && previous_final) {
					number_final--;
				}

				if (strongStates[next] && is_final) {
					strong_reached = true;
					strong_bindings.add(binding);
				}
			}
		}

		@Override
		public void update(QBindingImpl binding, int[] lasts, int[] nexts) {
			if (guard == null || guard.check(binding)) {
				boolean is_final = false;
				for (int s : nexts) {
					if (finalStates[s]) {
						is_final = true;
						if (strongStates[s]) {
							strong_reached = true;
							strong_bindings.add(binding);
						}
					}
				}
				boolean previous_final = false;
				for (int s : lasts) {
					if (finalStates[s]) {
						previous_final = true;
						break;
					}
				}
				if (is_final && !previous_final) {
					number_final++;
				} else if (!is_final && previous_final) {
					number_final--;
				}
			}
		}

		@Override
		public Verdict verdict(boolean at_end) {
			boolean failing = number_final == 0;
			Verdict result;
			if (failing) {
				if (at_end) {
					result = FAILURE;
				} else {
					result = WEAK_FAILURE;
				}
			} else if (at_end || strong_reached) {
				result = SUCCESS;
			} else {
				result = WEAK_SUCCESS;
			}
			if (negated) {
				result = result.negated();
			}
			return result;
		}

		@Override
		public void removeStrong(QBindingImpl binding) {
			strong_reached = false; // Assumption - all strongs will
									// be being removed!
			number_final--; // binding must have been final
							// to be strong

		}

		@Override
		public boolean isActive(int state) {
			return finalStates[state];
		}

		@Override
		public boolean relevantBinding(QBindingImpl binding) {
			return guard==null || guard.check(binding);
		}		
		
	}

	public static class OneAlternationChecker extends IncrementalChecker {
		
		// qx is true if qx is universal
		private final boolean q1;
		private final boolean q2;
		private final boolean negated;
		private final Guard g1;
		private final Guard g2;

		public OneAlternationChecker(boolean[] finalStates,
				boolean[] strongStates, QEntry one, QEntry two, boolean negated) {
			super(finalStates, strongStates);
			this.negated=negated;
			switch (one.quantification) {
			case FORALL:
				q1 = true;
				break;
			case EXISTS:
				q1 = false;
				break;
			default:
				throw new RuntimeException("Unexpected quantification "
						+ one.quantification);
			}
			switch (two.quantification) {
			case FORALL:
				q2 = true;
				break;
			case EXISTS:
				q2 = false;
				break;
			default:
				throw new RuntimeException("Unexpected quantification "
						+ two.quantification);
			}
			g1 = one.guard;
			g2 = two.guard;

		}

		// TODO - Important, when we turn garbage on these need to
		// be weak
		// maps a value for q1 to the number of
		// final values for q2 if q2 is existential
		// non-final values for q2 is universal

		// Should probably make it weak
		// TODO - what are the implications if the removed binding
		// is in a failure state
		private Map<Object, Integer> q2_map = new OurWeakHashMap<Object, Integer>();

		// count is of non_final if universal
		// and of final if existential
		private int no_q1_count = 0;

		/*
		 * based on no_q1_final, which should always be updated based on q2_map
		 * 
		 * No strong verdicts as we have alternation
		 */
		@Override
		public Verdict verdict(boolean at_end) {
			Verdict result = null;

			boolean failing = q1 ? no_q1_count > 0 : no_q1_count == 0;
			if (failing) {
				if (at_end) {
					result = FAILURE;
				} else {
					result = WEAK_FAILURE;
				}
			} else if (at_end) {
				result = SUCCESS;
			} else {
				result = WEAK_SUCCESS;
			}

			if (negated) {
				return result.negated();
			}
			return result;
		}

		private boolean checkGuards(QBindingImpl binding) {
			if (g1 != null && !g1.check(binding)) {
				return false;
			}
			if (g2 != null && !g2.check(binding)) {
				return false;
			}
			return true;
		}

		@Override
		public void newBinding(QBindingImpl binding, int state) {
			if (checkGuards(binding)) {
				Object one = binding.getValue(-1);
				// Object two = binding.getValue(-2); Not needed?!
				boolean isfinal = finalStates[state];
				int add = q2 ? isfinal ? 0 : 1 : isfinal ? 1 : 0;
				if(DEBUG) System.err.println("Add "+binding+" with "+state);
				process(one, add);
			}
			else if(DEBUG) System.err.println("Not added "+binding+" as guard false");
		}

		@Override
		public void newBinding(QBindingImpl binding, int[] states) {
			if (checkGuards(binding)) {
				Object one = binding.getValue(-1);
				boolean isfinal = false;
				for (int s : states) {
					if (finalStates[s]) {
						isfinal = true;
						break;
					}
				}

				int add = q2 ? isfinal ? 0 : 1 : isfinal ? 1 : 0;
				process(one, add);
			}
		}

		@Override
		public void update(QBindingImpl binding, int last_state, int next_state) {
			if (checkGuards(binding)) {
				Object one = binding.getValue(-1);
				//Object two = binding.getValue(-2);

				boolean last_final = finalStates[last_state];
				boolean next_final = finalStates[next_state];

				int inc_final = last_final && !next_final ? -1 : !last_final
						&& next_final ? 1 : 0;

				int add = q2 ? -inc_final : inc_final;
				if(DEBUG) System.err.println("Update "+binding+" for "+last_state+" -> "+next_state);
				process(one, add);
			}
		}

		@Override
		public void update(QBindingImpl binding, int[] last_states,
				int[] next_states) {
			if (checkGuards(binding)) {
				Object one = binding.getValue(-1);
				//Object two = binding.getValue(-2);

				boolean last_final = false;
				boolean next_final = false;
				for (int s : last_states) {
					if (finalStates[s]) {
						last_final = true;
						break;
					}
				}
				for (int s : next_states) {
					if (finalStates[s]) {
						next_final = true;
						break;
					}
				}

				int inc_final = last_final && !next_final ? -1 : !last_final
						&& next_final ? 1 : 0;
				
				int add = q2 ? -inc_final : inc_final;
				process(one, add);
			}
		}

		private void process(Object one, int add) {

			Integer last = q2_map.get(one);
			if (last == null) {
				last = 0;
				// this is the first one so we need to
				// update no_q1_count
				// in all cases letting last=0
				// means that we assume that one has been counted
				no_q1_count++;

			}
			int next = last + add;

			// 0 if value for one not changed
			// -1 if it has gone from true to false
			// 1 if it has gone from false to true
			int direction = 0;
			if (q2) {
				if (last > 0 && next == 0) {
					direction = 1;
				} else if (last == 0 && next > 0) {
					direction = -1;
				}
			} else {
				if (last > 0 && next == 0) {
					direction = -1;
				} else if (last == 0 && next > 0) {
					direction = 1;
				}
			}
			// based on q1 we update no_q1_count
			if (q1) {
				no_q1_count -= direction;
			} else {
				no_q1_count += direction;
			}

			if(DEBUG) System.err.println("Add "+one+" as "+next+" add was "+add);
			q2_map.put(one, next);
		}

		@Override
		public String toString() {
			String res = "q1count\t" + no_q1_count + "\ncheck_map\n";
			for (Map.Entry<Object, Integer> entry : q2_map.entrySet()) {
				res += entry.getKey() + "\t" + entry.getValue() + "\n";
			}
			return res;
		}

		@Override
		public void removeStrong(QBindingImpl binding) {
			// Does nothing as we cannot have strong bindings

		}

		@Override
		public boolean isActive(int state) {
			// Sadly, every state could effect the outcome
			return true;
		}

		@Override
		public boolean relevantBinding(QBindingImpl binding) {
			if(g1==null && g2==null) return true;
			throw new RuntimeException("not implemented");
		}

	}

	private static class Record{
		
		public void setQuantification(Quantification q){
			this.this_quantification=q;
		}
		
		public Quantification this_quantification; 
		public final Record parent;
		
		private ArrayList<Object> bound_so_far = new ArrayList<Object>();
		public int index(){
			return bound_so_far.size(); 
		}
		
		public Record(Record parent){
			this.parent=parent;
		}
		
		public void add(Object o){
			if(o==null) throw new RuntimeException("Object in total binding should not be null");
			//System.err.println("add: "+o);
			bound_so_far.add(o);
		}
		@Override
		public boolean equals(Object other){
			if(other instanceof Record){
				return ((Record) other).bound_so_far.equals(bound_so_far);
			}
			return false;
		}
		@Override
		public int hashCode(){ return bound_so_far.hashCode(); }
		
		@Override
		public String toString(){ 
			return bound_so_far+" with "+this_quantification;
		}
		
	}
	
	public static class GeneralChecker extends IncrementalChecker {

		private final boolean negated;
		private final QEntry[] lambda;
		
		
		// count is of non_final if top universal
		// and of final if top existential
		private final Record top;
		private final boolean top_forall;
		private final boolean innner_forall;
		
		public GeneralChecker(boolean n, QEntry[] l,
				boolean[] finalStates, boolean[] strongStates) {
			super(finalStates, strongStates);
		
			lambda = new QEntry[l.length-1];
			System.arraycopy(l, 1, lambda, 0, lambda.length);
			negated=n;
			top_forall = lambda[0].quantification == FORALL;
			innner_forall = lambda[lambda.length-1].quantification == FORALL;
			
			top = new Record(null);
			top.setQuantification(lambda[0].quantification);
			record_map.put(top,0);
		}

		@Override
		public Verdict verdict(boolean at_end) {
			Verdict result = null;

			// Note, similar to OneAlternation, consider generalisation
			int top_count = record_map.get(top);
			
			if(DEBUG) System.err.println("verdict: "+top_count);
			
			boolean failing = top_forall ? top_count > 0 : top_count == 0;
			if (failing) {
				if (at_end) {
					result = FAILURE;
				} else {
					result = WEAK_FAILURE;
				}
			} else if (at_end) {
				result = SUCCESS;
			} else {
				result = WEAK_SUCCESS;
			}

			if (negated) {
				return result.negated();
			}
			return result;
		}

		private boolean checkGuards(QBindingImpl binding) {
			for(int i=0;i<lambda.length;i++){
				if(lambda[i].guard!=null){
					if(!lambda[i].guard.check(binding))
						return false;
				}
			}
			return true;
		}		
		
		private Map<Record,Integer> record_map = new HashMap<>();
		public Record getNext(Record from, QBindingImpl binding){
			
			//System.err.println("index: "+from.index());
			if(from.index()==lambda.length) return null;
			
			Record r = null;
			Quantification q = null;
			r = new Record(from);
			for(int i = 0; i<lambda.length;i++){
				if(i >= from.index()){
					if(q==null){
						q = lambda[i].quantification;							
					}
					else{
						if(lambda[i].quantification!=q){
							r.setQuantification(lambda[i].quantification);
							break;				
						}
					}
				}
				//System.err.println(i+":"+binding.getValue(-(i+1)));
				r.add(binding.getValue(-(i+1)));
			}
			return r;
		}
		
		// In this case 
		private int process(Record record, int add, QBindingImpl binding){
			
			if(DEBUG) System.err.println("process "+record+", and add="+add);
			
			Integer last = record_map.get(record);
			boolean first = false;
			if (last==null) {
				last=0;
				first=true;
				record_map.put(record,last);
				// this is the first one so we need to
				// update the parent's count, we do this below on first
				// in all cases letting last=0
				// means that we assume that one has been counted				
			}			
			
			
			Record next_rec = getNext(record,binding);
			int next;
			if(next_rec.index()!=lambda.length){
				next = process(next_rec,add,binding);
			}else{
				next = last + add;
			}

			// 0 if value for one not changed
			// -1 if it has gone from true to false
			// 1 if it has gone from false to true
			int direction = 0;
			if (record.this_quantification==FORALL) {
				if (last > 0 && next == 0) {
					direction = 1;
				} else if (last == 0 && next > 0) {
					direction = -1;
				}
			} else {
				if (last > 0 && next == 0) {
					direction = -1;
				} else if (last == 0 && next > 0) {
					direction = 1;
				}
			}
			if(DEBUG) System.err.println("direction is "+direction);
			int parent_count = record_map.get(record.parent); // assume will be there
			if(first) parent_count++;
			if(direction!=0){								
				if (record.parent.this_quantification==FORALL) {
					parent_count -= direction;
				} else {
					parent_count += direction;
				}				
			}
			record_map.put(record.parent,parent_count);

			//if(DEBUG) System.err.println("Add "+record+" as "+next+" add was "+add);
			if(DEBUG) System.err.println("setting this to "+next);
			if(next!=last){
				record_map.put(record, next);
			}
			if(DEBUG) System.err.println("Process return "+parent_count);
			return parent_count;
		}
		
		@Override
		public void newBinding(QBindingImpl binding, int state) {
			
			if(DEBUG) System.err.println("new "+binding+" "+state);
			
			if (checkGuards(binding)) {
				Record r = getNext(top,binding);
				boolean isfinal = finalStates[state];
				int add = innner_forall ? isfinal ? 0 : 1 : isfinal ? 1 : 0;
				//if(DEBUG) System.err.println("Add "+binding+" with "+state);
				process(r, add,binding);
			}
			else if(DEBUG) System.err.println("Not added "+binding+" as guard false");
		}

		@Override
		public void newBinding(QBindingImpl binding, int[] states) {
			if(DEBUG) System.err.println("newBinding "+binding+" "+Arrays.toString(states));
			if (checkGuards(binding)) {
				Record r = getNext(top,binding);
				boolean isfinal = false;
				for (int s : states) {
					if (finalStates[s]) {
						isfinal = true;
						break;
					}
				}
				int add = innner_forall ? isfinal ? 0 : 1 : isfinal ? 1 : 0;
				process(r,add,binding);
			}
		}

		@Override
		public void update(QBindingImpl binding, int last_state, int next_state) {
			if (checkGuards(binding)) {
				
				if(DEBUG) System.err.println("update "+binding+" with "+last_state+","+next_state);
				
				Record r = getNext(top,binding);

				boolean last_final = finalStates[last_state];
				boolean next_final = finalStates[next_state];

				int inc_final = last_final && !next_final ? -1 : !last_final
						&& next_final ? 1 : 0;

				int add = innner_forall ? -inc_final : inc_final;
				
				if(DEBUG) System.err.println("Update "+binding+" for "+last_state+" -> "+next_state);
				process(r, add,binding);
			}
		}

		@Override
		public void update(QBindingImpl binding, int[] last_states,
				int[] next_states) {
			if (checkGuards(binding)) {
				
				if(DEBUG) System.err.println("update "+binding+" with "+Arrays.toString(last_states)+","+Arrays.toString(next_states));
				
				Record r = getNext(top,binding);

				boolean last_final = false;
				boolean next_final = false;
				for (int s : last_states) {
					if (finalStates[s]) {
						last_final = true;
						break;
					}
				}
				for (int s : next_states) {
					if (finalStates[s]) {
						next_final = true;
						break;
					}
				}

				int inc_final = last_final && !next_final ? -1 : !last_final
						&& next_final ? 1 : 0;
				
				int add = innner_forall ? -inc_final : inc_final;
				process(r, add,binding);
			}
			
		}		
		
		
		/*
		 * Optimisation helper methods below
		 */
		
		@Override
		public void removeStrong(QBindingImpl binding) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isActive(int state) {
			// TODO Auto-generated method stub
			return true;
		}		
		@Override
		public boolean relevantBinding(QBindingImpl binding) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override public String toString(){
			String ret="RECORD MAP\n";
			for(Map.Entry<Record,Integer> entry : record_map.entrySet()){
				ret += entry.toString()+"\n";
			}
			return ret;
		}
		
	}
	
}
