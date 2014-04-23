
import java.util.*;
import javamoprt.*;
import java.lang.ref.*;
import org.aspectj.lang.*;

class NestedCommandMOPMonitor_Set extends javamoprt.MOPSet {
	protected NestedCommandMOPMonitor[] elementData;

	public NestedCommandMOPMonitor_Set(){
		this.size = 0;
		this.elementData = new NestedCommandMOPMonitor[4];
	}

	public final int size(){
		while(size > 0 && elementData[size-1].MOP_terminated) {
			elementData[--size] = null;
		}
		return size;
	}

	public final boolean add(MOPMonitor e){
		ensureCapacity();
		elementData[size++] = (NestedCommandMOPMonitor)e;
		return true;
	}

	public final void endObject(int idnum){
		int numAlive = 0;
		for(int i = 0; i < size; i++){
			NestedCommandMOPMonitor monitor = elementData[i];
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

	public final boolean alive(){
		for(int i = 0; i < size; i++){
			MOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				return true;
			}
		}
		return false;
	}

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

	public final void ensureCapacity() {
		int oldCapacity = elementData.length;
		if (size + 1 > oldCapacity) {
			cleanup();
		}
		if (size + 1 > oldCapacity) {
			NestedCommandMOPMonitor[] oldData = elementData;
			int newCapacity = (oldCapacity * 3) / 2 + 1;
			if (newCapacity < size + 1){
				newCapacity = size + 1;
			}
			elementData = Arrays.copyOf(oldData, newCapacity);
		}
	}

	public final void cleanup() {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			NestedCommandMOPMonitor monitor = (NestedCommandMOPMonitor)elementData[i];
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

	public final void event_com_x(Object x) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			NestedCommandMOPMonitor monitor = (NestedCommandMOPMonitor)this.elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_com_x(x);
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(x, null);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_suc_x(Object x) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			NestedCommandMOPMonitor monitor = (NestedCommandMOPMonitor)this.elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_suc_x(x);
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(x, null);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_com_y(Object y) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			NestedCommandMOPMonitor monitor = (NestedCommandMOPMonitor)this.elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_com_y(y);
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(null, y);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_suc_y(Object y) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			NestedCommandMOPMonitor monitor = (NestedCommandMOPMonitor)this.elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_suc_y(y);
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(null, y);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elementData[i] = null;
		}
		size = numAlive;
	}
}

class NestedCommandMOPMonitor extends javamoprt.MOPMonitor implements Cloneable, javamoprt.MOPObject {
	public long tau = -1;
	public Object clone() {
		try {
			NestedCommandMOPMonitor ret = (NestedCommandMOPMonitor) super.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}
	Object saved_x;
	Object saved_y;
	String last_event = "";

	int Prop_1_state;
	static final int Prop_1_transition_com_x[] = {3, 5, 5, 5, 1, 5};;
	static final int Prop_1_transition_suc_x[] = {5, 4, 5, 0, 5, 5};;
	static final int Prop_1_transition_com_y[] = {4, 5, 5, 2, 5, 5};;
	static final int Prop_1_transition_suc_y[] = {5, 5, 3, 5, 0, 5};;

	boolean Prop_1_Category_fail = false;

	public NestedCommandMOPMonitor () {
		Prop_1_state = 0;

	}

	public final void Prop_1_event_com_x(Object x) {
		MOP_lastevent = 0;

		Prop_1_state = Prop_1_transition_com_x[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 5;
		{
			saved_x = x;
			last_event = "com_x";
		}
	}

	public final void Prop_1_event_suc_x(Object x) {
		MOP_lastevent = 1;

		Prop_1_state = Prop_1_transition_suc_x[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 5;
		{
			last_event = "suc_x";
		}
	}

	public final void Prop_1_event_com_y(Object y) {
		MOP_lastevent = 2;

		Prop_1_state = Prop_1_transition_com_y[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 5;
		{
			saved_y = y;
			last_event = "com_y";
		}
	}

	public final void Prop_1_event_suc_y(Object y) {
		MOP_lastevent = 3;

		Prop_1_state = Prop_1_transition_suc_y[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 5;
		{
			last_event = "suc_y";
		}
	}

	public final void Prop_1_handler_fail (Object x, Object y){
		{
			if (saved_x != saved_y) System.err.println("error in NestedCommand with " + saved_x + " and " + saved_y + " on " + last_event);
		}

	}

	public final void reset() {
		MOP_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_fail = false;
	}

	public javamoprt.ref.MOPMultiTagWeakReference MOPRef_x;
	public javamoprt.ref.MOPMultiTagWeakReference MOPRef_y;

	//alive_parameters_0 = [Object x]
	public boolean alive_parameters_0 = true;
	//alive_parameters_1 = [Object y]
	public boolean alive_parameters_1 = true;

	public final void endObject(int idnum){
		switch(idnum){
			case 0:
			alive_parameters_0 = false;
			break;
			case 1:
			alive_parameters_1 = false;
			break;
		}
		switch(MOP_lastevent) {
			case -1:
			return;
			case 0:
			//com_x
			//alive_x || alive_y
			if(!(alive_parameters_0 || alive_parameters_1)){
				MOP_terminated = true;
				return;
			}
			break;

			case 1:
			//suc_x
			//alive_x || alive_y
			if(!(alive_parameters_0 || alive_parameters_1)){
				MOP_terminated = true;
				return;
			}
			break;

			case 2:
			//com_y
			//alive_x || alive_y
			if(!(alive_parameters_0 || alive_parameters_1)){
				MOP_terminated = true;
				return;
			}
			break;

			case 3:
			//suc_y
			//alive_x || alive_y
			if(!(alive_parameters_0 || alive_parameters_1)){
				MOP_terminated = true;
				return;
			}
			break;

		}
		return;
	}

}

public aspect NestedCommandMOPMonitorAspect implements javamoprt.MOPObject {
	javamoprt.map.MOPMapManager NestedCommandMOPMapManager;
	public NestedCommandMOPMonitorAspect(){
		NestedCommandMOPMapManager = new javamoprt.map.MOPMapManager();
		NestedCommandMOPMapManager.start();
	}

	// Declarations for the Lock
	static Object NestedCommandMOP_MOPLock = new Object();

	// Declarations for Timestamps
	static long NestedCommandMOP_timestamp = 1;

	static boolean NestedCommandMOP_activated = false;

	// Declarations for Indexing Trees
	static javamoprt.map.MOPAbstractMap NestedCommandMOP_x_y_Map = new javamoprt.map.MOPMapOfAll(0);
	static javamoprt.ref.MOPMultiTagWeakReference NestedCommandMOP_x_y_Map_cachekey_0 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static javamoprt.ref.MOPMultiTagWeakReference NestedCommandMOP_x_y_Map_cachekey_1 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static NestedCommandMOPMonitor NestedCommandMOP_x_y_Map_cachenode = null;
	static javamoprt.ref.MOPMultiTagWeakReference NestedCommandMOP_x_Map_cachekey_0 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static NestedCommandMOPMonitor_Set NestedCommandMOP_x_Map_cacheset = null;
	static NestedCommandMOPMonitor NestedCommandMOP_x_Map_cachenode = null;
	static javamoprt.map.MOPAbstractMap NestedCommandMOP_y_Map = new javamoprt.map.MOPMapOfSetMon(1);
	static javamoprt.ref.MOPMultiTagWeakReference NestedCommandMOP_y_Map_cachekey_1 = javamoprt.map.MOPMultiTagRefMap.NULRef;
	static NestedCommandMOPMonitor_Set NestedCommandMOP_y_Map_cacheset = null;
	static NestedCommandMOPMonitor NestedCommandMOP_y_Map_cachenode = null;
	static NestedCommandMOPMonitor_Set NestedCommandMOP__To__x_Set = new NestedCommandMOPMonitor_Set();
	static NestedCommandMOPMonitor_Set NestedCommandMOP__To__y_Set = new NestedCommandMOPMonitor_Set();

	// Trees for References
	static javamoprt.map.MOPRefMap NestedCommandMOP_Object_RefMap = new javamoprt.map.MOPMultiTagRefMap(2);

	pointcut MOP_CommonPointCut() : !within(javamoprt.MOPObject+) && !adviceexecution();
	pointcut NestedCommandMOP_com_x(Object x) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.command(Object)) && args(x)) && MOP_CommonPointCut();
	after (Object x) : NestedCommandMOP_com_x(x) {
		NestedCommandMOP_activated = true;
		synchronized(NestedCommandMOP_MOPLock) {
			Object obj;
			javamoprt.map.MOPMap tempMap;
			NestedCommandMOPMonitor mainMonitor = null;
			NestedCommandMOPMonitor origMonitor = null;
			NestedCommandMOPMonitor lastMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			javamoprt.map.MOPMap lastMap = null;
			NestedCommandMOPMonitor_Set mainSet = null;
			NestedCommandMOPMonitor_Set origSet = null;
			NestedCommandMOPMonitor_Set monitors = null;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_x;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_y;

			// Cache Retrieval
			if (x == NestedCommandMOP_x_Map_cachekey_0.get()) {
				TempRef_x = NestedCommandMOP_x_Map_cachekey_0;

				mainSet = NestedCommandMOP_x_Map_cacheset;
				mainMonitor = NestedCommandMOP_x_Map_cachenode;
			} else {
				TempRef_x = NestedCommandMOP_Object_RefMap.getMultiTagRef(x, thisJoinPoint.getStaticPart().getId());
			}

			if (mainSet == null || mainMonitor == null) {
				mainMap = NestedCommandMOP_x_y_Map;
				mainMonitor = (NestedCommandMOPMonitor)mainMap.getNode(TempRef_x);
				mainSet = (NestedCommandMOPMonitor_Set)mainMap.getSet(TempRef_x);
				if (mainSet == null){
					mainSet = new NestedCommandMOPMonitor_Set();
					mainMap.putSet(TempRef_x, mainSet);
				}

				if (mainMonitor == null) {
					origSet = NestedCommandMOP__To__y_Set;
					if (origSet!= null) {
						int numAlive = 0;
						for(int i = 0; i < origSet.size; i++) {
							origMonitor = origSet.elementData[i];
							Object y = (Object)origMonitor.MOPRef_y.get();
							if (!origMonitor.MOP_terminated && y != null) {
								origSet.elementData[numAlive] = origMonitor;
								numAlive++;

								TempRef_y = origMonitor.MOPRef_y;

								tempMap = NestedCommandMOP_x_y_Map;
								obj = tempMap.getMap(TempRef_x);
								if (obj == null) {
									obj = new javamoprt.map.MOPMapOfMonitor(1);
									tempMap.putMap(TempRef_x, obj);
								}
								lastMap = (javamoprt.map.MOPAbstractMap)obj;
								lastMonitor = (NestedCommandMOPMonitor)lastMap.getNode(TempRef_y);
								if (lastMonitor == null) {
									boolean timeCheck = true;

									if (TempRef_x.disable[0] > origMonitor.tau|| (TempRef_x.tau[0] > 0 && TempRef_x.tau[0] < origMonitor.tau)) {
										timeCheck = false;
									}

									if (timeCheck) {
										lastMonitor = (NestedCommandMOPMonitor)origMonitor.clone();
										lastMonitor.MOPRef_x = TempRef_x;
										if (TempRef_x.tau[0] == -1){
											TempRef_x.tau[0] = origMonitor.tau;
										}
										lastMap.putNode(TempRef_y, lastMonitor);

										mainMap = NestedCommandMOP_x_y_Map;
										obj = mainMap.getSet(TempRef_x);
										mainSet = (NestedCommandMOPMonitor_Set)obj;
										if (mainSet == null) {
											mainSet = new NestedCommandMOPMonitor_Set();
											mainMap.putSet(TempRef_x, mainSet);
										}
										mainSet.add(lastMonitor);

										tempMap = NestedCommandMOP_y_Map;
										obj = tempMap.getSet(TempRef_y);
										monitors = (NestedCommandMOPMonitor_Set)obj;
										if (monitors == null) {
											monitors = new NestedCommandMOPMonitor_Set();
											tempMap.putSet(TempRef_y, monitors);
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
						mainMonitor = new NestedCommandMOPMonitor();

						mainMonitor.MOPRef_x = TempRef_x;

						NestedCommandMOP_x_y_Map.putNode(TempRef_x, mainMonitor);
						mainSet.add(mainMonitor);
						mainMonitor.tau = NestedCommandMOP_timestamp;
						if (TempRef_x.tau[0] == -1){
							TempRef_x.tau[0] = NestedCommandMOP_timestamp;
						}
						NestedCommandMOP_timestamp++;

						NestedCommandMOP__To__x_Set.add(mainMonitor);
					}

					TempRef_x.disable[0] = NestedCommandMOP_timestamp;
					NestedCommandMOP_timestamp++;
				}

				NestedCommandMOP_x_Map_cachekey_0 = TempRef_x;
				NestedCommandMOP_x_Map_cacheset = mainSet;
				NestedCommandMOP_x_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_com_x(x);
			}
		}
	}

	pointcut NestedCommandMOP_suc_x(Object x) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.succeed(Object)) && args(x)) && MOP_CommonPointCut();
	after (Object x) : NestedCommandMOP_suc_x(x) {
		NestedCommandMOP_activated = true;
		synchronized(NestedCommandMOP_MOPLock) {
			Object obj;
			javamoprt.map.MOPMap tempMap;
			NestedCommandMOPMonitor mainMonitor = null;
			NestedCommandMOPMonitor origMonitor = null;
			NestedCommandMOPMonitor lastMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			javamoprt.map.MOPMap lastMap = null;
			NestedCommandMOPMonitor_Set mainSet = null;
			NestedCommandMOPMonitor_Set origSet = null;
			NestedCommandMOPMonitor_Set monitors = null;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_x;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_y;

			// Cache Retrieval
			if (x == NestedCommandMOP_x_Map_cachekey_0.get()) {
				TempRef_x = NestedCommandMOP_x_Map_cachekey_0;

				mainSet = NestedCommandMOP_x_Map_cacheset;
				mainMonitor = NestedCommandMOP_x_Map_cachenode;
			} else {
				TempRef_x = NestedCommandMOP_Object_RefMap.getMultiTagRef(x, thisJoinPoint.getStaticPart().getId());
			}

			if (mainSet == null || mainMonitor == null) {
				mainMap = NestedCommandMOP_x_y_Map;
				mainMonitor = (NestedCommandMOPMonitor)mainMap.getNode(TempRef_x);
				mainSet = (NestedCommandMOPMonitor_Set)mainMap.getSet(TempRef_x);
				if (mainSet == null){
					mainSet = new NestedCommandMOPMonitor_Set();
					mainMap.putSet(TempRef_x, mainSet);
				}

				if (mainMonitor == null) {
					origSet = NestedCommandMOP__To__y_Set;
					if (origSet!= null) {
						int numAlive = 0;
						for(int i = 0; i < origSet.size; i++) {
							origMonitor = origSet.elementData[i];
							Object y = (Object)origMonitor.MOPRef_y.get();
							if (!origMonitor.MOP_terminated && y != null) {
								origSet.elementData[numAlive] = origMonitor;
								numAlive++;

								TempRef_y = origMonitor.MOPRef_y;

								tempMap = NestedCommandMOP_x_y_Map;
								obj = tempMap.getMap(TempRef_x);
								if (obj == null) {
									obj = new javamoprt.map.MOPMapOfMonitor(1);
									tempMap.putMap(TempRef_x, obj);
								}
								lastMap = (javamoprt.map.MOPAbstractMap)obj;
								lastMonitor = (NestedCommandMOPMonitor)lastMap.getNode(TempRef_y);
								if (lastMonitor == null) {
									boolean timeCheck = true;

									if (TempRef_x.disable[0] > origMonitor.tau|| (TempRef_x.tau[0] > 0 && TempRef_x.tau[0] < origMonitor.tau)) {
										timeCheck = false;
									}

									if (timeCheck) {
										lastMonitor = (NestedCommandMOPMonitor)origMonitor.clone();
										lastMonitor.MOPRef_x = TempRef_x;
										if (TempRef_x.tau[0] == -1){
											TempRef_x.tau[0] = origMonitor.tau;
										}
										lastMap.putNode(TempRef_y, lastMonitor);

										mainMap = NestedCommandMOP_x_y_Map;
										obj = mainMap.getSet(TempRef_x);
										mainSet = (NestedCommandMOPMonitor_Set)obj;
										if (mainSet == null) {
											mainSet = new NestedCommandMOPMonitor_Set();
											mainMap.putSet(TempRef_x, mainSet);
										}
										mainSet.add(lastMonitor);

										tempMap = NestedCommandMOP_y_Map;
										obj = tempMap.getSet(TempRef_y);
										monitors = (NestedCommandMOPMonitor_Set)obj;
										if (monitors == null) {
											monitors = new NestedCommandMOPMonitor_Set();
											tempMap.putSet(TempRef_y, monitors);
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
						mainMonitor = new NestedCommandMOPMonitor();

						mainMonitor.MOPRef_x = TempRef_x;

						NestedCommandMOP_x_y_Map.putNode(TempRef_x, mainMonitor);
						mainSet.add(mainMonitor);
						mainMonitor.tau = NestedCommandMOP_timestamp;
						if (TempRef_x.tau[0] == -1){
							TempRef_x.tau[0] = NestedCommandMOP_timestamp;
						}
						NestedCommandMOP_timestamp++;

						NestedCommandMOP__To__x_Set.add(mainMonitor);
					}

					TempRef_x.disable[0] = NestedCommandMOP_timestamp;
					NestedCommandMOP_timestamp++;
				}

				NestedCommandMOP_x_Map_cachekey_0 = TempRef_x;
				NestedCommandMOP_x_Map_cacheset = mainSet;
				NestedCommandMOP_x_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_suc_x(x);
			}
		}
	}

	pointcut NestedCommandMOP_com_y(Object y) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.command(Object)) && args(y)) && MOP_CommonPointCut();
	after (Object y) : NestedCommandMOP_com_y(y) {
		NestedCommandMOP_activated = true;
		synchronized(NestedCommandMOP_MOPLock) {
			Object obj;
			javamoprt.map.MOPMap tempMap;
			NestedCommandMOPMonitor mainMonitor = null;
			NestedCommandMOPMonitor origMonitor = null;
			NestedCommandMOPMonitor lastMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			javamoprt.map.MOPMap lastMap = null;
			NestedCommandMOPMonitor_Set mainSet = null;
			NestedCommandMOPMonitor_Set origSet = null;
			NestedCommandMOPMonitor_Set monitors = null;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_x;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_y;

			// Cache Retrieval
			if (y == NestedCommandMOP_y_Map_cachekey_1.get()) {
				TempRef_y = NestedCommandMOP_y_Map_cachekey_1;

				mainSet = NestedCommandMOP_y_Map_cacheset;
				mainMonitor = NestedCommandMOP_y_Map_cachenode;
			} else {
				TempRef_y = NestedCommandMOP_Object_RefMap.getMultiTagRef(y, thisJoinPoint.getStaticPart().getId());
			}

			if (mainSet == null || mainMonitor == null) {
				mainMap = NestedCommandMOP_y_Map;
				mainMonitor = (NestedCommandMOPMonitor)mainMap.getNode(TempRef_y);
				mainSet = (NestedCommandMOPMonitor_Set)mainMap.getSet(TempRef_y);
				if (mainSet == null){
					mainSet = new NestedCommandMOPMonitor_Set();
					mainMap.putSet(TempRef_y, mainSet);
				}

				if (mainMonitor == null) {
					origSet = NestedCommandMOP__To__x_Set;
					if (origSet!= null) {
						int numAlive = 0;
						for(int i = 0; i < origSet.size; i++) {
							origMonitor = origSet.elementData[i];
							Object x = (Object)origMonitor.MOPRef_x.get();
							if (!origMonitor.MOP_terminated && x != null) {
								origSet.elementData[numAlive] = origMonitor;
								numAlive++;

								TempRef_x = origMonitor.MOPRef_x;

								tempMap = NestedCommandMOP_x_y_Map;
								obj = tempMap.getMap(TempRef_x);
								if (obj == null) {
									obj = new javamoprt.map.MOPMapOfMonitor(1);
									tempMap.putMap(TempRef_x, obj);
								}
								lastMap = (javamoprt.map.MOPAbstractMap)obj;
								lastMonitor = (NestedCommandMOPMonitor)lastMap.getNode(TempRef_y);
								if (lastMonitor == null) {
									boolean timeCheck = true;

									if (TempRef_y.disable[0] > origMonitor.tau|| (TempRef_y.tau[0] > 0 && TempRef_y.tau[0] < origMonitor.tau)) {
										timeCheck = false;
									}

									if (timeCheck) {
										lastMonitor = (NestedCommandMOPMonitor)origMonitor.clone();
										lastMonitor.MOPRef_y = TempRef_y;
										if (TempRef_y.tau[0] == -1){
											TempRef_y.tau[0] = origMonitor.tau;
										}
										lastMap.putNode(TempRef_y, lastMonitor);

										tempMap = NestedCommandMOP_x_y_Map;
										obj = tempMap.getSet(TempRef_x);
										monitors = (NestedCommandMOPMonitor_Set)obj;
										if (monitors == null) {
											monitors = new NestedCommandMOPMonitor_Set();
											tempMap.putSet(TempRef_x, monitors);
										}
										monitors.add(lastMonitor);

										mainMap = NestedCommandMOP_y_Map;
										obj = mainMap.getSet(TempRef_y);
										mainSet = (NestedCommandMOPMonitor_Set)obj;
										if (mainSet == null) {
											mainSet = new NestedCommandMOPMonitor_Set();
											mainMap.putSet(TempRef_y, mainSet);
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
						mainMonitor = new NestedCommandMOPMonitor();

						mainMonitor.MOPRef_y = TempRef_y;

						NestedCommandMOP_y_Map.putNode(TempRef_y, mainMonitor);
						mainSet.add(mainMonitor);
						mainMonitor.tau = NestedCommandMOP_timestamp;
						if (TempRef_y.tau[0] == -1){
							TempRef_y.tau[0] = NestedCommandMOP_timestamp;
						}
						NestedCommandMOP_timestamp++;

						NestedCommandMOP__To__y_Set.add(mainMonitor);
					}

					TempRef_y.disable[0] = NestedCommandMOP_timestamp;
					NestedCommandMOP_timestamp++;
				}

				NestedCommandMOP_y_Map_cachekey_1 = TempRef_y;
				NestedCommandMOP_y_Map_cacheset = mainSet;
				NestedCommandMOP_y_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_com_y(y);
			}
		}
	}

	pointcut NestedCommandMOP_suc_y(Object y) : (execution(void benchmark.rovers.mop.MopDoEval$MopDoWork.succeed(Object)) && args(y)) && MOP_CommonPointCut();
	after (Object y) : NestedCommandMOP_suc_y(y) {
		NestedCommandMOP_activated = true;
		synchronized(NestedCommandMOP_MOPLock) {
			Object obj;
			javamoprt.map.MOPMap tempMap;
			NestedCommandMOPMonitor mainMonitor = null;
			NestedCommandMOPMonitor origMonitor = null;
			NestedCommandMOPMonitor lastMonitor = null;
			javamoprt.map.MOPMap mainMap = null;
			javamoprt.map.MOPMap lastMap = null;
			NestedCommandMOPMonitor_Set mainSet = null;
			NestedCommandMOPMonitor_Set origSet = null;
			NestedCommandMOPMonitor_Set monitors = null;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_x;
			javamoprt.ref.MOPMultiTagWeakReference TempRef_y;

			// Cache Retrieval
			if (y == NestedCommandMOP_y_Map_cachekey_1.get()) {
				TempRef_y = NestedCommandMOP_y_Map_cachekey_1;

				mainSet = NestedCommandMOP_y_Map_cacheset;
				mainMonitor = NestedCommandMOP_y_Map_cachenode;
			} else {
				TempRef_y = NestedCommandMOP_Object_RefMap.getMultiTagRef(y, thisJoinPoint.getStaticPart().getId());
			}

			if (mainSet == null || mainMonitor == null) {
				mainMap = NestedCommandMOP_y_Map;
				mainMonitor = (NestedCommandMOPMonitor)mainMap.getNode(TempRef_y);
				mainSet = (NestedCommandMOPMonitor_Set)mainMap.getSet(TempRef_y);
				if (mainSet == null){
					mainSet = new NestedCommandMOPMonitor_Set();
					mainMap.putSet(TempRef_y, mainSet);
				}

				if (mainMonitor == null) {
					origSet = NestedCommandMOP__To__x_Set;
					if (origSet!= null) {
						int numAlive = 0;
						for(int i = 0; i < origSet.size; i++) {
							origMonitor = origSet.elementData[i];
							Object x = (Object)origMonitor.MOPRef_x.get();
							if (!origMonitor.MOP_terminated && x != null) {
								origSet.elementData[numAlive] = origMonitor;
								numAlive++;

								TempRef_x = origMonitor.MOPRef_x;

								tempMap = NestedCommandMOP_x_y_Map;
								obj = tempMap.getMap(TempRef_x);
								if (obj == null) {
									obj = new javamoprt.map.MOPMapOfMonitor(1);
									tempMap.putMap(TempRef_x, obj);
								}
								lastMap = (javamoprt.map.MOPAbstractMap)obj;
								lastMonitor = (NestedCommandMOPMonitor)lastMap.getNode(TempRef_y);
								if (lastMonitor == null) {
									boolean timeCheck = true;

									if (TempRef_y.disable[0] > origMonitor.tau|| (TempRef_y.tau[0] > 0 && TempRef_y.tau[0] < origMonitor.tau)) {
										timeCheck = false;
									}

									if (timeCheck) {
										lastMonitor = (NestedCommandMOPMonitor)origMonitor.clone();
										lastMonitor.MOPRef_y = TempRef_y;
										if (TempRef_y.tau[0] == -1){
											TempRef_y.tau[0] = origMonitor.tau;
										}
										lastMap.putNode(TempRef_y, lastMonitor);

										tempMap = NestedCommandMOP_x_y_Map;
										obj = tempMap.getSet(TempRef_x);
										monitors = (NestedCommandMOPMonitor_Set)obj;
										if (monitors == null) {
											monitors = new NestedCommandMOPMonitor_Set();
											tempMap.putSet(TempRef_x, monitors);
										}
										monitors.add(lastMonitor);

										mainMap = NestedCommandMOP_y_Map;
										obj = mainMap.getSet(TempRef_y);
										mainSet = (NestedCommandMOPMonitor_Set)obj;
										if (mainSet == null) {
											mainSet = new NestedCommandMOPMonitor_Set();
											mainMap.putSet(TempRef_y, mainSet);
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
						mainMonitor = new NestedCommandMOPMonitor();

						mainMonitor.MOPRef_y = TempRef_y;

						NestedCommandMOP_y_Map.putNode(TempRef_y, mainMonitor);
						mainSet.add(mainMonitor);
						mainMonitor.tau = NestedCommandMOP_timestamp;
						if (TempRef_y.tau[0] == -1){
							TempRef_y.tau[0] = NestedCommandMOP_timestamp;
						}
						NestedCommandMOP_timestamp++;

						NestedCommandMOP__To__y_Set.add(mainMonitor);
					}

					TempRef_y.disable[0] = NestedCommandMOP_timestamp;
					NestedCommandMOP_timestamp++;
				}

				NestedCommandMOP_y_Map_cachekey_1 = TempRef_y;
				NestedCommandMOP_y_Map_cacheset = mainSet;
				NestedCommandMOP_y_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_suc_y(y);
			}
		}
	}

}
