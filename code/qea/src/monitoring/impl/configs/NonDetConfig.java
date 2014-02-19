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

	/*
	 * Override toString to print out config
	 */
	@Override
	public String toString(){
		// currently ignores bindings
		return Arrays.toString(states);
	}
}
