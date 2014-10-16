package qea;

import static qea.structure.impl.other.Quantification.FORALL;
import qea.creation.QEABuilder;
import qea.monitoring.impl.MonitorFactory;
import qea.monitoring.intf.Monitor;
import qea.structure.impl.qeas.QEAType;
import qea.structure.intf.Guard;
import qea.structure.intf.QEA;

public class NumVarTest {

	public static int A = 1;
	public static int B = 2;
	
	public static void main(String[] args){
		
		for(int c=1;c<10;c++){
		for (int i=2;i<6;i++){
			QEA qea = createqea(i);
			test(qea,i);
		}
		}
		
	}
	
	public static QEA createqea(int vars){
		QEABuilder b = new QEABuilder("test "+vars);
		
		b.addQuantification(FORALL, -1);
		
		int[] args = new int[vars];
		args[0]=-1;
		for(int i=1;i<vars;i++){
			args[i]=i;
		}
		b.addTransition(1, A, args, 1);
		b.addTransition(1, B, args, Guard.isGreaterThanConstant(2, 1),2);
		
		b.addFinalStates(2);
		
		return b.make();
	}
	
	public static void test(QEA qea, int vars){
		
		Monitor one = 
				MonitorFactory.create(QEABuilder.change(qea, QEAType.QVAR01_FVAR_DET_QEA));
		
		Monitor two = MonitorFactory.create(qea);
		int steps = 1000000;
		long start = System.currentTimeMillis();
		for(int i=0;i<steps;i++){
			switch(vars){
				case 1 : one.step(A,0); break;
				case 2 : one.step(A,0,0) ; break;
				case 3 : one.step(A,0,0,0); break;
				case 4 : one.step(A,0,0,0,0); break;
				case 5 : one.step(A,0,0,0,0,0); break;
				default:
					int [] args = new int[vars+1];					
					one.step(A,args);				
			}

		}
		long middle = System.currentTimeMillis();
		for(int i=0;i<steps;i++){
		switch(vars){
		case 1 : two.step(A,0); break;
		case 2 : two.step(A,0,0) ; break;
		case 3 : two.step(A,0,0,0); break;
		case 4 : two.step(A,0,0,0,0); break;
		case 5 : two.step(A,0,0,0,0,0); break;
		default:
			int [] args = new int[vars+1];					
			two.step(A,args);				
		}
	}
		long end = System.currentTimeMillis();
		
		long first = middle-start;
		long second = end-middle;
		System.err.println(vars+"\t"+first+"\t"+second);
		
	}
	
}
