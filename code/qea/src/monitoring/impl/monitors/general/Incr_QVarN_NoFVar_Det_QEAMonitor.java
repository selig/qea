package monitoring.impl.monitors.general;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import monitoring.impl.GarbageMode;
import monitoring.impl.RestartMode;
import structure.impl.other.QBindingImpl;
import structure.impl.other.Transition;
import structure.impl.other.Verdict;
import structure.impl.qeas.QEAType;
import structure.impl.qeas.QVarN_Det_QEA;
import util.OurWeakHashMap;

/**
 * A small-step monitor for the most general *deterministic* QEA
 *
 * We use the symbol-indexing concept
 *
 *
 * TODO - when we extend for garbage review every place we use System.identityHashCode
 *
 * @author Giles Reger
 */
public class Incr_QVarN_NoFVar_Det_QEAMonitor extends Abstr_Incr_QVarN_QEAMonitor<QVarN_Det_QEA> {
	
	// Not final as step(int) might replace it
	private Map<QBindingImpl,Integer> mapping;
	
	public Incr_QVarN_NoFVar_Det_QEAMonitor(RestartMode restart,
			GarbageMode garbage, QVarN_Det_QEA qea) {
		super(restart, garbage, qea);
		if(qea.getQEAType()!=QEAType.QVARN_NOFVAR_DET_QEA)
			throw new RuntimeException("Cannot be used with free variables");
		
		//UPDATE new_mapping in step(int) as well
		switch(garbage){
		case NONE: 
			mapping = new HashMap<QBindingImpl,Integer>();
			break;
		case UNSAFE_LAZY:
			mapping = new OurWeakHashMap<QBindingImpl,Integer>();
			break;
	default: throw new RuntimeException("Garbage mode "+garbage+" not currently supported");
	}
			
	mapping.put(bottom,qea.getInitialState());		
		
	}

	protected static final Object[] dummyArgs = new Object[]{};
	@Override
	public Verdict step(int eventName) {
		if(saved!=null){
			if(!restart()) return saved;
		}	
		Map<QBindingImpl,Integer> new_mapping; 
		switch(garbage_mode){
		case NONE: 
			new_mapping = new HashMap<QBindingImpl,Integer>(mapping.size());
			break;
		case UNSAFE_LAZY:
			new_mapping = new OurWeakHashMap<QBindingImpl,Integer>(mapping.size());
			break;
	default: throw new RuntimeException("Garbage mode "+garbage_mode+" not currently supported");
	}		
		
		
		for(Map.Entry<QBindingImpl,Integer> entry : mapping.entrySet()){
			QBindingImpl binding = entry.getKey();
			Integer previous_state = entry.getValue();
			Integer next_state = qea.getNextConfig(binding,previous_state,eventName,dummyArgs);
			if(next_state!=null){
				new_mapping.put(binding,next_state);
				if(binding.isTotal()){
					checker.update(binding, previous_state, next_state);
				}
			}else{
				if(!qea.isNormal()){
					new_mapping.put(binding,previous_state);
					if(binding.isTotal()){
						checker.update(binding, previous_state, previous_state);
					}					
				}
			}
			
		}
		mapping = new_mapping;
		Verdict v = checker.verdict(false);
		if(v.isStrong()) saved=v;
		return v;
	}	
	
	
	protected void processBinding(int eventName, Object[] args,
			boolean has_q_blanks, QBindingImpl binding) {
		
		Integer previous_state = mapping.get(binding);
		
		if(DEBUG) System.err.println("Processing "+binding+" with "+previous_state);
		
		//can previous_state be null?
		//Shouldn't be!!
		if(previous_state==null){
			System.err.println("previous state null");
			System.err.println("Binding: "+binding);
			System.err.println("------------");
			System.err.println(this);			
		}
		
		// Whether we can use redundancy elimination here is based on the global
		// option and whether the previous state is active		
		boolean this_use_red = use_red && !checker.isActive(previous_state);
		
		if(DEBUG) System.err.println("this_use_red: "+this_use_red);
		
		//Attempt extensions only if we an extensions could be nontrivial
		if(!this_use_red || could_leave[previous_state][eventName]){
			QBindingImpl[] bs = qea.makeBindings(eventName, args);
			if(DEBUG) System.err.println("bs: "+Arrays.toString(bs));
			for(QBindingImpl from_binding : bs){
				// are binding and from_binding guaranteed to be consistent?
				// - no!
				if(binding.consistentWith(from_binding)){
					QBindingImpl ext = binding.updateWith(from_binding);
					if(DEBUG) System.err.println("Checking "+ext);
					if(!mapping.containsKey(ext)){												
						Integer next_state = qea.getNextConfig(ext,previous_state,eventName,args);
						if(DEBUG) System.err.println("Adding new "+ext+" with "+next_state);
						if(next_state!=null){
							//If next_state loops then it's trivial
							// if in red_mode we ignore it
							if(!this_use_red || next_state!=previous_state){							
								addSupport(ext);
								mapping.put(ext,next_state);
								add_to_maps(ext);
								if(ext.isTotal()){
									checker.newBinding(ext,previous_state);
									checker.update(ext,previous_state,next_state);
								}
							}
						}else{
							if(!qea.isNormal()){
								addSupport(ext);
								mapping.put(ext,previous_state);
								add_to_maps(ext);
								if(ext.isTotal()){
									checker.newBinding(ext,previous_state);
									checker.update(ext,previous_state,previous_state);
								}					
							}
						}
					}
				}
			}
		}

		//Update configurations - check relevance first
		// will not be relevant if blanks refer only to quantified variables
		if(!has_q_blanks){			
			Integer next_state = qea.getNextConfig(binding,previous_state,eventName,args);
			if(DEBUG) System.err.println("Updating "+binding+" with "+next_state);
			if(next_state!=null){
				mapping.put(binding,next_state);
				if(binding.isTotal()){
					checker.update(binding,previous_state,next_state);
				}	
			}else{
				if(!qea.isNormal()){
					mapping.put(binding,previous_state);
					if(binding.isTotal()){
						checker.update(binding, previous_state, previous_state);
					}					
				}
			}
		}
	}

	
	@Override
	protected Transition[] getTransitions(int s, int e) {
		return new Transition[]{qea.getDelta()[s][e]};
	}

	@Override
	protected int removeStrongBindings() {
				
		Set<QBindingImpl> strong_bindings = checker.getStrongBindings();
		
		if(DEBUG) System.err.println("Removing strong: "+strong_bindings);
		
		for(QBindingImpl binding : strong_bindings){
			// first remove from mapping
			mapping.remove(binding);
			// next remove references in maps
			add_to_maps(binding,false);			
			// finally, we need to update checker
			checker.removeStrong(binding);
		}
		
		int removed = strong_bindings.size();
		strong_bindings.clear();
		return removed;
	}

	@Override
	protected int rollbackStrongBindings() {
		throw new RuntimeException("Rollback restart not implemented");
	}

	@Override
	protected int ignoreStrongBindings() {
		throw new RuntimeException("Ignore restart not implemented");
	}	


	@Override
	public String getStatus() {
		String ret = "mapping\n";
		for(Map.Entry<QBindingImpl,Integer> entry : mapping.entrySet())
			ret += entry.getKey()+"\t"+entry.getValue()+"\n";
		ret+= "maps\n";
		for(int e=1;e<maps.length;e++){
			Map<String,BindingRecord> m = maps[e];
			ret+=e+"\n";
			for(Map.Entry<String,BindingRecord> entry : m.entrySet())
				ret += "\t"+entry.getKey()+"\t"+entry.getValue()+"\n";			
		}
		ret +="\n"+checker;
		return ret;
	}	
	
	@Override
	protected void printMaps() {
		System.err.println("Mapping: "+mapping.size());
		for(int i=1;i<maps.length;i++) System.err.println("map "+i+": "+maps[i].size());
	}		
	
}
