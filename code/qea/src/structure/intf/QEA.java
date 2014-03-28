package structure.intf;

import java.util.HashMap;
import java.util.Map;

import structure.impl.qeas.QEAType;

/*
 * A QEA object is a specification
 */

public abstract class QEA {

	private String name;

	public QEA() {
		name = "No-name";
	}

	public QEA(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String name() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Retrieves the set of states Q for this QEA
	 * 
	 * @return Array of integers with the names of the states
	 */
	public abstract int[] getStates();

	/**
	 * Retrieves the alphabet of events A for this QEA
	 * 
	 * @return Array of integers with the names of the events
	 */
	public abstract int[] getEventsAlphabet();

	/**
	 * Returns the quantification list in order. Variables are positive if
	 * existentially quantified and negative if universally quantified.
	 * 
	 * @return Returns Lambda
	 */
	public abstract int[] getLambda();

	/**
	 * Checks if the QEA is deterministic
	 * 
	 * @return True if the QEA is deterministic, false otherwise
	 */
	public abstract boolean isDeterministic();

	/**
	 * Checks if the QEA uses free variables
	 * 
	 * @return True if the QEA is uses free variables, false otherwise
	 */
	public abstract boolean usesFreeVariables();

	/**
	 * Determines if the specified state is in the set of final states
	 * 
	 * @param state
	 * @return true if the specified state is a final state. Otherwise, false
	 */
	public abstract boolean isStateFinal(int state);

	/**
	 * Determines if the specified state is in the set of strong states
	 * 
	 * @param state
	 * @return true if the specified state is a strong state. Otherwise, false
	 */
	public abstract boolean isStateStrong(int state);

	/**
	 * Returns the ID for the type of this QEA
	 * 
	 * @return {@link QEAType}
	 */
	public abstract QEAType getQEAType();

	Map<String, Integer> name_lookup;

	/**
	 * Records a name for an event id
	 * 
	 * @param name
	 * @param id
	 */
	public void record_event_name(String name, int id) {
		if (name_lookup == null) {
			name_lookup = new HashMap<String, Integer>();
		}
		name_lookup.put(name, id);
	}

	/**
	 * Gets the event id for a name
	 * 
	 * @param name
	 * @return id
	 */
	public int get_event_id(String name) {
		Integer id = name_lookup.get(name);
		if (id == null) {
			return -1;
		}
		return id;
	}

}
