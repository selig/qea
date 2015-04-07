package qea.monitoring.impl.translators;

import qea.monitoring.intf.Monitor;
import qea.structure.impl.other.Verdict;

/**
 * Provides the base implementation for a translator that translates the generic
 * events of a trace to the events and parameters required by a specific
 * property monitor
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public abstract class OfflineTranslator {

	/**
	 * Monitor processing the translated events
	 */
	protected Monitor monitor;

	/**
	 * Determines if the specified event is applicable to the property
	 * associated to the monitor of this translator, in which case translates
	 * the event names and parameters as required, invokes the <code>step</code>
	 * method of the monitor and returns the verdict.
	 * 
	 * @param eventName
	 *            Name of the event
	 * @param paramNames
	 *            Array of parameter names
	 * @param paramValues
	 *            Array of parameter values
	 * @return Verdict after processing the specified event or <code>null</code>
	 *         if the event is not relevant for the property associated to the
	 *         monitor of this translator
	 */
	public abstract Verdict translateAndStep(String eventName,
			String[] paramNames, String[] paramValues);

	/**
	 * Determines if the specified event without parameters is applicable to the
	 * property associated to the monitor of this translator, in which case
	 * translates the event name as required, invokes the <code>step</code>
	 * method of the monitor and returns the verdict.
	 * 
	 * @param eventName
	 *            Name of the event
	 * @return Verdict after processing the specified event or <code>null</code>
	 *         if the event is not relevant for the property associated to the
	 *         monitor of this translator
	 */
	public abstract Verdict translateAndStep(String eventName);

	/**
	 * Sets the monitor for this translator. Once events are translated, they
	 * will be passed to the specified monitor to be processed
	 * 
	 * @param monitor
	 *            Monitor that will process the events translated by this
	 *            translator
	 */
	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	/**
	 * Returns the monitor of this translator
	 * 
	 * @return The monitor of this translator
	 */
	public Monitor getMonitor() {
		return monitor;
	}

	/*
	 * Until we fully debug the symbol-based version sometimes we need to use naive
	 */
	private boolean use_naive=false;
	public void setNaive(){ use_naive=true;}
	public boolean useNaive() {
		return use_naive;
	}
}
