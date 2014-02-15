package monitoring.impl;

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
	 * Creates a non-deterministic configuration with the default state
	 */
	public NonDetConfig() {
		states = new int[1];
		states[0] = 1;
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

}
