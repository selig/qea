package monitoring.impl.monitors;

import monitoring.impl.IncrementalMonitor;
import structure.impl.NonSimpleQEA;

public abstract class IncrementalNonSimpleQEAMonitor<Q extends NonSimpleQEA>
		extends IncrementalMonitor<Q> {

	public IncrementalNonSimpleQEAMonitor(Q q) {
		super(q);
	}

}
