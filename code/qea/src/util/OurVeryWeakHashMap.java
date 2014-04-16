package util;

import org.apache.commons.collections4.map.ReferenceMap;


@SuppressWarnings("serial")
public class OurVeryWeakHashMap<K,V> extends ReferenceMap<K,V> {

    public OurVeryWeakHashMap() {
    	// Weak keys
    	// Weak values
    	// purge on collection
        super(ReferenceStrength.WEAK, ReferenceStrength.WEAK, true);
    }
    
}

