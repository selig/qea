package qea.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 * There is an assumption that inner uses System.identityHashCode
 * and == to identify keys.
 * 
 * EagerGarbageHashMap should have this functionality built in, as it deals with 
 * identity differently
 */

public class IgnoreIdentityWrapper<K, V> implements Map<K, V>, IgnoreWrapper<K> {

	private final Map<K, V> inner;
	private final HashSet<Integer> ignored_ids = new HashSet<Integer>();

	@Override
	public void ignore(K key) {
		int id = System.identityHashCode(key);
		ignored_ids.add(id);
	}

	public IgnoreIdentityWrapper(Map<K, V> inner) {
		this.inner = inner;
	}

	@Override
	public void clear() {
		inner.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return inner.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return inner.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return inner.entrySet();
	}

	/**
	 * Returns null if the key is ignored
	 */
	@Override
	public V get(Object key) {
		int id = System.identityHashCode(key);
		if (ignored_ids.contains(id)) {
			return null;
		}
		return inner.get(key);
	}

        /**
         * Returns true if the key is ignored
         */
        @Override
        public boolean isIgnored(Object key) {
                int id = System.identityHashCode(key);
                if (ignored_ids.contains(id)) {
                        return true;
                }
                return false;
        }

	@Override
	public boolean isEmpty() {
		return inner.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return inner.keySet();
	}

	@Override
	public V put(K key, V value) {
		return inner.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		inner.putAll(m);
	}

	@Override
	public V remove(Object key) {
		return inner.remove(key);
	}

	@Override
	public int size() {
		return inner.size();
	}

	@Override
	public Collection<V> values() {
		return inner.values();
	}

}
