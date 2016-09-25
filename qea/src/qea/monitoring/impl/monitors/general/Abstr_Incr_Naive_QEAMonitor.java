package qea.monitoring.impl.monitors.general;

import java.util.Arrays;

import qea.exceptions.ShouldNotHappenException;
import qea.monitoring.impl.GarbageMode;
import qea.monitoring.impl.IncrementalMonitor;
import qea.monitoring.impl.RestartMode;
import qea.structure.impl.other.QBindingImpl;
import qea.structure.impl.other.Verdict;
import qea.structure.impl.qeas.Abstr_QVarN_QEA;
import qea.structure.impl.qeas.Abstr_QVarN_QEA.QEntry;

public abstract class Abstr_Incr_Naive_QEAMonitor<Q extends Abstr_QVarN_QEA>
		extends IncrementalMonitor<Q> {

			
	protected final QBindingImpl bottom;
	protected final QBindingImpl[] dummyEmptyBinding = new QBindingImpl[1];
	protected final Object[] emptyArgs = new Object[0];

	protected final IncrementalChecker checker;

	public Abstr_Incr_Naive_QEAMonitor(Q qea) {
		super(RestartMode.NONE, GarbageMode.NONE, qea);
		qea.setupMatching();
		qea.isNormal(); // set normal
		bottom = qea.newQBinding();
		dummyEmptyBinding[0] = bottom;
		
		QEntry[] lambda = qea.getFullLambda();
		for(int i=1;i<lambda.length;i++){
			QEntry q = lambda[i];
			if(q.partial) 
				throw new ShouldNotHappenException("Partial Quantifiers not supported here");
		}
		
		checker = IncrementalChecker.make(qea.getFullLambda(),qea.isNegated(),
				qea.getFinalStates(), qea.getStrongStates());
		if (bottom.isTotal()) {
			checker.newBinding(bottom, qea.getInitialState());
		}
	}

	@Override
	public Verdict step(int eventName, Object[] args) {
		
		if(DEBUG) printEvent(eventName,args);
		
		QBindingImpl[] qbindings = qea.makeBindings(eventName, args);
		innerStep(eventName, qbindings, args);
		Verdict result = checker.verdict(false);
		if (result.isStrong()) {
			saved = result;
		}
		return result;
	}

	@Override
	public Verdict step(int eventName) {
		innerStep(eventName, dummyEmptyBinding, emptyArgs);
		Verdict result = checker.verdict(false);
		if (result.isStrong()) {
			saved = result;
		}
		return result;
	}

	protected abstract void innerStep(int eventName, QBindingImpl[] qbindings,
			Object[] args);

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
