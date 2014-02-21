package structure.impl;

import monitoring.impl.configs.NonDetConfig;


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
public class SimpleNonDetQEA extends SimpleQEA {

	private int[][][] delta;

	public SimpleNonDetQEA(int numStates, int numEvents, int initialState,
			Quantification quantification) {
		super(numStates, initialState, quantification);
		delta = new int[numStates + 1][numEvents + 1][];

	}

	/**
	 * Adds a transition to the transition function delta of this QEA
	 * 
	 * @param startState
	 *            Start state for the transition
	 * @param event
	 *            Name of the event
	 * @param endState
	 *            End state for the transition
	 */
	public void addTransition(int startState, int event, int endState) {
		delta[startState][event] = new int[] { endState };
	}

	/**
	 * Adds a set of transitions to the transition function delta of this QEA
	 * 
	 * @param startState
	 *            Start state for the transition
	 * @param event
	 *            Name of the event
	 * @param endStates
	 *            Array of end states for the transition
	 */
	public void addTransitions(int startState, int event, int[] endStates) {
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
					int newEndState = intermEndStates[j];
					if (!endStatesBool[newEndState]) {
						endStatesBool[newEndState] = true;
						endStatesCount++;
					}
				}
			}

			// System.out.println("endStatesBool: "+java.util.Arrays.toString(endStatesBool));

			// Remove state 0 if there are other end states
			// Because state 0 is the failure state, and can be safely removed
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

	@Override
	public int[] getEventsAlphabet() {
		int[] a = new int[delta[0].length - 1];
		for (int i = 0; i < a.length; i++) {
			a[i] = i + 1;
		}
		return a;
	}

	@Override
	public boolean isDeterministic() {
		return false;
	}

}
