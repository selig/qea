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
	 * Retrieves the different bindings for the quantified variable, according
	 * to the event mask and arguments specified
	 * 
	 * @param eventMask
	 *            Array containing a value <code>true</code> in the positions of
	 *            the quantified variable. The size must be equal to the size of
	 *            <code>args</code>
	 * @param args
	 *            Array of arguments for the event. The size must be equal to
	 *            the size of <code>eventMask</code>
	 * @param numQVarPositions
	 *            Number of positions in the mask for the quantified variable,
	 *            i.e., number of positions in <code>eventMask</code> whose
	 *            value is <code>true</code>
	 * @return An array with the different bindings (values) for the quantified
	 *         variable
	 */
	protected Object[] getUniqueQVarBindings(boolean[] eventMask,
			Object[] args, int numQVarPositions) {

		Object[] bindings = new Object[numQVarPositions];
		int numBindings = 0;
		boolean existingBinding;

		// Iterate over the mask
		for (int i = 0; i < eventMask.length; i++) {

			if (eventMask[i]) { // QVar position found

				// Check if the binding is already in the array
				existingBinding = false;
				for (int j = 0; j < numBindings; j++) {
					if (args[i].equals(bindings[j])) {
						existingBinding = true;
					}
				}

				// If it's a new binding, add it
				if (!existingBinding) {
					bindings[numBindings] = args[i];
					numBindings++;
				}
			}
		}

		// Resize array if needed
		if (numBindings != numQVarPositions) {
			Object[] resizedBindings = new Object[numBindings];
			System.arraycopy(bindings, 0, resizedBindings, 0, numBindings);
			return resizedBindings;
		}

		return bindings;
	}

	/**
	 * Retrieves the first binding for the quantified variable, according to the
	 * event mask and arguments specified
	 * 
	 * @param eventMask
	 *            Array containing a value <code>true</code> in the positions of
	 *            the quantified variable. The size must be equal to the size of
	 *            <code>args</code>
	 * @param args
	 *            Array of arguments for the event. The size must be equal to
	 *            the size of <code>eventMask</code>
	 * @return The first binding (value) for the quantified variable or
	 *         <code>null</code> if the array <code>eventMask</code> does not
	 *         contain any mark for the quantified variable
	 */
	protected Object getFirstQVarBinding(boolean[] eventMask, Object[] args) {

		for (int i = 0; i < eventMask.length; i++) {
			if (eventMask[i]) {
				return args[i];
			}
		}
		return null;
	}

}
