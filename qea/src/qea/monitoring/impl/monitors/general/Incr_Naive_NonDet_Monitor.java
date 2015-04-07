package qea.monitoring.impl.monitors.general;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import qea.monitoring.impl.configs.NonDetConfig;
import qea.structure.impl.other.QBindingImpl;
import qea.structure.impl.qeas.QVarN_NonDet_QEA;
import qea.exceptions.NotRelevantException;

public class Incr_Naive_NonDet_Monitor extends
		Abstr_Incr_Naive_QEAMonitor<QVarN_NonDet_QEA> {

	private final HashMap<QBindingImpl, NonDetConfig> mapping = new HashMap<QBindingImpl, NonDetConfig>();
	private final TreeSet<QBindingImpl> B = new TreeSet<QBindingImpl>(
			new QBindingImpl.QBindingImplComparator());

	public Incr_Naive_NonDet_Monitor(QVarN_NonDet_QEA qea) {
		super(qea);

		NonDetConfig initialConfig = new NonDetConfig(qea.getInitialState(),
				qea.newFBinding(), null);
		;
		mapping.put(bottom, initialConfig);
		B.add(bottom);
	}

	@Override
	protected void innerStep(int eventName, QBindingImpl[] qbindings,
			Object[] args) {

		TreeSet<QBindingImpl> B_ = new TreeSet<QBindingImpl>(
				new QBindingImpl.QBindingImplComparator());
		for (QBindingImpl b : B) {
			//if(DEBUG) System.err.println("Consider extending "+b);
			Set<QBindingImpl> consistent = null;
			for (QBindingImpl other : qbindings) {
				if (b.consistentWith(other)) {
					if (consistent == null) {
						consistent = new HashSet<QBindingImpl>();
					}
					QBindingImpl updated = other.updateWith(b);
					//if(DEBUG) System.err.println("Add "+updated+" to consistent from"+other);
					consistent.add(updated);
				}
				//else if(DEBUG) System.err.println("Not consistent: "+other);
			}

			if (consistent != null) {
				for (QBindingImpl b_extended : consistent) {

					NonDetConfig config = mapping.get(b_extended);

					if (config == null || b.equals(b_extended)) {

						// The qea updates the config, so we should copy if
						// extending
						if (config == null) {
							config = mapping.get(b).copyForExtension();
							B_.add(b_extended);
							if(b_extended.isTotal()){
								checker.newBinding(b_extended, config.getStates());
							}
						}

						int[] previous_states = config.getStates();// safe as
																	// nondet
																	// will
																	// update
						try {
							NonDetConfig next_config = qea.getNextConfig(
									b_extended, config, eventName, args);
							mapping.put(b_extended, next_config);

							//System.err.println("For "+b_extended+" : "+config+" -> "+next_config);
							
							if (b_extended.isTotal()) {
								checker.update(b_extended, previous_states,
										next_config.getStates());
							}
						} catch (NotRelevantException e) {
							if (!qea.isNormal()) {
								mapping.put(b_extended, config);

								if (b_extended.isTotal()) {
									checker.update(b_extended, previous_states,
											config.getStates());
								}
							}
						}

					}

				}
			}
		}
		B.addAll(B_);
	}

	@Override
	public String getStatus() {
		return checker.toString();
		/*
		String ret = "mapping:\n";
		for (Map.Entry<QBindingImpl, NonDetConfig> entry : mapping.entrySet()) {
			ret += entry.getKey() + "\t" + entry.getValue() + "\n";
		}
		ret += checker;
		return ret;
		*/
	}

}
