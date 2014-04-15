package structure.impl.qeas;


public abstract class Abstr_QVarN_NoFVar_QEA extends Abstr_QVarN_QEA {


	
	public Abstr_QVarN_NoFVar_QEA(int numStates, int numEvents, int initialState,
			int qVariablesCount) {
		super(numStates,numEvents,initialState,qVariablesCount,0);
	}	
	
}
