package qea.monitoring.impl.monitors.general;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import qea.monitoring.impl.GarbageMode;
import qea.monitoring.impl.RestartMode;
import qea.monitoring.intf.Configuration;
import qea.structure.impl.other.QBindingImpl;
import qea.structure.impl.other.Verdict;
import qea.structure.impl.qeas.Abstr_QVarN_QEA;
import qea.util.OurWeakHashMap;

/**
 * 
 * We use the symbol-indexing concept
 * 
 * @author Giles Reger
 */
public abstract class Abstr_Incr_QVarN_FVar_QEAMonitor<Q extends Abstr_QVarN_QEA, C extends Configuration>
		extends Abstr_Incr_QVarN_QEAMonitor<Q> {

	protected final Map<QBindingImpl, C> mapping;

	public Abstr_Incr_QVarN_FVar_QEAMonitor(RestartMode restart,
			GarbageMode garbage, Q qea) {
		super(restart, garbage, qea);

		// Remember that incremental_checker might contain references
		// to objects if quantification is alternating
		switch (garbage) {
		case NONE:
			mapping = new HashMap<QBindingImpl, C>();
			break;
		case UNSAFE_LAZY:
			mapping = new OurWeakHashMap<QBindingImpl, C>();
			break;
		default:
			throw new RuntimeException("Garbage mode " + garbage
					+ " not currently supported");
		}

		C initial = initialConfig();
		mapping.put(bottom, initial);

	}

	protected abstract C initialConfig();

	/*
	 * A binding ext that is being introduced extending from 
	 * is required for completeness if there is no existing binding B such that
	 *  i) |B| >= |from|
	 *  ii) ext subsumes B
	 * Note that this depends on the bindings being traversed in a certain way
	 */
	protected boolean requiredForCompleteness(QBindingImpl ext,QBindingImpl from){
		// Search using submaps of ext that are at least |from| large
		System.err.println("Checking if "+ext+" required for completeness");
		QBindingImpl[] smaller = ext.submaps();
		for(int i=0;i<smaller.length;i++){
			QBindingImpl B = smaller[i];
			System.err.println("Consider "+B);
			if(B.size() < from.size()) return true;
			// the B exists if it is in the mapping
			if(mapping.containsKey(B) && ext.contains(B)){
				System.err.println("not okay");
				return false;
			}
			System.err.println("okay");
		}
		return true;
	}
	
	protected static final Object[] dummyArgs = new Object[] {};

	@Override
	public Verdict step(int eventName) {
		if (saved != null) {
			if (!restart()) {
				return saved;
			}
		}
		Map<QBindingImpl,C> updates = new HashMap<QBindingImpl,C>();
		for (Map.Entry<QBindingImpl, C> entry : mapping.entrySet()) {
			QBindingImpl binding = entry.getKey();
			C config = entry.getValue();
			// to stop ConcurrentModificationException we store the updates
			// to mapping and do them at the end
			C next_config = processPropositionalBindingUpdate(eventName, binding, config);
			if (next_config!=null) updates.put(binding,next_config);
		}
		mapping.putAll(updates);
		Verdict v = checker.verdict(false);
		if (v.isStrong()) {
			saved = v;
		}
		return v;
	}


	protected abstract void processPropositionalBinding(int eventName,
			QBindingImpl binding, C config);

	protected abstract C processPropositionalBindingUpdate(int eventName,
			QBindingImpl binding, C config);	
	
	@Override
	public String getStatus() {
		String ret = "mapping\n";
		for (Map.Entry<QBindingImpl, C> entry : mapping.entrySet()) {
			//if(entry.getValue().isZero())
			ret += entry.getKey() + "\t" + entry.getValue() + "\n";
		}
		/*
		ret += "maps\n";		
		for (int e = 1; e < maps.length; e++) {
			BindingRecord r = empty_paths[e];
			Map<String, BindingRecord> m = maps[e];
			ret += e + "\n";
			ret += "\t empty \t"+r+"\n";
			for (Map.Entry<String, BindingRecord> entry : m.entrySet()) {
				ret += "\t" + entry.getKey() + "\t" + entry.getValue() + "\n";
			}
		}
		
		ret += "\n" + checker;
		*/
		return ret;
	}

	@Override
	protected int removeStrongBindings() {
		Set<QBindingImpl> strong_bindings = checker.getStrongBindings();
		for (QBindingImpl binding : strong_bindings) {
			// first remove from mapping
			mapping.remove(binding);
			// next remove references in maps
			add_to_maps(binding, false);
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
	protected void printMaps() {
		System.err.println("Mapping: " + mapping.size());
		for (int i = 1; i < maps.length; i++) {
			System.err.println("map " + i + ": " + maps[i].size());
		}
	}

}
