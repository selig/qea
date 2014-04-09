package monitoring.impl.monitors.general;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import monitoring.impl.GarbageMode;
import monitoring.impl.IncrementalMonitor;
import monitoring.impl.RestartMode;
import monitoring.impl.configs.DetConfig;
import structure.impl.other.QBindingImpl;
import structure.impl.other.Verdict;
import structure.impl.qeas.QVarN_FVar_Det_QEA;

public class Incr_Naive_Det_Monitor extends IncrementalMonitor<QVarN_FVar_Det_QEA>  {

	private final HashMap<QBindingImpl,DetConfig> mapping = new HashMap<QBindingImpl,DetConfig>();
	private final TreeSet<QBindingImpl> B = new TreeSet<QBindingImpl>(new QBindingImpl.QBindingImplComparator());
	private final QBindingImpl bottom;
	private final Set<QBindingImpl> dummyEmptyBinding = new HashSet<QBindingImpl>();
	private final Object[] emptyArgs = new Object[0];
	
	private final IncrementalChecker checker;

	
	
	public Incr_Naive_Det_Monitor(QVarN_FVar_Det_QEA qea) {
		super(RestartMode.NONE,GarbageMode.NONE,qea);
		qea.setupMatching();
		bottom = qea.newQBinding();
		dummyEmptyBinding.add(bottom);
		DetConfig initialConfig = new DetConfig(qea.getInitialState(),qea.newFBinding());;
		mapping.put(bottom, initialConfig);
		B.add(bottom);
		checker = IncrementalChecker.make(qea.lambda);
		if(bottom.isTotal()) checker.newBinding(qea.isStateFinal(qea.getInitialState()));
	}

	@Override
	public Verdict step(int eventName, Object[] args) {
		Set<QBindingImpl> qbindings = qea.makeBindings(eventName,args);
		innerStep(eventName,qbindings,args);
		Verdict result = checker.verdict(false);		
		if(result.isStrong()) saved=result;
		return result;
	}


	@Override
	public Verdict step(int eventName) {
		innerStep(eventName,dummyEmptyBinding,emptyArgs);
		Verdict result = checker.verdict(false);		
		if(result.isStrong()) saved=result;
		return result;
	}
	
	private void innerStep(int eventName, Set<QBindingImpl> qbindings, Object[] args) {			
		
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
				
				DetConfig config = mapping.get(b_extended);
				
				if(config==null || b.equals(b_extended)){
					
					//The qea updates the config, so we should copy if extending
					if(config==null){
						config = mapping.get(b).copy();
						mapping.put(b_extended,config);
						B.add(b_extended);
						checker.newBinding(qea.isStateFinal(config.getState()));
					}
					
					boolean previous_final = qea.isStateFinal(config.getState());
					qea.getNextConfig(b_extended, config, eventName, args);
					
					if(b_extended.isTotal()){
						boolean this_final = qea.isStateFinal(config.getState());
						checker.update(b_extended,this_final,previous_final);
					}
	
				}
				
			}
		}			
	}

	@Override
	public Verdict end() {
		return checker.verdict(true);
	}
	
	@Override
	public String getStatus() {
		String ret = "mapping:\n";
		for(Map.Entry<QBindingImpl,DetConfig> entry : mapping.entrySet()){
			ret += entry.getKey()+"\t"+entry.getValue()+"\n";
		}
		
		return ret;
	}

	@Override
	protected int removeStrongBindings() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int rollbackStrongBindings() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int ignoreStrongBindings() {
		// TODO Auto-generated method stub
		return 0;
	}

}
