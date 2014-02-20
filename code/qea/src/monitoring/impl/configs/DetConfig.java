package monitoring.impl.configs;

import monitoring.intf.Configuration;
import structure.intf.Binding;

/**
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public class DetConfig implements Configuration {

	private int state;

	private Binding binding;

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

}
