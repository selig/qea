package monitoring.impl.monitors.general;

import monitoring.impl.GarbageMode;
import monitoring.impl.RestartMode;
import monitoring.impl.configs.DetConfig;
import monitoring.impl.configs.NonDetConfig;
import monitoring.intf.Configuration;
import structure.impl.other.QBindingImpl;
import structure.impl.other.Transition;
import structure.impl.qeas.QVarN_FVar_NonDet_QEA;

public class Incr_QVarN_FVar_NonDet_QEAMonitor extends
		Abstr_Incr_QVarN_FVar_QEAMonitor<QVarN_FVar_NonDet_QEA,NonDetConfig> {

	public Incr_QVarN_FVar_NonDet_QEAMonitor(RestartMode restart,
			GarbageMode garbage, QVarN_FVar_NonDet_QEA qea) {
		super(restart, garbage, qea);
	}

	@Override
	protected Transition[] getTransitions(int s, int e) {
		Transition[] result = qea.getDelta()[s][e];
		if(result==null) return new Transition[]{};
		return result;
	}

	@Override
	protected NonDetConfig initialConfig() {
		return new NonDetConfig(qea.getInitialState(),qea.newFBinding());
	}

	@Override
	protected void processBinding(int eventName, Object[] args,
			boolean has_q_blanks, QBindingImpl binding) {

		NonDetConfig config = mapping.get(binding);
		int[] previous_states = config.getStates();		
		
		//Attempt extensions
				QBindingImpl[] bs = qea.makeBindings(eventName, args);
				for(QBindingImpl from_binding : bs){
					// are binding and from_binding guaranteed to be consistent?
					// - no!
					if(binding.consistentWith(from_binding)){
						QBindingImpl ext = binding.updateWith(from_binding);
						if(!mapping.containsKey(ext)){
							if(DEBUG) System.err.println("Adding new "+ext);
							NonDetConfig next_config = config.copy();							
							next_config = qea.getNextConfig(ext,next_config,eventName,args); 
							mapping.put(ext,next_config);
							add_to_maps(ext);							
							if(ext.isTotal()){
								checker.newBinding(ext,previous_states);
								checker.update(ext,previous_states,next_config.getStates());
							}							
						}
					}
				}	

				//Update configurations - check relevance first
				// will not be relevant if blanks refer only to quantified variables
				if(!has_q_blanks){
					NonDetConfig next_config = qea.getNextConfig(binding,config,eventName,args);
					mapping.put(binding,next_config);
					if(binding.isTotal()){
						checker.update(binding,previous_states,next_config.getStates());
					}					
				}		

	}

	@Override
	protected void processPropositionalBinding(int eventName,
			QBindingImpl binding, NonDetConfig config) {
		int[] previous_states = config.getStates();
		//Warning: this is called whilst iterating over mapping!
		NonDetConfig next_config= qea.getNextConfig(binding, config, eventName, dummyArgs);
		mapping.put(binding,next_config);
		if(binding.isTotal()){
			checker.update(binding, previous_states, next_config.getStates());
		}

	}

}
