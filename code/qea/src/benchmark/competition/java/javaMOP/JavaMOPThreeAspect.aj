package benchmark.competition.java.javaMOP;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import properties.Property;
import properties.competition.JavaMOP;
import structure.impl.other.Verdict;
import structure.intf.QEA;

public aspect JavaMOPThreeAspect {

	private final Monitor monitor;

	private final int A = 1;
	private final int B = 2;
	private final int C = 3;

	public JavaMOPThreeAspect() {
		QEA qea = new JavaMOP().make(Property.JAVAMOP_THREE);
		monitor = MonitorFactory.create(qea);
	}
	
	pointcut A() : call(void CThread2.A());
	
	pointcut B() : call(void CThread2.B());
	
	pointcut C() : call(void CThread2.C());
	
	before() : A() {
		Verdict verdict;
		synchronized (monitor) {
			verdict = monitor.step(A);
		}
		if (verdict == Verdict.FAILURE) {
			System.err
			.println("Violation in JavaMOP 3. The functions A(), B(), C() must be called equal times.");
		}
	}
	
	before() : B() {
		Verdict verdict;
		synchronized (monitor) {
			verdict = monitor.step(B);
		}
		if (verdict == Verdict.FAILURE) {
			System.err
			.println("Violation in JavaMOP 3. The functions A(), B(), C() must be called equal times.");
		}
	}
	
	before() : C() {
		Verdict verdict;
		synchronized (monitor) {
			verdict = monitor.step(C);
		}
		if (verdict == Verdict.FAILURE) {
			System.err
			.println("Violation in JavaMOP 3. The functions A(), B(), C() must be called equal times.");
		}
	}
	

	pointcut endOfProgram() : execution(void SRSTest.main(String[]));

	after() : endOfProgram() {
		Verdict verdict = monitor.end();
		if (verdict == Verdict.FAILURE || verdict == Verdict.WEAK_FAILURE) {
			System.err
					.println("Violation in JavaMOP 3. The functions A(), B(), C() must be called equal times.");
		} else {
			System.err.println("Property JavaMOP 3 satisfied");
		}
	}
}
