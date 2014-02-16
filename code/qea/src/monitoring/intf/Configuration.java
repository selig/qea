package monitoring.intf;

/**
 * A configuration represents the status of *one* instantiation of an event automata
 * i.e. the states (and bindings of free variables) it has
 * 
 * Some implementations will not include multiple states or free variables if they
 * are not possible.
 * 
 * In the paper a Configuration is a pair of a state and a binding. Here we use it to
 * represent the set of such pairs if we have non-determinism.
 *
 * 
 * @author Helena Cuenca
 * @author Giles Reger
 */
public interface Configuration {

}
