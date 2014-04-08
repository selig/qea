package monitoring.impl.monitors;

import monitoring.impl.GarbageMode;
import monitoring.impl.IncrementalMonitor;
import monitoring.impl.RestartMode;
import structure.impl.other.Verdict;
import structure.impl.qeas.Abstr_QVar01_NoFVar_QEA;
import exceptions.ShouldNotHappenException;

public abstract class Abstr_Incr_QVar1_NoFVar_QEAMonitor<Q extends Abstr_QVar01_NoFVar_QEA>
		extends IncrementalMonitor<Q> {

	public Abstr_Incr_QVar1_NoFVar_QEAMonitor(RestartMode restart, GarbageMode garbage, Q q) {
		super(restart, garbage, q);
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
		return computeVerdict(true);
	}

	/**
	 * Computes the verdict for this monitor according to the current state of
	 * the binding(s)
	 * 
	 * @param end
	 *            <code>true</code> if all the events have been processed and a
	 *            final verdict is to be computed; <code>false</code> otherwise
	 * 
	 * @return <ul>
	 *         <li>{@link Verdict#SUCCESS} for a strong success
	 *         <li>{@link Verdict#FAILURE} for a strong failure
	 *         <li>{@link Verdict#WEAK_SUCCESS} for a weak success
	 *         <li>{@link Verdict#WEAK_FAILURE} for a weak failure
	 *         </ul>
	 * 
	 *         A strong success or failure means that other events processed
	 *         after this will produce the same verdict, while a weak success or
	 *         failure indicates that the verdict can change
	 */
	protected Verdict computeVerdict(boolean end) {

		boolean universal = qea.isQuantificationUniversal();
		Verdict result = null;
		
		if ((universal && allBindingsInFinalState())
				|| (!qea.isQuantificationUniversal() && existsOneBindingInFinalState())) {
			if (end || (finalStrongState && !universal)) {
				saved = Verdict.SUCCESS;
				result = Verdict.SUCCESS;
			}
			else result = Verdict.WEAK_SUCCESS;
		}
		else if (end || (nonFinalStrongState && universal)) {
			saved = Verdict.FAILURE;
			result = Verdict.FAILURE;
		}
		else result = Verdict.WEAK_FAILURE;
		
		if(qea.isQuantificationNegated())
			result = result.negated();
		return result;
	}

}
