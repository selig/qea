package structure.intf;

public interface Guard {

	
	/**
	 * Checking the guard
	 * 
	 * @return true if guard evaluates to true on binding
	 */	
	public boolean check(Binding binding);
}
