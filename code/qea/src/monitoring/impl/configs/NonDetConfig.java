package monitoring.impl.configs;

import java.util.Arrays;

import monitoring.intf.Configuration;
import structure.intf.Binding;

/**
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class NonDetConfig implements Configuration {

	private int[] states;

	private Binding[] bindings;

	/**
	 * Creates a non-deterministic configuration with the initial state
	 */
	public NonDetConfig(int initialState) {
		states = new int[1];
		states[0] = initialState;
	}

	public int[] getStates() {
		return states;
	}

	public Binding[] getBindings() {
		return bindings;
	}

	public void setStates(int[] states) {
		this.states = states;
	}

	public void setBindings(Binding[] bindings) {
		this.bindings = bindings;
	}

	/**
	 * Replaces the state at the specified position in the array of states
	 * 
	 * @param index
	 *            Index of the state to replace
	 * @param state
	 *            State to be stored at the specified position
	 */
	public void setState(int index, int state) {
		states[index] = state;
	}

	/**
	 * Replaces the binding at the specified position in the array of bindings
	 * 
	 * @param index
	 *            Index of the binding to replace
	 * @param binding
	 *            Binding to be stored at the specified position
	 */
	public void setBinding(int index, Binding binding) {
		bindings[index] = binding;
	}

	/**
	 * Override toString to print out config
	 */
	@Override
	public String toString() {
		// currently ignores bindings
		return Arrays.toString(states);
	}
}
