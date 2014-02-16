package monitoring.impl.monitors;

import exceptions.ShouldNotHappenException;
import monitoring.impl.IncrementalMonitor;
import structure.impl.SimpleQEA;
import structure.impl.Verdict;

public abstract class IncrementalSimpleQEAMonitor<Q extends SimpleQEA> extends IncrementalMonitor<Q> {

	public IncrementalSimpleQEAMonitor(Q q) {
		super(q);
		bindingsInNonFinalStateCount = 0;
		bindingsInFinalStateCount = 0;		
	}

	@Override
	public Verdict step(int eventName, Object[] args) {
		if(args.length>1) throw new ShouldNotHappenException("Was only expecting one parameter");
		return step(eventName,args[0]);
	}	

	@Override
	public Verdict end() {

		// According to the quantification of the variable, return verdict
		if (qea.isQuantificationUniversal() && allBindingsInFinalState()
				|| !qea.isQuantificationUniversal()
				&& existsOneBindingInFinalState()) {
			return Verdict.SUCCESS;
		}
		return Verdict.FAILURE;
	}	
	
}
