package qea.monitoring.impl.configs;

import qea.monitoring.intf.Configuration;
import qea.structure.intf.Binding;

/**
 * 
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class DetConfig implements Configuration {

	private int state;

	private Binding binding;

	/**
	 * Creates a deterministic configuration with the specified initial state.
	 * 
	 * @param initialState
	 */
	public DetConfig(int initialState) {
		state = initialState;
	}

	public DetConfig(int state, Binding binding) {
		this.state = state;
		this.binding = binding;
	}

	public DetConfig copy() {
		return new DetConfig(state, binding.copy());
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public Binding getBinding() {
		return binding;
	}

	public void setBinding(Binding binding) {
		this.binding = binding;
	}

	/**
	 * Override toString to print out config
	 */
	@Override
	public String toString() {
		return "(" + state + "," + binding + ")";
	}

}
