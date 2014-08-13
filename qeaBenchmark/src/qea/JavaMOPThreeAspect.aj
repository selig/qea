package qea;

import qea.properties.Property;
import qea.properties.competition.JavaMOP;
import qea.structure.impl.other.Verdict;
import qea.benchmark.competition.java.QEAMonitoringAspect;

public aspect JavaMOPThreeAspect extends QEAMonitoringAspect {

	private final int A = 1;
	private final int B = 2;
	private final int C = 3;

	public JavaMOPThreeAspect() {
		super(new JavaMOP().make(Property.JAVAMOP_THREE));
		validationMsg = "Property JavaMOP 3 satisfied";
		violationMsg = "Property JavaMOP 3 violated. The functions A(), B(), C() must be called equal times";
	}

	pointcut A() : call(void CThread.A());

	pointcut B() : call(void CThread.B());

	pointcut C() : call(void CThread.C());

	before() : A() {
		Verdict verdict;
		synchronized (monitor) {
			verdict = monitor.step(A);
		}
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg);
			printTimeAndExit();
		}
	}

	before() : B() {
		Verdict verdict;
		synchronized (monitor) {
			verdict = monitor.step(B);
		}
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg);
			printTimeAndExit();
		}
	}

	before() : C() {
		Verdict verdict;
		synchronized (monitor) {
			verdict = monitor.step(C);
		}
		if (verdict == Verdict.FAILURE) {
			System.err.println(violationMsg);
			printTimeAndExit();
		}
	}
}
