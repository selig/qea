package monitoring.impl;

public enum RestartMode {
	NONE, REMOVE, ROLLBACK, IGNORE;

	public boolean on() {
		if(this==NONE) return false;
		return true;
	}
}
