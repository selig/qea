package qea.monitoring.impl.monitors.general;

import java.util.HashSet;
import java.util.Set;

import qea.exceptions.NotRelevantException;
import qea.monitoring.impl.GarbageMode;
import qea.monitoring.impl.RestartMode;
import qea.monitoring.impl.configs.DetConfig;
import qea.structure.impl.other.QBindingImpl;
import qea.structure.impl.other.Transition;
import qea.structure.impl.qeas.QEAType;
import qea.structure.impl.qeas.QVarN_Det_QEA;

/**
 * A small-step monitor for the most general *deterministic* QEA
 * 
 * We use the symbol-indexing concept
 * 
 * 
 * TODO - when we extend for garbage review every place we use
 * System.identityHashCode
 * 
 * @author Giles Reger
 */
public class Incr_QVarN_FVar_Det_QEAMonitor extends
		Abstr_Incr_QVarN_FVar_QEAMonitor<QVarN_Det_QEA, DetConfig> {

	public Incr_QVarN_FVar_Det_QEAMonitor(RestartMode restart,
			GarbageMode garbage, QVarN_Det_QEA qea) {
		super(restart, garbage, qea);
		if (qea.getQEAType() != QEAType.QVARN_FVAR_DET_QEA) {
			System.err.println("Warning: Expected for use with free variables");
		}
	}

	@Override
	protected void processBinding(int eventName, Object[] args,
			boolean has_q_blanks, QBindingImpl binding,Set<QBindingImpl> created) {
		DetConfig config = mapping.get(binding);
		if (config == null) {
			return; // The binding has gone!
		}
		int previous_state = config.getState();

		// Attempt extensions
		QBindingImpl[] bs = qea.makeBindings(eventName, args);
		for (QBindingImpl from_binding : bs) {
			// are binding and from_binding guaranteed to be consistent?
			// - no!
			if (binding.consistentWith(from_binding)) {
				QBindingImpl ext = binding.updateWith(from_binding);
				// TODO - no redundancy checking here!!
				if (!mapping.containsKey(ext) && !created.contains(ext)) {

					DetConfig next_config = config.copy();
					try {
						created.add(ext);
						qea.getNextConfig(ext, next_config, eventName, args);
						addSupport(ext);
						mapping.put(ext, next_config);
						add_to_maps(ext);
						
						if (DEBUG) {
							System.err.println("X Adding new " + ext+" from "+binding+" with "+next_config);
						}
						
						if (ext.isTotal()) {
							checker.newBinding(ext, previous_state);
							checker.update(ext, previous_state,
									next_config.getState());
						}
						// TODO - how common is this exception? Exceptions are
						// bad!
					} catch (NotRelevantException e) {
						// we mark this as created as it could have been, but was marked non-relevant
						created.add(ext);
						if (!qea.isNormal() && requiredForCompleteness(ext,binding)) {							
							addSupport(ext);
							mapping.put(ext, next_config);
							if (DEBUG) {
								System.err.println("Y Adding new " + ext+" from "+binding+" with "+next_config);
							}
							add_to_maps(ext);
							if (ext.isTotal()) {
								checker.newBinding(ext, previous_state);
								checker.update(ext, previous_state,
										next_config.getState());
							}
						}
					}
				}
			}
		}

		// Update configurations - check relevance first
		// will not be relevant if blanks refer only to quantified variables
		if (!has_q_blanks) {
			try {
				qea.getNextConfig(binding, config, eventName, args); // config
																		// updated
																		// in-place
																		// as
																		// Det
				if (binding.isTotal()) {
					checker.update(binding, previous_state, config.getState());
				}
			} catch (NotRelevantException e) {
				if (!qea.isNormal() && binding.isTotal()) {
					// Shouldn't actually do anything!
					checker.update(binding, previous_state, config.getState());
				}
			}
		}
	}

	@Override
	protected void processPropositionalBinding(int eventName,
			QBindingImpl binding, DetConfig config) {
		int previous_state = config.getState();
		try {
			qea.getNextConfig(binding, config, eventName, dummyArgs);
			if (binding.isTotal()) {
				checker.update(binding, previous_state, config.getState());
			}
		} catch (NotRelevantException e) {
			if (!qea.isNormal() && binding.isTotal()) {
				// Shouldn't actually do anything!
				checker.update(binding, previous_state, config.getState());
			}
		}
	}

	@Override
	protected DetConfig processPropositionalBindingUpdate(int eventName,
			QBindingImpl binding, DetConfig config) {
		// config is updated in-place!
		processPropositionalBinding(eventName,binding,config);
		return null;
	}	

	@Override
	protected Transition[] getTransitions(int s, int e) {
		return new Transition[] { qea.getDelta()[s][e] };
	}

	@Override
	protected DetConfig initialConfig() {
		return new DetConfig(qea.getInitialState(), qea.newFBinding());
	}

}
