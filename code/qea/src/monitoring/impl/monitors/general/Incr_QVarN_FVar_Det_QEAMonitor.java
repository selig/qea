package monitoring.impl.monitors.general;

import monitoring.impl.GarbageMode;
import monitoring.impl.RestartMode;
import monitoring.impl.configs.DetConfig;
import structure.impl.other.QBindingImpl;
import structure.impl.other.Transition;
import structure.impl.qeas.QEAType;
import structure.impl.qeas.QVarN_Det_QEA;
import exceptions.NotRelevantException;

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
public class Incr_QVarN_FVar_Det_QEAMonitor extends Abstr_Incr_QVarN_FVar_QEAMonitor<QVarN_Det_QEA,DetConfig> {


	
	public Incr_QVarN_FVar_Det_QEAMonitor(RestartMode restart,
			GarbageMode garbage, QVarN_Det_QEA qea) {
		super(restart, garbage, qea);
		if(qea.getQEAType()!=QEAType.QVARN_FVAR_DET_QEA)
			System.err.println("Warning: Expected for use with free variables");
	}

	protected void processBinding(int eventName, Object[] args,
			boolean has_q_blanks, QBindingImpl binding) {
		DetConfig config = mapping.get(binding);
		if(config==null) return; // The binding has gone!
		int previous_state = config.getState();
		
		//Attempt extensions
		QBindingImpl[] bs = qea.makeBindings(eventName, args);
		for(QBindingImpl from_binding : bs){
			// are binding and from_binding guaranteed to be consistent?
			// - no!
			if(binding.consistentWith(from_binding)){
				QBindingImpl ext = binding.updateWith(from_binding);
				//TODO - no redundancy checking here!!
				if(!mapping.containsKey(ext)){
					if(DEBUG) System.err.println("Adding new "+ext);
					DetConfig next_config = config.copy();		
					try{
						qea.getNextConfig(ext,next_config,eventName,args);
						addSupport(ext);
						mapping.put(ext,next_config);
						add_to_maps(ext);
						if(ext.isTotal()){
							checker.newBinding(ext,previous_state);
							checker.update(ext,previous_state,next_config.getState());
						}	
						//TODO - how common is this exception? Exceptions are bad!
					}catch(NotRelevantException e){
						if(!qea.isNormal()){
							addSupport(ext);
							mapping.put(ext,next_config);
							add_to_maps(ext);
							if(ext.isTotal()){
								checker.newBinding(ext,previous_state);
								checker.update(ext,previous_state,next_config.getState());
							}	
						}
					}
				}
			}
		}	

		//Update configurations - check relevance first
		// will not be relevant if blanks refer only to quantified variables
		if(!has_q_blanks){
			try{
				qea.getNextConfig(binding,config,eventName,args); // config updated in-place as Det
				if(binding.isTotal()){
					checker.update(binding,previous_state,config.getState());
				}		
			}catch(NotRelevantException e){				
				if(!qea.isNormal() && binding.isTotal()){
					//Shouldn't actually do anything!					
					checker.update(binding,previous_state,config.getState());
				}
			}
		}
	}

	protected void processPropositionalBinding(int eventName,
			QBindingImpl binding, DetConfig config) {
		int previous_state = config.getState();
		try{
			qea.getNextConfig(binding, config, eventName, dummyArgs);
			if(binding.isTotal()){
				checker.update(binding, previous_state, config.getState());
			}
		}catch(NotRelevantException e){
			if(!qea.isNormal() && binding.isTotal()){
				//Shouldn't actually do anything!
				checker.update(binding, previous_state, config.getState());
			}
		}
	}	
	
	@Override
	protected Transition[] getTransitions(int s, int e) {
		return new Transition[]{qea.getDelta()[s][e]};
	}

	@Override
	protected DetConfig initialConfig() {
		return new DetConfig(qea.getInitialState(),qea.newFBinding());
	}
	
}
