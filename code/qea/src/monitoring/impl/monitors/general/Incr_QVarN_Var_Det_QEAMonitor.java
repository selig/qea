package monitoring.impl.monitors.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;

import monitoring.impl.GarbageMode;
import monitoring.impl.IncrementalMonitor;
import monitoring.impl.RestartMode;
import monitoring.impl.configs.DetConfig;
import structure.impl.other.QBindingImpl;
import structure.impl.other.Transition;
import structure.impl.other.Verdict;
import structure.impl.qeas.QVarN_FVar_Det_QEA;
import exceptions.ShouldNotHappenException;

/**
 * A small-step monitor for the most general *deterministic* QEA
 *
 * We use the symbol-indexing concept
 *
 *
 * TODO - when we extend for garbage review every place we use System.identityHashCode
 *
 * @author Giles Reger
 */
public class Incr_QVarN_Var_Det_QEAMonitor extends IncrementalMonitor<QVarN_FVar_Det_QEA> {

	private static final boolean DEBUG = false;
	
	private final IncrementalChecker checker;
	private final HashMap<QBindingImpl,DetConfig> mapping;

	private final QBindingImpl bottom;
	
	private final BindingRecord[] empty_paths;
	private final boolean[] empty_has_q_blanks;//true if event *must* bind a qvar with empty mask
	private final HashMap<String,BindingRecord>[] maps;

	private static final String BLANK = "_";
	//0 for value, 1 for qblank (only qvars), 2 for fblank (some fvars)
	//Important - when creating masks if we have a choice between 1 and 2, pick 2
	// masks should be ordered from specific to general
	int[][][] masks;

	public Incr_QVarN_Var_Det_QEAMonitor(RestartMode restart, GarbageMode garbage, QVarN_FVar_Det_QEA qea) {
		super(restart,garbage,qea);
		checker = IncrementalChecker.make(qea.getFullLambda(),qea.getFinalStates(),qea.getStrongStates());		
		mapping = new HashMap<QBindingImpl,DetConfig>();
		qea.setupMatching();

		DetConfig initial = new DetConfig(qea.getInitialState(),qea.newFBinding());		
		bottom = qea.newQBinding();
		if(bottom.isTotal()) checker.newBinding(qea.getInitialState());
		mapping.put(bottom,initial);
		
       //create a lookup map per event name
		int num_events = qea.getEventsAlphabet().length+1;
		maps = new HashMap[num_events];
		for(int i=0;i<num_events;i++){
			maps[i] = new HashMap<String,BindingRecord>();
		}
		//make empty paths and empty_has_q_blanks
		empty_paths = new BindingRecord[num_events];
		empty_has_q_blanks = new boolean[num_events];
		for(int i=0;i<num_events;i++){
			empty_paths[i] = new BindingRecord(bottom);
		}
		
		Transition[][] delta = qea.getTransitions();

		//generate masks

		masks = new int[num_events][][];
		for(int e=0;e<num_events;e++){

                        // get the most general signature
                        int[] general_args = null;
                        for(int s=1;s<delta.length;s++){
                                Transition t = delta[s][e];
                                if(t!=null){
                                   int[] targs = t.getVariableNames();
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
                        if(general_args==null) general_args = new int[]{};
                        
                        // if general_args contains a qvar then empty_has_q_blanks should be true
                        for(int m : general_args){
                        	if(m==1) empty_has_q_blanks[e] = true;
                        }
                        
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

	}

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

	@Override
	public Verdict step(int eventName, Object[] args) {

		/*for(int e = 1; e<masks.length;e++){
			System.out.println(e);
			for(int[] m : masks[e])
				System.out.println(Arrays.toString(m));
		}
		System.out.println(Arrays.toString(empty_has_q_blanks));
		System.exit(0);*/
		
		//retrieve consistent bindings in order of informativeness
		// do updates and extensions in-place

		//keep track of bindings used
		Set<QBindingImpl> used = new HashSet<QBindingImpl>();
		
		// Look at the masks, as long as the map is non-empty
		// TODO- is this empty check premature optimisation?
		HashMap<String,BindingRecord> map = maps[eventName];
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
					if(i==0){
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
		if(result.isStrong()) saved=result;
		
		//System.err.println(getStatus());
		
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
		int numbindings = record.num_bindings;
		
		for(int j=numbindings-1;j>=0;j--){
			QBindingImpl binding = record.bindings[j];

			if(DEBUG) System.err.println(j+":"+binding);
			
			// If the binding hasn't already been encountered
			if(used.add(binding)){

				DetConfig config = mapping.get(binding);
				int previous_state = config.getState();
				
				//Attempt extensions
				QBindingImpl[] bs = qea.makeBindings(eventName, args);
				for(QBindingImpl from_binding : bs){
					// are binding and from_binding guaranteed to be consistent?
					// - no!
					if(binding.consistentWith(from_binding)){
						QBindingImpl ext = binding.updateWith(from_binding);
						if(!mapping.containsKey(ext)){
							if(DEBUG) System.err.println("Adding new "+ext);
							DetConfig next_config = config.copy();							
							qea.getNextConfig(ext,next_config,eventName,args); 
							mapping.put(ext,next_config);
							add_to_maps(ext);
							checker.newBinding(previous_state);
							if(ext.isTotal()){
								checker.update(ext,previous_state,next_config.getState());
							}							
						}
					}
				}	

				//Update configurations - check relevance first
				// will not be relevant if blanks refer only to quantified variables
				if(!has_q_blanks){
					qea.getNextConfig(binding,config,eventName,args); // config updated in-place as Det
					if(binding.isTotal()){
						checker.update(binding,previous_state,config.getState());
					}					
				}
			}
		}
		
	}

	private void add_to_maps(QBindingImpl ext) {
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
					BindingRecord record = empty ? empty_paths[e] : maps[e].get(q);
					if(record==null){ // empty must be false
						record = new BindingRecord(ext);
						maps[e].put(q,record);
					}
					else record.addBinding(ext);
				}
			}
		}
	}

	@Override
	public Verdict step(int eventName) {
		throw new ShouldNotHappenException("We do not consider propositional events here yet");
	}

	@Override
	public Verdict end() {
		return checker.verdict(true);
	}

	@Override
	public String getStatus() {
		String ret = "mapping\n";
		for(Map.Entry<QBindingImpl,DetConfig> entry : mapping.entrySet())
			ret += entry.getKey()+"\t"+entry.getValue()+"\n";
		ret+= "maps\n";
		for(int e=1;e<maps.length;e++){
			Map<String,BindingRecord> m = maps[e];
			ret+=e+"\n";
			for(Map.Entry<String,BindingRecord> entry : m.entrySet())
				ret += "\t"+entry.getKey()+"\t"+entry.getValue()+"\n";			
		}
		return ret;
	}	

	/*
	 * Bindings are added to the end
	 * So we should always iterate from the back
	 * to get the newest (largest) bindings first
	 */
	public static class BindingRecord{
		QBindingImpl[] bindings;
		int num_bindings=0;
		
		public String toString(){
			return "record of "+Arrays.toString(bindings);
		}
		
		BindingRecord(QBindingImpl b){
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
		
	}

	@Override
	protected int removeStrongBindings() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int rollbackStrongBindings() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int ignoreStrongBindings() {
		// TODO Auto-generated method stub
		return 0;
	}	
	
}
