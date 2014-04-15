package monitoring.impl.monitors.general;

import java.util.HashMap;

import monitoring.impl.GarbageMode;
import monitoring.impl.RestartMode;
import monitoring.impl.configs.DetConfig;
import monitoring.impl.monitors.general.Abstr_Incr_QVarN_FVar_QEAMonitor.BindingRecord;
import structure.impl.other.QBindingImpl;
import structure.impl.other.Transition;
import structure.impl.qeas.QVarN_FVar_Det_QEA;

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
public class Incr_QVarN_FVar_Det_QEAMonitor extends Abstr_Incr_QVarN_FVar_QEAMonitor<QVarN_FVar_Det_QEA,DetConfig> {


	
	public Incr_QVarN_FVar_Det_QEAMonitor(RestartMode restart,
			GarbageMode garbage, QVarN_FVar_Det_QEA qea) {
		super(restart, garbage, qea);
	}

	protected void processBinding(int eventName, Object[] args,
			boolean has_q_blanks, QBindingImpl binding) {
		DetConfig config = mapping.get(binding);
		int previous_state = config.getState();
		
		//Attempt extensions
		QBindingImpl[] bs = qea.makeBindings(eventName, args);
		for(QBindingImpl from_binding : bs){
			// are binding and from_binding guaranteed to be consistent?
			// - no!
			if(binding.consistentWith(from_binding)){
				QBindingImpl ext = binding.updateWith(from_binding);
				if(!mapping.containsKey(ext)){
					if(DEBUG) System.err.println("Adding new "+ext);
					DetConfig next_config = config.copy();							
					qea.getNextConfig(ext,next_config,eventName,args);
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

		//Update configurations - check relevance first
		// will not be relevant if blanks refer only to quantified variables
		if(!has_q_blanks){
			qea.getNextConfig(binding,config,eventName,args); // config updated in-place as Det
			if(binding.isTotal()){
				checker.update(binding,previous_state,config.getState());
			}					
		}
	}

	protected void processPropositionalBinding(int eventName,
			QBindingImpl binding, DetConfig config) {
		int previous_state = config.getState();
		qea.getNextConfig(binding, config, eventName, dummyArgs);
		if(binding.isTotal()){
			checker.update(binding, previous_state, config.getState());
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
