package monitoring.impl.monitors.general;

import java.util.HashSet;
import java.util.Set;

import monitoring.impl.GarbageMode;
import monitoring.impl.IncrementalMonitor;
import monitoring.impl.RestartMode;
import structure.impl.other.QBindingImpl;
import structure.impl.other.Verdict;
import structure.impl.qeas.Abstr_QVarN_FVar_QEA;

public abstract class Abstr_Incr_Naive_QEAMonitor<Q extends Abstr_QVarN_FVar_QEA> extends IncrementalMonitor<Q>  {

	protected final QBindingImpl bottom;
	protected final QBindingImpl[] dummyEmptyBinding = new QBindingImpl[1];
	protected final Object[] emptyArgs = new Object[0];
	
	protected final IncrementalChecker checker;

		
	public Abstr_Incr_Naive_QEAMonitor(Q qea) {
		super(RestartMode.NONE,GarbageMode.NONE,qea);
		qea.setupMatching();
		bottom = qea.newQBinding();
		dummyEmptyBinding[0]=bottom;	
		checker = IncrementalChecker.make(qea.getFullLambda(),qea.getFinalStates(),qea.getStrongStates());
		if(bottom.isTotal()) checker.newBinding(qea.getInitialState());
	}

	@Override
	public Verdict step(int eventName, Object[] args) {
		QBindingImpl[] qbindings = qea.makeBindings(eventName,args);
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
	
	protected abstract void innerStep(int eventName, QBindingImpl[] qbindings, Object[] args);

	@Override
	public Verdict end() {
		return checker.verdict(true);
	}

	@Override
	protected int removeStrongBindings() {
		// No restart for naive monitor
		return 0;
	}

	@Override
	protected int rollbackStrongBindings() {
		// No restart for naive monitor
		return 0;
	}

	@Override
	protected int ignoreStrongBindings() {
		// No restart for naive monitor
		return 0;
	}

}
