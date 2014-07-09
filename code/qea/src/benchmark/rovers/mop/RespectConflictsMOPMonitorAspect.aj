package benchmark.rovers.mop;
import java.util.Arrays;

import javamoprt.MOPMonitor;

class RespectConflictsMOPMonitor_Set extends javamoprt.MOPSet {
	protected RespectConflictsMOPMonitor[] elementData;

	public RespectConflictsMOPMonitor_Set(){
		size = 0;
		elementData = new RespectConflictsMOPMonitor[4];
	}

	@Override
	public final int size(){
		while(size > 0 && elementData[size-1].MOP_terminated) {
			elementData[--size] = null;
		}
		return size;
	}

	@Override
	public final boolean add(MOPMonitor e){
		ensureCapacity();
		elementData[size++] = (RespectConflictsMOPMonitor)e;
		return true;
	}

	@Override
	public final void endObject(int idnum){
		int numAlive = 0;
		for(int i = 0; i < size; i++){
			RespectConflictsMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				monitor.endObject(idnum);
			}
			if(!monitor.MOP_terminated){
				elementData[numAlive++] = monitor;
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}

	@Override
	public final boolean alive(){
		for(int i = 0; i < size; i++){
			MOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				return true;
			}
		}
		return false;
	}

	@Override
	public final void endObjectAndClean(int idnum){
		int size = this.size;
		this.size = 0;
		for(int i = size - 1; i >= 0; i--){
			MOPMonitor monitor = elementData[i];
			if(monitor != null && !monitor.MOP_terminated){
				monitor.endObject(idnum);
			}
			elementData[i] = null;
		}
		elementData = null;
	}

	@Override
	public final void ensureCapacity() {
		int oldCapacity = elementData.length;
		if (size + 1 > oldCapacity) {
			cleanup();
		}
		if (size + 1 > oldCapacity) {
			RespectConflictsMOPMonitor[] oldData = elementData;
			int newCapacity = oldCapacity * 3 / 2 + 1;
			if (newCapacity < size + 1){
				newCapacity = size + 1;
			}
			elementData = Arrays.copyOf(oldData, newCapacity);
		}
	}

	public final void cleanup() {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			RespectConflictsMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_conflict(Object r1, Object r2) {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			RespectConflictsMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_conflict(r1, r2);
				if(monitor.Prop_1_Category_error) {
					monitor.Prop_1_handler_error(r1, r2);
				}
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_grant_r1(Object r1) {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			RespectConflictsMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_grant_r1(r1);
				if(monitor.Prop_1_Category_error) {
					monitor.Prop_1_handler_error(r1, null);
				}
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_grant_r2(Object r2) {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			RespectConflictsMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_grant_r2(r2);
				if(monitor.Prop_1_Category_error) {
					monitor.Prop_1_handler_error(null, r2);
				}
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_release_r1(Object r1) {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			RespectConflictsMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_release_r1(r1);
				if(monitor.Prop_1_Category_error) {
					monitor.Prop_1_handler_error(r1, null);
				}
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_release_r2(Object r2) {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			RespectConflictsMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_release_r2(r2);
				if(monitor.Prop_1_Category_error) {
					monitor.Prop_1_handler_error(null, r2);
				}
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}
}

class RespectConflictsMOPMonitor extends javamoprt.MOPMonitor implements Cloneable, javamoprt.MOPObject {
	public long tau = -1;
	@Override
	public Object clone() {
		try {
			RespectConflictsMOPMonitor ret = (RespectConflictsMOPMonitor) super.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	int Prop_1_state;
	static final int Prop_1_transition_conflict[] = {2, 5, 2, 5, 5, 5};;
	static final int Prop_1_transition_grant_r1[] = {5, 5, 1, 4, 5, 5};;
	static final int Prop_1_transition_grant_r2[] = {5, 4, 3, 5, 5, 5};;
	static final int Prop_1_transition_release_r1[] = {5, 2, 5, 5, 5, 5};;
	static final int Prop_1_transition_release_r2[] = {5, 5, 5, 2, 5, 5};;

	boolean Prop_1_Category_error = false;

	public RespectConflictsMOPMonitor () {
		Prop_1_state = 0;

	}

	public final void Prop_1_event_conflict(Object r1, Object r2) {
		MOP_lastevent = 0;

		Prop_1_state = Prop_1_transition_conflict[Prop_1_state];
		Prop_1_Category_error = Prop_1_state == 4;
	}

	public final void Prop_1_event_grant_r1(Object r1) {
		MOP_lastevent = 1;

		Prop_1_state = Prop_1_transition_grant_r1[Prop_1_state];
		Prop_1_Category_error = Prop_1_state == 4;
	}

	public final void Prop_1_event_grant_r2(Object r2) {
		MOP_lastevent = 2;

		Prop_1_state = Prop_1_transition_grant_r2[Prop_1_state];
		Prop_1_Category_error = Prop_1_state == 4;
	}

	public final void Prop_1_event_release_r1(Object r1) {
		MOP_lastevent = 3;

		Prop_1_state = Prop_1_transition_release_r1[Prop_1_state];
		Prop_1_Category_error = Prop_1_state == 4;
	}

	public final void Prop_1_event_release_r2(Object r2) {
		MOP_lastevent = 4;

		Prop_1_state = Prop_1_transition_release_r2[Prop_1_state];
		Prop_1_Category_error = Prop_1_state == 4;
	}

	public final void Prop_1_handler_error (Object r1, Object r2){
		{
			System.err.println("conflicted resources granted at the same time");
		}

	}

	public final void reset() {
		MOP_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_error = false;
	}

	public javamoprt.ref.MOPMultiTagWeakReference MOPRef_r1;
	public javamoprt.ref.MOPMultiTagWeakReference MOPRef_r2;

	//alive_parameters_0 = [Object r1, Object r2]
	public boolean alive_parameters_0 = true;
	//alive_parameters_1 = [Object r2]
	public boolean alive_parameters_1 = true;
	//alive_parameters_2 = [Object r1]
	public boolean alive_parameters_2 = true;

	@Override
	public final void endObject(int idnum){
		switch(idnum){
			case 0:
			alive_parameters_0 = false;
			alive_parameters_2 = false;
			break;
			case 1:
			alive_parameters_0 = false;
			alive_parameters_1 = false;
			break;
		}
		switch(MOP_lastevent) {
			case -1:
			return;
			case 0:
			//conflict
			//alive_r1 && alive_r2
			if(!alive_parameters_0){
				MOP_terminated = true;
				return;
			}
			break;

			case 1:
			//grant_r1
			//alive_r2
			if(!alive_parameters_1){
				MOP_terminated = true;
				return;
			}
			break;

			case 2:
			//grant_r2
			//alive_r1
			if(!alive_parameters_2){
				MOP_terminated = true;
				return;
			}
			break;

			case 3:
			//release_r1
			//alive_r1 && alive_r2
			if(!alive_parameters_0){
				MOP_terminated = true;
				return;
			}
			break;

			case 4:
			//release_r2
			//alive_r1 && alive_r2
			if(!alive_parameters_0){
				MOP_terminated = true;
				return;
			}
			break;

		}
		return;
	}

}

public aspect RespectConflictsMOPMonitorAspect implements javamoprt.MOPObject {
	javamoprt.map.MOPMapManager RespectConflictsMOPMapManager;
	public RespectConflictsMOPMonitorAspect(){
		RespectConflictsMOPMapManager = new javamoprt.map.MOPMapManager();
		RespectConflictsMOPMapManager.start();
	}

	// Declarations for the Lock
	static Object RespectConflictsMOP_MOPLock = new Object();

	// Declarations for Timestamps
	static long RespectConflictsMOP_timestamp = 1;

	static boolean RespectConflictsMOP_activated = false;

	// Declarations for Indexing Trees
	static javamoprt.map.MOPAbstractMap RespectConflictsMOP_r2_Map = new javamoprt.map.MOPMapOfSetMon(1);
	static javamoprt.ref.MOPMultiTagWeakReference RespectConflictsMOP_r2_Map_cachekey_1 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static RespectConflictsMOPMonitor_Set RespectConflictsMOP_r2_Map_cacheset = null;
	static RespectConflictsMOPMonitor RespectConflictsMOP_r2_Map_cachenode = null;
	static javamoprt.map.MOPAbstractMap RespectConflictsMOP_r1_r2_Map = new javamoprt.map.MOPMapOfAll(0);
	static javamoprt.ref.MOPMultiTagWeakReference RespectConflictsMOP_r1_r2_Map_cachekey_0 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static javamoprt.ref.MOPMultiTagWeakReference RespectConflictsMOP_r1_r2_Map_cachekey_1 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static RespectConflictsMOPMonitor RespectConflictsMOP_r1_r2_Map_cachenode = null;
	static javamoprt.ref.MOPMultiTagWeakReference RespectConflictsMOP_r1_Map_cachekey_0 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static RespectConflictsMOPMonitor_Set RespectConflictsMOP_r1_Map_cacheset = null;
	static RespectConflictsMOPMonitor RespectConflictsMOP_r1_Map_cachenode = null;

	// Trees for References
	static javamoprt.map.MOPRefMap RespectConflictsMOP_Object_RefMap = new javamoprt.map.MOPMultiTagRefMap(2);

	pointcut MOP_CommonPointCut() : !within(javamoprt.MOPObject+) && !adviceexecution();
	pointcut RespectConflictsMOP_conflict(Object r1, Object r2) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.conflict(Object, Object)) && args(r1, r2)) && MOP_CommonPointCut();
	after (Object r1, Object r2) : RespectConflictsMOP_conflict(r1, r2) {
		RespectConflictsMOP_activated = true;
		synchronized(RespectConflictsMOP_MOPLock) {
			Object obj;
			javamoprt.map.MOPMap tempMap;
			RespectConflictsMOPMonitor mainMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			RespectConflictsMOPMonitor_Set monitors = null;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_r1;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_r2;

			// Cache Retrieval
			if (r1 == RespectConflictsMOP_r1_r2_Map_cachekey_0.get() && r2 == RespectConflictsMOP_r1_r2_Map_cachekey_1.get()) {
				TempRef_r1 = RespectConflictsMOP_r1_r2_Map_cachekey_0;
				TempRef_r2 = RespectConflictsMOP_r1_r2_Map_cachekey_1;

				mainMonitor = RespectConflictsMOP_r1_r2_Map_cachenode;
			} else {
				TempRef_r1 = RespectConflictsMOP_Object_RefMap.getMultiTagRef(r1, thisJoinPoint.getStaticPart().getId());
				TempRef_r2 = RespectConflictsMOP_Object_RefMap.getMultiTagRef(r2, thisJoinPoint.getStaticPart().getId());
			}

			if (mainMonitor == null) {
				tempMap = RespectConflictsMOP_r1_r2_Map;
				obj = tempMap.getMap(TempRef_r1);
				if (obj == null) {
					obj = new javamoprt.map.MOPMapOfMonitor(1);
					tempMap.putMap(TempRef_r1, obj);
				}
				mainMap = (javamoprt.map.MOPAbstractMap)obj;
				mainMonitor = (RespectConflictsMOPMonitor)mainMap.getNode(TempRef_r2);

				if (mainMonitor == null) {
					mainMonitor = new RespectConflictsMOPMonitor();

					mainMonitor.MOPRef_r1 = TempRef_r1;
					mainMonitor.MOPRef_r2 = TempRef_r2;

					mainMap.putNode(TempRef_r2, mainMonitor);
					mainMonitor.tau = RespectConflictsMOP_timestamp;
					if (TempRef_r1.tau[0] == -1){
						TempRef_r1.tau[0] = RespectConflictsMOP_timestamp;
					}
					if (TempRef_r2.tau[0] == -1){
						TempRef_r2.tau[0] = RespectConflictsMOP_timestamp;
					}
					RespectConflictsMOP_timestamp++;

					tempMap = RespectConflictsMOP_r2_Map;
					obj = tempMap.getSet(TempRef_r2);
					monitors = (RespectConflictsMOPMonitor_Set)obj;
					if (monitors == null) {
						monitors = new RespectConflictsMOPMonitor_Set();
						tempMap.putSet(TempRef_r2, monitors);
					}
					monitors.add(mainMonitor);

					tempMap = RespectConflictsMOP_r1_r2_Map;
					obj = tempMap.getSet(TempRef_r1);
					monitors = (RespectConflictsMOPMonitor_Set)obj;
					if (monitors == null) {
						monitors = new RespectConflictsMOPMonitor_Set();
						tempMap.putSet(TempRef_r1, monitors);
					}
					monitors.add(mainMonitor);
				}

				RespectConflictsMOP_r1_r2_Map_cachekey_0 = TempRef_r1;
				RespectConflictsMOP_r1_r2_Map_cachekey_1 = TempRef_r2;
				RespectConflictsMOP_r1_r2_Map_cachenode = mainMonitor;
			}

			mainMonitor.Prop_1_event_conflict(r1, r2);
			if(mainMonitor.Prop_1_Category_error) {
				mainMonitor.Prop_1_handler_error(r1, r2);
			}
		}
	}

	pointcut RespectConflictsMOP_grant_r1(Object r1) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.grant_rc(Object)) && args(r1)) && MOP_CommonPointCut();
	after (Object r1) : RespectConflictsMOP_grant_r1(r1) {
		RespectConflictsMOP_activated = true;
		synchronized(RespectConflictsMOP_MOPLock) {
			RespectConflictsMOPMonitor mainMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			RespectConflictsMOPMonitor_Set mainSet = null;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_r1;

			// Cache Retrieval
			if (r1 == RespectConflictsMOP_r1_Map_cachekey_0.get()) {
				TempRef_r1 = RespectConflictsMOP_r1_Map_cachekey_0;

				mainSet = RespectConflictsMOP_r1_Map_cacheset;
				mainMonitor = RespectConflictsMOP_r1_Map_cachenode;
			} else {
				TempRef_r1 = RespectConflictsMOP_Object_RefMap.getMultiTagRef(r1, thisJoinPoint.getStaticPart().getId());
			}

			if (mainSet == null || mainMonitor == null) {
				mainMap = RespectConflictsMOP_r1_r2_Map;
				mainMonitor = (RespectConflictsMOPMonitor)mainMap.getNode(TempRef_r1);
				mainSet = (RespectConflictsMOPMonitor_Set)mainMap.getSet(TempRef_r1);
				if (mainSet == null){
					mainSet = new RespectConflictsMOPMonitor_Set();
					mainMap.putSet(TempRef_r1, mainSet);
				}

				if (mainMonitor == null) {
					mainMonitor = new RespectConflictsMOPMonitor();

					mainMonitor.MOPRef_r1 = TempRef_r1;

					RespectConflictsMOP_r1_r2_Map.putNode(TempRef_r1, mainMonitor);
					mainSet.add(mainMonitor);
					mainMonitor.tau = RespectConflictsMOP_timestamp;
					if (TempRef_r1.tau[0] == -1){
						TempRef_r1.tau[0] = RespectConflictsMOP_timestamp;
					}
					RespectConflictsMOP_timestamp++;
				}

				RespectConflictsMOP_r1_Map_cachekey_0 = TempRef_r1;
				RespectConflictsMOP_r1_Map_cacheset = mainSet;
				RespectConflictsMOP_r1_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_grant_r1(r1);
			}
		}
	}

	pointcut RespectConflictsMOP_grant_r2(Object r2) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.grant_rc(Object)) && args(r2)) && MOP_CommonPointCut();
	after (Object r2) : RespectConflictsMOP_grant_r2(r2) {
		RespectConflictsMOP_activated = true;
		synchronized(RespectConflictsMOP_MOPLock) {
			RespectConflictsMOPMonitor mainMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			RespectConflictsMOPMonitor_Set mainSet = null;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_r2;

			// Cache Retrieval
			if (r2 == RespectConflictsMOP_r2_Map_cachekey_1.get()) {
				TempRef_r2 = RespectConflictsMOP_r2_Map_cachekey_1;

				mainSet = RespectConflictsMOP_r2_Map_cacheset;
				mainMonitor = RespectConflictsMOP_r2_Map_cachenode;
			} else {
				TempRef_r2 = RespectConflictsMOP_Object_RefMap.getMultiTagRef(r2, thisJoinPoint.getStaticPart().getId());
			}

			if (mainSet == null || mainMonitor == null) {
				mainMap = RespectConflictsMOP_r2_Map;
				mainMonitor = (RespectConflictsMOPMonitor)mainMap.getNode(TempRef_r2);
				mainSet = (RespectConflictsMOPMonitor_Set)mainMap.getSet(TempRef_r2);
				if (mainSet == null){
					mainSet = new RespectConflictsMOPMonitor_Set();
					mainMap.putSet(TempRef_r2, mainSet);
				}

				if (mainMonitor == null) {
					mainMonitor = new RespectConflictsMOPMonitor();

					mainMonitor.MOPRef_r2 = TempRef_r2;

					RespectConflictsMOP_r2_Map.putNode(TempRef_r2, mainMonitor);
					mainSet.add(mainMonitor);
					mainMonitor.tau = RespectConflictsMOP_timestamp;
					if (TempRef_r2.tau[0] == -1){
						TempRef_r2.tau[0] = RespectConflictsMOP_timestamp;
					}
					RespectConflictsMOP_timestamp++;
				}

				RespectConflictsMOP_r2_Map_cachekey_1 = TempRef_r2;
				RespectConflictsMOP_r2_Map_cacheset = mainSet;
				RespectConflictsMOP_r2_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_grant_r2(r2);
			}
		}
	}

	pointcut RespectConflictsMOP_release_r1(Object r1) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.cancel_rc(Object)) && args(r1)) && MOP_CommonPointCut();
	after (Object r1) : RespectConflictsMOP_release_r1(r1) {
		RespectConflictsMOP_activated = true;
		synchronized(RespectConflictsMOP_MOPLock) {
			RespectConflictsMOPMonitor mainMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			RespectConflictsMOPMonitor_Set mainSet = null;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_r1;

			// Cache Retrieval
			if (r1 == RespectConflictsMOP_r1_Map_cachekey_0.get()) {
				TempRef_r1 = RespectConflictsMOP_r1_Map_cachekey_0;

				mainSet = RespectConflictsMOP_r1_Map_cacheset;
				mainMonitor = RespectConflictsMOP_r1_Map_cachenode;
			} else {
				TempRef_r1 = RespectConflictsMOP_Object_RefMap.getMultiTagRef(r1, thisJoinPoint.getStaticPart().getId());
			}

			if (mainSet == null || mainMonitor == null) {
				mainMap = RespectConflictsMOP_r1_r2_Map;
				mainMonitor = (RespectConflictsMOPMonitor)mainMap.getNode(TempRef_r1);
				mainSet = (RespectConflictsMOPMonitor_Set)mainMap.getSet(TempRef_r1);
				if (mainSet == null){
					mainSet = new RespectConflictsMOPMonitor_Set();
					mainMap.putSet(TempRef_r1, mainSet);
				}

				if (mainMonitor == null) {
					mainMonitor = new RespectConflictsMOPMonitor();

					mainMonitor.MOPRef_r1 = TempRef_r1;

					RespectConflictsMOP_r1_r2_Map.putNode(TempRef_r1, mainMonitor);
					mainSet.add(mainMonitor);
					mainMonitor.tau = RespectConflictsMOP_timestamp;
					if (TempRef_r1.tau[0] == -1){
						TempRef_r1.tau[0] = RespectConflictsMOP_timestamp;
					}
					RespectConflictsMOP_timestamp++;
				}

				RespectConflictsMOP_r1_Map_cachekey_0 = TempRef_r1;
				RespectConflictsMOP_r1_Map_cacheset = mainSet;
				RespectConflictsMOP_r1_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_release_r1(r1);
			}
		}
	}

	pointcut RespectConflictsMOP_release_r2(Object r2) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.cancel_rc(Object)) && args(r2)) && MOP_CommonPointCut();
	after (Object r2) : RespectConflictsMOP_release_r2(r2) {
		RespectConflictsMOP_activated = true;
		synchronized(RespectConflictsMOP_MOPLock) {
			RespectConflictsMOPMonitor mainMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			RespectConflictsMOPMonitor_Set mainSet = null;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_r2;

			// Cache Retrieval
			if (r2 == RespectConflictsMOP_r2_Map_cachekey_1.get()) {
				TempRef_r2 = RespectConflictsMOP_r2_Map_cachekey_1;

				mainSet = RespectConflictsMOP_r2_Map_cacheset;
				mainMonitor = RespectConflictsMOP_r2_Map_cachenode;
			} else {
				TempRef_r2 = RespectConflictsMOP_Object_RefMap.getMultiTagRef(r2, thisJoinPoint.getStaticPart().getId());
			}

			if (mainSet == null || mainMonitor == null) {
				mainMap = RespectConflictsMOP_r2_Map;
				mainMonitor = (RespectConflictsMOPMonitor)mainMap.getNode(TempRef_r2);
				mainSet = (RespectConflictsMOPMonitor_Set)mainMap.getSet(TempRef_r2);
				if (mainSet == null){
					mainSet = new RespectConflictsMOPMonitor_Set();
					mainMap.putSet(TempRef_r2, mainSet);
				}

				if (mainMonitor == null) {
					mainMonitor = new RespectConflictsMOPMonitor();

					mainMonitor.MOPRef_r2 = TempRef_r2;

					RespectConflictsMOP_r2_Map.putNode(TempRef_r2, mainMonitor);
					mainSet.add(mainMonitor);
					mainMonitor.tau = RespectConflictsMOP_timestamp;
					if (TempRef_r2.tau[0] == -1){
						TempRef_r2.tau[0] = RespectConflictsMOP_timestamp;
					}
					RespectConflictsMOP_timestamp++;
				}

				RespectConflictsMOP_r2_Map_cachekey_1 = TempRef_r2;
				RespectConflictsMOP_r2_Map_cacheset = mainSet;
				RespectConflictsMOP_r2_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_release_r2(r2);
			}
		}
	}

}
