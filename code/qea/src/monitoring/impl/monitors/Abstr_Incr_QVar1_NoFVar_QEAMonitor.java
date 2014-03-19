package monitoring.impl.monitors;

import monitoring.impl.IncrementalMonitor;
import structure.impl.other.Verdict;
import structure.impl.qeas.Abstr_QVar01_NoFVar_QEA;
import exceptions.ShouldNotHappenException;

public abstract class Abstr_Incr_QVar1_NoFVar_QEAMonitor<Q extends Abstr_QVar01_NoFVar_QEA>
		extends IncrementalMonitor<Q> {

	public Abstr_Incr_QVar1_NoFVar_QEAMonitor(Q q) {
		super(q);
	}

	@Override
	public Verdict step(int eventName, Object[] args) {
		if (args.length > 1) {
			throw new ShouldNotHappenException(
					"Was only expecting one parameter");
		}
		return step(eventName, args[0]);
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
