package util;

import org.apache.commons.collections4.map.ReferenceIdentityMap;


@SuppressWarnings("serial")
public class WeakIdentityHashMap<K,V> extends ReferenceIdentityMap<K,V> {

    public WeakIdentityHashMap() {
        super(ReferenceStrength.WEAK, ReferenceStrength.HARD, true);
    }
    
}

