package qea.monitoring.impl.configs;

import java.util.Arrays;

import qea.monitoring.intf.Configuration;
import qea.structure.impl.other.FBindingImpl;
import qea.structure.intf.Binding;

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

	public NonDetConfig(int initialState, Binding binding) {
		states = new int[1];
		states[0] = initialState;

		bindings = new FBindingImpl[1];
		bindings[0] = binding;
	}

	public NonDetConfig(int[] states, Binding[] bindings) {
		this.states = states;
		this.bindings = bindings;
	}

	public NonDetConfig copy() {

		int[] statesCopy = new int[states.length];
		System.arraycopy(states, 0, statesCopy, 0, states.length);

		Binding[] bindingsCopy = null;
		if (bindings != null) {
			bindingsCopy = new Binding[bindings.length];
			for (int i = 0; i < bindings.length; i++) {
				bindingsCopy[i] = bindings[i].copy();
			}
		}

		return new NonDetConfig(statesCopy, bindingsCopy);
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
		String[] out = new String[states.length];
		for (int i = 0; i < states.length; i++) {
			String b = "[]";
			if (bindings != null) {
				b = bindings[i].toString();
			}
			out[i] = "(" + states[i] + "," + b + ")";
		}
		return Arrays.toString(out);
	}
}
