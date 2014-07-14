package benchmark.competition.java.javaMOP.monitoring;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.Property;
import properties.competition.JavaMOP;
import structure.impl.other.Verdict;
import structure.intf.QEA;
import benchmark.competition.java.javaMOP.MapIteratorTest;

@SuppressWarnings("rawtypes")
public aspect JavaMOPFourAspect {

	private final Monitor monitor;

	private final int CREATE = 1;
	private final int ITERATOR = 2;
	private final int USE = 3;
	private final int UPDATE = 4;

	public JavaMOPFourAspect() {
		QEA qea = new JavaMOP().make(Property.JAVAMOP_FOUR);
		monitor = MonitorFactory.create(qea);
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
			System.err.println("Violation in JavaMOP 4. [map=" + map + ", col="
					+ col + "]. UnsafeMapIterator");
			System.exit(0);
		}
	}

	after(Collection col) returning (Iterator iter) : iterator(col) {
		Verdict verdict = monitor.step(ITERATOR, col, iter);
		if (verdict == Verdict.FAILURE) {
			System.err.println("Violation in JavaMOP 4. [col=" + col
					+ ", iter=" + iter + "]. UnsafeMapIterator");
			System.exit(0);
		}
	}

	before(Map map) : update(map) {
		Verdict verdict = monitor.step(UPDATE, map);
		if (verdict == Verdict.FAILURE) {
			System.err.println("Violation in JavaMOP 4. [map=" + map
					+ "]. UnsafeMapIterator");
			System.exit(0);
		}
	}

	before(Iterator iter) : use(iter) {
		Verdict verdict = monitor.step(USE, iter);
		if (verdict == Verdict.FAILURE) {
			System.err.println("Violation in JavaMOP 4. [iter=" + iter
					+ "]. UnsafeMapIterator");
			System.exit(0);
		}
	}

	pointcut endOfProgram() : execution(void MapIteratorTest.main(String[]));

	after() : endOfProgram() {
		Verdict verdict = monitor.end();
		if (verdict == Verdict.FAILURE || verdict == Verdict.WEAK_FAILURE) {
			System.err.println("Violation in JavaMOP 4. UnsafeMapIterator.");
		} else {
			System.err.println("Property JavaMOP 4 satisfied");
		}
	}
}
