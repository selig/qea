package benchmark.rovers.mop;
import java.util.Arrays;

import javamoprt.MOPMonitor;

class GrantCancelMOPMonitor_Set extends javamoprt.MOPSet {
	protected GrantCancelMOPMonitor[] elementData;

	public GrantCancelMOPMonitor_Set(){
		size = 0;
		elementData = new GrantCancelMOPMonitor[4];
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
		elementData[size++] = (GrantCancelMOPMonitor)e;
		return true;
	}

	@Override
	public final void endObject(int idnum){
		int numAlive = 0;
		for(int i = 0; i < size; i++){
			GrantCancelMOPMonitor monitor = elementData[i];
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
			GrantCancelMOPMonitor[] oldData = elementData;
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
			GrantCancelMOPMonitor monitor = elementData[i];
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

	public final void event_grant_t1(Object t1, Object r) {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			GrantCancelMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_grant_t1(t1, r);
				if(monitor.Prop_1_Category_error) {
					monitor.Prop_1_handler_error(t1, null, r);
				}
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_grant_t2(Object t2, Object r) {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			GrantCancelMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_grant_t2(t2, r);
				if(monitor.Prop_1_Category_error) {
					monitor.Prop_1_handler_error(null, t2, r);
				}
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_cancel_t1(Object t1, Object r) {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			GrantCancelMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_cancel_t1(t1, r);
				if(monitor.Prop_1_Category_error) {
					monitor.Prop_1_handler_error(t1, null, r);
				}
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_cancel_t2(Object t2, Object r) {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			GrantCancelMOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_cancel_t2(t2, r);
				if(monitor.Prop_1_Category_error) {
					monitor.Prop_1_handler_error(null, t2, r);
				}
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}
}

class GrantCancelMOPMonitor extends javamoprt.MOPMonitor implements Cloneable, javamoprt.MOPObject {
	public long tau = -1;
	@Override
	public Object clone() {
		try {
			GrantCancelMOPMonitor ret = (GrantCancelMOPMonitor) super.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	int Prop_1_state;
	static final int Prop_1_transition_grant_t1[] = {3, 2, 4, 4, 4};;
	static final int Prop_1_transition_grant_t2[] = {1, 4, 4, 2, 4};;
	static final int Prop_1_transition_cancel_t1[] = {2, 4, 4, 0, 4};;
	static final int Prop_1_transition_cancel_t2[] = {2, 0, 4, 4, 4};;

	boolean Prop_1_Category_error = false;

	public GrantCancelMOPMonitor () {
		Prop_1_state = 0;

	}

	public final void Prop_1_event_grant_t1(Object t1, Object r) {
		MOP_lastevent = 0;

		Prop_1_state = Prop_1_transition_grant_t1[Prop_1_state];
		Prop_1_Category_error = Prop_1_state == 2;
	}

	public final void Prop_1_event_grant_t2(Object t2, Object r) {
		MOP_lastevent = 1;

		Prop_1_state = Prop_1_transition_grant_t2[Prop_1_state];
		Prop_1_Category_error = Prop_1_state == 2;
	}

	public final void Prop_1_event_cancel_t1(Object t1, Object r) {
		MOP_lastevent = 2;

		Prop_1_state = Prop_1_transition_cancel_t1[Prop_1_state];
		Prop_1_Category_error = Prop_1_state == 2;
	}

	public final void Prop_1_event_cancel_t2(Object t2, Object r) {
		MOP_lastevent = 3;

		Prop_1_state = Prop_1_transition_cancel_t2[Prop_1_state];
		Prop_1_Category_error = Prop_1_state == 2;
	}

	public final void Prop_1_handler_error (Object t1, Object t2, Object r){
		{
			System.err.println("a resource granted to more than one task");
		}

	}

	public final void reset() {
		MOP_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_error = false;
	}

	public javamoprt.ref.MOPMultiTagWeakReference MOPRef_t1;
	public javamoprt.ref.MOPMultiTagWeakReference MOPRef_t2;
	public javamoprt.ref.MOPMultiTagWeakReference MOPRef_r;

	//alive_parameters_0 = [Object t1, Object r]
	public boolean alive_parameters_0 = true;
	//alive_parameters_1 = [Object t2, Object r]
	public boolean alive_parameters_1 = true;

	@Override
	public final void endObject(int idnum){
		switch(idnum){
			case 0:
			alive_parameters_0 = false;
			break;
			case 1:
			alive_parameters_1 = false;
			break;
			case 2:
			alive_parameters_0 = false;
			alive_parameters_1 = false;
			break;
		}
		switch(MOP_lastevent) {
			case -1:
			return;
			case 0:
			//grant_t1
			//alive_t1 && alive_r || alive_t2 && alive_r
			if(!(alive_parameters_0 || alive_parameters_1)){
				MOP_terminated = true;
				return;
			}
			break;

			case 1:
			//grant_t2
			//alive_t1 && alive_r || alive_t2 && alive_r
			if(!(alive_parameters_0 || alive_parameters_1)){
				MOP_terminated = true;
				return;
			}
			break;

			case 2:
			//cancel_t1
			//alive_t1 && alive_r || alive_t2 && alive_r
			if(!(alive_parameters_0 || alive_parameters_1)){
				MOP_terminated = true;
				return;
			}
			break;

			case 3:
			//cancel_t2
			//alive_t1 && alive_r || alive_t2 && alive_r
			if(!(alive_parameters_0 || alive_parameters_1)){
				MOP_terminated = true;
				return;
			}
			break;

		}
		return;
	}

}

public aspect GrantCancelMOPMonitorAspect implements javamoprt.MOPObject {
	javamoprt.map.MOPMapManager GrantCancelMOPMapManager;
	public GrantCancelMOPMonitorAspect(){
		GrantCancelMOPMapManager = new javamoprt.map.MOPMapManager();
		GrantCancelMOPMapManager.start();
	}

	// Declarations for the Lock
	static Object GrantCancelMOP_MOPLock = new Object();

	// Declarations for Timestamps
	static long GrantCancelMOP_timestamp = 1;

	static boolean GrantCancelMOP_activated = false;

	// Declarations for Indexing Trees
	static javamoprt.map.MOPAbstractMap GrantCancelMOP_t1_t2_r_Map = new javamoprt.map.MOPMapOfAll(0);
	static javamoprt.ref.MOPMultiTagWeakReference GrantCancelMOP_t1_t2_r_Map_cachekey_0 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static javamoprt.ref.MOPMultiTagWeakReference GrantCancelMOP_t1_t2_r_Map_cachekey_1 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static javamoprt.ref.MOPMultiTagWeakReference GrantCancelMOP_t1_t2_r_Map_cachekey_2 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static GrantCancelMOPMonitor GrantCancelMOP_t1_t2_r_Map_cachenode = null;
	static javamoprt.map.MOPAbstractMap GrantCancelMOP_t2_r_Map = new javamoprt.map.MOPMapOfAll(1);
	static javamoprt.ref.MOPMultiTagWeakReference GrantCancelMOP_t2_r_Map_cachekey_1 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static javamoprt.ref.MOPMultiTagWeakReference GrantCancelMOP_t2_r_Map_cachekey_2 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static GrantCancelMOPMonitor_Set GrantCancelMOP_t2_r_Map_cacheset = null;
	static GrantCancelMOPMonitor GrantCancelMOP_t2_r_Map_cachenode = null;
	static javamoprt.map.MOPAbstractMap GrantCancelMOP_t1_r_Map = new javamoprt.map.MOPMapOfAll(0);
	static javamoprt.ref.MOPMultiTagWeakReference GrantCancelMOP_t1_r_Map_cachekey_0 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static javamoprt.ref.MOPMultiTagWeakReference GrantCancelMOP_t1_r_Map_cachekey_2 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static GrantCancelMOPMonitor_Set GrantCancelMOP_t1_r_Map_cacheset = null;
	static GrantCancelMOPMonitor GrantCancelMOP_t1_r_Map_cachenode = null;
	static javamoprt.map.MOPAbstractMap GrantCancelMOP_r__To__t1_r_Map = new javamoprt.map.MOPMapOfSetMon(2);
	static javamoprt.map.MOPAbstractMap GrantCancelMOP_r__To__t2_r_Map = new javamoprt.map.MOPMapOfSetMon(2);

	// Trees for References
	static javamoprt.map.MOPRefMap GrantCancelMOP_Object_RefMap = new javamoprt.map.MOPMultiTagRefMap(3);

	pointcut MOP_CommonPointCut() : !within(javamoprt.MOPObject+) && !adviceexecution();
	pointcut GrantCancelMOP_grant_t1(Object t1, Object r) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.grant_gc(Object, Object)) && args(t1, r)) && MOP_CommonPointCut();
	after (Object t1, Object r) : GrantCancelMOP_grant_t1(t1, r) {
		GrantCancelMOP_activated = true;
		synchronized(GrantCancelMOP_MOPLock) {
			Object obj;
			javamoprt.map.MOPMap tempMap;
			GrantCancelMOPMonitor mainMonitor = null;
			GrantCancelMOPMonitor origMonitor = null;
			GrantCancelMOPMonitor lastMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			javamoprt.map.MOPMap origMap = null;
			javamoprt.map.MOPMap lastMap = null;
			GrantCancelMOPMonitor_Set mainSet = null;
			GrantCancelMOPMonitor_Set origSet = null;
			GrantCancelMOPMonitor_Set monitors = null;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_t1;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_t2;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_r;

			// Cache Retrieval
			if (t1 == GrantCancelMOP_t1_r_Map_cachekey_0.get() && r == GrantCancelMOP_t1_r_Map_cachekey_2.get()) {
				TempRef_t1 = GrantCancelMOP_t1_r_Map_cachekey_0;
				TempRef_r = GrantCancelMOP_t1_r_Map_cachekey_2;

				mainSet = GrantCancelMOP_t1_r_Map_cacheset;
				mainMonitor = GrantCancelMOP_t1_r_Map_cachenode;
			} else {
				TempRef_t1 = GrantCancelMOP_Object_RefMap.getMultiTagRef(t1, thisJoinPoint.getStaticPart().getId());
				TempRef_r = GrantCancelMOP_Object_RefMap.getMultiTagRef(r, thisJoinPoint.getStaticPart().getId());
			}

			if (mainSet == null || mainMonitor == null) {
				tempMap = GrantCancelMOP_t1_r_Map;
				obj = tempMap.getMap(TempRef_t1);
				if (obj == null) {
					obj = new javamoprt.map.MOPMapOfSetMon(2);
					tempMap.putMap(TempRef_t1, obj);
				}
				mainMap = (javamoprt.map.MOPAbstractMap)obj;
				mainMonitor = (GrantCancelMOPMonitor)mainMap.getNode(TempRef_r);
				mainSet = (GrantCancelMOPMonitor_Set)mainMap.getSet(TempRef_r);
				if (mainSet == null){
					mainSet = new GrantCancelMOPMonitor_Set();
					mainMap.putSet(TempRef_r, mainSet);
				}

				if (mainMonitor == null) {
					origMap = GrantCancelMOP_r__To__t2_r_Map;
					origSet = (GrantCancelMOPMonitor_Set)origMap.getSet(TempRef_r);
					if (origSet!= null) {
						int numAlive = 0;
						for(int i = 0; i < origSet.size; i++) {
							origMonitor = origSet.elementData[i];
							Object t2 = origMonitor.MOPRef_t2.get();
							if (!origMonitor.MOP_terminated && t2 != null) {
								origSet.elementData[numAlive] = origMonitor;
								numAlive++;

								TempRef_t2 = origMonitor.MOPRef_t2;

								tempMap = GrantCancelMOP_t1_t2_r_Map;
								obj = tempMap.getMap(TempRef_t1);
								if (obj == null) {
									obj = new javamoprt.map.MOPMapOfAll(1);
									tempMap.putMap(TempRef_t1, obj);
								}
								tempMap = (javamoprt.map.MOPAbstractMap)obj;
								obj = tempMap.getMap(TempRef_t2);
								if (obj == null) {
									obj = new javamoprt.map.MOPMapOfMonitor(2);
									tempMap.putMap(TempRef_t2, obj);
								}
								lastMap = (javamoprt.map.MOPAbstractMap)obj;
								lastMonitor = (GrantCancelMOPMonitor)lastMap.getNode(TempRef_r);
								if (lastMonitor == null) {
									boolean timeCheck = true;

									if (TempRef_t1.disable[0] > origMonitor.tau|| TempRef_t1.tau[0] > 0 && TempRef_t1.tau[0] < origMonitor.tau) {
										timeCheck = false;
									}

									if (timeCheck) {
										lastMonitor = (GrantCancelMOPMonitor)origMonitor.clone();
										lastMonitor.MOPRef_t1 = TempRef_t1;
										if (TempRef_t1.tau[0] == -1){
											TempRef_t1.tau[0] = origMonitor.tau;
										}
										lastMap.putNode(TempRef_r, lastMonitor);

										tempMap = GrantCancelMOP_t2_r_Map;
										obj = tempMap.getMap(TempRef_t2);
										if (obj == null) {
											obj = new javamoprt.map.MOPMapOfSetMon(2);
											tempMap.putMap(TempRef_r, obj);
										}
										tempMap = (javamoprt.map.MOPAbstractMap)obj;
										obj = tempMap.getSet(TempRef_r);
										monitors = (GrantCancelMOPMonitor_Set)obj;
										if (monitors == null) {
											monitors = new GrantCancelMOPMonitor_Set();
											tempMap.putSet(TempRef_r, monitors);
										}
										monitors.add(lastMonitor);

										mainMap = GrantCancelMOP_t1_r_Map;
										obj = mainMap.getMap(TempRef_t1);
										if (obj == null) {
											obj = new javamoprt.map.MOPMapOfSetMon(2);
											mainMap.putMap(TempRef_r, obj);
										}
										mainMap = (javamoprt.map.MOPAbstractMap)obj;
										obj = mainMap.getSet(TempRef_r);
										mainSet = (GrantCancelMOPMonitor_Set)obj;
										if (mainSet == null) {
											mainSet = new GrantCancelMOPMonitor_Set();
											mainMap.putSet(TempRef_r, mainSet);
										}
										mainSet.add(lastMonitor);
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
						mainMonitor = new GrantCancelMOPMonitor();

						mainMonitor.MOPRef_t1 = TempRef_t1;
						mainMonitor.MOPRef_r = TempRef_r;

						mainMap.putNode(TempRef_r, mainMonitor);
						mainSet.add(mainMonitor);
						mainMonitor.tau = GrantCancelMOP_timestamp;
						if (TempRef_t1.tau[0] == -1){
							TempRef_t1.tau[0] = GrantCancelMOP_timestamp;
						}
						if (TempRef_r.tau[0] == -1){
							TempRef_r.tau[0] = GrantCancelMOP_timestamp;
						}
						GrantCancelMOP_timestamp++;

						tempMap = GrantCancelMOP_r__To__t1_r_Map;
						obj = tempMap.getSet(TempRef_r);
						monitors = (GrantCancelMOPMonitor_Set)obj;
						if (monitors == null) {
							monitors = new GrantCancelMOPMonitor_Set();
							tempMap.putSet(TempRef_r, monitors);
						}
						monitors.add(mainMonitor);
					}

					TempRef_t1.disable[0] = GrantCancelMOP_timestamp;
					GrantCancelMOP_timestamp++;
				}

				GrantCancelMOP_t1_r_Map_cachekey_0 = TempRef_t1;
				GrantCancelMOP_t1_r_Map_cachekey_2 = TempRef_r;
				GrantCancelMOP_t1_r_Map_cacheset = mainSet;
				GrantCancelMOP_t1_r_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_grant_t1(t1, r);
			}
		}
	}

	pointcut GrantCancelMOP_grant_t2(Object t2, Object r) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.grant_gc(Object, Object)) && args(t2, r)) && MOP_CommonPointCut();
	after (Object t2, Object r) : GrantCancelMOP_grant_t2(t2, r) {
		GrantCancelMOP_activated = true;
		synchronized(GrantCancelMOP_MOPLock) {
			Object obj;
			javamoprt.map.MOPMap tempMap;
			GrantCancelMOPMonitor mainMonitor = null;
			GrantCancelMOPMonitor origMonitor = null;
			GrantCancelMOPMonitor lastMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			javamoprt.map.MOPMap origMap = null;
			javamoprt.map.MOPMap lastMap = null;
			GrantCancelMOPMonitor_Set mainSet = null;
			GrantCancelMOPMonitor_Set origSet = null;
			GrantCancelMOPMonitor_Set monitors = null;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_t1;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_t2;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_r;

			// Cache Retrieval
			if (t2 == GrantCancelMOP_t2_r_Map_cachekey_1.get() && r == GrantCancelMOP_t2_r_Map_cachekey_2.get()) {
				TempRef_t2 = GrantCancelMOP_t2_r_Map_cachekey_1;
				TempRef_r = GrantCancelMOP_t2_r_Map_cachekey_2;

				mainSet = GrantCancelMOP_t2_r_Map_cacheset;
				mainMonitor = GrantCancelMOP_t2_r_Map_cachenode;
			} else {
				TempRef_t2 = GrantCancelMOP_Object_RefMap.getMultiTagRef(t2, thisJoinPoint.getStaticPart().getId());
				TempRef_r = GrantCancelMOP_Object_RefMap.getMultiTagRef(r, thisJoinPoint.getStaticPart().getId());
			}

			if (mainSet == null || mainMonitor == null) {
				tempMap = GrantCancelMOP_t2_r_Map;
				obj = tempMap.getMap(TempRef_t2);
				if (obj == null) {
					obj = new javamoprt.map.MOPMapOfSetMon(2);
					tempMap.putMap(TempRef_t2, obj);
				}
				mainMap = (javamoprt.map.MOPAbstractMap)obj;
				mainMonitor = (GrantCancelMOPMonitor)mainMap.getNode(TempRef_r);
				mainSet = (GrantCancelMOPMonitor_Set)mainMap.getSet(TempRef_r);
				if (mainSet == null){
					mainSet = new GrantCancelMOPMonitor_Set();
					mainMap.putSet(TempRef_r, mainSet);
				}

				if (mainMonitor == null) {
					origMap = GrantCancelMOP_r__To__t1_r_Map;
					origSet = (GrantCancelMOPMonitor_Set)origMap.getSet(TempRef_r);
					if (origSet!= null) {
						int numAlive = 0;
						for(int i = 0; i < origSet.size; i++) {
							origMonitor = origSet.elementData[i];
							Object t1 = origMonitor.MOPRef_t1.get();
							if (!origMonitor.MOP_terminated && t1 != null) {
								origSet.elementData[numAlive] = origMonitor;
								numAlive++;

								TempRef_t1 = origMonitor.MOPRef_t1;

								tempMap = GrantCancelMOP_t1_t2_r_Map;
								obj = tempMap.getMap(TempRef_t1);
								if (obj == null) {
									obj = new javamoprt.map.MOPMapOfAll(1);
									tempMap.putMap(TempRef_t1, obj);
								}
								tempMap = (javamoprt.map.MOPAbstractMap)obj;
								obj = tempMap.getMap(TempRef_t2);
								if (obj == null) {
									obj = new javamoprt.map.MOPMapOfMonitor(2);
									tempMap.putMap(TempRef_t2, obj);
								}
								lastMap = (javamoprt.map.MOPAbstractMap)obj;
								lastMonitor = (GrantCancelMOPMonitor)lastMap.getNode(TempRef_r);
								if (lastMonitor == null) {
									boolean timeCheck = true;

									if (TempRef_t2.disable[0] > origMonitor.tau|| TempRef_t2.tau[0] > 0 && TempRef_t2.tau[0] < origMonitor.tau) {
										timeCheck = false;
									}

									if (timeCheck) {
										lastMonitor = (GrantCancelMOPMonitor)origMonitor.clone();
										lastMonitor.MOPRef_t2 = TempRef_t2;
										if (TempRef_t2.tau[0] == -1){
											TempRef_t2.tau[0] = origMonitor.tau;
										}
										lastMap.putNode(TempRef_r, lastMonitor);

										mainMap = GrantCancelMOP_t2_r_Map;
										obj = mainMap.getMap(TempRef_t2);
										if (obj == null) {
											obj = new javamoprt.map.MOPMapOfSetMon(2);
											mainMap.putMap(TempRef_r, obj);
										}
										mainMap = (javamoprt.map.MOPAbstractMap)obj;
										obj = mainMap.getSet(TempRef_r);
										mainSet = (GrantCancelMOPMonitor_Set)obj;
										if (mainSet == null) {
											mainSet = new GrantCancelMOPMonitor_Set();
											mainMap.putSet(TempRef_r, mainSet);
										}
										mainSet.add(lastMonitor);

										tempMap = GrantCancelMOP_t1_r_Map;
										obj = tempMap.getMap(TempRef_t1);
										if (obj == null) {
											obj = new javamoprt.map.MOPMapOfSetMon(2);
											tempMap.putMap(TempRef_r, obj);
										}
										tempMap = (javamoprt.map.MOPAbstractMap)obj;
										obj = tempMap.getSet(TempRef_r);
										monitors = (GrantCancelMOPMonitor_Set)obj;
										if (monitors == null) {
											monitors = new GrantCancelMOPMonitor_Set();
											tempMap.putSet(TempRef_r, monitors);
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
						mainMonitor = new GrantCancelMOPMonitor();

						mainMonitor.MOPRef_t2 = TempRef_t2;
						mainMonitor.MOPRef_r = TempRef_r;

						mainMap.putNode(TempRef_r, mainMonitor);
						mainSet.add(mainMonitor);
						mainMonitor.tau = GrantCancelMOP_timestamp;
						if (TempRef_t2.tau[0] == -1){
							TempRef_t2.tau[0] = GrantCancelMOP_timestamp;
						}
						if (TempRef_r.tau[0] == -1){
							TempRef_r.tau[0] = GrantCancelMOP_timestamp;
						}
						GrantCancelMOP_timestamp++;

						tempMap = GrantCancelMOP_r__To__t2_r_Map;
						obj = tempMap.getSet(TempRef_r);
						monitors = (GrantCancelMOPMonitor_Set)obj;
						if (monitors == null) {
							monitors = new GrantCancelMOPMonitor_Set();
							tempMap.putSet(TempRef_r, monitors);
						}
						monitors.add(mainMonitor);
					}

					TempRef_t2.disable[0] = GrantCancelMOP_timestamp;
					GrantCancelMOP_timestamp++;
				}

				GrantCancelMOP_t2_r_Map_cachekey_1 = TempRef_t2;
				GrantCancelMOP_t2_r_Map_cachekey_2 = TempRef_r;
				GrantCancelMOP_t2_r_Map_cacheset = mainSet;
				GrantCancelMOP_t2_r_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_grant_t2(t2, r);
			}
		}
	}

	pointcut GrantCancelMOP_cancel_t1(Object t1, Object r) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.cancel_gc(Object, Object)) && args(t1, r)) && MOP_CommonPointCut();
	after (Object t1, Object r) : GrantCancelMOP_cancel_t1(t1, r) {
		GrantCancelMOP_activated = true;
		synchronized(GrantCancelMOP_MOPLock) {
			Object obj;
			javamoprt.map.MOPMap tempMap;
			GrantCancelMOPMonitor mainMonitor = null;
			GrantCancelMOPMonitor origMonitor = null;
			GrantCancelMOPMonitor lastMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			javamoprt.map.MOPMap origMap = null;
			javamoprt.map.MOPMap lastMap = null;
			GrantCancelMOPMonitor_Set mainSet = null;
			GrantCancelMOPMonitor_Set origSet = null;
			GrantCancelMOPMonitor_Set monitors = null;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_t1;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_t2;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_r;

			// Cache Retrieval
			if (t1 == GrantCancelMOP_t1_r_Map_cachekey_0.get() && r == GrantCancelMOP_t1_r_Map_cachekey_2.get()) {
				TempRef_t1 = GrantCancelMOP_t1_r_Map_cachekey_0;
				TempRef_r = GrantCancelMOP_t1_r_Map_cachekey_2;

				mainSet = GrantCancelMOP_t1_r_Map_cacheset;
				mainMonitor = GrantCancelMOP_t1_r_Map_cachenode;
			} else {
				TempRef_t1 = GrantCancelMOP_Object_RefMap.getMultiTagRef(t1, thisJoinPoint.getStaticPart().getId());
				TempRef_r = GrantCancelMOP_Object_RefMap.getMultiTagRef(r, thisJoinPoint.getStaticPart().getId());
			}

			if (mainSet == null || mainMonitor == null) {
				tempMap = GrantCancelMOP_t1_r_Map;
				obj = tempMap.getMap(TempRef_t1);
				if (obj == null) {
					obj = new javamoprt.map.MOPMapOfSetMon(2);
					tempMap.putMap(TempRef_t1, obj);
				}
				mainMap = (javamoprt.map.MOPAbstractMap)obj;
				mainMonitor = (GrantCancelMOPMonitor)mainMap.getNode(TempRef_r);
				mainSet = (GrantCancelMOPMonitor_Set)mainMap.getSet(TempRef_r);
				if (mainSet == null){
					mainSet = new GrantCancelMOPMonitor_Set();
					mainMap.putSet(TempRef_r, mainSet);
				}

				if (mainMonitor == null) {
					origMap = GrantCancelMOP_r__To__t2_r_Map;
					origSet = (GrantCancelMOPMonitor_Set)origMap.getSet(TempRef_r);
					if (origSet!= null) {
						int numAlive = 0;
						for(int i = 0; i < origSet.size; i++) {
							origMonitor = origSet.elementData[i];
							Object t2 = origMonitor.MOPRef_t2.get();
							if (!origMonitor.MOP_terminated && t2 != null) {
								origSet.elementData[numAlive] = origMonitor;
								numAlive++;

								TempRef_t2 = origMonitor.MOPRef_t2;

								tempMap = GrantCancelMOP_t1_t2_r_Map;
								obj = tempMap.getMap(TempRef_t1);
								if (obj == null) {
									obj = new javamoprt.map.MOPMapOfAll(1);
									tempMap.putMap(TempRef_t1, obj);
								}
								tempMap = (javamoprt.map.MOPAbstractMap)obj;
								obj = tempMap.getMap(TempRef_t2);
								if (obj == null) {
									obj = new javamoprt.map.MOPMapOfMonitor(2);
									tempMap.putMap(TempRef_t2, obj);
								}
								lastMap = (javamoprt.map.MOPAbstractMap)obj;
								lastMonitor = (GrantCancelMOPMonitor)lastMap.getNode(TempRef_r);
								if (lastMonitor == null) {
									boolean timeCheck = true;

									if (TempRef_t1.disable[0] > origMonitor.tau|| TempRef_t1.tau[0] > 0 && TempRef_t1.tau[0] < origMonitor.tau) {
										timeCheck = false;
									}

									if (timeCheck) {
										lastMonitor = (GrantCancelMOPMonitor)origMonitor.clone();
										lastMonitor.MOPRef_t1 = TempRef_t1;
										if (TempRef_t1.tau[0] == -1){
											TempRef_t1.tau[0] = origMonitor.tau;
										}
										lastMap.putNode(TempRef_r, lastMonitor);

										tempMap = GrantCancelMOP_t2_r_Map;
										obj = tempMap.getMap(TempRef_t2);
										if (obj == null) {
											obj = new javamoprt.map.MOPMapOfSetMon(2);
											tempMap.putMap(TempRef_r, obj);
										}
										tempMap = (javamoprt.map.MOPAbstractMap)obj;
										obj = tempMap.getSet(TempRef_r);
										monitors = (GrantCancelMOPMonitor_Set)obj;
										if (monitors == null) {
											monitors = new GrantCancelMOPMonitor_Set();
											tempMap.putSet(TempRef_r, monitors);
										}
										monitors.add(lastMonitor);

										mainMap = GrantCancelMOP_t1_r_Map;
										obj = mainMap.getMap(TempRef_t1);
										if (obj == null) {
											obj = new javamoprt.map.MOPMapOfSetMon(2);
											mainMap.putMap(TempRef_r, obj);
										}
										mainMap = (javamoprt.map.MOPAbstractMap)obj;
										obj = mainMap.getSet(TempRef_r);
										mainSet = (GrantCancelMOPMonitor_Set)obj;
										if (mainSet == null) {
											mainSet = new GrantCancelMOPMonitor_Set();
											mainMap.putSet(TempRef_r, mainSet);
										}
										mainSet.add(lastMonitor);
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
						mainMonitor = new GrantCancelMOPMonitor();

						mainMonitor.MOPRef_t1 = TempRef_t1;
						mainMonitor.MOPRef_r = TempRef_r;

						mainMap.putNode(TempRef_r, mainMonitor);
						mainSet.add(mainMonitor);
						mainMonitor.tau = GrantCancelMOP_timestamp;
						if (TempRef_t1.tau[0] == -1){
							TempRef_t1.tau[0] = GrantCancelMOP_timestamp;
						}
						if (TempRef_r.tau[0] == -1){
							TempRef_r.tau[0] = GrantCancelMOP_timestamp;
						}
						GrantCancelMOP_timestamp++;

						tempMap = GrantCancelMOP_r__To__t1_r_Map;
						obj = tempMap.getSet(TempRef_r);
						monitors = (GrantCancelMOPMonitor_Set)obj;
						if (monitors == null) {
							monitors = new GrantCancelMOPMonitor_Set();
							tempMap.putSet(TempRef_r, monitors);
						}
						monitors.add(mainMonitor);
					}

					TempRef_t1.disable[0] = GrantCancelMOP_timestamp;
					GrantCancelMOP_timestamp++;
				}

				GrantCancelMOP_t1_r_Map_cachekey_0 = TempRef_t1;
				GrantCancelMOP_t1_r_Map_cachekey_2 = TempRef_r;
				GrantCancelMOP_t1_r_Map_cacheset = mainSet;
				GrantCancelMOP_t1_r_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_cancel_t1(t1, r);
			}
		}
	}

	pointcut GrantCancelMOP_cancel_t2(Object t2, Object r) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.cancel_gc(Object, Object)) && args(t2, r)) && MOP_CommonPointCut();
	after (Object t2, Object r) : GrantCancelMOP_cancel_t2(t2, r) {
		GrantCancelMOP_activated = true;
		synchronized(GrantCancelMOP_MOPLock) {
			Object obj;
			javamoprt.map.MOPMap tempMap;
			GrantCancelMOPMonitor mainMonitor = null;
			GrantCancelMOPMonitor origMonitor = null;
			GrantCancelMOPMonitor lastMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			javamoprt.map.MOPMap origMap = null;
			javamoprt.map.MOPMap lastMap = null;
			GrantCancelMOPMonitor_Set mainSet = null;
			GrantCancelMOPMonitor_Set origSet = null;
			GrantCancelMOPMonitor_Set monitors = null;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_t1;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_t2;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_r;

			// Cache Retrieval
			if (t2 == GrantCancelMOP_t2_r_Map_cachekey_1.get() && r == GrantCancelMOP_t2_r_Map_cachekey_2.get()) {
				TempRef_t2 = GrantCancelMOP_t2_r_Map_cachekey_1;
				TempRef_r = GrantCancelMOP_t2_r_Map_cachekey_2;

				mainSet = GrantCancelMOP_t2_r_Map_cacheset;
				mainMonitor = GrantCancelMOP_t2_r_Map_cachenode;
			} else {
				TempRef_t2 = GrantCancelMOP_Object_RefMap.getMultiTagRef(t2, thisJoinPoint.getStaticPart().getId());
				TempRef_r = GrantCancelMOP_Object_RefMap.getMultiTagRef(r, thisJoinPoint.getStaticPart().getId());
			}

			if (mainSet == null || mainMonitor == null) {
				tempMap = GrantCancelMOP_t2_r_Map;
				obj = tempMap.getMap(TempRef_t2);
				if (obj == null) {
					obj = new javamoprt.map.MOPMapOfSetMon(2);
					tempMap.putMap(TempRef_t2, obj);
				}
				mainMap = (javamoprt.map.MOPAbstractMap)obj;
				mainMonitor = (GrantCancelMOPMonitor)mainMap.getNode(TempRef_r);
				mainSet = (GrantCancelMOPMonitor_Set)mainMap.getSet(TempRef_r);
				if (mainSet == null){
					mainSet = new GrantCancelMOPMonitor_Set();
					mainMap.putSet(TempRef_r, mainSet);
				}

				if (mainMonitor == null) {
					origMap = GrantCancelMOP_r__To__t1_r_Map;
					origSet = (GrantCancelMOPMonitor_Set)origMap.getSet(TempRef_r);
					if (origSet!= null) {
						int numAlive = 0;
						for(int i = 0; i < origSet.size; i++) {
							origMonitor = origSet.elementData[i];
							Object t1 = origMonitor.MOPRef_t1.get();
							if (!origMonitor.MOP_terminated && t1 != null) {
								origSet.elementData[numAlive] = origMonitor;
								numAlive++;

								TempRef_t1 = origMonitor.MOPRef_t1;

								tempMap = GrantCancelMOP_t1_t2_r_Map;
								obj = tempMap.getMap(TempRef_t1);
								if (obj == null) {
									obj = new javamoprt.map.MOPMapOfAll(1);
									tempMap.putMap(TempRef_t1, obj);
								}
								tempMap = (javamoprt.map.MOPAbstractMap)obj;
								obj = tempMap.getMap(TempRef_t2);
								if (obj == null) {
									obj = new javamoprt.map.MOPMapOfMonitor(2);
									tempMap.putMap(TempRef_t2, obj);
								}
								lastMap = (javamoprt.map.MOPAbstractMap)obj;
								lastMonitor = (GrantCancelMOPMonitor)lastMap.getNode(TempRef_r);
								if (lastMonitor == null) {
									boolean timeCheck = true;

									if (TempRef_t2.disable[0] > origMonitor.tau|| TempRef_t2.tau[0] > 0 && TempRef_t2.tau[0] < origMonitor.tau) {
										timeCheck = false;
									}

									if (timeCheck) {
										lastMonitor = (GrantCancelMOPMonitor)origMonitor.clone();
										lastMonitor.MOPRef_t2 = TempRef_t2;
										if (TempRef_t2.tau[0] == -1){
											TempRef_t2.tau[0] = origMonitor.tau;
										}
										lastMap.putNode(TempRef_r, lastMonitor);

										mainMap = GrantCancelMOP_t2_r_Map;
										obj = mainMap.getMap(TempRef_t2);
										if (obj == null) {
											obj = new javamoprt.map.MOPMapOfSetMon(2);
											mainMap.putMap(TempRef_r, obj);
										}
										mainMap = (javamoprt.map.MOPAbstractMap)obj;
										obj = mainMap.getSet(TempRef_r);
										mainSet = (GrantCancelMOPMonitor_Set)obj;
										if (mainSet == null) {
											mainSet = new GrantCancelMOPMonitor_Set();
											mainMap.putSet(TempRef_r, mainSet);
										}
										mainSet.add(lastMonitor);

										tempMap = GrantCancelMOP_t1_r_Map;
										obj = tempMap.getMap(TempRef_t1);
										if (obj == null) {
											obj = new javamoprt.map.MOPMapOfSetMon(2);
											tempMap.putMap(TempRef_r, obj);
										}
										tempMap = (javamoprt.map.MOPAbstractMap)obj;
										obj = tempMap.getSet(TempRef_r);
										monitors = (GrantCancelMOPMonitor_Set)obj;
										if (monitors == null) {
											monitors = new GrantCancelMOPMonitor_Set();
											tempMap.putSet(TempRef_r, monitors);
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
						mainMonitor = new GrantCancelMOPMonitor();

						mainMonitor.MOPRef_t2 = TempRef_t2;
						mainMonitor.MOPRef_r = TempRef_r;

						mainMap.putNode(TempRef_r, mainMonitor);
						mainSet.add(mainMonitor);
						mainMonitor.tau = GrantCancelMOP_timestamp;
						if (TempRef_t2.tau[0] == -1){
							TempRef_t2.tau[0] = GrantCancelMOP_timestamp;
						}
						if (TempRef_r.tau[0] == -1){
							TempRef_r.tau[0] = GrantCancelMOP_timestamp;
						}
						GrantCancelMOP_timestamp++;

						tempMap = GrantCancelMOP_r__To__t2_r_Map;
						obj = tempMap.getSet(TempRef_r);
						monitors = (GrantCancelMOPMonitor_Set)obj;
						if (monitors == null) {
							monitors = new GrantCancelMOPMonitor_Set();
							tempMap.putSet(TempRef_r, monitors);
						}
						monitors.add(mainMonitor);
					}

					TempRef_t2.disable[0] = GrantCancelMOP_timestamp;
					GrantCancelMOP_timestamp++;
				}

				GrantCancelMOP_t2_r_Map_cachekey_1 = TempRef_t2;
				GrantCancelMOP_t2_r_Map_cachekey_2 = TempRef_r;
				GrantCancelMOP_t2_r_Map_cacheset = mainSet;
				GrantCancelMOP_t2_r_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_cancel_t2(t2, r);
			}
		}
	}

}
