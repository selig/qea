package structure.impl;

import monitoring.impl.configs.NonDetConfig;
import structure.intf.QEA;

/**
 * This class represents a simple Quantified Event Automaton (QEA) with the
 * following characteristics:
 * <ul>
 * <li>There is at most one quantified variable
 * <li>The transitions in the function delta consist of a start state, an event
 * and a set of end states, no guards or assigns are considered
 * <li>The QEA can is non-deterministic
 * </ul>
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class SimpleNonDeterministicQEA implements QEA {

	private int[] finalStates;

	private int[][][] delta;

	protected final int initialState;

	private boolean quantificationUniversal;

	public SimpleNonDeterministicQEA(int numStates, int numEvents,
			int initialState, boolean quantificationUniversal) {
		delta = new int[numStates + 1][numEvents + 1][numStates + 1];
		finalStates = new int[numStates + 1];
		this.initialState = initialState;
		this.quantificationUniversal = quantificationUniversal;
	}

	/**
	 * Adds a transition for the transition function delta of this QEA
	 * 
	 * @param startState
	 *            Start state for this transition
	 * @param event
	 *            Name of the event
	 * @param endStates
	 *            Array of end states for this transition
	 */
	public void addTransition(int startState, int event, int[] endStates) {
		delta[startState][event] = endStates;
	}

	/**
	 * Retrieves the final configuration for a given start configuration and an
	 * event, according to the transition function delta of this QEA
	 * 
	 * @param config
	 *            Start configuration containing the set of start states
	 * @param event
	 *            Name of the event
	 * @return End configuration containing the set of end states
	 */
	public NonDetConfig getNextStates(NonDetConfig config, int event) {

		if (config.getStates().length == 1) { // Only one state in the start
												// configuration
			config.setStates(delta[config.getStates()[0]][event]);

		} else { // More than one state in the start configuration

			// Get a reference to the start states
			int[] startStates = config.getStates();

			// Create a boolean array of size equalto the number of states
			boolean[] endStatesBool = new boolean[delta.length];

			// Initialise end states count
			int endStatesCount = 0;

			// Iterate over the multiple arrays of end states
			for (int i = 0; i < startStates.length; i++) {

				int[] intermEndStates = delta[startStates[i]][event];

				// Iterate over the intermediate arrays of end states
				for (int j = 0; j < intermEndStates.length; j++) {
					if (!endStatesBool[j]) {
						endStatesBool[j] = true;
						endStatesCount++;
					}
				}
			}

			// Remove state 0 if there are other end states
			if (endStatesBool[0] && endStatesCount > 1) {
				endStatesBool[0] = false;
				endStatesCount--;
			}

			int[] endStates;

			// Check if the number of end states is the same as start states
			if (endStatesCount == startStates.length) { // Same size
				// Use the same array
				endStates = startStates;
			} else { // Different number of start states and end states
				// Create a new array with the right size
				endStates = new int[endStatesCount];
			}

			// Populate array of end states
			int j = 0;
			for (int i = 0; i < endStatesBool.length; i++) {
				if (endStatesBool[i]) {
					endStates[j] = i;
					j++;
				}
			}

			config.setStates(endStates);
		}

		return config;
	}

	/**
	 * Determines if the set of states in the specified configuration contains
	 * at least one final state
	 * 
	 * @param config
	 *            Configuration encapsulating the set of states to be checked
	 * @return <code>true</code> if the set of states in the specified
	 *         configuration contains at least one final state;
	 *         <code>false</code> otherwise
	 */
	public boolean containsFinalState(NonDetConfig config) {
		for (int i = 0; i < config.getStates().length; i++) {
			if (isStateFinal(config.getStates()[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds the specified state to the set of final states
	 * 
	 * @param state
	 *            State name
	 */
	public void setStateAsFinal(int state) {
		finalStates[state] = 1;
	}

	/**
	 * Adds the specified states to the set of final statess
	 * 
	 * @param states
	 *            Names of states to add
	 */
	public void setStatesAsFinal(int... states) {
		for (int state : states)
			finalStates[state] = 1;
	}

	@Override
	public int[] getStates() {
		int[] q = new int[delta.length];
		for (int i = 0; i < q.length; i++) {
			q[i] = i + 1; // TODO Is this method returning one more state?
		}
		return q;
	}

	@Override
	public int[] getEventsAlphabet() {
		int[] a = new int[delta[0].length];
		for (int i = 0; i < a.length; i++) {
			a[i] = i + 1;
		}
		return a;
	}

	@Override
	public int[] getLambda() {
		if (quantificationUniversal) {
			return new int[] { 1 };
		}
		return new int[] { -1 };
	}

	@Override
	public boolean isDeterministic() {
		return false;
	}

	@Override
	public boolean usesFreeVariables() {
		return false;
	}

	@Override
	public boolean isStateFinal(int state) {
		if (finalStates[state] == 1) {
			return true;
		}
		return false;
	}

	public boolean isQuantificationUniversal() {
		return quantificationUniversal;
	}

}
