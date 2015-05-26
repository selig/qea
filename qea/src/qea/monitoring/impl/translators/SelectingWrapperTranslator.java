package qea.monitoring.impl.translators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import qea.monitoring.intf.Monitor;
import qea.structure.impl.other.Verdict;

public class SelectingWrapperTranslator<Inner extends OfflineTranslator> extends OfflineTranslator {

	// TODO consider if a better data structure would improve things
	// selecting_mask.get(e) = null means no reordering
	// selecting_mask.get(e) is the reordered list of parameters
	private Map<String,ArrayList<Integer>> reordering_list = new HashMap<String,ArrayList<Integer>>();	
	
	public void setOrder(String e, Integer... order){ 
		ArrayList<Integer> orderAL = new ArrayList<Integer>();
		reordering_list.put(e, orderAL);
		for(Integer i : order) orderAL.add(i);
	}
	
	private final Inner inner;	
	public SelectingWrapperTranslator(Inner inner){ this.inner = inner; }
	
	@Override
	public Verdict translateAndStep(String eventName, String[] paramNames,
			String[] paramValues) {
		
		
		ArrayList<Integer> order = reordering_list.get(eventName);
		if(order==null){			
			if(paramValues==null) inner.translateAndStep(eventName);
			return inner.translateAndStep(eventName,paramNames,paramValues);
		}
		
		String[] newPv;

		newPv = new String[order.size()];
		int p=0;
		for(int i=0;i<order.size();i++){
			newPv[p++] = paramValues[order.get(i)];
		}

		if(newPv.length==0) return inner.translateAndStep(eventName);
		return inner.translateAndStep(eventName,null,newPv);
	}

	@Override
	public Verdict translateAndStep(String eventName) {
		return inner.translateAndStep(eventName);
	}

	@Override
	public void allowUnknown() {
		inner.allowUnknown();
	}
	@Override
	public void setMonitor(Monitor monitor) {
		inner.monitor = monitor;
	}	
	@Override
	public Monitor getMonitor(){ return inner.getMonitor(); }
}
