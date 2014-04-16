package test.functionality;

import java.util.Map;
import java.util.Set;

import org.junit.Test;

import properties.papers.DaCapo;
import structure.intf.QEA;
import creation.QEABuilder;
import creation.QEABuilder.TempEvent;

public class EnableSets {

	@Test
	public void test() {
		
		QEA qea = DaCapo.makeUnsafeIter();
		
		QEABuilder b = QEABuilder.deconstruct(qea);
		
		Map<TempEvent,Set<Set<TempEvent>>> es = b.computeEnableSets();
		
		for(Map.Entry<TempEvent,Set<Set<TempEvent>>> entry : es.entrySet()){
			System.err.println(entry.getKey()+"\t"+entry.getValue());
		}
		
	}

}
