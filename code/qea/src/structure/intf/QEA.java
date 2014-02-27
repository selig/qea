package structure.intf;

/*
 * A QEA object is a specification
 */

public interface QEA {

	/**
	 * Retrieves the set of states Q for this QEA
	 * 
	 * @return Array of integers with the names of the states
	 */
	public int[] getStates();

	/**
	 * Retrieves the alphabet of events A for this QEA
	 * 
	 * @return Array of integers with the names of the events
	 */
	public int[] getEventsAlphabet();

	/**
	 * Returns the quantification list in order. Variables are positive if
	 * existentially quantified and negative if universally quantified.
	 * 
	 * @return Returns Lambda
	 */
	public int[] getLambda();

	/**
	 * Checks if the QEA is deterministic
	 * 
	 * @return True if the QEA is deterministic, false otherwise
	 */
	public boolean isDeterministic();

	/**
	 * Checks if the QEA uses free variables
	 * 
	 * @return True if the QEA is uses free variables, false otherwise
	 */
	public boolean usesFreeVariables();

	/**
	 * Determines if the specified state is in the set of final states
	 * 
	 * @param state
	 * @return true if the specified state is a final state. Otherwise, false
	 */
	public boolean isStateFinal(int state);
}
