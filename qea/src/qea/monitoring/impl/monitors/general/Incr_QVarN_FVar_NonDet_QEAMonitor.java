package qea.monitoring.impl.monitors.general;

import qea.monitoring.impl.GarbageMode;
import qea.monitoring.impl.RestartMode;
import qea.monitoring.impl.configs.NonDetConfig;
import qea.structure.impl.other.QBindingImpl;
import qea.structure.impl.other.Transition;
import qea.structure.impl.qeas.QEAType;
import qea.structure.impl.qeas.QVarN_NonDet_QEA;
import qea.exceptions.NotRelevantException;

public class Incr_QVarN_FVar_NonDet_QEAMonitor extends
		Abstr_Incr_QVarN_FVar_QEAMonitor<QVarN_NonDet_QEA, NonDetConfig> {

	public Incr_QVarN_FVar_NonDet_QEAMonitor(RestartMode restart,
			GarbageMode garbage, QVarN_NonDet_QEA qea) {
		super(restart, garbage, qea);
		if (qea.getQEAType() != QEAType.QVARN_FVAR_NONDET_QEA) {
			System.err.println("Warning: Expected for use with free variables");
		}
	}

	@Override
	protected Transition[] getTransitions(int s, int e) {
		Transition[] result = qea.getDelta()[s][e];
		if (result == null) {
			return new Transition[] {};
		}
		return result;
	}

	@Override
	protected NonDetConfig initialConfig() {
		return new NonDetConfig(qea.getInitialState(), qea.newFBinding());
	}

	@Override
	protected void processBinding(int eventName, Object[] args,
			boolean has_q_blanks, QBindingImpl binding) {

		NonDetConfig config = mapping.get(binding);
		if (config == null) {
			return; // the binding is gone!
		}
		int[] previous_states = config.getStates();

		// Attempt extensions
		QBindingImpl[] bs = qea.makeBindings(eventName, args);
		for (QBindingImpl from_binding : bs) {
			// are binding and from_binding guaranteed to be consistent?
			// - no!
			if (binding.consistentWith(from_binding)) {
				QBindingImpl ext = binding.updateWith(from_binding);
				if (!mapping.containsKey(ext)) {
					if (DEBUG) {
						System.err.println("Adding new " + ext);
					}
					NonDetConfig next_config = config.copy();
					try {
						next_config = qea.getNextConfig(ext, next_config,
								eventName, args);
						if (DEBUG) {
							System.err
									.println("New has configs " + next_config);
						}
						addSupport(ext);
						mapping.put(ext, next_config);
						add_to_maps(ext);
						if (ext.isTotal()) {
							checker.newBinding(ext, previous_states);
							checker.update(ext, previous_states,
									next_config.getStates());
						}
					} catch (NotRelevantException e) {
						if (!qea.isNormal()) {
							// Carry on with next_config unchanged
							addSupport(ext);
							mapping.put(ext, next_config);
							add_to_maps(ext);
							if (ext.isTotal()) {
								checker.newBinding(ext, previous_states);
								checker.update(ext, previous_states,
										next_config.getStates());
							}
						}
					}
				}
			}
		}

		// Update configurations - check relevance first
		// will not be relevant if blanks refer only to quantified variables
		if (!has_q_blanks) {
			if (DEBUG) {
				System.err.println("Updating configs for " + binding);
			}
			try {
				NonDetConfig next_config = qea.getNextConfig(binding, config,
						eventName, args);
				if (DEBUG) {
					System.err.println("New configs are " + next_config);
				}
				mapping.put(binding, next_config);
				if (binding.isTotal()) {
					checker.update(binding, previous_states,
							next_config.getStates());
				}
			} catch (NotRelevantException e) {
				NonDetConfig next_config = config.copy();
				mapping.put(binding, next_config);
				if (binding.isTotal()) {
					checker.update(binding, previous_states,
							next_config.getStates());
				}
			}
		}

	}

	
	protected NonDetConfig processPropositionalBindingUpdate(int eventName,
			QBindingImpl binding, NonDetConfig config) {
		int[] previous_states = config.getStates();

		
		 try{ 
			 NonDetConfig next_config= qea.getNextConfig(binding, config,eventName, dummyArgs); 
			 //mapping.put(binding,next_config);
			 if(binding.isTotal()){ 
				 checker.update(binding, previous_states,next_config.getStates()); 
			 } 
			 return next_config;
		 } 
		 catch(NotRelevantException e){ 
			 //That'sokay 
			 return null;
		 }
		 

	}

	@Override
	protected void processPropositionalBinding(int eventName,
			QBindingImpl binding, NonDetConfig config) {
		throw new RuntimeException("Not implemented for this - use processPropositionalBindingUpdate");
		
	}

}
