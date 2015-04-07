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
	
	public final NonDetConfig extending;

	/**
	 * Creates a non-deterministic configuration with the initial state
	 */
	public NonDetConfig(int initialState,NonDetConfig extending) {
		states = new int[1];
		states[0] = initialState;
		//System.err.println("Created with "+extending);
		if(extending==null) this.extending = new NonDetConfig(initialState,this); 
		else this.extending = extending;
	}

	public NonDetConfig(int initialState, Binding binding, NonDetConfig extending) {
		states = new int[1];
		states[0] = initialState;

		bindings = new FBindingImpl[1];
		bindings[0] = binding;
		//System.err.println("Created with "+extending);
		if(extending==null) this.extending = new NonDetConfig(initialState,binding,this); 
		else this.extending = extending;
	}

	public NonDetConfig(int[] states, Binding[] bindings, NonDetConfig extending) {
		this.states = states;
		this.bindings = bindings;
		//System.err.println("Created with "+extending);
		if(extending==null) this.extending = new NonDetConfig(states,bindings,this); 
		else this.extending = extending;
	}

	public NonDetConfig copyForExtension(){ return copy(this);}
	public NonDetConfig copyForLocal(){ return copy(extending);}
	
	private NonDetConfig copy(NonDetConfig use_extending) {

		int[] statesCopy = new int[states.length];
		System.arraycopy(states, 0, statesCopy, 0, states.length);

		Binding[] bindingsCopy = null;
		if (bindings != null) {
			bindingsCopy = new Binding[bindings.length];
			for (int i = 0; i < bindings.length; i++) {
				bindingsCopy[i] = bindings[i].copy();
			}
		}

		NonDetConfig c = new NonDetConfig(statesCopy, bindingsCopy,use_extending);
		//System.err.println("Created "+c+" as a copy of "+this);
		return c;
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
		return Arrays.toString(out);//+" @"+System.identityHashCode(this);
	}
	
	/**
	 * Override equals
	 */
	@Override
	public boolean equals(Object other){
		if(other instanceof NonDetConfig){
			NonDetConfig other_config = (NonDetConfig) other;
			if(other_config.states.length!=states.length) return false;
			for(int i=0;i<states.length;i++) if(states[i]!=other_config.states[i]) return false;
			for(int i=0;i<bindings.length;i++){
				if(bindings[i]==null){
					if(other_config.bindings[i]!=null) return false;
				}
				else if(other_config.bindings[i]==null){
					if(bindings[i]!=null) return false;
				}
				else if(!bindings[i].equals(other_config.bindings[i])) return false;
			}
			return true;
		}
		return false;
	}
	/**
	 * Override hash
	 */
	@Override
	public int hashCode(){
		int code = 0;
		for(int i=0;i<states.length;i++) code += 100*states[i];
		for(int i=0;i<bindings.length;i++) if(bindings[i]!=null) code += bindings[i].hashCode();
		return code;
	}

	public boolean hasReturned() {		
		boolean result = this.equals(extending);
		//if(result) System.err.println("hasReturned true with "+this+" and "+extending+" note that "+(this==extending));
		return result;
	}

	@Override
	public boolean isZero() {
		// This is probably assuming a lot about how NonDetConfig works
		return states[0]==0;
	}
}
