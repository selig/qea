package qea.benchmark.rovers.mop;
import java.util.Arrays;

import javamoprt.MOPMonitor;

class ReleaseResourceMOPMonitor_Set extends javamoprt.MOPSet {
	protected ReleaseResourceMOPMonitor[] elementData;

	public ReleaseResourceMOPMonitor_Set(){
		size = 0;
		elementData = new ReleaseResourceMOPMonitor[4];
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
		elementData[size++] = (ReleaseResourceMOPMonitor)e;
		return true;
	}

	@Override
	public final void endObject(int idnum){
		int numAlive = 0;
		for(int i = 0; i < size; i++){
			ReleaseResourceMOPMonitor monitor = elementData[i];
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
			ReleaseResourceMOPMonitor[] oldData = elementData;
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
			ReleaseResourceMOPMonitor monitor = elementData[i];
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

	public final void event_schedule(Object t, Object c) {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			ReleaseResourceMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_schedule(t, c);
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(t, c, null);
				}
				if(monitor.Prop_1_Category_error) {
					monitor.Prop_1_handler_error(t, c, null);
				}
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_grant(Object t, Object r) {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			ReleaseResourceMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_grant(t, r);
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(t, null, r);
				}
				if(monitor.Prop_1_Category_error) {
					monitor.Prop_1_handler_error(t, null, r);
				}
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_cancel(Object t, Object r) {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			ReleaseResourceMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_cancel(t, r);
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(t, null, r);
				}
				if(monitor.Prop_1_Category_error) {
					monitor.Prop_1_handler_error(t, null, r);
				}
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_finish(Object c) {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			ReleaseResourceMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_finish(c);
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(null, c, null);
				}
				if(monitor.Prop_1_Category_error) {
					monitor.Prop_1_handler_error(null, c, null);
				}
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}
}

class ReleaseResourceMOPMonitor extends javamoprt.MOPMonitor implements Cloneable, javamoprt.MOPObject {
	public long tau = -1;
	@Override
	public Object clone() {
		try {
			ReleaseResourceMOPMonitor ret = (ReleaseResourceMOPMonitor) super.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}
	String last_event;

	int Prop_1_state;
	static final int Prop_1_transition_schedule[] = {4, 5, 5, 5, 5, 5};;
	static final int Prop_1_transition_grant[] = {5, 5, 5, 5, 2, 5};;
	static final int Prop_1_transition_cancel[] = {5, 5, 4, 5, 5, 5};;
	static final int Prop_1_transition_finish[] = {5, 5, 3, 5, 1, 5};;

	boolean Prop_1_Category_fail = false;
	boolean Prop_1_Category_error = false;

	public ReleaseResourceMOPMonitor () {
		Prop_1_state = 0;

	}

	public final void Prop_1_event_schedule(Object t, Object c) {
		MOP_lastevent = 0;

		Prop_1_state = Prop_1_transition_schedule[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 5;
		Prop_1_Category_error = Prop_1_state == 3;
		{
			last_event = "schedule";
		}
	}

	public final void Prop_1_event_grant(Object t, Object r) {
		MOP_lastevent = 1;

		Prop_1_state = Prop_1_transition_grant[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 5;
		Prop_1_Category_error = Prop_1_state == 3;
		{
			last_event = "grant";
		}
	}

	public final void Prop_1_event_cancel(Object t, Object r) {
		MOP_lastevent = 2;

		Prop_1_state = Prop_1_transition_cancel[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 5;
		Prop_1_Category_error = Prop_1_state == 3;
		{
			last_event = "cancel";
		}
	}

	public final void Prop_1_event_finish(Object c) {
		MOP_lastevent = 3;

		Prop_1_state = Prop_1_transition_finish[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 5;
		Prop_1_Category_error = Prop_1_state == 3;
		{
			last_event = "finish";
		}
	}

	public final void Prop_1_handler_fail (Object t, Object c, Object r){
		{
			System.err.println("error in ReleaseResource with " + last_event);
		}

	}

	public final void Prop_1_handler_error (Object t, Object c, Object r){
		{
			System.err.println("finished with resource granted");
		}

	}

	public final void reset() {
		MOP_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_fail = false;
		Prop_1_Category_error = false;
	}

	public javamoprt.ref.MOPMultiTagWeakReference MOPRef_t;
	public javamoprt.ref.MOPMultiTagWeakReference MOPRef_c;
	public javamoprt.ref.MOPMultiTagWeakReference MOPRef_r;

	//alive_parameters_0 = [Object c]
	public boolean alive_parameters_0 = true;
	//alive_parameters_1 = [Object t, Object r]
	public boolean alive_parameters_1 = true;

	@Override
	public final void endObject(int idnum){
		switch(idnum){
			case 0:
			alive_parameters_1 = false;
			break;
			case 1:
			alive_parameters_0 = false;
			break;
			case 2:
			alive_parameters_1 = false;
			break;
		}
		switch(MOP_lastevent) {
			case -1:
			return;
			case 0:
			//schedule
			//alive_c || alive_t && alive_r
			if(!(alive_parameters_0 || alive_parameters_1)){
				MOP_terminated = true;
				return;
			}
			break;

			case 1:
			//grant
			//alive_c || alive_t && alive_r
			if(!(alive_parameters_0 || alive_parameters_1)){
				MOP_terminated = true;
				return;
			}
			break;

			case 2:
			//cancel
			//alive_c || alive_t && alive_r
			if(!(alive_parameters_0 || alive_parameters_1)){
				MOP_terminated = true;
				return;
			}
			break;

			case 3:
			//finish
			//alive_c || alive_t && alive_r
			if(!(alive_parameters_0 || alive_parameters_1)){
				MOP_terminated = true;
				return;
			}
			break;

		}
		return;
	}

}

public aspect ReleaseResourceMOPMonitorAspect implements javamoprt.MOPObject {
	javamoprt.map.MOPMapManager ReleaseResourceMOPMapManager;
	public ReleaseResourceMOPMonitorAspect(){
		ReleaseResourceMOPMapManager = new javamoprt.map.MOPMapManager();
		ReleaseResourceMOPMapManager.start();
	}

	// Declarations for the Lock
	static Object ReleaseResourceMOP_MOPLock = new Object();

	// Declarations for Timestamps
	static long ReleaseResourceMOP_timestamp = 1;

	static boolean ReleaseResourceMOP_activated = false;

	// Declarations for Indexing Trees
	static javamoprt.map.MOPAbstractMap ReleaseResourceMOP_c_Map = new javamoprt.map.MOPMapOfSetMon(1);
	static javamoprt.ref.MOPMultiTagWeakReference ReleaseResourceMOP_c_Map_cachekey_1 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static ReleaseResourceMOPMonitor_Set ReleaseResourceMOP_c_Map_cacheset = null;
	static ReleaseResourceMOPMonitor ReleaseResourceMOP_c_Map_cachenode = null;
	static javamoprt.map.MOPAbstractMap ReleaseResourceMOP_t_c_r_Map = new javamoprt.map.MOPMapOfAll(0);
	static javamoprt.ref.MOPMultiTagWeakReference ReleaseResourceMOP_t_c_r_Map_cachekey_0 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static javamoprt.ref.MOPMultiTagWeakReference ReleaseResourceMOP_t_c_r_Map_cachekey_1 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static javamoprt.ref.MOPMultiTagWeakReference ReleaseResourceMOP_t_c_r_Map_cachekey_2 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static ReleaseResourceMOPMonitor ReleaseResourceMOP_t_c_r_Map_cachenode = null;
	static javamoprt.map.MOPAbstractMap ReleaseResourceMOP_t_r_Map = new javamoprt.map.MOPMapOfAll(0);
	static javamoprt.ref.MOPMultiTagWeakReference ReleaseResourceMOP_t_r_Map_cachekey_0 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static javamoprt.ref.MOPMultiTagWeakReference ReleaseResourceMOP_t_r_Map_cachekey_2 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static ReleaseResourceMOPMonitor_Set ReleaseResourceMOP_t_r_Map_cacheset = null;
	static ReleaseResourceMOPMonitor ReleaseResourceMOP_t_r_Map_cachenode = null;
	static javamoprt.ref.MOPMultiTagWeakReference ReleaseResourceMOP_t_c_Map_cachekey_0 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static javamoprt.ref.MOPMultiTagWeakReference ReleaseResourceMOP_t_c_Map_cachekey_1 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static ReleaseResourceMOPMonitor_Set ReleaseResourceMOP_t_c_Map_cacheset = null;
	static ReleaseResourceMOPMonitor ReleaseResourceMOP_t_c_Map_cachenode = null;
	static javamoprt.map.MOPAbstractMap ReleaseResourceMOP_t__To__t_c_Map = new javamoprt.map.MOPMapOfSetMon(0);

	// Trees for References
	static javamoprt.map.MOPRefMap ReleaseResourceMOP_Object_RefMap = new javamoprt.map.MOPMultiTagRefMap(3);

	pointcut MOP_CommonPointCut() : !within(javamoprt.MOPObject+) && !adviceexecution();
	pointcut ReleaseResourceMOP_schedule(Object t, Object c) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.schedule(Object, Object)) && args(t, c)) && MOP_CommonPointCut();
	after (Object t, Object c) : ReleaseResourceMOP_schedule(t, c) {
		ReleaseResourceMOP_activated = true;
		synchronized(ReleaseResourceMOP_MOPLock) {
			Object obj;
			javamoprt.map.MOPMap tempMap;
			ReleaseResourceMOPMonitor mainMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			ReleaseResourceMOPMonitor_Set mainSet = null;
			ReleaseResourceMOPMonitor_Set monitors = null;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_t;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_c;

			// Cache Retrieval
			if (t == ReleaseResourceMOP_t_c_Map_cachekey_0.get() && c == ReleaseResourceMOP_t_c_Map_cachekey_1.get()) {
				TempRef_t = ReleaseResourceMOP_t_c_Map_cachekey_0;
				TempRef_c = ReleaseResourceMOP_t_c_Map_cachekey_1;

				mainSet = ReleaseResourceMOP_t_c_Map_cacheset;
				mainMonitor = ReleaseResourceMOP_t_c_Map_cachenode;
			} else {
				TempRef_t = ReleaseResourceMOP_Object_RefMap.getMultiTagRef(t, thisJoinPoint.getStaticPart().getId());
				TempRef_c = ReleaseResourceMOP_Object_RefMap.getMultiTagRef(c, thisJoinPoint.getStaticPart().getId());
			}

			if (mainSet == null || mainMonitor == null) {
				tempMap = ReleaseResourceMOP_t_c_r_Map;
				obj = tempMap.getMap(TempRef_t);
				if (obj == null) {
					obj = new javamoprt.map.MOPMapOfSetMon(1);
					tempMap.putMap(TempRef_t, obj);
				}
				mainMap = (javamoprt.map.MOPAbstractMap)obj;
				mainMonitor = (ReleaseResourceMOPMonitor)mainMap.getNode(TempRef_c);
				mainSet = (ReleaseResourceMOPMonitor_Set)mainMap.getSet(TempRef_c);
				if (mainSet == null){
					mainSet = new ReleaseResourceMOPMonitor_Set();
					mainMap.putSet(TempRef_c, mainSet);
				}

				if (mainMonitor == null) {
					mainMonitor = new ReleaseResourceMOPMonitor();

					mainMonitor.MOPRef_t = TempRef_t;
					mainMonitor.MOPRef_c = TempRef_c;

					mainMap.putNode(TempRef_c, mainMonitor);
					mainSet.add(mainMonitor);
					mainMonitor.tau = ReleaseResourceMOP_timestamp;
					if (TempRef_t.tau[0] == -1){
						TempRef_t.tau[0] = ReleaseResourceMOP_timestamp;
					}
					if (TempRef_c.tau[0] == -1){
						TempRef_c.tau[0] = ReleaseResourceMOP_timestamp;
					}
					ReleaseResourceMOP_timestamp++;

					tempMap = ReleaseResourceMOP_c_Map;
					obj = tempMap.getSet(TempRef_c);
					monitors = (ReleaseResourceMOPMonitor_Set)obj;
					if (monitors == null) {
						monitors = new ReleaseResourceMOPMonitor_Set();
						tempMap.putSet(TempRef_c, monitors);
					}
					monitors.add(mainMonitor);

					tempMap = ReleaseResourceMOP_t__To__t_c_Map;
					obj = tempMap.getSet(TempRef_t);
					monitors = (ReleaseResourceMOPMonitor_Set)obj;
					if (monitors == null) {
						monitors = new ReleaseResourceMOPMonitor_Set();
						tempMap.putSet(TempRef_t, monitors);
					}
					monitors.add(mainMonitor);
				}

				ReleaseResourceMOP_t_c_Map_cachekey_0 = TempRef_t;
				ReleaseResourceMOP_t_c_Map_cachekey_1 = TempRef_c;
				ReleaseResourceMOP_t_c_Map_cacheset = mainSet;
				ReleaseResourceMOP_t_c_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_schedule(t, c);
			}
		}
	}

	pointcut ReleaseResourceMOP_grant(Object t, Object r) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.grant_rr(Object, Object)) && args(t, r)) && MOP_CommonPointCut();
	after (Object t, Object r) : ReleaseResourceMOP_grant(t, r) {
		ReleaseResourceMOP_activated = true;
		synchronized(ReleaseResourceMOP_MOPLock) {
			Object obj;
			javamoprt.map.MOPMap tempMap;
			ReleaseResourceMOPMonitor mainMonitor = null;
			ReleaseResourceMOPMonitor origMonitor = null;
			ReleaseResourceMOPMonitor lastMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			javamoprt.map.MOPMap origMap = null;
			javamoprt.map.MOPMap lastMap = null;
			ReleaseResourceMOPMonitor_Set mainSet = null;
			ReleaseResourceMOPMonitor_Set origSet = null;
			ReleaseResourceMOPMonitor_Set monitors = null;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_t;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_c;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_r;

			// Cache Retrieval
			if (t == ReleaseResourceMOP_t_r_Map_cachekey_0.get() && r == ReleaseResourceMOP_t_r_Map_cachekey_2.get()) {
				TempRef_t = ReleaseResourceMOP_t_r_Map_cachekey_0;
				TempRef_r = ReleaseResourceMOP_t_r_Map_cachekey_2;

				mainSet = ReleaseResourceMOP_t_r_Map_cacheset;
				mainMonitor = ReleaseResourceMOP_t_r_Map_cachenode;
			} else {
				TempRef_t = ReleaseResourceMOP_Object_RefMap.getMultiTagRef(t, thisJoinPoint.getStaticPart().getId());
				TempRef_r = ReleaseResourceMOP_Object_RefMap.getMultiTagRef(r, thisJoinPoint.getStaticPart().getId());
			}

			if (mainSet == null || mainMonitor == null) {
				tempMap = ReleaseResourceMOP_t_r_Map;
				obj = tempMap.getMap(TempRef_t);
				if (obj == null) {
					obj = new javamoprt.map.MOPMapOfSetMon(2);
					tempMap.putMap(TempRef_t, obj);
				}
				mainMap = (javamoprt.map.MOPAbstractMap)obj;
				mainMonitor = (ReleaseResourceMOPMonitor)mainMap.getNode(TempRef_r);
				mainSet = (ReleaseResourceMOPMonitor_Set)mainMap.getSet(TempRef_r);
				if (mainSet == null){
					mainSet = new ReleaseResourceMOPMonitor_Set();
					mainMap.putSet(TempRef_r, mainSet);
				}

				if (mainMonitor == null) {
					origMap = ReleaseResourceMOP_t__To__t_c_Map;
					origSet = (ReleaseResourceMOPMonitor_Set)origMap.getSet(TempRef_t);
					if (origSet!= null) {
						int numAlive = 0;
						for(int i = 0; i < origSet.size; i++) {
							origMonitor = origSet.elementData[i];
							Object c = origMonitor.MOPRef_c.get();
							if (!origMonitor.MOP_terminated && c != null) {
								origSet.elementData[numAlive] = origMonitor;
								numAlive++;

								TempRef_c = origMonitor.MOPRef_c;

								tempMap = ReleaseResourceMOP_t_c_r_Map;
								obj = tempMap.getMap(TempRef_t);
								if (obj == null) {
									obj = new javamoprt.map.MOPMapOfAll(1);
									tempMap.putMap(TempRef_t, obj);
								}
								tempMap = (javamoprt.map.MOPAbstractMap)obj;
								obj = tempMap.getMap(TempRef_c);
								if (obj == null) {
									obj = new javamoprt.map.MOPMapOfMonitor(2);
									tempMap.putMap(TempRef_c, obj);
								}
								lastMap = (javamoprt.map.MOPAbstractMap)obj;
								lastMonitor = (ReleaseResourceMOPMonitor)lastMap.getNode(TempRef_r);
								if (lastMonitor == null) {
									boolean timeCheck = true;

									if (TempRef_r.disable[0] > origMonitor.tau|| TempRef_r.tau[0] > 0 && TempRef_r.tau[0] < origMonitor.tau) {
										timeCheck = false;
									}

									if (timeCheck) {
										lastMonitor = (ReleaseResourceMOPMonitor)origMonitor.clone();
										lastMonitor.MOPRef_r = TempRef_r;
										if (TempRef_r.tau[0] == -1){
											TempRef_r.tau[0] = origMonitor.tau;
										}
										lastMap.putNode(TempRef_r, lastMonitor);

										tempMap = ReleaseResourceMOP_c_Map;
										obj = tempMap.getSet(TempRef_c);
										monitors = (ReleaseResourceMOPMonitor_Set)obj;
										if (monitors == null) {
											monitors = new ReleaseResourceMOPMonitor_Set();
											tempMap.putSet(TempRef_c, monitors);
										}
										monitors.add(lastMonitor);

										mainMap = ReleaseResourceMOP_t_r_Map;
										obj = mainMap.getMap(TempRef_t);
										if (obj == null) {
											obj = new javamoprt.map.MOPMapOfSetMon(2);
											mainMap.putMap(TempRef_r, obj);
										}
										mainMap = (javamoprt.map.MOPAbstractMap)obj;
										obj = mainMap.getSet(TempRef_r);
										mainSet = (ReleaseResourceMOPMonitor_Set)obj;
										if (mainSet == null) {
											mainSet = new ReleaseResourceMOPMonitor_Set();
											mainMap.putSet(TempRef_r, mainSet);
										}
										mainSet.add(lastMonitor);

										tempMap = ReleaseResourceMOP_t_c_r_Map;
										obj = tempMap.getMap(TempRef_t);
										if (obj == null) {
											obj = new javamoprt.map.MOPMapOfSetMon(1);
											tempMap.putMap(TempRef_c, obj);
										}
										tempMap = (javamoprt.map.MOPAbstractMap)obj;
										obj = tempMap.getSet(TempRef_c);
										monitors = (ReleaseResourceMOPMonitor_Set)obj;
										if (monitors == null) {
											monitors = new ReleaseResourceMOPMonitor_Set();
											tempMap.putSet(TempRef_c, monitors);
										}
										monitors.add(lastMonitor);
									}
								}
							}
						}

						for(int i = numAlive; i < origSet.size; i++) {
							origSet.elementData[i] = null;
						}
						origSet.size = numAlive;
					}
					if (mainMonitor == null) {
						mainMonitor = new ReleaseResourceMOPMonitor();

						mainMonitor.MOPRef_t = TempRef_t;
						mainMonitor.MOPRef_r = TempRef_r;

						mainMap.putNode(TempRef_r, mainMonitor);
						mainSet.add(mainMonitor);
						mainMonitor.tau = ReleaseResourceMOP_timestamp;
						if (TempRef_t.tau[0] == -1){
							TempRef_t.tau[0] = ReleaseResourceMOP_timestamp;
						}
						if (TempRef_r.tau[0] == -1){
							TempRef_r.tau[0] = ReleaseResourceMOP_timestamp;
						}
						ReleaseResourceMOP_timestamp++;
					}

					TempRef_r.disable[0] = ReleaseResourceMOP_timestamp;
					ReleaseResourceMOP_timestamp++;
				}

				ReleaseResourceMOP_t_r_Map_cachekey_0 = TempRef_t;
				ReleaseResourceMOP_t_r_Map_cachekey_2 = TempRef_r;
				ReleaseResourceMOP_t_r_Map_cacheset = mainSet;
				ReleaseResourceMOP_t_r_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_grant(t, r);
			}
		}
	}

	pointcut ReleaseResourceMOP_cancel(Object t, Object r) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.cancel_rr(Object, Object)) && args(t, r)) && MOP_CommonPointCut();
	after (Object t, Object r) : ReleaseResourceMOP_cancel(t, r) {
		ReleaseResourceMOP_activated = true;
		synchronized(ReleaseResourceMOP_MOPLock) {
			Object obj;
			javamoprt.map.MOPMap tempMap;
			ReleaseResourceMOPMonitor mainMonitor = null;
			ReleaseResourceMOPMonitor origMonitor = null;
			ReleaseResourceMOPMonitor lastMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			javamoprt.map.MOPMap origMap = null;
			javamoprt.map.MOPMap lastMap = null;
			ReleaseResourceMOPMonitor_Set mainSet = null;
			ReleaseResourceMOPMonitor_Set origSet = null;
			ReleaseResourceMOPMonitor_Set monitors = null;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_t;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_c;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_r;

			// Cache Retrieval
			if (t == ReleaseResourceMOP_t_r_Map_cachekey_0.get() && r == ReleaseResourceMOP_t_r_Map_cachekey_2.get()) {
				TempRef_t = ReleaseResourceMOP_t_r_Map_cachekey_0;
				TempRef_r = ReleaseResourceMOP_t_r_Map_cachekey_2;

				mainSet = ReleaseResourceMOP_t_r_Map_cacheset;
				mainMonitor = ReleaseResourceMOP_t_r_Map_cachenode;
			} else {
				TempRef_t = ReleaseResourceMOP_Object_RefMap.getMultiTagRef(t, thisJoinPoint.getStaticPart().getId());
				TempRef_r = ReleaseResourceMOP_Object_RefMap.getMultiTagRef(r, thisJoinPoint.getStaticPart().getId());
			}

			if (mainSet == null || mainMonitor == null) {
				tempMap = ReleaseResourceMOP_t_r_Map;
				obj = tempMap.getMap(TempRef_t);
				if (obj == null) {
					obj = new javamoprt.map.MOPMapOfSetMon(2);
					tempMap.putMap(TempRef_t, obj);
				}
				mainMap = (javamoprt.map.MOPAbstractMap)obj;
				mainMonitor = (ReleaseResourceMOPMonitor)mainMap.getNode(TempRef_r);
				mainSet = (ReleaseResourceMOPMonitor_Set)mainMap.getSet(TempRef_r);
				if (mainSet == null){
					mainSet = new ReleaseResourceMOPMonitor_Set();
					mainMap.putSet(TempRef_r, mainSet);
				}

				if (mainMonitor == null) {
					origMap = ReleaseResourceMOP_t__To__t_c_Map;
					origSet = (ReleaseResourceMOPMonitor_Set)origMap.getSet(TempRef_t);
					if (origSet!= null) {
						int numAlive = 0;
						for(int i = 0; i < origSet.size; i++) {
							origMonitor = origSet.elementData[i];
							Object c = origMonitor.MOPRef_c.get();
							if (!origMonitor.MOP_terminated && c != null) {
								origSet.elementData[numAlive] = origMonitor;
								numAlive++;

								TempRef_c = origMonitor.MOPRef_c;

								tempMap = ReleaseResourceMOP_t_c_r_Map;
								obj = tempMap.getMap(TempRef_t);
								if (obj == null) {
									obj = new javamoprt.map.MOPMapOfAll(1);
									tempMap.putMap(TempRef_t, obj);
								}
								tempMap = (javamoprt.map.MOPAbstractMap)obj;
								obj = tempMap.getMap(TempRef_c);
								if (obj == null) {
									obj = new javamoprt.map.MOPMapOfMonitor(2);
									tempMap.putMap(TempRef_c, obj);
								}
								lastMap = (javamoprt.map.MOPAbstractMap)obj;
								lastMonitor = (ReleaseResourceMOPMonitor)lastMap.getNode(TempRef_r);
								if (lastMonitor == null) {
									boolean timeCheck = true;

									if (TempRef_r.disable[0] > origMonitor.tau|| TempRef_r.tau[0] > 0 && TempRef_r.tau[0] < origMonitor.tau) {
										timeCheck = false;
									}

									if (timeCheck) {
										lastMonitor = (ReleaseResourceMOPMonitor)origMonitor.clone();
										lastMonitor.MOPRef_r = TempRef_r;
										if (TempRef_r.tau[0] == -1){
											TempRef_r.tau[0] = origMonitor.tau;
										}
										lastMap.putNode(TempRef_r, lastMonitor);

										tempMap = ReleaseResourceMOP_c_Map;
										obj = tempMap.getSet(TempRef_c);
										monitors = (ReleaseResourceMOPMonitor_Set)obj;
										if (monitors == null) {
											monitors = new ReleaseResourceMOPMonitor_Set();
											tempMap.putSet(TempRef_c, monitors);
										}
										monitors.add(lastMonitor);

										mainMap = ReleaseResourceMOP_t_r_Map;
										obj = mainMap.getMap(TempRef_t);
										if (obj == null) {
											obj = new javamoprt.map.MOPMapOfSetMon(2);
											mainMap.putMap(TempRef_r, obj);
										}
										mainMap = (javamoprt.map.MOPAbstractMap)obj;
										obj = mainMap.getSet(TempRef_r);
										mainSet = (ReleaseResourceMOPMonitor_Set)obj;
										if (mainSet == null) {
											mainSet = new ReleaseResourceMOPMonitor_Set();
											mainMap.putSet(TempRef_r, mainSet);
										}
										mainSet.add(lastMonitor);

										tempMap = ReleaseResourceMOP_t_c_r_Map;
										obj = tempMap.getMap(TempRef_t);
										if (obj == null) {
											obj = new javamoprt.map.MOPMapOfSetMon(1);
											tempMap.putMap(TempRef_c, obj);
										}
										tempMap = (javamoprt.map.MOPAbstractMap)obj;
										obj = tempMap.getSet(TempRef_c);
										monitors = (ReleaseResourceMOPMonitor_Set)obj;
										if (monitors == null) {
											monitors = new ReleaseResourceMOPMonitor_Set();
											tempMap.putSet(TempRef_c, monitors);
										}
										monitors.add(lastMonitor);
									}
								}
							}
						}

						for(int i = numAlive; i < origSet.size; i++) {
							origSet.elementData[i] = null;
						}
						origSet.size = numAlive;
					}
					if (mainMonitor == null) {
						mainMonitor = new ReleaseResourceMOPMonitor();

						mainMonitor.MOPRef_t = TempRef_t;
						mainMonitor.MOPRef_r = TempRef_r;

						mainMap.putNode(TempRef_r, mainMonitor);
						mainSet.add(mainMonitor);
						mainMonitor.tau = ReleaseResourceMOP_timestamp;
						if (TempRef_t.tau[0] == -1){
							TempRef_t.tau[0] = ReleaseResourceMOP_timestamp;
						}
						if (TempRef_r.tau[0] == -1){
							TempRef_r.tau[0] = ReleaseResourceMOP_timestamp;
						}
						ReleaseResourceMOP_timestamp++;
					}

					TempRef_r.disable[0] = ReleaseResourceMOP_timestamp;
					ReleaseResourceMOP_timestamp++;
				}

				ReleaseResourceMOP_t_r_Map_cachekey_0 = TempRef_t;
				ReleaseResourceMOP_t_r_Map_cachekey_2 = TempRef_r;
				ReleaseResourceMOP_t_r_Map_cacheset = mainSet;
				ReleaseResourceMOP_t_r_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_cancel(t, r);
			}
		}
	}

	pointcut ReleaseResourceMOP_finish(Object c) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.finish(Object)) && args(c)) && MOP_CommonPointCut();
	after (Object c) : ReleaseResourceMOP_finish(c) {
		ReleaseResourceMOP_activated = true;
		synchronized(ReleaseResourceMOP_MOPLock) {
			ReleaseResourceMOPMonitor mainMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			ReleaseResourceMOPMonitor_Set mainSet = null;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_c;

			// Cache Retrieval
			if (c == ReleaseResourceMOP_c_Map_cachekey_1.get()) {
				TempRef_c = ReleaseResourceMOP_c_Map_cachekey_1;

				mainSet = ReleaseResourceMOP_c_Map_cacheset;
				mainMonitor = ReleaseResourceMOP_c_Map_cachenode;
			} else {
				TempRef_c = ReleaseResourceMOP_Object_RefMap.getMultiTagRef(c, thisJoinPoint.getStaticPart().getId());
			}

			if (mainSet == null || mainMonitor == null) {
				mainMap = ReleaseResourceMOP_c_Map;
				mainMonitor = (ReleaseResourceMOPMonitor)mainMap.getNode(TempRef_c);
				mainSet = (ReleaseResourceMOPMonitor_Set)mainMap.getSet(TempRef_c);
				if (mainSet == null){
					mainSet = new ReleaseResourceMOPMonitor_Set();
					mainMap.putSet(TempRef_c, mainSet);
				}

				if (mainMonitor == null) {
					mainMonitor = new ReleaseResourceMOPMonitor();

					mainMonitor.MOPRef_c = TempRef_c;

					ReleaseResourceMOP_c_Map.putNode(TempRef_c, mainMonitor);
					mainSet.add(mainMonitor);
					mainMonitor.tau = ReleaseResourceMOP_timestamp;
					if (TempRef_c.tau[0] == -1){
						TempRef_c.tau[0] = ReleaseResourceMOP_timestamp;
					}
					ReleaseResourceMOP_timestamp++;
				}

				ReleaseResourceMOP_c_Map_cachekey_1 = TempRef_c;
				ReleaseResourceMOP_c_Map_cacheset = mainSet;
				ReleaseResourceMOP_c_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_finish(c);
			}
		}
	}

}
