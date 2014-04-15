package test;

import creation.QEABuilder;
import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import structure.impl.qeas.QEAType;
import structure.intf.QEA;

public class TestSettings {

	public static Monitor create(QEA qea){
		
		// Standard operation
		//return MonitorFactory.create(qea);
		
		//To test QVarN versions
		QEABuilder b = QEABuilder.deconstruct(qea);
		try{
			return MonitorFactory.create(b.make(QEAType.QVARN_FVAR_DET_QEA));
		}catch(Exception e){
			return MonitorFactory.create(b.make(QEAType.QVARN_FVAR_NONDET_QEA));
		}
	}

}
