package util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

@SuppressWarnings("serial")
public class EagerGarbageHashMap<V> implements Map<Object,V> {

	private final HashMap<Integer,V> store = new HashMap<Integer,V>();
	private final ReferenceQueue to_remove = new ReferenceQueue();
	private final WeakHashMap<Object,GarbageRef> garbage_store = new WeakHashMap<Object,GarbageRef>();

	private int counter = 0;
	public final int frequency = 10;
	
	private class GarbageRef extends WeakReference<Object>{		
		public GarbageRef(Object k, int i){
			super(k,to_remove);
			id=i;
		}
		public final int id;		
	}
	
	private static class EntryWrapper {
		public EntryWrapper(int i){id=i;}
		public final int id;
	}
	
	private int getId(Object key){
		if(key instanceof EntryWrapper)
			return ((EntryWrapper) key).id;
		return System.identityHashCode(key);	
	}
	
	@Override
	public V put(Object key, V value){
		if(counter++%frequency==0) clearGarbage();
		
		Integer id = getId(key);		
		V old = store.put(id,value);
		
		//Record key - if not already in store
		if(old==null){
			GarbageRef g = new GarbageRef(key,id);
			garbage_store.put(key,g);
		}

		return old;
	}

	public void clearGarbage(){
		Object r =  to_remove.poll();
		while(r!=null){
			GarbageRef g = (GarbageRef) r;
			store.remove(g.id);
			r = to_remove.poll();
		}
	}
	
	@Override
	public V get(Object key) {
		if(counter++%frequency==0) clearGarbage();		
		Integer id = getId(key);
		return store.get(id);
	}

	@Override
	public V remove(Object key) {
		Integer id = getId(key);
		//TODO - perhaps remove from garbage_store? not sure how
		V old = store.remove(id);
		return old;
	}	
	
	@Override
	public void clear() {
		store.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		Integer id = getId(key);		
		return store.containsKey(id);
	}

	@Override
	public boolean containsValue(Object arg0) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Set<java.util.Map.Entry<Object, V>> entrySet() {
		throw new RuntimeException("Not implemented");
	}

	public Set<java.util.Map.Entry<Integer, V>> storeEntrySet() {
		return store.entrySet();
	}	
	
	@Override
	public boolean isEmpty() {
		return store.isEmpty();
	}


	private final class KeyWrapperIterator implements Iterator<Object> {

		private final Iterator<Integer> rawIterator;
		
		public KeyWrapperIterator(Iterator<Integer> iterator) {
			rawIterator=iterator;
		}

		@Override
		public boolean hasNext() {
			return rawIterator.hasNext();
		}

		@Override
		public Object next() {
			Integer next = rawIterator.next();
			return new EntryWrapper(next);
		}

		@Override
		public void remove() {
			throw new RuntimeException("Not implemented");			
		}
		
	}

    private final class KeySet extends AbstractSet<Object> {

    	private final Set<Integer> rawKeySet;
    	
		public KeySet(Set<Integer> rawKeySet) {
			this.rawKeySet=rawKeySet;
		}

		@Override
		public Iterator<Object> iterator() {
			return new KeyWrapperIterator(rawKeySet.iterator());
		}

		@Override
		public int size() {
			return rawKeySet.size();
		}

    }	
	
	@Override
	public Set<Object> keySet() {
		Set<Integer> rawKeySet = store.keySet();
		return new KeySet(rawKeySet);		
	}

	@Override
	public void putAll(Map<? extends Object, ? extends V> arg0) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public int size() {
		return store.size();
	}

	@Override
	public Collection<V> values() {
		throw new RuntimeException("Not implemented");
	}

}
