package monitoring.impl.monitors;

import monitoring.impl.IncrementalMonitor;
import structure.impl.NonSimpleQEA;
import structure.impl.Verdict;

public abstract class IncrementalNonSimpleQEAMonitor<Q extends NonSimpleQEA>
		extends IncrementalMonitor<Q> {

	public IncrementalNonSimpleQEAMonitor(Q q) {
		super(q);
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
