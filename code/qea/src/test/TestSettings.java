package test;

import creation.QEABuilder;
import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import structure.impl.qeas.QEAType;
import structure.intf.QEA;

public class TestSettings {

	public static enum TestMode {
		standard,
		general
	}
	public static TestMode mode = TestMode.standard;
	
	public static Monitor create(QEA qea){
		
		switch(mode){
			case standard:
				// Standard operation
				return MonitorFactory.create(qea);
			
			case general:
				//To test QVarN versions
				QEABuilder b = QEABuilder.deconstruct(qea);
				try{
					return MonitorFactory.create(b.make(QEAType.QVARN_FVAR_DET_QEA));
				}catch(Exception e){
					return MonitorFactory.create(b.make(QEAType.QVARN_FVAR_NONDET_QEA));
				}
		}
		return null;
	}

}
