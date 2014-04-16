package monitoring.impl;
/*
 * Note that the semantics dictate that a binding can be removed if
 * it currently is safe to remove (i.e. does not effect verdict)
 * and no states with different verdicts are reachable using
 * non-garbage objects.
 */

public enum GarbageMode {
	NONE, 
	LAZY, 
	EAGER, 
	UNSAFE_LAZY,  //remove a binding if all member becomes garbage
				  // I think this is what JavaMOP does
				  // it's technically unsafe as the binding might be
				  // extended to change the verdict
				  //
				  // There are more unsafe versions ie. remove if any
				  // member becomes garbage.
	
	OVERSAFE_LAZY // not sure yet
}
