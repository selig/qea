package benchmark.competition.java.javaMOP.monitoring;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import monitoring.intf.QEAMonitoringAspect;
import properties.Property;
import properties.competition.JavaMOP;
import structure.impl.other.Verdict;
import benchmark.competition.java.javaMOP.MapIteratorTest;

@SuppressWarnings("rawtypes")
public aspect JavaMOPFourAspect extends QEAMonitoringAspect {

	private final int CREATE = 1;
	private final int ITERATOR = 2;
	private final int USE = 3;
	private final int UPDATE = 4;

	public JavaMOPFourAspect() {
		super(new JavaMOP().make(Property.JAVAMOP_FOUR));
		validationMsg = "Property JavaMOP 4 satisfied";
		violationMsg = "Property JavaMOP 4 violated. UnsafeMapIterator";
	}

	// create(m,c)
	pointcut create(Map map) : call(Set Map.keySet()) && target(map)
		&& within(MapIteratorTest);

	// iterator(c,i)
	pointcut iterator(Collection col) : call(Iterator Collection.iterator())
		&& target(col) && within(MapIteratorTest);

	// update(m)
	pointcut update(Map map) : call(* Map.put(*, *)) && target(map)
		&& within(MapIteratorTest);

	// use(i)
	pointcut use(Iterator iter) : call(* Iterator.*(..)) && target(iter)
		&& within(MapIteratorTest);

	after(Map map) returning (Collection col) : create(map) {
		Verdict verdict = monitor.step(CREATE, map, col);
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [map=" + map + ", col=" + col
					+ "]");
			printTimeAndExit();
		}
	}

	after(Collection col) returning (Iterator iter) : iterator(col) {
		Verdict verdict = monitor.step(ITERATOR, col, iter);
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [col=" + col + ", iter=" + iter
					+ "]");
			printTimeAndExit();
		}
	}

	before(Map map) : update(map) {
		Verdict verdict = monitor.step(UPDATE, map);
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [map=" + map + "]");
			printTimeAndExit();
		}
	}

	before(Iterator iter) : use(iter) {
		Verdict verdict = monitor.step(USE, iter);
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg + " [iter=" + iter + "]");
			printTimeAndExit();
		}
	}
}
