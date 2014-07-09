package benchmark.rovers.mop;
import java.util.Arrays;

import javamoprt.MOPMonitor;

class ResourceLifecycleMOPMonitor_Set extends javamoprt.MOPSet {
	protected ResourceLifecycleMOPMonitor[] elementData;

	public ResourceLifecycleMOPMonitor_Set(){
		size = 0;
		elementData = new ResourceLifecycleMOPMonitor[4];
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
		elementData[size++] = (ResourceLifecycleMOPMonitor)e;
		return true;
	}

	@Override
	public final void endObject(int idnum){
		int numAlive = 0;
		for(int i = 0; i < size; i++){
			ResourceLifecycleMOPMonitor monitor = elementData[i];
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
			ResourceLifecycleMOPMonitor[] oldData = elementData;
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
			ResourceLifecycleMOPMonitor monitor = elementData[i];
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

	public final void event_request(Object r) {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			ResourceLifecycleMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_request(r);
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(r);
				}
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_deny(Object r) {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			ResourceLifecycleMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_deny(r);
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(r);
				}
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_grant(Object r) {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			ResourceLifecycleMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_grant(r);
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(r);
				}
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_cancel(Object r) {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			ResourceLifecycleMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_cancel(r);
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(r);
				}
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_rescind(Object r) {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			ResourceLifecycleMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_rescind(r);
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(r);
				}
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}
}

class ResourceLifecycleMOPMonitor extends javamoprt.MOPMonitor implements Cloneable, javamoprt.MOPObject {
	@Override
	public Object clone() {
		try {
			ResourceLifecycleMOPMonitor ret = (ResourceLifecycleMOPMonitor) super.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	int Prop_1_state;
	static final int Prop_1_transition_request[] = {2, 3, 3, 3};;
	static final int Prop_1_transition_deny[] = {3, 3, 0, 3};;
	static final int Prop_1_transition_grant[] = {3, 3, 1, 3};;
	static final int Prop_1_transition_cancel[] = {3, 0, 3, 3};;
	static final int Prop_1_transition_rescind[] = {3, 1, 3, 3};;

	boolean Prop_1_Category_fail = false;

	public ResourceLifecycleMOPMonitor () {
		Prop_1_state = 0;

	}

	public final void Prop_1_event_request(Object r) {
		MOP_lastevent = 0;

		Prop_1_state = Prop_1_transition_request[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 3;
	}

	public final void Prop_1_event_deny(Object r) {
		MOP_lastevent = 1;

		Prop_1_state = Prop_1_transition_deny[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 3;
	}

	public final void Prop_1_event_grant(Object r) {
		MOP_lastevent = 2;

		Prop_1_state = Prop_1_transition_grant[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 3;
	}

	public final void Prop_1_event_cancel(Object r) {
		MOP_lastevent = 3;

		Prop_1_state = Prop_1_transition_cancel[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 3;
	}

	public final void Prop_1_event_rescind(Object r) {
		MOP_lastevent = 4;

		Prop_1_state = Prop_1_transition_rescind[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 3;
	}

	public final void Prop_1_handler_fail (Object r){
		{
			System.err.println("A resource was used incorrectly");
			System.exit(0);
		}

	}

	public final void reset() {
		MOP_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_fail = false;
	}

	public javamoprt.ref.MOPWeakReference MOPRef_r;

	//alive_parameters_0 = [Object r]
	public boolean alive_parameters_0 = true;

	@Override
	public final void endObject(int idnum){
		switch(idnum){
			case 0:
			alive_parameters_0 = false;
			break;
		}
		switch(MOP_lastevent) {
			case -1:
			return;
			case 0:
			//request
			//alive_r
			if(!alive_parameters_0){
				MOP_terminated = true;
				return;
			}
			break;

			case 1:
			//deny
			//alive_r
			if(!alive_parameters_0){
				MOP_terminated = true;
				return;
			}
			break;

			case 2:
			//grant
			//alive_r
			if(!alive_parameters_0){
				MOP_terminated = true;
				return;
			}
			break;

			case 3:
			//cancel
			//alive_r
			if(!alive_parameters_0){
				MOP_terminated = true;
				return;
			}
			break;

			case 4:
			//rescind
			//alive_r
			if(!alive_parameters_0){
				MOP_terminated = true;
				return;
			}
			break;

		}
		return;
	}

}

public aspect ResourceLifecycleMOPMonitorAspect implements javamoprt.MOPObject {
	javamoprt.map.MOPMapManager ResourceLifecycleMOPMapManager;
	public ResourceLifecycleMOPMonitorAspect(){
		ResourceLifecycleMOPMapManager = new javamoprt.map.MOPMapManager();
		ResourceLifecycleMOPMapManager.start();
	}

	// Declarations for the Lock
	static Object ResourceLifecycleMOP_MOPLock = new Object();

	static boolean ResourceLifecycleMOP_activated = false;

	// Declarations for Indexing Trees
	static javamoprt.map.MOPBasicRefMapOfMonitor ResourceLifecycleMOP_r_Map = new javamoprt.map.MOPBasicRefMapOfMonitor(0);
	static javamoprt.ref.MOPWeakReference ResourceLifecycleMOP_r_Map_cachekey_0 = javamoprt.map.MOPBasicRefMapOfMonitor.NULRef;
	static ResourceLifecycleMOPMonitor ResourceLifecycleMOP_r_Map_cachenode = null;

	// Trees for References
	static javamoprt.map.MOPRefMap ResourceLifecycleMOP_Object_RefMap = ResourceLifecycleMOP_r_Map;

	pointcut MOP_CommonPointCut() : !within(javamoprt.MOPObject+) && !adviceexecution();
	pointcut ResourceLifecycleMOP_request(Object r) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.request(Object)) && args(r)) && MOP_CommonPointCut();
	after (Object r) : ResourceLifecycleMOP_request(r) {
		ResourceLifecycleMOP_activated = true;
		synchronized(ResourceLifecycleMOP_MOPLock) {
			ResourceLifecycleMOPMonitor mainMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			javamoprt.ref.MOPWeakReference TempRef_r;

			// Cache Retrieval
			if (r == ResourceLifecycleMOP_r_Map_cachekey_0.get()) {
				TempRef_r = ResourceLifecycleMOP_r_Map_cachekey_0;

				mainMonitor = ResourceLifecycleMOP_r_Map_cachenode;
			} else {
				TempRef_r = ResourceLifecycleMOP_r_Map.getRef(r);
			}

			if (mainMonitor == null) {
				mainMap = ResourceLifecycleMOP_r_Map;
				mainMonitor = (ResourceLifecycleMOPMonitor)mainMap.getNode(TempRef_r);

				if (mainMonitor == null) {
					mainMonitor = new ResourceLifecycleMOPMonitor();

					mainMonitor.MOPRef_r = TempRef_r;

					ResourceLifecycleMOP_r_Map.putNode(TempRef_r, mainMonitor);
				}

				ResourceLifecycleMOP_r_Map_cachekey_0 = TempRef_r;
				ResourceLifecycleMOP_r_Map_cachenode = mainMonitor;
			}

			mainMonitor.Prop_1_event_request(r);
			if(mainMonitor.Prop_1_Category_fail) {
				mainMonitor.Prop_1_handler_fail(r);
			}
		}
	}

	pointcut ResourceLifecycleMOP_deny(Object r) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.deny(Object)) && args(r)) && MOP_CommonPointCut();
	after (Object r) : ResourceLifecycleMOP_deny(r) {
		ResourceLifecycleMOP_activated = true;
		synchronized(ResourceLifecycleMOP_MOPLock) {
			ResourceLifecycleMOPMonitor mainMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			javamoprt.ref.MOPWeakReference TempRef_r;

			// Cache Retrieval
			if (r == ResourceLifecycleMOP_r_Map_cachekey_0.get()) {
				TempRef_r = ResourceLifecycleMOP_r_Map_cachekey_0;

				mainMonitor = ResourceLifecycleMOP_r_Map_cachenode;
			} else {
				TempRef_r = ResourceLifecycleMOP_r_Map.getRef(r);
			}

			if (mainMonitor == null) {
				mainMap = ResourceLifecycleMOP_r_Map;
				mainMonitor = (ResourceLifecycleMOPMonitor)mainMap.getNode(TempRef_r);

				if (mainMonitor == null) {
					mainMonitor = new ResourceLifecycleMOPMonitor();

					mainMonitor.MOPRef_r = TempRef_r;

					ResourceLifecycleMOP_r_Map.putNode(TempRef_r, mainMonitor);
				}

				ResourceLifecycleMOP_r_Map_cachekey_0 = TempRef_r;
				ResourceLifecycleMOP_r_Map_cachenode = mainMonitor;
			}

			mainMonitor.Prop_1_event_deny(r);
			if(mainMonitor.Prop_1_Category_fail) {
				mainMonitor.Prop_1_handler_fail(r);
			}
		}
	}

	pointcut ResourceLifecycleMOP_grant(Object r) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.grant_rl(Object)) && args(r)) && MOP_CommonPointCut();
	after (Object r) : ResourceLifecycleMOP_grant(r) {
		ResourceLifecycleMOP_activated = true;
		synchronized(ResourceLifecycleMOP_MOPLock) {
			ResourceLifecycleMOPMonitor mainMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			javamoprt.ref.MOPWeakReference TempRef_r;

			// Cache Retrieval
			if (r == ResourceLifecycleMOP_r_Map_cachekey_0.get()) {
				TempRef_r = ResourceLifecycleMOP_r_Map_cachekey_0;

				mainMonitor = ResourceLifecycleMOP_r_Map_cachenode;
			} else {
				TempRef_r = ResourceLifecycleMOP_r_Map.getRef(r);
			}

			if (mainMonitor == null) {
				mainMap = ResourceLifecycleMOP_r_Map;
				mainMonitor = (ResourceLifecycleMOPMonitor)mainMap.getNode(TempRef_r);

				if (mainMonitor == null) {
					mainMonitor = new ResourceLifecycleMOPMonitor();

					mainMonitor.MOPRef_r = TempRef_r;

					ResourceLifecycleMOP_r_Map.putNode(TempRef_r, mainMonitor);
				}

				ResourceLifecycleMOP_r_Map_cachekey_0 = TempRef_r;
				ResourceLifecycleMOP_r_Map_cachenode = mainMonitor;
			}

			mainMonitor.Prop_1_event_grant(r);
			if(mainMonitor.Prop_1_Category_fail) {
				mainMonitor.Prop_1_handler_fail(r);
			}
		}
	}

	pointcut ResourceLifecycleMOP_cancel(Object r) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.cancel_rl(Object)) && args(r)) && MOP_CommonPointCut();
	after (Object r) : ResourceLifecycleMOP_cancel(r) {
		ResourceLifecycleMOP_activated = true;
		synchronized(ResourceLifecycleMOP_MOPLock) {
			ResourceLifecycleMOPMonitor mainMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			javamoprt.ref.MOPWeakReference TempRef_r;

			// Cache Retrieval
			if (r == ResourceLifecycleMOP_r_Map_cachekey_0.get()) {
				TempRef_r = ResourceLifecycleMOP_r_Map_cachekey_0;

				mainMonitor = ResourceLifecycleMOP_r_Map_cachenode;
			} else {
				TempRef_r = ResourceLifecycleMOP_r_Map.getRef(r);
			}

			if (mainMonitor == null) {
				mainMap = ResourceLifecycleMOP_r_Map;
				mainMonitor = (ResourceLifecycleMOPMonitor)mainMap.getNode(TempRef_r);

				if (mainMonitor == null) {
					mainMonitor = new ResourceLifecycleMOPMonitor();

					mainMonitor.MOPRef_r = TempRef_r;

					ResourceLifecycleMOP_r_Map.putNode(TempRef_r, mainMonitor);
				}

				ResourceLifecycleMOP_r_Map_cachekey_0 = TempRef_r;
				ResourceLifecycleMOP_r_Map_cachenode = mainMonitor;
			}

			mainMonitor.Prop_1_event_cancel(r);
			if(mainMonitor.Prop_1_Category_fail) {
				mainMonitor.Prop_1_handler_fail(r);
			}
		}
	}

	pointcut ResourceLifecycleMOP_rescind(Object r) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.rescind(Object)) && args(r)) && MOP_CommonPointCut();
	after (Object r) : ResourceLifecycleMOP_rescind(r) {
		ResourceLifecycleMOP_activated = true;
		synchronized(ResourceLifecycleMOP_MOPLock) {
			ResourceLifecycleMOPMonitor mainMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			javamoprt.ref.MOPWeakReference TempRef_r;

			// Cache Retrieval
			if (r == ResourceLifecycleMOP_r_Map_cachekey_0.get()) {
				TempRef_r = ResourceLifecycleMOP_r_Map_cachekey_0;

				mainMonitor = ResourceLifecycleMOP_r_Map_cachenode;
			} else {
				TempRef_r = ResourceLifecycleMOP_r_Map.getRef(r);
			}

			if (mainMonitor == null) {
				mainMap = ResourceLifecycleMOP_r_Map;
				mainMonitor = (ResourceLifecycleMOPMonitor)mainMap.getNode(TempRef_r);

				if (mainMonitor == null) {
					mainMonitor = new ResourceLifecycleMOPMonitor();

					mainMonitor.MOPRef_r = TempRef_r;

					ResourceLifecycleMOP_r_Map.putNode(TempRef_r, mainMonitor);
				}

				ResourceLifecycleMOP_r_Map_cachekey_0 = TempRef_r;
				ResourceLifecycleMOP_r_Map_cachenode = mainMonitor;
			}

			mainMonitor.Prop_1_event_rescind(r);
			if(mainMonitor.Prop_1_Category_fail) {
				mainMonitor.Prop_1_handler_fail(r);
			}
		}
	}

}
