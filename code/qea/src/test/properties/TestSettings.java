package test.properties;

import monitoring.impl.MonitorFactory;
import monitoring.intf.Monitor;
import structure.intf.QEA;

public class TestSettings {

	public static Monitor create(QEA qea){
		return MonitorFactory.create(qea);
	}

}
