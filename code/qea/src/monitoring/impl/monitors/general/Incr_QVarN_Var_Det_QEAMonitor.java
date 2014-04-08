package monitoring.impl.monitors.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import monitoring.impl.IncrementalMonitor;
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
 * @author Giles Reger
 */
public class Incr_QVarN_Var_Det_QEAMonitor extends IncrementalMonitor<QVarN_FVar_Det_QEA> {

	private final IncrementalChecker checker;
	private final HashMap<QBindingImpl,DetConfig> configs;

	BindingRecord[] empty_paths;
	HashMap<String,BindingRecord>[] maps;

	private static final String BLANK = "_";
	//0 for value, 1 for qblank (only qvars), 2 for fblank (some fvars)
	//Important - when creating masks if we have a choice between 1 and 2, pick 2
	// masks should be ordered from specific to general
	int[][][] masks;

        // This records the signatures for each event
	int[][][] sigsLookup;

	public Incr_QVarN_Var_Det_QEAMonitor(QVarN_FVar_Det_QEA qea) {
		super(qea);
		//TODO - consider doing checking internally instead
		checker = IncrementalChecker.make(qea.lambda);
		configs = new HashMap<QBindingImpl,DetConfig>();

                //create a lookup map per event name
		int num_events = qea.getEventsAlphabet().length;
		maps = new HashMap[num_events];
		for(int i=0;i<num_events;i++){
			maps[i] = new HashMap<String,BindingRecord>();
		}
		//make empty paths
		empty_paths = new BindingRecord[num_events];
		for(int i=0;i<num_events;i++){
			empty_paths[i] = new BindingRecord(QBindingImpl.emptyBinding());
		}
                Transition[][] delta = qea.getTransitions();

		//generate masks
		// sigsLookup is generated as a side-effect!
        Map<Integer,Set<ArrayList<Integer>>> sigmap = new HashMap<Integer,Set<ArrayList<Integer>>>();

		masks = new int[num_events][][];
		for(int e=0;e<num_events;e++){
                        Set<ArrayList<Integer>> esigset = new HashSet<ArrayList<Integer>>();
                        sigmap.put(e, esigset);

                        // get the most general signature
                        int[] general_args = null;
                        for(int s=1;s<delta.length;s++){
                                Transition t = delta[s][e];
                                if(t!=null){
                                   int[] targs = t.getVariableNames();
                                   ArrayList<Integer> itargs = new ArrayList<Integer>();
                                   for(int v : targs) itargs.add(v);
                                   esigset.add(itargs);
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
                        //create versions of this signature - not the empty (i.e. all 0)
                        // order from most specific (no 0 replacements) to least
                        //TODO - we can statically determine some masks are not required - do this!

                        // Without this trimming, and assuming no zeros in general_args, the number of
                        // masks is 2^args.length -1 (don't consider empty)
                        // but will will use a recursive function with lists and then turn it into an array!
                        List<List<Integer>> emasks_lists = makeMasks(general_args,0);
                        int[][] emasks = new int[emasks_lists.size()][general_args.length];
                        for(int i=0;i<emasks.length;i++){
                                List<Integer> emasks_list = emasks_lists.get(i);
                                for(int j=0;j<general_args.length;j++) emasks[i][j] = emasks_list.get(j);
                        }
                        masks[e] = emasks;

                }

		//copy stuff from esigmap into sigs_lookup
                sigsLookup = new int[num_events][][];
                for(int e=0;e<num_events;e++){
                	sigsLookup[e] = new int[sigmap.size()][];
                        int i=0;
                        for(ArrayList<Integer> siglist : sigmap.get(e)){
                             int[] sig = new int[siglist.size()];
                             for(int i1=0;i1<sig.length;i1++) sig[i1]=siglist.get(i1);
                             sigsLookup[e][i]=sig;
                             i++;
                        }
                }
	}

        private void makeMasksLevel(int[] args,boolean [] used, int level,List<List<Integer>> masks, int taken){
              if(taken==level){
                      // make the mask
                      List<Integer> mask = new ArrayList<Integer>();
                      for(int i=0;i<args.length;i++){
                              if(used[i]) mask.add(0);
                              else mask.add(args[i]);
                      }
                      masks.add(mask);
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
        private List<List<Integer>> makeMasks(int[] args, int level){
                List<List<Integer>> this_level = new ArrayList<List<Integer>>();

                if(level==0){
                  List<Integer> l = new ArrayList<Integer>();
                  for(int x : args) l.add(x);
                  this_level.add(l);
                }
                else{
                     boolean [] used = new boolean[args.length];
                     makeMasksLevel(args,used,level,this_level,0);
                }

                // Stop when level = args.length-1
                if(level < args.length){
                         List<List<Integer>> next_level = makeMasks(args,level+1);
                         this_level.addAll(next_level); // always append
                }
                return this_level;
        }

	@Override
	public Verdict step(int eventName, Object[] args) {

		//retrieve consistent bindings in order of informativeness

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

				if(record!=null){

					for(int j=record.num_bindings-1;j>=0;j++){
						QBindingImpl binding = record.bindings[j];

						//TODO Check that we haven't already processed this binding

						//Attempt extensions
						int[][] sigs = sigsLookup[eventName];

						//TODO how to check if a binding has been created before
						//An important invariant will be to ensure each qbinding
						//is created exactly once - factory method with lookup?
                                                // 
                                                // use Arrays.hashCode(int[]) and Arrays.equals(int[],int[]) in Binding


						//Update configurations - check relevance first
						// will not be relevant if blanks refer only to quantified variables
						if(!has_q_blanks){
							DetConfig config = configs.get(binding);
							qea.getNextConfig(binding,config,eventName,args);
						}

					}
					if(i==0){
						used_full=true;
						break; // for full optimisation
					}
				}
			}
		}
		if(!used_full){
		//	now the empty mask
			BindingRecord record = empty_paths[eventName];
			for(int j=record.num_bindings-1;j>=0;j++){

				//do the same as above, factor out!

			}			
		}
		
		return checker.verdict(false);
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
		String ret = "not written yet";
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
		
		BindingRecord(QBindingImpl b){
			bindings =new QBindingImpl[5];
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
	
}
