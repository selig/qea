package creation;

import static structure.impl.other.Quantification.EXISTS;
import static structure.impl.other.Quantification.FORALL;
import static structure.impl.other.Quantification.NONE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import structure.impl.other.Quantification;
import structure.impl.other.Transition;
import structure.impl.qeas.Abstr_QVarN_QEA;
import structure.impl.qeas.Abstr_QVarN_QEA.QEntry;
import structure.impl.qeas.QEAType;
import structure.impl.qeas.QVar01_FVar_Det_QEA;
import structure.impl.qeas.QVar01_FVar_NonDet_QEA;
import structure.impl.qeas.QVar01_NoFVar_Det_QEA;
import structure.impl.qeas.QVar01_NoFVar_NonDet_QEA;
import structure.impl.qeas.QVar1_FVar_Det_FixedQVar_QEA;
import structure.impl.qeas.QVar1_FVar_NonDet_FixedQVar_QEA;
import structure.impl.qeas.QVarN_FVar_Det_QEA;
import structure.impl.qeas.QVarN_FVar_NonDet_QEA;
import structure.intf.Assignment;
import structure.intf.Guard;
import structure.intf.QEA;
import structure.intf.QEA_det_free;
import structure.intf.QEA_nondet_free;
import structure.intf.QEA_single;
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

	private static final boolean DEBUG = false;

	/*
	 * To create a QEA we need - a set of transitions - a set of final stats - a
	 * quanfication list
	 * 
	 * We use internal representations
	 */
	private static class VarOrVal {
		static Map<Integer,VarOrVal> var_cache = new HashMap<Integer,VarOrVal>();
		static VarOrVal makeVar(int var){
			VarOrVal v = var_cache.get(var);
			if(v==null){
				v=new VarOrVal();
				v.var=var;
				var_cache.put(var,v);
			}
			return v;
		}
		static Map<Object,VarOrVal> val_cache = new HashMap<Object,VarOrVal>();
		static VarOrVal makeVal(Object val){
			VarOrVal v = val_cache.get(val);
			if(v==null){
				v=new VarOrVal();
				v.val=val;
				val_cache.put(val,v);
			}
			return v;
		}		
		static int nonvar = -1000;
		int var = nonvar;
		Object val;
		public String toString(){ return (val==null) ? "x_"+var : "v_"+val;}
	}

	private static class TempTransition {
		int start, end = -1;
		int event_name = -1;
		VarOrVal[] event_args;
		Guard g;
		Assignment a;

		TempEvent temp_event = null;

		public TempTransition(){}
		public TempTransition(int s, int e, Transition transition) {
			start=s;
			event_name=e;
			int[] vargs = transition.getVariableNames();
			event_args = new VarOrVal[vargs.length];
			for(int i=0;i<vargs.length;i++) 
				event_args[i] = VarOrVal.makeVar(vargs[i]);
			g = transition.getGuard();
			a = transition.getAssignment();
			end = transition.getEndState();
		}

		public TempTransition(int s, int n, int e) {
			start=s;
			event_name=n;
			end = e;
		}
		public int[] var_args() {
			if (event_args == null) {
				return new int[] {};
			}
			int[] vargs = new int[event_args.length];
			for (int i = 0; i < event_args.length; i++) {
				vargs[i] = event_args[i].var;
				if (vargs[i] == VarOrVal.nonvar) {
					throw new RuntimeException(
							"Do not yet support values in transitions");
				}
			}
			return vargs;
		}

		@Override
		public String toString() {
			return start + "-" + event_name + "(" + Arrays.toString(event_args)
					+ ")-" + end;
		}
	}

	private static class TempEvent {
		int event_name = -1;
		VarOrVal[] event_args = null;

		@Override
		public int hashCode() {
			return (event_name + Arrays.toString(event_args)).hashCode();
		}

		@Override
		public boolean equals(Object o) {
			// if(o==this) return true;
			if (o instanceof TempEvent) {
				TempEvent other = (TempEvent) o;
				if (other.event_name != event_name) {
					return false;
				}
				if (other.event_args == null && event_args == null) {
					return true;
				}
				if (other.event_args == null || event_args == null) {
					return false;
				}
				if (other.event_args.length != event_args.length) {
					return false;
				}
				for (int i = 0; i < event_args.length; i++) {
					if (other.event_args[i] != event_args[i]) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}

		@Override
		public String toString() {
			return event_name + "(" + Arrays.toString(event_args) + ")";
		}
	}

	private static class Quant {
		boolean universal;
		int var = 1;
		Guard g;
		public String toString(){
			return (universal ? "F" : "E")+var+(g==null ? "" : "g");
		}
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
		String error = wellFormed();
		if (error != null) {
			throw new ShouldNotHappenException(error);
		}
		if(inctrans!=null)
			throw new ShouldNotHappenException("Unclosed transition");		

		// first get number of states and events
		int states = countStates();
		int events = countEvents();

		QEA qea = null;
		
		// Propositional first
		if (quants.size() == 0) {
			qea = makeProp(states, events);
		}
		// Single quantifier next
		else if (quants.size() == 1) {
			qea = makeSingle(states, events);
		}
		else qea = makeGeneral(states,events);
		

		if (qea != null) {
			qea.setName(name);
			return qea;
		}

		throw new ShouldNotHappenException(
				"I don't know how to make that kind of QEA yet");
	}

	public QEA make(QEAType type) {
		String error = wellFormed();
		if (error != null) {
			throw new ShouldNotHappenException(error);
		}
		if(inctrans!=null)
			throw new ShouldNotHappenException("Unclosed transition");

		// first get number of states and events
		int states = countStates();
		int events = countEvents();		
		// next get other expected info
		boolean[] strong = computeStrongStates();
		int fvars = countFreeVars();
		boolean det = isEventDeterministic();
		boolean nofree = fvars==0;
		int qs = quants.size();
		Quantification[] qqs = new Quantification[qs];
		for(int i=0;i<qs;i++){
			Quant q = quants.get(i);
			qqs[i] = q.universal ? FORALL : EXISTS;
		}
		boolean ggs = usesGlobalGuards();
		
		switch(type){
			case QVAR01_FVAR_DET_QEA : 
				if(qs==0 && det && !ggs) return makePropFreeDet(states,events,strong);
				if(qs==1 && det && !ggs) return makeSingleFreeDet(states,events,strong,qqs[0],fvars);
				break;
			case QVAR01_FVAR_NONDET_QEA : 
				if(qs==0 && !ggs) return makePropFreeNondet(states,events,strong);
				if(qs==1 && !ggs) return makeSingleFreeNondet(states,events,strong,qqs[0],fvars);
				break;
			case QVAR01_NOFVAR_DET_QEA : 
				if(qs==0 && det && nofree && !ggs) return makePropNofreeDet(states,events,strong);
				if(qs==1 && det && nofree && !ggs) return makeSingleNofreeDet(states,events,strong,qqs[0]);
				break;
			case QVAR01_NOFVAR_NONDET_QEA : 
				if(qs==0 && nofree && !ggs) return makePropNofreeNondet(states,events,strong);
				if(qs==1 && nofree && !ggs) return makeSingleNofreeNondet(states,events,strong,qqs[0]);
				break;
			case QVAR1_FVAR_DET_FIXEDQVAR_QEA : 
				if(qs==1 && fixedQVar() && det && !ggs) return makeSingleFreeDetFixed(states,events,strong,qqs[0],fvars);
				break;
			case QVAR1_FVAR_NONDET_FIXEDQVAR_QEA : 
				if(qs==1 && fixedQVar() && !ggs) return makeSingleFreeNondetFixed(states,events,strong,qqs[0],fvars);
				break;
			case QVARN_FVAR_DET_QEA :
				if(det) return makeGeneralFreeDet(states,events,strong,fvars);
				break;
			case QVARN_FVAR_NONDET_QEA : 
				return makeGeneralFreeNondet(states,events,strong,fvars);			
		}

		throw new ShouldNotHappenException(
				"Could not make a QEA of that type");
	}

	public static QEA change(QEA qea, QEAType type){
		QEABuilder b = deconstruct(qea);
		QEA qea_new = b.make(type);
		// copy
		for(Map.Entry<String,Integer> entry : qea.get_name_lookup().entrySet())
			qea_new.record_event_name(entry.getKey(),entry.getValue());
		return qea_new;
	}
	
	public static QEABuilder deconstruct(QEA qea){
		QEABuilder q = new QEABuilder(qea.name());
		/*
		 * Sort out transitions first
		 */
		if(qea instanceof QEA_det_free){
			Transition[][] ts = ((QEA_det_free) qea).getDelta();
			for(int s = 1;s<ts.length;s++){
				for(int e = 1;e<ts[0].length;e++){
					if(ts[s][e]!=null && ts[s][e].getEndState()>0){
						TempTransition tt = new TempTransition(s,e,ts[s][e]);
						q.addTransition(tt);
					}
				}
			}				
		}
		else if(qea instanceof QEA_nondet_free){
			Transition[][][] ts = ((QEA_nondet_free) qea).getDelta();
			for(int s = 1;s<ts.length;s++){
				for(int e = 1;e<ts[0].length;e++){
					if(ts[s][e]!=null)
					for(Transition t : ts[s][e]){
						if(t!=null && t.getEndState()>0){
							TempTransition tt = new TempTransition(s,e,t);
							q.addTransition(tt);
						}
					}
				}
			}			
		}
		else if(qea instanceof QVar01_NoFVar_Det_QEA){
			int[][] ts = ((QVar01_NoFVar_Det_QEA) qea).getDelta();
			for(int s = 1;s<ts.length;s++){
				for(int e = 1;e<ts[0].length;e++){
					if(ts[s][e]>0){						
						TempTransition tt = new TempTransition(s,e,ts[s][e]);
						if(!((QVar01_NoFVar_Det_QEA) qea).isProp(e)){
							tt.event_args = new VarOrVal[]{VarOrVal.makeVar(-1)};
						}
						q.addTransition(tt);
					}
				}
			}			
		}
		else if(qea instanceof QVar01_NoFVar_NonDet_QEA){
			int[][][] ts = ((QVar01_NoFVar_NonDet_QEA) qea).getDelta();
			for(int s = 1;s<ts.length;s++){
				for(int e = 1;e<ts[0].length;e++){
					if(ts[s][e]!=null)
					for(int t : ts[s][e]){
						if(t>0){
							TempTransition tt = new TempTransition(s,e,t);
							if(!((QVar01_NoFVar_NonDet_QEA) qea).isProp(e)){
								tt.event_args = new VarOrVal[]{VarOrVal.makeVar(-1)};
							}							
							q.addTransition(tt);
						}
					}
				}
			}			
		}
		else 
			throw new ShouldNotHappenException("Cannot currently deconstruct qea of type "+qea.getClass());
		
		//for(TempTransition tt : q.transitions)
		//	System.err.println(tt);
		
		/*
		 * FinalStates
		 */
		for(int s : qea.getStates()){
			if(qea.isStateFinal(s)) q.addFinalStates(s);
		}
		
		/*
		 * Quantifications
		 */
		if(qea.getLambda().length==0){
			//do nothing
		}
		else if(qea.getLambda().length==1){
			if(((QEA_single) qea).isQuantificationUniversal())
				q.addQuantification(FORALL, -1);
			else
				q.addQuantification(EXISTS, -1);
		}
		else{
			try{
				QEntry[] entry = ((Abstr_QVarN_QEA) qea).getFullLambda();
				for(int i=1;i<entry.length;i++){
					q.addQuantification(entry[i].quantification,-i,entry[i].guard);
				}
			}
			catch(ClassCastException e){
				throw new ShouldNotHappenException("deconstruct needs to be expanded to account for "+qea.getClass());
			}
		}
		
		//for(Quant qq : q.quants)
		//	System.err.println(qq);
		
		return q;
	}
	
	private void addTransition(TempTransition tt) {
		transitions.add(tt);
	}

	public QEA makeMostGeneral() {
		String error = wellFormed();
		if (error != null) {
			throw new ShouldNotHappenException(error);
		}

		// first get number of states and events
		int states = countStates();
		int events = countEvents();

		return makeGeneral(states,events);
	}	
	
	private QEA makeProp(int states, int events) {
		boolean[] strongStates = computeStrongStates();
		
		if(usesGlobalGuards()) throw new ShouldNotHappenException("Global guards not supported here yet");
		if (noFreeVariables()) {
			if (isEventDeterministic()) {
				return makePropNofreeDet(states, events, strongStates);
			} else {
				return makePropNofreeNondet(states, events, strongStates);
			}
		} else {
			if (isEventDeterministic()) {

				return makePropFreeDet(states, events, strongStates);

			} else {
				return makePropFreeNondet(states, events, strongStates);
			}
		}
	}

	private QEA makePropFreeNondet(int states, int events,
			boolean[] strongStates) {
		int frees = countFreeVars();
		QVar01_FVar_NonDet_QEA qea = new QVar01_FVar_NonDet_QEA(states,
				events, 1, NONE, frees);
		Map<Integer, Set<Transition>>[] actual_trans = new Map[states + 1];
		for (TempTransition t : transitions) {
			Map<Integer, Set<Transition>> this_state = actual_trans[t.start];
			if (this_state == null) {
				actual_trans[t.start] = new HashMap<Integer, Set<Transition>>();
			}
			this_state = actual_trans[t.start];

			Set<Transition> nextstates = this_state.get(t.event_name);
			if (nextstates == null) {
				nextstates = new HashSet<Transition>();
				this_state.put(t.event_name, nextstates);
			}
			nextstates
					.add(new Transition(t.var_args(), t.g, t.a, t.end));
		}
		for (int i = 0; i < actual_trans.length; i++) {
			if (actual_trans[i] != null) {
				for (Map.Entry<Integer, Set<Transition>> entry : actual_trans[i]
						.entrySet()) {
					Transition[] nextstates = new Transition[entry
							.getValue().size()];
					int j = 0;
					for (Transition s : entry.getValue()) {
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
		for (int i = 0; i < strongStates.length; i++) {
			if (strongStates[i]) {
				qea.setStateAsStrong(i);
			}
		}
		return qea;
	}

	private QEA makePropFreeDet(int states, int events, boolean[] strongStates) {
		int fvars = countFreeVars();
		QVar01_FVar_Det_QEA qea = new QVar01_FVar_Det_QEA(states,
				events, 1, NONE, fvars);
		for (TempTransition t : transitions) {
			Transition trans = new Transition(t.var_args(), t.g, t.end);
			qea.addTransition(t.start, t.event_name, trans);
		}
		for (Integer s : finalstates) {
			qea.setStateAsFinal(s);
		}
		for (int i = 0; i < strongStates.length; i++) {
			if (strongStates[i]) {
				qea.setStateAsStrong(i);
			}
		}
		return qea;
	}

	private QEA makePropNofreeNondet(int states, int events,
			boolean[] strongStates) {
		QVar01_NoFVar_NonDet_QEA qea = new QVar01_NoFVar_NonDet_QEA(
				states, events, 1, NONE);
		Map<Integer, Set<Integer>>[] actual_trans = new Map[states + 1];
		boolean[][] prop = new boolean[states+1][states+1];
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
			if(t.event_args==null || t.event_args.length==0)
				prop[t.start][t.end]=true;	
			nextstates.add(t.end);
		}
		for (int start = 0; start < actual_trans.length; start++) {
			if (actual_trans[start] != null) {
				for (Map.Entry<Integer, Set<Integer>> entry : actual_trans[start]
						.entrySet()) {
					int[] nextstates = new int[entry.getValue().size()];
					int j = 0;
					for (Integer s : entry.getValue()) {
						nextstates[j] = s;
						j++;
					}
					int event = (entry.getKey());
					boolean p = prop[start][nextstates[0]];
					qea.addTransitions(start, event, nextstates,p);
				}
			}
		}
		for (Integer s : finalstates) {
			qea.setStateAsFinal(s);
		}
		for (int i = 0; i < strongStates.length; i++) {
			if (strongStates[i]) {
				qea.setStateAsStrong(i);
			}
		}
		return qea;
	}

	private QEA makePropNofreeDet(int states, int events, boolean[] strongStates) {
		QVar01_NoFVar_Det_QEA qea = new QVar01_NoFVar_Det_QEA(states,
				events, 1, NONE);
		for (TempTransition t : transitions) {
			boolean prop = (t.event_args==null) || (t.event_args.length==0);
			qea.addTransition(t.start, t.event_name, t.end,prop);
		}
		for (Integer s : finalstates) {
			qea.setStateAsFinal(s);
		}
		for (int i = 0; i < strongStates.length; i++) {
			if (strongStates[i]) {
				qea.setStateAsStrong(i);
			}
		}
		return qea;
	}

	private QEA makeSingle(int states, int events) {
		if(usesGlobalGuards()) throw new ShouldNotHappenException("Global guards not supported here yet");
		boolean[] strongStates = computeStrongStates();
		
		Quantification q;
		if (quants.get(0).universal) {
			q = FORALL;
		} else {
			q = EXISTS;
		}

		if (noFreeVariables()) {
			if (isEventDeterministic()) {
				return makeSingleNofreeDet(states, events, strongStates, q);
			} else {
				return makeSingleNofreeNondet(states, events, strongStates, q);
			}
		} else {
			int frees = countFreeVars();
			if (fixedQVar()) {
				if (isEventDeterministic()) {
					return makeSingleFreeDetFixed(states, events, strongStates,
							q, frees);
				} else {
					return makeSingleFreeNondetFixed(states, events,
							strongStates, q, frees);
				}
			} else {
				if (isEventDeterministic()) {
					return makeSingleFreeDet(states, events, strongStates, q,
							frees);
				} else {
					return makeSingleFreeNondet(states, events, strongStates,
							q, frees);
				}
			}
		}
	}

	private QEA makeSingleFreeNondet(int states, int events,
			boolean[] strongStates, Quantification q, int frees) {
		QVar01_FVar_NonDet_QEA qea = new QVar01_FVar_NonDet_QEA(
				states, events, 1, q, frees);
		Map<Integer, Set<Transition>>[] actual_trans = new Map[states + 1];
		for (TempTransition t : transitions) {
			Map<Integer, Set<Transition>> this_state = actual_trans[t.start];
			if (this_state == null) {
				actual_trans[t.start] = new HashMap<Integer, Set<Transition>>();
			}
			this_state = actual_trans[t.start];

			Set<Transition> nextstates = this_state
					.get(t.event_name);
			if (nextstates == null) {
				nextstates = new HashSet<Transition>();
				this_state.put(t.event_name, nextstates);
			}
			nextstates.add(new Transition(t.var_args(), t.g, t.a,
					t.end));
		}
		for (int i = 0; i < actual_trans.length; i++) {
			if (actual_trans[i] != null) {
				for (Map.Entry<Integer, Set<Transition>> entry : actual_trans[i]
						.entrySet()) {
					Transition[] nextstates = new Transition[entry
							.getValue().size()];
					int j = 0;
					for (Transition s : entry.getValue()) {
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
		for (int i = 0; i < strongStates.length; i++) {
			if (strongStates[i]) {
				qea.setStateAsStrong(i);
			}
		}
		return qea;
	}

	private QEA makeSingleFreeDet(int states, int events,
			boolean[] strongStates, Quantification q, int frees) {
		QVar01_FVar_Det_QEA qea = new QVar01_FVar_Det_QEA(states,
				events, 1, q, frees);

		for (TempTransition t : transitions) {
			Transition trans = new Transition(t.var_args(), t.g,
					t.a, t.end);
			trans.setAssignment(t.a);
			trans.setGuard(t.g);
			qea.addTransition(t.start, t.event_name, trans);
		}
		for (Integer s : finalstates) {
			qea.setStateAsFinal(s);
		}
		for (int i = 0; i < strongStates.length; i++) {
			if (strongStates[i]) {
				qea.setStateAsStrong(i);
			}
		}
		return qea;
	}

	private QEA makeSingleFreeNondetFixed(int states, int events,
			boolean[] strongStates, Quantification q, int frees) {
		QVar1_FVar_NonDet_FixedQVar_QEA qea = new QVar1_FVar_NonDet_FixedQVar_QEA(
				states, events, 1, q, frees);
		Map<Integer, Set<Transition>>[] actual_trans = new Map[states + 1];
		for (TempTransition t : transitions) {
			Map<Integer, Set<Transition>> this_state = actual_trans[t.start];
			if (this_state == null) {
				actual_trans[t.start] = new HashMap<Integer, Set<Transition>>();
			}
			this_state = actual_trans[t.start];

			Set<Transition> nextstates = this_state
					.get(t.event_name);
			if (nextstates == null) {
				nextstates = new HashSet<Transition>();
				this_state.put(t.event_name, nextstates);
			}
			nextstates.add(new Transition(t.var_args(), t.g, t.a,
					t.end));
		}
		for (int i = 0; i < actual_trans.length; i++) {
			if (actual_trans[i] != null) {
				for (Map.Entry<Integer, Set<Transition>> entry : actual_trans[i]
						.entrySet()) {
					Transition[] nextstates = new Transition[entry
							.getValue().size()];
					int j = 0;
					for (Transition s : entry.getValue()) {
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
		for (int i = 0; i < strongStates.length; i++) {
			if (strongStates[i]) {
				qea.setStateAsStrong(i);
			}
		}
		return qea;
	}

	private QEA makeSingleFreeDetFixed(int states, int events,
			boolean[] strongStates, Quantification q, int frees) {
		QVar1_FVar_Det_FixedQVar_QEA qea = new QVar1_FVar_Det_FixedQVar_QEA(
				states, events, 1, q, frees);

		for (TempTransition t : transitions) {
			Transition trans = new Transition(t.var_args(), t.g,
					t.a, t.end);
			qea.addTransition(t.start, t.event_name, trans);
		}
		for (Integer s : finalstates) {
			qea.setStateAsFinal(s);
		}
		for (int i = 0; i < strongStates.length; i++) {
			if (strongStates[i]) {
				qea.setStateAsStrong(i);
			}
		}
		return qea;
	}

	private QEA makeSingleNofreeNondet(int states, int events,
			boolean[] strongStates, Quantification q) {
		QVar01_NoFVar_NonDet_QEA qea = new QVar01_NoFVar_NonDet_QEA(
				states, events, 1, q);
		Map<Integer, Set<Integer>>[] actual_trans = new Map[states + 1];
		boolean[][] prop = new boolean[states+1][states+1];
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
			if(t.event_args==null || t.event_args.length==0)
				prop[t.start][t.end]=true;
			nextstates.add(t.end);
		}
		for (int start = 0; start < actual_trans.length; start++) {
			if (actual_trans[start] != null) {
				for (Map.Entry<Integer, Set<Integer>> entry : actual_trans[start]
						.entrySet()) {
					int[] nextstates = new int[entry.getValue().size()];
					int j = 0;
					for (Integer s : entry.getValue()) {
						nextstates[j] = s;
						j++;
					}
					int event = (entry.getKey());
					boolean p = prop[start][nextstates[0]];
					qea.addTransitions(start, event, nextstates,p);
				}
			}
		}
		for (Integer s : finalstates) {
			qea.setStateAsFinal(s);
		}
		for (int i = 0; i < strongStates.length; i++) {
			if (strongStates[i]) {
				qea.setStateAsStrong(i);
			}
		}
		return qea;
	}

	private QEA makeSingleNofreeDet(int states, int events,
			boolean[] strongStates, Quantification q) {
		QVar01_NoFVar_Det_QEA qea = new QVar01_NoFVar_Det_QEA(states,
				events, 1, q);
		for (TempTransition t : transitions) {
			boolean prop = (t.event_args==null) || (t.event_args.length==0);
			qea.addTransition(t.start, t.event_name, t.end,prop);
		}
		for (Integer s : finalstates) {
			qea.setStateAsFinal(s);
		}
		for (int i = 0; i < strongStates.length; i++) {
			if (strongStates[i]) {
				qea.setStateAsStrong(i);
			}
		}
		return qea;
	}

	private QEA makeGeneral(int states, int events) {
		boolean[] strongStates = computeStrongStates();

		int frees = countFreeVars();
		if (isEventDeterministic()) {
					return makeGeneralFreeDet(states, events, strongStates,
							frees);
		} else {
				return makeGeneralFreeNondet(states, events, strongStates,
						frees);
		}
	}

	private QEA makeGeneralFreeNondet(int states, int events,
			boolean[] strongStates, int frees) {
		QVarN_FVar_NonDet_QEA qea = new QVarN_FVar_NonDet_QEA(
					states, events, 1, quants.size(),frees);
		
		//Add quantifications
		for(Quant q : quants){
			qea.addQuantification(q.var, (q.universal ? FORALL : EXISTS), q.g);
		}				
		
			Map<Integer, Set<Transition>>[] actual_trans = new Map[states + 1];
			for (TempTransition t : transitions) {
				Map<Integer, Set<Transition>> this_state = actual_trans[t.start];
				if (this_state == null) {
					actual_trans[t.start] = new HashMap<Integer, Set<Transition>>();
				}
				this_state = actual_trans[t.start];

				Set<Transition> nextstates = this_state
						.get(t.event_name);
				if (nextstates == null) {
					nextstates = new HashSet<Transition>();
					this_state.put(t.event_name, nextstates);
				}
				nextstates.add(new Transition(t.var_args(), t.g, t.a,
						t.end));
			}
			for (int i = 0; i < actual_trans.length; i++) {
				if (actual_trans[i] != null) {
					for (Map.Entry<Integer, Set<Transition>> entry : actual_trans[i]
							.entrySet()) {
						Transition[] nextstates = new Transition[entry
								.getValue().size()];
						int j = 0;
						for (Transition s : entry.getValue()) {
							nextstates[j] = s;
							j++;
						}
						qea.addTransitions(i, (entry.getKey()),
								nextstates);
					}
				}
			}
			for (Integer s : finalstates) {
				qea.setStatesAsFinal(s);
			}
			for (int i = 0; i < strongStates.length; i++) {
				if (strongStates[i]) {
					qea.setStatesAsStrong(i);
				}
			}
			return qea;
	}

	private QEA makeGeneralFreeDet(int states, int events,
			boolean[] strongStates, int frees) {
		QVarN_FVar_Det_QEA qea = new QVarN_FVar_Det_QEA(states,
				events, 1, quants.size(),frees);

		
		//Add quantifications
		for(Quant q : quants){
			qea.addQuantification(q.var, (q.universal ? FORALL : EXISTS), q.g);
		}
		
		for (TempTransition t : transitions) {
			Transition trans = new Transition(t.var_args(), t.g,
					t.a, t.end);
			trans.setAssignment(t.a);
			trans.setGuard(t.g);
			qea.addTransition(t.start, t.event_name, trans);
		}
		for (Integer s : finalstates) {
			qea.setStatesAsFinal(s);
		}
		for (int i = 0; i < strongStates.length; i++) {
			if (strongStates[i]) {
				qea.setStatesAsStrong(i);
			}
		}
		return qea;
	}	
	
	private int countStates() {
		int max = 0;
		for (TempTransition t : transitions) {
			if (t.start > max) {
				max = t.start;
			}
			if (t.end > max) {
				max = t.end;
			}
		}
		return max;
	}

	private int countEvents() {
		int max = 0;
		for (TempTransition t : transitions) {
			if (t.event_name > max) {
				max = t.event_name;
			}
		}
		return max;
	}

	private int countFreeVars() {
		Set<Integer> qvars = new HashSet<Integer>();
		int max = 0;
		for (Quant q : quants) {
			qvars.add(q.var);
		}
		// Set<Integer> fvars = new HashSet<Integer>();
		for (TempTransition t : transitions) {
			if (t.event_args != null) {
				for (VarOrVal event_arg : t.event_args) {
					int var = event_arg.var;
					if (var != 0 && !qvars.contains(var)) {
						if (var > max) {
							max = var;
						}
					}
				}
				if(t.g!=null){
					for(int var : t.g.vars()){
						if (var != 0 && !qvars.contains(var)) {
							// fvars.add(var);
							if (var > max) {
								max = var;
							}
						}						
					}
				}
				if(t.a!=null){
					for(int var : t.a.vars()){
						if (var != 0 && !qvars.contains(var)) {
							// fvars.add(var);
							if (var > max) {
								max = var;
							}
						}							
					}
				}
			}
		}
		return max;
	}

	/*
	 * Check that all transitions have a start, end and event name And that all
	 * quantifications have a var
	 */
	private String wellFormed() {
		int[] num_args = new int[1 + countEvents()];
		for (int i = 0; i < num_args.length; i++) {
			num_args[i] = -1;
		}

		for (TempTransition trans : transitions) {
			if (trans.start < 1) {
				return "All states should be >0";
			}
			if (trans.end < 1) {
				return "All states should be >0";
			}
			if (trans.event_name < 1) {
				System.err.println(trans);
				return "All event names should be >0";
			}
			if (trans.event_args != null) {
				if (num_args[trans.event_name] == -1) {
					num_args[trans.event_name] = trans.event_args.length;
				} else if (num_args[trans.event_name] != trans.event_args.length) {
					return "All events with the same name should have the same number of parameters";
				}
				for (VarOrVal event_arg : trans.event_args) {
					if (event_arg == null
							|| (event_arg.val == null && event_arg.var == -1000)) {
						return "The paramters were not correctly defined in a transition";
					}
				}
			}
		}
		int qstep = -1;
		for (Quant q : quants) {
			if (q.var != qstep) {
				return "Quantified variables should named -1,-2,...";
			}
			qstep--;
		}
		return null;
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

		Map<Integer, Set<Integer>> start_trans = new HashMap<Integer, Set<Integer>>();
		for (TempTransition t : transitions) {
			Set<Integer> states = start_trans.get(t.start);
			if (states == null) {
				states = new HashSet<Integer>();
				start_trans.put(t.start, states);
			}
			if (!states.add(t.event_name)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the QEA of this builder meets the conditions for the FixedQVar
	 * optimisation. This is:
	 * <ul>
	 * <li>There is a single quantified variable
	 * <li>All the events contain the quantified variable as the first parameter
	 * <li>There are no events without parameters
	 * </ul>
	 * 
	 * @return <code>true</code> if the QEA of this builder meets the conditions
	 *         for the FixedQVar optimisation; <code>false</code> otherwise
	 */
	private boolean fixedQVar() {

		// Check there is a single quantified variable
		if (quants.size() != 1) {
			return false;
		}
		int qVar = quants.get(0).var;

		// Iterate over all the transitions
		for (TempTransition t : transitions) {

			// Check it's not an event without parameters
			if (t.event_args == null || t.event_args.length == 0) {
				return false;
			}

			// Check the quantified variable is in the first position
			if (t.event_args[0].var != qVar) {
				return false;
			}

			// Check starting from the 2nd position there are only free vars
			for (int i = 1; i < t.event_args.length; i++) {
				if (t.event_args[i].var == qVar) {
					return false;
				}
			}
		}
		return true;
	}
	private boolean usesGlobalGuards() {
		for(Quant q : quants)
			if(q.g!=null) return true;
		return false;
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

	// add a single variable transition
	public void addTransition(int start, int propname, int var, int end) {
		TempTransition trans = new TempTransition();
		trans.start = start;
		trans.end = end;
		trans.event_name = propname;
		VarOrVal[] args = new VarOrVal[]{VarOrVal.makeVar(var)};
		trans.event_args = args;
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
			args[i] = VarOrVal.makeVar(vargs[i]);
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
		incargs.add(VarOrVal.makeVar(var));
	}

	public void addValArg(Object val) {
		incargs.add(VarOrVal.makeVal(val));
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
		for (int state : states) {
			finalstates.add(state);
		}
	}

	/*
	 * Must be called after all transitions that mention these events are
	 * defined
	 */
	public void setSkipStates(int... states) {

		Set<TempEvent> events = getEvents();

		for (int state : states) {

			if (usesGuards(state)) {
				throw new ShouldNotHappenException(
						"Cannot currently add skip states where guards are used"
								+ " - you need to do this manually");
			}

			for (TempEvent event : events) {
				if (!existsTransition(state, event)) {
					//System.err.println("Adding skip transition for "+state+" and "+event);
					addTransition(state, event, state);
				}
			}
		}

	}

	private boolean usesGuards(int state) {
		for (TempTransition trans : transitions) {
			if (trans.start == state && trans.g != null) {
				return true;
			}
		}
		return false;
	}

	private void addTransition(int start, TempEvent event, int end) {
		TempTransition trans = new TempTransition();
		trans.start = start;
		trans.end = end;
		trans.event_name = event.event_name;
		trans.event_args = event.event_args;
		trans.temp_event = event;
		transitions.add(trans);

	}

	private boolean existsTransition(int state, TempEvent event) {
		for (TempTransition trans : transitions) {
			if (trans.start == state && trans.temp_event.equals(event)) {
				return true;
			}
		}
		return false;
	}

	private Set<TempEvent> getEvents() {
		Set<TempEvent> events = new HashSet<TempEvent>();

		for (TempTransition trans : transitions) {
			TempEvent e = new TempEvent();
			e.event_name = trans.event_name;
			e.event_args = trans.event_args;
			events.add(e);
			trans.temp_event = e;
		}

		return events;
	}

	/*
	 * Should only be called after all transitions are created.
	 */
	private boolean[] computeStrongStates() {

		int nstates = countStates();

		boolean[] strong = new boolean[nstates + 1];

		// state 0 is always strong
		strong[0] = true;

		// reach[1][2] is true if state 2 is reachable from state 1
		boolean[][] reach = new boolean[nstates + 1][nstates + 1];

		Set<TempEvent> events = getEvents();
		Set<TempEvent>[] used = new Set[nstates + 1];

		for (int i = 1; i < reach.length; i++) {
			reach[i][i] = true;
			used[i] = new HashSet<TempEvent>();
		}

		for (TempTransition t : transitions) {
			reach[t.start][t.end] = true;
			used[t.start].add(t.temp_event);
		}

		for (int i = 1; i < reach.length; i++) {
			if (used[i].size() != events.size()) {
				reach[i][0] = true;
			}
		}

		for (int i = 1; i < nstates; i++) {
			for (int start = 1; start < (nstates + 1); start++) {
				for (int middle = 1; middle < (nstates + 1); middle++) {
					// for each entry in reach where start can reach middle
					if (reach[start][middle]) {
						// for every state end that middle can reach
						for (int end = 1; end < (nstates + 1); end++) {
							if (reach[middle][end]) {
								// set that start can reach end
								reach[start][end] = true;
							}
						}
					}
				}
			}
		}

		if (DEBUG) {
			System.err.println("reach:");
			for (int i = 1; i < reach.length; i++) {
				System.err.println(i + "\t->\t" + Arrays.toString(reach[i]));
			}
			System.err.println();
		}

		for (int i = 1; i <= nstates; i++) {
			boolean is_strong = true;
			for (int j = 0; j <= nstates; j++) {
				if (reach[i][j]) {
					is_strong &= (finalstates.contains(i) == finalstates
							.contains(j));
				}
			}
			strong[i] = is_strong;
		}

		if (DEBUG) {
			System.err.println("\nStrong states: " + Arrays.toString(strong));
		}

		return strong;
	}


}
