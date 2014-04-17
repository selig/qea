package benchmark.rovers;

import static de.uni_luebeck.isp.rvtool.javamonitorinjection.api.syntax.Syntax.Always;
import static de.uni_luebeck.isp.rvtool.javamonitorinjection.api.syntax.Syntax.X;
import static de.uni_luebeck.isp.rvtool.javamonitorinjection.api.syntax.Syntax.and;
import static de.uni_luebeck.isp.rvtool.javamonitorinjection.api.syntax.Syntax.called;
import static de.uni_luebeck.isp.rvtool.javamonitorinjection.api.syntax.Syntax.implies;
import static de.uni_luebeck.isp.rvtool.javamonitorinjection.api.syntax.Syntax.or;

import java.io.Serializable;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.ImmutableList;

import de.uni_luebeck.isp.rvtool.javamonitorinjection.api.syntax.Event;
import de.uni_luebeck.isp.rvtool.javamonitorinjection.api.syntax.Property;
import de.uni_luebeck.isp.rvtool.javamonitorinjection.api.syntax.SMTPredicate;
import de.uni_luebeck.isp.rvtool.junitrv.DFLTL4Monitor;
import de.uni_luebeck.isp.rvtool.junitrv.FLTL4Monitor;
import de.uni_luebeck.isp.rvtool.junitrv.Monitor;
import de.uni_luebeck.isp.rvtool.junitrv.Monitors;
import de.uni_luebeck.isp.rvtool.junitrv.RVRunner;
import de.uni_luebeck.isp.rvtool.rvlib.syntax.ltl.AND;
import de.uni_luebeck.isp.rvtool.rvlib.syntax.ltl.FALSE;
import de.uni_luebeck.isp.rvtool.rvlib.syntax.ltl.LTL;
import de.uni_luebeck.isp.rvtool.rvlib.syntax.ltl.NOT;
import de.uni_luebeck.isp.rvtool.rvlib.syntax.ltl.OR;
import de.uni_luebeck.isp.rvtool.rvlib.syntax.ltl.Proposition;
import de.uni_luebeck.isp.rvtool.rvlib.syntax.ltl.R;
import de.uni_luebeck.isp.rvtool.rvlib.syntax.ltl.X;
import de.uni_luebeck.isp.rvtool.rvlib.syntax.smt.SMTExpression;
import de.uni_luebeck.isp.rvtool.rvlib.syntax.smt.SMTInteger;
import de.uni_luebeck.isp.rvtool.rvlib.syntax.smt.SMTOperator;
import de.uni_luebeck.isp.rvtool.rvlib.syntax.smt.SMTSymbol;
import de.uni_luebeck.isp.rvtool.rvlib.syntax.smt.SMTVariable;

@RunWith(RVRunner.class)
public class RVunitDoEval extends DoEval<String> {
	
	@Override
	public DoWork<String> makeWork() {
		return new RVunitDoWork();
	}

	private static final String dowork = "benchmark.rovers.RVunitDoWork";
	// SETUP for propsoitional resource lifecycle
	private static Event request = called(dowork,"request");
	private static Event deny = called(dowork,"deny");
	private static Event grant = called(dowork,"grant");
	private static Event cancel = called(dowork,"cancel");
	private static Event rescind = called(dowork,"rescind");
	
	private static Monitor propositional_resourceLifecycle = new FLTL4Monitor(
			Always(
					and(
							and(
									and(
											implies(request, X(or(deny,grant))),
											implies(deny, X(or(request,request)))
									),
									and(
											implies(grant, X(or(cancel,rescind))),
											implies(cancel, X(or(request,request)))
									)
							),
							implies(rescind, X(or(cancel,rescind)))					
					)
			)
	);
	
	//SETUP for first-order resource lifecycle
	
    private static final SMTPredicate id = new SMTPredicate("p", new Serializable() {
        public SMTExpression<Void> p(RVunitDoWork work, Object o) {
            return new SMTOperator<>("eq", ImmutableList.<SMTExpression<Void>>of(
                    new SMTVariable<Void>("x"),
                    new SMTInteger<Void>(System.identityHashCode(o))));
        }
    });
    private static final Proposition<SMTExpression<Property>> propId = new Proposition<SMTExpression<Property>>(new SMTSymbol<Property>(id)); 
    
    private static final LTL<SMTExpression<Property>> request_f = new AND<>(ImmutableList.<LTL<SMTExpression<Property>>>of(propId, new Proposition< SMTExpression< Property>>(new SMTSymbol<Property>(request))));    
    private static final LTL<SMTExpression<Property>> grant_f = new AND<>(ImmutableList.<LTL<SMTExpression<Property>>>of(propId, new Proposition< SMTExpression< Property>>(new SMTSymbol<Property>(grant))));
    private static final LTL<SMTExpression<Property>> deny_f = new AND<>(ImmutableList.<LTL<SMTExpression<Property>>>of(propId, new Proposition< SMTExpression< Property>>(new SMTSymbol<Property>(deny))));
    private static final LTL<SMTExpression<Property>> cancel_f = new AND<>(ImmutableList.<LTL<SMTExpression<Property>>>of(propId, new Proposition< SMTExpression< Property>>(new SMTSymbol<Property>(cancel))));
    private static final LTL<SMTExpression<Property>> rescind_f = new AND<>(ImmutableList.<LTL<SMTExpression<Property>>>of(propId, new Proposition< SMTExpression< Property>>(new SMTSymbol<Property>(rescind))));
    
	private static final LTL<SMTExpression<Property>> request_next = new OR<>(ImmutableList.of(new NOT<>(request_f), 
			new X<>(new OR<>(ImmutableList.of(deny_f,grant_f)))));    
	private static final LTL<SMTExpression<Property>> grant_next = new OR<>(ImmutableList.of(new NOT<>(grant_f), 
			new X<>(new OR<>(ImmutableList.of(cancel_f,rescind_f)))));
	private static final LTL<SMTExpression<Property>> deny_next = new OR<>(ImmutableList.of(new NOT<>(deny_f), 
			new X<>(new OR<>(ImmutableList.of(request_f)))));
	private static final LTL<SMTExpression<Property>> cancel_next = new OR<>(ImmutableList.of(new NOT<>(cancel_f), 
			new X<>(new OR<>(ImmutableList.of(request_f))))); 
	private static final LTL<SMTExpression<Property>> rescind_next = new OR<>(ImmutableList.of(new NOT<>(rescind_f), 
			new X<>(new OR<>(ImmutableList.of(cancel_f,rescind_f)))));
	
	private static final AND<SMTExpression<Property>> all_together = 
		new AND<>(ImmutableList.of(
			request_next,
			grant_next,
			deny_next,
			cancel_next,
			rescind_next											
		));
	
	private static Monitor parametric_resourceLifecycle = new DFLTL4Monitor(
			new R<>(new FALSE<SMTExpression<Property>>(),all_together)	
	);    
    
	@Test
	@Monitors({"parametric_resourceLifecycle"})
	public void testResourceLifecycle(){
		//eval_for_ResourceLifecycle("ResourceLifecycle","ResourceLifecycle");
		DoWork work = makeWork();
		Object resource = new Object();
		work.request(resource);
		work.request(resource);
	}

	
	
	
}
