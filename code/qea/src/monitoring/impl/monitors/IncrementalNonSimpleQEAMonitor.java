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

	/**
	 * Retrieves the value of the quantified variable (if present) in the
	 * specified array of arguments according to the specified variable names
	 * 
	 * @param variableNames
	 *            Array containing names of variables. The quantified variable
	 *            name is an integer < 0
	 * @param args
	 *            Array containing the data values
	 * @return Data value for the quantified variable or <code>null</code> if
	 *         there is no quantified variable in the array of variable names
	 */
	protected Object getQVarValue(Object[] args, int[] variableNames) {
		for (int i = 0; i < variableNames.length; i++) {
			if (variableNames[i] < 0) {
				return args[i];
			}
		}
		return null;
	}

	/**
	 * Determines if the specified array of event signatures contains at least
	 * one signature where all parameters correspond to free variables
	 * 
	 * @param eventSignatures
	 *            Array of parameters
	 * @return <code>true</code> if there is at least one set of parameters
	 *         containing only free variables; <code>false</code> otherwise
	 */
	protected boolean containsSignatureOnlyFVars(int[][] eventSignatures) {
		for (int[] variableNames : eventSignatures) {
			boolean qVar = false;
			for (int varName : variableNames) {
				if (varName < 0) {
					qVar = true;
				}
			}
			if (!qVar) {
				return true;
			}
		}
		return false;
	}

}
