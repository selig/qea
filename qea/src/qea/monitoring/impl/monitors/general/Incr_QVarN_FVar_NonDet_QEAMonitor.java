package qea.monitoring.impl.monitors.general;

import java.util.HashSet;
import java.util.Set;

import qea.exceptions.NotRelevantException;
import qea.monitoring.impl.GarbageMode;
import qea.monitoring.impl.RestartMode;
import qea.monitoring.impl.configs.NonDetConfig;
import qea.structure.impl.other.QBindingImpl;
import qea.structure.impl.other.Transition;
import qea.structure.impl.qeas.QEAType;
import qea.structure.impl.qeas.QVarN_NonDet_QEA;

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

	private static NonDetConfig initial_config=null;
	@Override
	protected NonDetConfig initialConfig() {
		if(initial_config==null){
			initial_config = new NonDetConfig(qea.getInitialState(), qea.newFBinding(),null);
		}
		return initial_config;
	}

	@Override
	protected void processBinding(int eventName, Object[] args,
			boolean has_q_blanks, QBindingImpl binding,Set<QBindingImpl> created) {

		//printEvent(eventName,args);
		//System.err.println("QB: "+binding);
		
		
		NonDetConfig config = mapping.get(binding);
		if (config == null) {
			return; // the binding is gone!
		}
		int[] previous_states = config.getStates();
		boolean this_use_red = use_red && !checker.isActive(previous_states);		
		
		if (!this_use_red || could_leave(previous_states,eventName)){ 

			// Attempt extensions
			QBindingImpl[] bs = qea.makeBindings(eventName, args);
			for (QBindingImpl from_binding : bs) {
				// are binding and from_binding guaranteed to be consistent?
				// - no!
				if (binding.consistentWith(from_binding)) {
					QBindingImpl ext = binding.updateWith(from_binding);
					if (!mapping.containsKey(ext) && !created.contains(ext) && (!ext.isTotal() || checker.relevantBinding(ext))) {
						if (DEBUG) {
							System.err.println("Adding new " + ext+" from "+binding);
						}
						NonDetConfig next_config = config.copyForExtension();					
						try {
							next_config = qea.getNextConfig(ext, next_config,
									eventName, args);				
							
							// Redundancy thing
							if(!this_use_red || !next_config.equals(config)){
							
								if (DEBUG) {
									System.err
											.println("New has configs " + next_config);
								}
								created.add(ext);
								addSupport(ext);
								mapping.put(ext, next_config);
								add_to_maps(ext);
								if (ext.isTotal()) {
									checker.newBinding(ext, previous_states);
									checker.update(ext, previous_states,
											next_config.getStates());
								}
							}else{
								if(DEBUG) System.err.println("Ignore "+ext);
							}
							
						} catch (NotRelevantException e) {
							if(DEBUG) System.err.println("Not relevant");
							if (!qea.isNormal()) {
								if(DEBUG) System.err.println("QEA not normal, keeping");
								// Carry on with next_config unchanged
								created.add(ext);
								addSupport(ext);
								mapping.put(ext, next_config);
								add_to_maps(ext);
								if (ext.isTotal()) {
									checker.newBinding(ext, previous_states);
									checker.update(ext, previous_states,
											next_config.getStates());
								}
							}else{
								if(DEBUG) System.err.println("QEA normal, skipping");
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
				
				
				if(remove_red && next_config.hasReturned()){
					if(DEBUG){ System.err.println("Remove "+binding+" in "+next_config); }
					mapping.remove(binding);
					add_to_maps(binding,false);
				}else{
				
					if (DEBUG) {
						System.err.println("New configs are " + next_config);
					}
					mapping.put(binding, next_config);
					if (binding.isTotal()) {
						checker.update(binding, previous_states,
								next_config.getStates());
					}
				}
			} catch (NotRelevantException e) {
				NonDetConfig next_config = config.copyForLocal();
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
