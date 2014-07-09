package util;

import org.apache.commons.collections4.map.ReferenceMap;

@SuppressWarnings("serial")
public class OurWeakHashMap<K, V> extends ReferenceMap<K, V> {

	public OurWeakHashMap() {
		// Weak keys
		// Hard values
		// purge on collection
		super(ReferenceStrength.WEAK, ReferenceStrength.HARD, true);
	}

	public OurWeakHashMap(int size) {
		super(ReferenceStrength.WEAK, ReferenceStrength.HARD, size, 0.75f, true);
	}

}
