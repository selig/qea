package monitoring.impl.monitors.general;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import monitoring.impl.configs.NonDetConfig;
import structure.impl.other.QBindingImpl;
import structure.impl.qeas.QVarN_FVar_NonDet_QEA;

public class Incr_Naive_NonDet_Monitor extends Abstr_Incr_Naive_QEAMonitor<QVarN_FVar_NonDet_QEA>  {

	private final HashMap<QBindingImpl,NonDetConfig> mapping = new HashMap<QBindingImpl,NonDetConfig>();
	private final TreeSet<QBindingImpl> B = new TreeSet<QBindingImpl>(new QBindingImpl.QBindingImplComparator());

	
	public Incr_Naive_NonDet_Monitor(QVarN_FVar_NonDet_QEA qea) {
		super(qea);
		
		NonDetConfig initialConfig = new NonDetConfig(qea.getInitialState(),qea.newFBinding());;
		mapping.put(bottom, initialConfig);
		B.add(bottom);
		if(bottom.isTotal()) checker.newBinding(qea.isStateFinal(qea.getInitialState()));
	}

	private boolean isFinal(NonDetConfig config){
		int[] states = config.getStates();
		for(int state : states)
			if(!qea.isStateFinal(state)) return false;
		return true;
	}
	
	@Override
	protected void innerStep(int eventName, QBindingImpl[] qbindings, Object[] args) {			
		
		for(QBindingImpl b : B){
			Set<QBindingImpl> consistent = null;
			for(QBindingImpl other : qbindings){
				if(b.consistentWith(other)){
					if(consistent==null) consistent = new HashSet<QBindingImpl>();
					consistent.add(other.updateWith(b));
				}
			}
			
			if(consistent!=null)
			for(QBindingImpl b_extended : consistent){
				
				NonDetConfig config = mapping.get(b_extended);
				
				if(config==null || b.equals(b_extended)){
					
					//The qea updates the config, so we should copy if extending
					if(config==null){
						config = mapping.get(b).copy();
						mapping.put(b_extended,config);
						B.add(b_extended);
						checker.newBinding(isFinal(config));
					}
					
					boolean previous_final = isFinal(config);
					qea.getNextConfig(b_extended, config, eventName, args);
					
					if(b_extended.isTotal()){
						boolean this_final = isFinal(config);
						checker.update(b_extended,this_final,previous_final);
					}
	
				}
				
			}
		}			
	}

	
	@Override
	public String getStatus() {
		String ret = "mapping:\n";
		for(Map.Entry<QBindingImpl,NonDetConfig> entry : mapping.entrySet()){
			ret += entry.getKey()+"\t"+entry.getValue()+"\n";
		}
		
		return ret;
	}

}
