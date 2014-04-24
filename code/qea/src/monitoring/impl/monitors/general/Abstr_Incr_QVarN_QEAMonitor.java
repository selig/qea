package monitoring.impl.monitors.general;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import monitoring.impl.GarbageMode;
import monitoring.impl.IncrementalMonitor;
import monitoring.impl.RestartMode;
import monitoring.intf.Configuration;
import structure.impl.other.QBindingImpl;
import structure.impl.other.Transition;
import structure.impl.other.Verdict;
import structure.impl.qeas.Abstr_QVarN_QEA;
import util.OurVeryWeakHashMap;
import util.OurWeakHashMap;

/**
 *
 * We use the symbol-indexing concept
 
 * @author Giles Reger
 */
public abstract class Abstr_Incr_QVarN_QEAMonitor<Q extends Abstr_QVarN_QEA> extends IncrementalMonitor<Q> {

	protected static boolean DEBUG = false;
	
	protected final IncrementalChecker checker;
	protected final Map<Object,QBindingImpl> support_bindings;
	protected final Map<QBindingImpl,String> support_queries;
	private final int qvars;
	private final int freevars;
	private final boolean use_weak;

	protected final QBindingImpl bottom;
	
	protected final BindingRecord[] empty_paths;
	protected final boolean[] empty_has_q_blanks;//true if event *must* bind a qvar with empty mask
	protected final Map<String,BindingRecord>[] maps;

	protected static final String BLANK = "_";
	//0 for value, 1 for qblank (only qvars), 2 for fblank (some fvars)
	//Important - when creating masks if we have a choice between 1 and 2, pick 2
	// masks should be ordered from specific to general
	protected int[][][] masks;

	// could_leave[state][eventname] is true if an event with eventname could leave state
	// 
	protected final boolean could_leave[][];
	
	public Abstr_Incr_QVarN_QEAMonitor(RestartMode restart, GarbageMode garbage, Q qea) {
		super(restart,garbage,qea);
		checker = IncrementalChecker.make(qea.getFullLambda(),qea.getFinalStates(),qea.getStrongStates());		
		qea.setupMatching();
		qea.isNormal(); // make sure normal is set
		qvars = qea.getFullLambda().length;
		freevars = qea.getFreeVars();
		int num_events = qea.getEventsAlphabet().length+1;
		
		//Remember that incremental_checker might contain references
		// to objects if quantification is alternating
		switch(garbage){
			case NONE: 
				support_bindings = null;
				support_queries=null;
				use_weak=false;
			    //create a lookup map per event name
				maps = new HashMap[num_events];
				for(int i=0;i<num_events;i++){
					maps[i] = new HashMap<String,BindingRecord>();
				}
				break;
			case UNSAFE_LAZY:
				/*
				 * The idea is that the qbinding is pointed to by
				 * the objects it contains.
				 * If all of those objects become garbage then
				 * that qbinding can be removed.
				 */
				support_bindings = new OurVeryWeakHashMap<Object,QBindingImpl>();
				support_queries = new OurWeakHashMap<QBindingImpl,String>();
				use_weak=true;
			    //create a lookup map per event name
				maps = new OurWeakHashMap[num_events];
				for(int i=0;i<num_events;i++){
					maps[i] = new OurWeakHashMap<String,BindingRecord>();
				}
				break;
		default: throw new RuntimeException("Garbage mode "+garbage+" not currently supported");
		}
			
		bottom = qea.newQBinding();
		if(bottom.isTotal()) checker.newBinding(bottom,qea.getInitialState());
		
		//make empty paths and empty_has_q_blanks
		empty_paths = new BindingRecord[num_events];
		empty_has_q_blanks = new boolean[num_events];
		for(int i=0;i<num_events;i++){
			empty_paths[i] = BindingRecord.make(bottom,use_weak);
		}

		masks = new int[num_events][][];
		
		//generate masks
		int num_states = qea.getStates().length+1;

		for(int e=0;e<num_events;e++){

                        // get the most general signature
                        int[] general_args = null;
                        boolean every_sig_has_qs=true;
                        for(int s=1;s<num_states;s++){                    
                                for(Transition t : getTransitions(s,e)){
	                                if(t!=null){
	                                   int[] targs = t.getVariableNames();
	                                   boolean some_q=false;
	                                   for(int v : targs)
	                                	   if(v<0) some_q=true;
	                                   every_sig_has_qs &= some_q;
	                                   if(general_args==null){
	                                     general_args = new int[targs.length];
	                                     for(int i=0;i<targs.length;i++){
	                                             // Update when we can have values here
	                                             if(targs[i]<0) general_args[i]=1;
	                                             else general_args[i]=2;
	                                     }
	                                   }
	                                   else{
	                                      // take max i.e. max(0,1)=1, max(1,2)=2
	                                      // currently could only be 1 or 2
	                                      for(int i=0;i<general_args.length;i++){
	                                             // At the moment only need to set to 2
	                                             // if required
	                                             if(targs[i]>0) general_args[i]=2;
	                                      }
	                                   }
	
	                                }
                                }
                        }
                        if(general_args==null) general_args = new int[]{};
                        
                        empty_has_q_blanks[e] = every_sig_has_qs;
                        
                        
                        //create versions of this signature - not the empty (i.e. all 0)
                        // order from most specific (no 0 replacements) to least
                        //TODO - we can statically determine some masks are not required - do this!

                        // Without this trimming, and assuming no zeros in general_args, the number of
                        // masks is 2^args.length -1 (don't consider empty)
                        // but will will use a recursive function with lists and then turn it into an array!
                        List<List<Integer>> emasks_lists = makeMasks(e,general_args,0);
                        int[][] emasks = new int[emasks_lists.size()][general_args.length];
                        for(int i=0;i<emasks.length;i++){
                                List<Integer> emasks_list = emasks_lists.get(i);
                                for(int j=0;j<general_args.length;j++) emasks[i][j] = emasks_list.get(j);
                        }
                        masks[e] = emasks;

                }

		/*
		 * Setup could leave
		 * Note: if the QEA is not normal then the whole array should be true
		 */
		could_leave = new boolean[num_states][num_events];//nums already+1 above
		for(int state=1;state<num_states;state++)
			for(int event=1;event<num_events;event++)
				could_leave[state][event] = qea.can_leave(state,event);
		
		//for(int state=1;state<num_states;state++)
		//	System.err.println(Arrays.toString(could_leave[state]));
		
	}

	/*
	 * For the det case this will return a singleton array
	 */
        protected abstract Transition[] getTransitions(int s, int e);

		private void makeMasksLevel(int[] args,boolean [] used, int level,List<List<Integer>> masks, int taken){
              if(taken==level){
                      // make the mask
                      List<Integer> mask = new ArrayList<Integer>();
                      boolean all_not_zero=true;
                      for(int i=0;i<args.length;i++){
                              if(used[i]){
                            	  mask.add(args[i]);                             	  
                              }
                              else{
                            	  mask.add(0);
                            	  all_not_zero=false;                            	                             	  
                              }
                      }
                      // if the mask is empty i.e all 0s
                      if(!all_not_zero){
                    	  masks.add(mask);
                      }
                      return;
              }
              for(int i=0;i<args.length;i++){
                      if(!used[i]){
                            used[i]=true;
                            makeMasksLevel(args,used,level,masks,taken+1);
                            used[i]=false;
                      }
              }
        }

        // level indicates the number of replacements we should make
        // The organisation is somewhat complicated as we want to do breadth-first
        // rather than depth-first
        private List<List<Integer>> makeMasks(int e, int[] args, int level){
                List<List<Integer>> this_level = new ArrayList<List<Integer>>();

                if(args==null || args.length==0) return this_level;
                
                if(level==0){
                  List<Integer> l = new ArrayList<Integer>();
                  for(int x : args) l.add(0); //replace x by 0
                  this_level.add(l);
                }
                else{
                     boolean [] used = new boolean[args.length];
                     makeMasksLevel(args,used,level,this_level,0);
                }

                // Stop when level = args.length-1
                if(level < args.length){
                         List<List<Integer>> next_level = makeMasks(e,args,level+1);
                         this_level.addAll(next_level); // always append
                }
                return this_level;
        }

        int rep = 0;
	@Override
	public Verdict step(int eventName, Object[] args) {

		if(rep++%1000==0) DEBUG = true;
		else DEBUG = false;
		
		if(DEBUG) System.err.println("********************\n\n********************\n=======> "+qea.get_event_name(eventName)+Arrays.toString(args));
		if(DEBUG) printMaps();
		
		if(saved!=null){
			if(!restart()) return saved;
		}			
		
				
		
		/*for(int e = 1; e<masks.length;e++){
			System.err.println(e);
			for(int[] m : masks[e])
				System.err.println(Arrays.toString(m));
		}
		System.err.println(Arrays.toString(empty_has_q_blanks));
		System.exit(0);*/
		
		//retrieve consistent bindings in order of informativeness
		// do updates and extensions in-place

		//keep track of bindings used
		Set<QBindingImpl> used = new HashSet<QBindingImpl>();
		
		// Look at the masks, as long as the map is non-empty
		// TODO- is this empty check premature optimisation?
		Map<String,BindingRecord> map = maps[eventName];
		boolean used_full=false;
		if(!map.isEmpty()){
			int[][] eventMasks = masks[eventName];

			for(int i=0;i<eventMasks.length;i++){
				int[] mask = eventMasks[i];
				StringBuilder b = new StringBuilder();
				boolean has_q_blanks=false;
				for(int j=0;j<mask.length;j++){
					if(mask[j]==0) b.append(System.identityHashCode(args[j]));
					else{
						if(mask[j]==1) has_q_blanks=true;
						b.append(BLANK);
					}
				}
				String query = b.toString();

				BindingRecord record = map.get(query);

				if(DEBUG) System.err.println("Query "+query);
				
				if(record!=null){
					process_record(record,eventName,args,used,has_q_blanks);					
					if(i==0 && freevars==0){
						// use_full not always viable
						//only allow it if we have no free variables
						// this is oversafe
						used_full=true;
						break; // for full optimisation
					}
				}
			}
		}
		if(!used_full){
		//	now the empty mask
			if(DEBUG) System.err.println("Processing empty");
			BindingRecord record = empty_paths[eventName];
			process_record(record,eventName,args,used,empty_has_q_blanks[eventName]);			
		}
		
		Verdict result = checker.verdict(false);
		if(DEBUG) System.err.println("result: "+result);
		if(result.isStrong()) saved=result;
		
		if(DEBUG){
			System.err.println("*******************************\n");
			System.err.println(getStatus());
			System.err.println("*******************************\n\n\n\n");
			System.err.println("*******************************\n");
		}
		
		return result;
	}

	private void process_record(BindingRecord record, int eventName, Object[] args,
			Set<QBindingImpl> used, boolean has_q_blanks){

		if(DEBUG) System.err.println("Processing "+record);
		
		/*
		 * Record might be updated whilst iterating over it
		 * But these will be added to the end, so get num bindings
		 * before we begin (although this shouldn't be needed)
		 */
		int numbindings = record.num_bindings();
		
		if(numbindings==0){
			// This record has become garbage, we should remove
			// the associate entry in maps
			// - can this happen?
			System.err.println("record garbage");
			if(DEBUG) System.err.println(record);
		}
		
		List<Integer> null_indexes = null;
		for(int j=numbindings-1;j>=0;j--){
			QBindingImpl binding = record.get(j);
			if(binding==null){
				if(DEBUG) System.err.println("record had an empty binding! "+record);
				if(null_indexes==null) null_indexes = new ArrayList<>();
				null_indexes.add(j);
				continue;
			}

			if(DEBUG) System.err.println(j+":"+binding);
			
			// If the binding hasn't already been encountered
			if(used.add(binding)){

				//It should be an invariant that binding is in mapping, check this								
				processBinding(eventName, args, has_q_blanks, binding);
			}
		}
		// remove null_indexes if they exist
		if(null_indexes!=null) record.removeIndexes(null_indexes);
		
	}

	protected abstract void processBinding(int eventName, Object[] args,
			boolean has_q_blanks, QBindingImpl binding);

	protected void addSupport(QBindingImpl binding){
		if(support_bindings!=null){
			for(int v=1;v<qvars;v++){
				Object value = binding.getValue(-v);
				if(value!=null)
					support_bindings.put(value,binding);
			}
		}
	}
	protected void add_to_maps(QBindingImpl ext){add_to_maps(ext,true);}
	protected void add_to_maps(QBindingImpl ext,boolean add) {
		int[][][] sigs = qea.getSigs();	
		for(int e=1;e<sigs.length;e++){
			Set<String> qs = new HashSet<String>();
			int[][] es = sigs[e];
			for(int s=0;s<es.length;s++){
				int[] sig = es[s];
				StringBuilder b = new StringBuilder();
				boolean empty = true;
				for(int j=0;j<sig.length;j++){
					int var=sig[j];
					Object val = var<0 ? ext.getValue(sig[j]) : null;
					if(val!=null){
						empty=false;
						b.append(System.identityHashCode(val));
					}
					else{
						b.append(BLANK);
					}
				}
				String q = b.toString();
				if(qs.add(q)){					
					if(add && support_queries!=null)
						support_queries.put(ext, q);
					BindingRecord record = empty ? empty_paths[e] : maps[e].get(q);
					if(add && record==null){ // empty must be false
						record = BindingRecord.make(ext,use_weak);
						maps[e].put(q,record);
					}
					else{
						if(add) record.addBinding(ext);
						else{
							record.removeBinding(ext);
							if(record.num_bindings()==0){
								maps[e].remove(q);
							}
						}
					}
				}
			}
		}
	}



	@Override
	public Verdict end() {
		return checker.verdict(true);
	}


	/*
	 * Bindings are added to the end
	 * So we should always iterate from the back
	 * to get the newest (largest) bindings first
	 */
	public static abstract class BindingRecord{
		
		public static BindingRecord make(QBindingImpl b, boolean weak){
			if(weak) return new WeakBindingRecord(b);
			return new StrongBindingRecord(b);
		}
		
		public abstract QBindingImpl get(int j);
		public abstract int num_bindings();
		abstract void addBinding(QBindingImpl b);
		abstract void removeBinding(QBindingImpl b);
		abstract void removeIndexes(List<Integer> is);
	}
	
	public static class StrongBindingRecord extends BindingRecord{
		QBindingImpl[] bindings;
		int num_bindings=0;
		
		public String toString(){
			return "record of "+Arrays.toString(bindings);
		}
		
		StrongBindingRecord(QBindingImpl b){
			bindings =new QBindingImpl[2];
			num_bindings=1;
			bindings[0]=b;
		}		
		void addBinding(QBindingImpl b){
			if(num_bindings==bindings.length){
				//extend bindings
				QBindingImpl [] temp = new QBindingImpl[bindings.length*2];
				System.arraycopy(bindings, 0, temp, 0, bindings.length);
				bindings=temp;
			}
			bindings[num_bindings]=b;
			num_bindings++;
		}
		void removeBinding(QBindingImpl b){
			// find binding
			int index = -1;
			for(int i=0;i<num_bindings;i++)
				if(bindings[i].equals(b)){
					index=i;
					break;
				}
			if(index == -1) return; // not found
			// move everything after index down
			for(int i=index;i+1<num_bindings;i++){
				bindings[i]=bindings[i+1];
			}
			// but if index is last element then null it
			if(index == bindings.length-1)
				bindings[bindings.length-1]=null;
			num_bindings--;
		}		
		// Invariant - indexes is ordered
		void removeIndexes(List<Integer> indexes){
			throw new RuntimeException("Should not be called");
		}		

		@Override
		public QBindingImpl get(int j) {
			return bindings[j];
		}

		@Override
		public int num_bindings() {
			return num_bindings;
		}
		
	}

	public static class WeakBindingRecord extends BindingRecord{
		WeakReference<QBindingImpl>[] bindings;
		int num_bindings=0;
		
		public String toString(){
			String ret = "record of "+num_bindings+" ";
			ret+=Arrays.toString(bindings);
			if(bindings.length>20) ret+="\n----------------\n"; 
			return ret;
		}
		
		WeakBindingRecord(QBindingImpl b){
			bindings = new WeakReference[2];
			num_bindings=1;
			bindings[0]= new WeakReference<QBindingImpl>(b);
		}		
		void addBinding(QBindingImpl b){
			if(DEBUG) System.err.println("Add "+b+" to "+this);
			if(num_bindings==bindings.length){
				//extend bindings
				WeakReference<QBindingImpl> [] temp = new WeakReference[bindings.length*2];
				System.arraycopy(bindings, 0, temp, 0, bindings.length);
				bindings=temp;
			}
			bindings[num_bindings]= new WeakReference<QBindingImpl>(b);
			num_bindings++;
		}
		void removeBinding(QBindingImpl b){
			// find binding
			int index = -1;
			for(int i=0;i<num_bindings;i++)
				if(b.equals(bindings[i].get())){ // b is not null, so call on this
					index=i;
					break;
				}
			if(index == -1){
				System.err.println("Failed removing "+b+" from "+this);								
				return; // not found
			}
			// move everything after index down
			// unless we're at the end
			if((index+1) < num_bindings){
				// minus 1 from num_bindings as we're dealing with indexes
				int left = (num_bindings-1)-index;
				System.arraycopy(bindings,index+1,bindings,index,left);
			}

			num_bindings--;
		}	
		// Invariant - indexes is ordered *in reverse order*
		void removeIndexes(List<Integer> indexes){
			if(DEBUG) System.err.println("remove "+indexes+" from "+this);
			System.err.println("Removing "+indexes.size()+" garbage in record");
			
			int removed=0;
			int ep = num_bindings-1;
			//iterate backwards as order is backwards
			for(int i=indexes.size()-1;i>=0;i--){
				int index = indexes.get(i);
				// The index has been shifted left removed times
				int index_place = index-removed;
				// move everything after index_place left one
				// unless we've reached the end
				if((index_place+1) < ep){
					int left = ep - index_place;
					System.arraycopy(bindings,index_place+1,bindings, index_place,left);				
					removed++;
					ep--;
				}
			}
			// don't actually need to null anything later or resize
			// as we use num_bindings to iterate over record :)
			// ep will point to the last binding, as we're zero-indexed
			// the actual number is one greater
			num_bindings = ep+1;
		}		

		@Override
		public QBindingImpl get(int j) {
			return bindings[j].get();
		}

		@Override
		public int num_bindings() {
			//trim removed bindings at this point
			//int c = 0;
			//for(int i=0;i<num_bindings;i++){
			//	if(bindings[i].get()!=null) c++;
			//}
			//WeakReference<QBindingImpl> [] temp = new WeakReference[c];
			//int p = 0;
			//for(int i=0;i<num_bindings;i++){
			//	if(bindings[i].get()!=null) temp[p++] = bindings[i];
			//}			
			//bindings=temp;
			//num_bindings=c;
			
			return num_bindings;
		}
		
	}	
	
	protected boolean restart(){
		switch(restart_mode){
			case NONE:
				return false;
			
			case REMOVE:
				// remove the offending binding
				removeStrongBindings();
				return true;
			case ROLLBACK:
				//rollback the offending bindings to initial state
				rollbackStrongBindings();
				return true;
			case IGNORE:
				//set the offending bindings to be ignored in the future
				//ignoreStrongBindings();
				return false;
		}
		return false;
	}	

	protected abstract void printMaps();
	
}
