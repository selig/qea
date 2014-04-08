package monitoring.impl;

public enum RestartMode {
	NONE, REMOVE, ROLLBACK;

	public boolean on() {
		if(this==NONE) return false;
		return true;
	}
}
