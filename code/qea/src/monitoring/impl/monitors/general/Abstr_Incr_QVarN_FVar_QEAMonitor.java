package monitoring.impl.monitors.general;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import monitoring.impl.GarbageMode;
import monitoring.impl.IncrementalMonitor;
import monitoring.impl.RestartMode;
import monitoring.intf.Configuration;
import structure.impl.other.QBindingImpl;
import structure.impl.other.Transition;
import structure.impl.other.Verdict;
import structure.impl.qeas.Abstr_QVarN_QEA;
import util.OurVeryWeakHashMap;
import util.OurWeakHashMap;

/**
 *
 * We use the symbol-indexing concept
 
 * @author Giles Reger
 */
public abstract class Abstr_Incr_QVarN_FVar_QEAMonitor<Q extends Abstr_QVarN_QEA, C extends Configuration> extends Abstr_Incr_QVarN_QEAMonitor<Q> {

	protected final Map<QBindingImpl,C> mapping;	
	


	public Abstr_Incr_QVarN_FVar_QEAMonitor(RestartMode restart, GarbageMode garbage, Q qea) {
		super(restart,garbage,qea);
		
		
		//Remember that incremental_checker might contain references
		// to objects if quantification is alternating
		switch(garbage){
			case NONE: 
				mapping = new HashMap<QBindingImpl,C>();
				break;
			case UNSAFE_LAZY:
				mapping = new OurWeakHashMap<QBindingImpl,C>();
				break;
		default: throw new RuntimeException("Garbage mode "+garbage+" not currently supported");
		}
		
		C initial = initialConfig();		
		mapping.put(bottom,initial);
		
	}

		protected abstract C initialConfig();

  
	protected static final Object[] dummyArgs = new Object[]{};
	@Override
	public Verdict step(int eventName) {
		if(saved!=null){
			if(!restart()) return saved;
		}	
		for(Map.Entry<QBindingImpl,C> entry : mapping.entrySet()){
			QBindingImpl binding = entry.getKey();
			C config = entry.getValue();
			processPropositionalBinding(eventName, binding, config);
		}
		Verdict v = checker.verdict(false);
		if(v.isStrong()) saved=v;
		return v;
	}

	protected abstract void processPropositionalBinding(int eventName,
			QBindingImpl binding, C config);

	
	@Override
	public String getStatus() {
		String ret = "mapping\n";
		for(Map.Entry<QBindingImpl,C> entry : mapping.entrySet())
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
	protected int removeStrongBindings() {
		Set<QBindingImpl> strong_bindings = checker.getStrongBindings();
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
	
}
