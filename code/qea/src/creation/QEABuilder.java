package creation;

import static structure.impl.Quantification.EXISTS;
import static structure.impl.Quantification.FORALL;
import static structure.impl.Quantification.NONE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import structure.impl.QVar1_FVar_Det_FixedQVar_QEA;
import structure.impl.QVar01_FVar_Det_QEA;
import structure.impl.QVar1_FVar_NonDet_FixedQVar_QEA;
import structure.impl.QVar01_FVar_NonDet_QEA;
import structure.impl.QVar01_NoFVar_Det_QEA;
import structure.impl.QVar01_NoFVar_NonDet_QEA;
import structure.impl.Quantification;
import structure.impl.TransitionImpl;
import structure.intf.Assignment;
import structure.intf.Guard;
import structure.intf.QEA;
import structure.intf.Transition;
import exceptions.ShouldNotHappenException;

/*
 * The general idea of this class is to allow us to build a QEA without knowing which
 * implementation it has. 
 * 
 * Usage:
 *   create a builder
 *   add stuff to it
 *   call make
 * 
 * This could be used along with a parser to parse some textual representation
 * of QEA, as we can add different kinds of transitions straightforwardly. However, I'm not sure
 * how guards and assignments would work in this case. 
 */

public class QEABuilder {

	/*
	 * To create a QEA we need - a set of transitions - a set of final stats - a
	 * quanfication list
	 * 
	 * We use internal representations
	 */
	private static class VarOrVal {
		int var = 0;
		Object val;
	}

	private static class TempTransition {
		int start, end = -1;
		int event_name = -1;
		VarOrVal[] event_args;
		Guard g;
		Assignment a;

		public int[] var_args() {
			int[] vargs = new int[event_args.length];
			for (int i = 0; i < event_args.length; i++) {
				vargs[i] = event_args[i].var;
				if (vargs[i] == 0) {
					throw new RuntimeException(
							"Do not yet support values in transitions");
				}
			}
			return vargs;
		}

	}

	private static class Quant {
		boolean universal;
		int var = -1;
		Guard g;
	}

	Set<TempTransition> transitions = new HashSet<TempTransition>();
	Set<Integer> finalstates = new HashSet<Integer>();
	List<Quant> quants = new ArrayList<Quant>();

	/*
	 * It will be good to give the qea a name
	 */
	String name;

	public QEABuilder(String name) {
		this.name = name;
	}

	public QEA make() {
		assert (wellFormed());

		// first get number of states and events
		int states = countStates();
		int events = countEvents();

		// Propositional first
		if (quants.size() == 0) {
			return makeProp(states, events);
		}
		// Single quantifier next
		if (quants.size() == 1) {
			return makeSingle(states, events);
		}

		throw new ShouldNotHappenException(
				"I don't know how to make that kind of QEA yet");
	}

	private QEA makeProp(int states, int events) {
		if (noFreeVariables()) {
			if (isEventDeterministic()) {
				QVar01_NoFVar_Det_QEA qea = new QVar01_NoFVar_Det_QEA(states,
						events, 1, NONE);
				for (TempTransition t : transitions) {
					qea.addTransition(t.start, t.event_name, t.end);
				}
				for (Integer s : finalstates) {
					qea.setStateAsFinal(s);
				}
				return qea;
			} else {
				QVar01_NoFVar_NonDet_QEA qea = new QVar01_NoFVar_NonDet_QEA(
						states, events, 1, NONE);
				Map<Integer, Set<Integer>>[] actual_trans = new Map[states];
				for (TempTransition t : transitions) {
					Map<Integer, Set<Integer>> this_state = actual_trans[t.start];
					if (this_state == null) {
						actual_trans[t.start] = new HashMap<Integer, Set<Integer>>();
					}
					this_state = actual_trans[t.start];

					Set<Integer> nextstates = this_state.get(t.event_name);
					if (nextstates == null) {
						nextstates = new HashSet<Integer>();
						this_state.put(t.event_name, nextstates);
					}
					nextstates.add(t.end);
				}
				for (int i = 0; i < actual_trans.length; i++) {
					if (actual_trans[i] != null) {
						for (Map.Entry<Integer, Set<Integer>> entry : actual_trans[i]
								.entrySet()) {
							int[] nextstates = new int[entry.getValue().size()];
							int j = 0;
							for (Integer s : entry.getValue()) {
								nextstates[j] = s;
								j++;
							}
							qea.addTransitions(i, (entry.getKey()), nextstates);
						}
					}
				}
				for (Integer s : finalstates) {
					qea.setStateAsFinal(s);
				}
				return qea;
			}
		} else {
			if (isEventDeterministic()) {

			} else {

			}
		}
		throw new ShouldNotHappenException(
				"I don't know how to make that kind of QEA yet");
	}

	private QEA makeSingle(int states, int events) {
		Quantification q;
		if (quants.get(0).universal) {
			q = FORALL;
		} else {
			q = EXISTS;
		}

		if (noFreeVariables()) {
			if (isEventDeterministic()) {
				QVar01_NoFVar_Det_QEA qea = new QVar01_NoFVar_Det_QEA(states,
						events, 1, q);
				for (TempTransition t : transitions) {
					qea.addTransition(t.start, t.event_name, t.end);
				}
				for (Integer s : finalstates) {
					qea.setStateAsFinal(s);
				}
				return qea;
			} else {
				QVar01_NoFVar_NonDet_QEA qea = new QVar01_NoFVar_NonDet_QEA(
						states, events, 1, q);
				Map<Integer, Set<Integer>>[] actual_trans = new Map[states];
				for (TempTransition t : transitions) {
					Map<Integer, Set<Integer>> this_state = actual_trans[t.start];
					if (this_state == null) {
						actual_trans[t.start] = new HashMap<Integer, Set<Integer>>();
					}
					this_state = actual_trans[t.start];

					Set<Integer> nextstates = this_state.get(t.event_name);
					if (nextstates == null) {
						nextstates = new HashSet<Integer>();
						this_state.put(t.event_name, nextstates);
					}
					nextstates.add(t.end);
				}
				for (int i = 0; i < actual_trans.length; i++) {
					if (actual_trans[i] != null) {
						for (Map.Entry<Integer, Set<Integer>> entry : actual_trans[i]
								.entrySet()) {
							int[] nextstates = new int[entry.getValue().size()];
							int j = 0;
							for (Integer s : entry.getValue()) {
								nextstates[j] = s;
								j++;
							}
							qea.addTransitions(i, (entry.getKey()), nextstates);
						}
					}
				}
				for (Integer s : finalstates) {
					qea.setStateAsFinal(s);
				}
				return qea;
			}
		} else {
			int frees = countFreeVars();
			if (fixedQVar()) {
				if (isEventDeterministic()) {
					QVar1_FVar_Det_FixedQVar_QEA qea = new QVar1_FVar_Det_FixedQVar_QEA(
							states, events, 1, q, frees);

					for (TempTransition t : transitions) {
						Transition trans = new TransitionImpl(t.var_args(),
								t.end);
						qea.addTransition(t.start, t.event_name, trans);
					}
					for (Integer s : finalstates) {
						qea.setStateAsFinal(s);
					}

					return qea;
				} else {
					QVar1_FVar_NonDet_FixedQVar_QEA qea = new QVar1_FVar_NonDet_FixedQVar_QEA(
							states, events, 1, q, frees);
					Map<Integer, Set<TransitionImpl>>[] actual_trans = new Map[states];
					for (TempTransition t : transitions) {
						Map<Integer, Set<TransitionImpl>> this_state = actual_trans[t.start];
						if (this_state == null) {
							actual_trans[t.start] = new HashMap<Integer, Set<TransitionImpl>>();
						}
						this_state = actual_trans[t.start];

						Set<TransitionImpl> nextstates = this_state
								.get(t.event_name);
						if (nextstates == null) {
							nextstates = new HashSet<TransitionImpl>();
							this_state.put(t.event_name, nextstates);
						}
						nextstates.add(new TransitionImpl(t.var_args(), t.end));
					}
					for (int i = 0; i < actual_trans.length; i++) {
						if (actual_trans[i] != null) {
							for (Map.Entry<Integer, Set<TransitionImpl>> entry : actual_trans[i]
									.entrySet()) {
								TransitionImpl[] nextstates = new TransitionImpl[entry
										.getValue().size()];
								int j = 0;
								for (TransitionImpl s : entry.getValue()) {
									nextstates[j] = s;
									j++;
								}
								qea.addTransitions(i, (entry.getKey()),
										nextstates);
							}
						}
					}
					for (Integer s : finalstates) {
						qea.setStateAsFinal(s);
					}
					return qea;
				}
			} else {
				if (isEventDeterministic()) {
					QVar01_FVar_Det_QEA qea = new QVar01_FVar_Det_QEA(states,
							events, 1, q, frees);

					for (TempTransition t : transitions) {
						Transition trans = new TransitionImpl(t.var_args(),
								t.end);
						((TransitionImpl) trans).setAssignment(t.a);
						((TransitionImpl) trans).setGuard(t.g);
						qea.addTransition(t.start, t.event_name, trans);
					}
					for (Integer s : finalstates) {
						qea.setStateAsFinal(s);
					}

					return qea;
				} else {
					QVar01_FVar_NonDet_QEA qea = new QVar01_FVar_NonDet_QEA(
							states, events, 1, q, frees);
					Map<Integer, Set<TransitionImpl>>[] actual_trans = new Map[states];
					for (TempTransition t : transitions) {
						Map<Integer, Set<TransitionImpl>> this_state = actual_trans[t.start];
						if (this_state == null) {
							actual_trans[t.start] = new HashMap<Integer, Set<TransitionImpl>>();
						}
						this_state = actual_trans[t.start];

						Set<TransitionImpl> nextstates = this_state
								.get(t.event_name);
						if (nextstates == null) {
							nextstates = new HashSet<TransitionImpl>();
							this_state.put(t.event_name, nextstates);
						}
						nextstates.add(new TransitionImpl(t.var_args(), t.end));
					}
					for (int i = 0; i < actual_trans.length; i++) {
						if (actual_trans[i] != null) {
							for (Map.Entry<Integer, Set<TransitionImpl>> entry : actual_trans[i]
									.entrySet()) {
								TransitionImpl[] nextstates = new TransitionImpl[entry
										.getValue().size()];
								int j = 0;
								for (TransitionImpl s : entry.getValue()) {
									nextstates[j] = s;
									j++;
								}
								qea.addTransitions(i, (entry.getKey()),
										nextstates);
							}
						}
					}
					for (Integer s : finalstates) {
						qea.setStateAsFinal(s);
					}
					return qea;
				}
			}
		}
	}

	private int countStates() {
		Set<Integer> states = new HashSet<Integer>();
		for (TempTransition t : transitions) {
			states.add(t.start);
			states.add(t.end);
		}
		return states.size();
	}

	private int countEvents() {
		Set<Integer> events = new HashSet<Integer>();
		for (TempTransition t : transitions) {
			events.add(t.event_name);
		}
		return events.size();
	}

	private int countFreeVars() {
		Set<Integer> qvars = new HashSet<Integer>();
		for (Quant q : quants) {
			qvars.add(q.var);
		}
		Set<Integer> fvars = new HashSet<Integer>();
		for (TempTransition t : transitions) {
			if (t.event_args != null) {
				for (int i = 0; i < t.event_args.length; i++) {
					int var = t.event_args[i].var;
					if (var != 0 && !qvars.contains(var)) {
						fvars.add(var);
					}
				}
			}
		}
		return fvars.size();
	}

	/*
	 * Check that all transitions have a start, end and event name And that all
	 * quantifications have a var
	 */
	private boolean wellFormed() {
		for (TempTransition trans : transitions) {
			if (trans.start == -1) {
				return false;
			}
			if (trans.end == -1) {
				return false;
			}
			if (trans.event_name == -1) {
				return false;
			}
			if (trans.event_args != null) {
				for (int i = 0; i < trans.event_args.length; i++) {
					if (trans.event_args[i] == null
							|| (trans.event_args[i].val == null && trans.event_args[i].var == -1)) {
						return false;
					}
				}
			}
		}
		for (Quant q : quants) {
			if (q.var == -1) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Get the quantified variables Check that all transitions that have
	 * arguments either use a value or a quantified variable
	 */
	private boolean noFreeVariables() {
		Set<Integer> qvars = new HashSet<Integer>();
		for (Quant q : quants) {
			qvars.add(q.var);
		}

		for (TempTransition t : transitions) {
			if (t.event_args != null) {
				for (int i = 0; i < t.event_args.length; i++) {
					if (t.event_args[i].val == null
							&& !(qvars.contains(t.event_args[i].var))) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/*
	 * Check that for each state and event name there is a single transition
	 */
	private boolean isEventDeterministic() {

		Map<Integer, Integer> start_trans = new HashMap<Integer, Integer>();
		for (TempTransition t : transitions) {
			if (start_trans.put(t.start, t.event_name) != null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if there is a single quantified variable appearing only in the
	 * first position of the events with parameters
	 * 
	 * @return <code>true</code> if the QEA of this builder meets the conditions
	 *         for the FixedQVar optimisation; <code>false</code> otherwise
	 */
	private boolean fixedQVar() {
		if (quants.size() != 1) {
			return false;
		}
		int var = quants.get(0).var;

		// Iterate over all the transitions
		for (TempTransition t : transitions) {

			if (t.event_args != null && t.event_args.length > 0) {

				// Check the quantified variable is in the first position
				if (t.event_args[0].var != var) {
					return false;
				}

				// Check starting from the 2nd position there are only free vars
				for (int i = 1; i < t.event_args.length; i++) {
					if (t.event_args[i].var == var) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/*
	 * CREATING QEA
	 */

	// add a propositional transition
	public void addTransition(int start, int propname, int end) {
		TempTransition trans = new TempTransition();
		trans.start = start;
		trans.end = end;
		trans.event_name = propname;
		transitions.add(trans);
	}

	// add a variable only transition
	public void addTransition(int start, int propname, int[] vargs, int end) {
		TempTransition trans = new TempTransition();
		trans.start = start;
		trans.end = end;
		trans.event_name = propname;
		VarOrVal[] args = new VarOrVal[vargs.length];
		for (int i = 0; i < vargs.length; i++) {
			args[i] = new VarOrVal();
			args[i].var = vargs[i];
		}
		trans.event_args = args;
		transitions.add(trans);
	}

	// incremental transition adding

	TempTransition inctrans = null;
	List<VarOrVal> incargs = null;

	public void startTransition(int start) {
		inctrans = new TempTransition();
		inctrans.start = start;
		incargs = new ArrayList<VarOrVal>();
	}

	public void eventName(int eventName) {
		inctrans.event_name = eventName;
	}

	public void addVarArg(int var) {
		VarOrVal v = new VarOrVal();
		v.var = var;
		incargs.add(v);
	}

	public void addValArg(Object val) {
		VarOrVal v = new VarOrVal();
		v.val = val;
		incargs.add(v);
	}

	public void addGuard(Guard g) {
		assert (inctrans.g == null);
		inctrans.g = g;
	}

	public void addAssignment(Assignment a) {
		assert (inctrans.a == null);
		inctrans.a = a;
	}

	public void endTransition(int end) {
		inctrans.end = end;
		if (incargs.size() > 0) {
			VarOrVal[] args = new VarOrVal[incargs.size()];
			incargs.toArray(args);
			inctrans.event_args = args;
		}
		transitions.add(inctrans);
		inctrans = null;
	}

	public void addQuantification(Quantification q, int var) {
		Quant quant = new Quant();
		quant.universal = (q == FORALL);
		quant.var = var;
		quants.add(quant);
	}

	public void addQuantification(Quantification q, int var, Guard g) {
		Quant quant = new Quant();
		quant.universal = (q == FORALL);
		quant.var = var;
		quant.g = g;
		quants.add(quant);
	}

	public void addFinalStates(int... states) {
		for (int i = 0; i < states.length; i++) {
			finalstates.add(states[i]);
		}
	}

}
