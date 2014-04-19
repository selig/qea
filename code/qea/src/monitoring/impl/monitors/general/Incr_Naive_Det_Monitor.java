package monitoring.impl.monitors.general;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import monitoring.impl.configs.DetConfig;
import structure.impl.other.QBindingImpl;
import structure.impl.qeas.QVarN_FVar_Det_QEA;

public class Incr_Naive_Det_Monitor extends Abstr_Incr_Naive_QEAMonitor<QVarN_FVar_Det_QEA>  {

	private final HashMap<QBindingImpl,DetConfig> mapping = new HashMap<QBindingImpl,DetConfig>();
	private final TreeSet<QBindingImpl> B = new TreeSet<QBindingImpl>(new QBindingImpl.QBindingImplComparator());

	
	public Incr_Naive_Det_Monitor(QVarN_FVar_Det_QEA qea) {
		super(qea);
		
		DetConfig initialConfig = new DetConfig(qea.getInitialState(),qea.newFBinding());;
		mapping.put(bottom, initialConfig);
		B.add(bottom);
	}

	@Override
	protected void innerStep(int eventName, QBindingImpl[] qbindings, Object[] args) {			
		TreeSet<QBindingImpl> B_ = new TreeSet<QBindingImpl>(new QBindingImpl.QBindingImplComparator());
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
				
				if(b_extended==null) System.err.println("null binding!");
				
				DetConfig config = mapping.get(b_extended);
				
				if(config==null || b.equals(b_extended)){
					
					//The qea updates the config, so we should copy if extending
					if(config==null){
						config = mapping.get(b).copy();
						mapping.put(b_extended,config);
						B_.add(b_extended);
						if(b_extended.isTotal()){
							checker.newBinding(b_extended,config.getState());
						}
					}
					
					int previous_state = config.getState();
					qea.getNextConfig(b_extended, config, eventName, args);
					
					if(b_extended.isTotal()){
						boolean this_final = qea.isStateFinal(config.getState());
						checker.update(b_extended,previous_state, config.getState());
					}
	
				}
				
			}
		}	
		B.addAll(B_);
	}

	
	@Override
	public String getStatus() {
		String ret = "mapping:\n";
		for(Map.Entry<QBindingImpl,DetConfig> entry : mapping.entrySet()){
			ret += entry.getKey()+"\t"+entry.getValue()+"\n";
		}
		
		return ret;
	}

}
