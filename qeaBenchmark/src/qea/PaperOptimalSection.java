package qea;


import static qea.structure.impl.other.Quantification.FORALL;
import static qea.structure.impl.other.Quantification.EXISTS;
import static qea.structure.intf.Assignment.decrement;
import static qea.structure.intf.Assignment.increment;
import static qea.structure.intf.Assignment.setVal;
import static qea.structure.intf.Guard.and;
import static qea.structure.intf.Guard.isEqualSem;
import static qea.structure.intf.Guard.isGreaterThanConstant;
import static qea.structure.intf.Guard.isSemEqualToConstant;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import qea.creation.QEABuilder;
import qea.monitoring.impl.CSVFileMonitor;
import qea.monitoring.impl.MonitorFactory;
import qea.monitoring.impl.translators.OfflineTranslator;
import qea.monitoring.intf.Monitor;
import qea.properties.competition.MonPoly;
import qea.properties.competition.translators.MonPolyTranslators;
import qea.structure.intf.QEA;

public class PaperOptimalSection {

	public static void main(String[] args) throws IOException{
	
		int i = 10;
		
		while(i-->0)
			publishers();
		
	}
	
	
	public static void withdrawal() throws IOException{
		
		QEA orig = (new MonPoly()).makeFour_withTwo();
		QEA optimal = (new MonPoly()).makeFour();
		
		OfflineTranslator t1 = (new MonPolyTranslators()).makeFour();
		OfflineTranslator t2 = (new MonPolyTranslators()).makeFour();
		
		String trace = "/Users/giles/git/csrv14/OFFLINE/Team4/Bench4/trace.csv";
		
		CSVFileMonitor one = new CSVFileMonitor(trace, orig, t1);
		CSVFileMonitor two = new CSVFileMonitor(trace, optimal, t2);
		
		long start = System.currentTimeMillis();
		System.out.println("one is "+one.monitor());
		long middle = System.currentTimeMillis();
		System.out.println("two is "+two.monitor());
		long end = System.currentTimeMillis();
		System.out.println("one: "+(middle-start)+", two: "+(end-middle));
			
		
	}
	
	public static void persistenthash(){
		
		int o = -1;
		int s = -2;

        final int ADD = 1;
        final int OBSERVE = 2;
        final int REMOVE = 3;		
		
		int hash = 1;
		int count_inside = 2;
		int hash_new = 3;		
		
		
		QEABuilder bo = new QEABuilder("PersistentHashes");	
		bo.addQuantification(FORALL, o);
		bo.addQuantification(FORALL, s);
		bo.addTransition(1,ADD,new int[]{s,o,hash},2);
		bo.addTransition(2,REMOVE,new int[]{s,o,hash_new},isEqualSem(hash,hash_new),1);
		bo.addTransition(2,OBSERVE,new int[]{o,hash_new},isEqualSem(hash, hash_new),2);
		bo.addFinalStates(1, 2);
		bo.setSkipStates(1);			
		QEA orig = bo.make();
		
		QEABuilder b = new QEABuilder("PersistentHashes");	
		b.addQuantification(FORALL, o);
		b.addTransition(1,ADD,new int[]{o,hash},setVal(count_inside,1),2);
		b.addTransition(2,ADD,new int[]{o,hash_new},isEqualSem(hash,hash_new),increment(count_inside),2);
		b.addTransition(2,REMOVE,new int[]{o,hash_new},
					and(isGreaterThanConstant(count_inside,1),
						  isEqualSem(hash,hash_new)),decrement(count_inside),2);
		b.addTransition(2,REMOVE,new int[]{o,hash_new},
					and(isSemEqualToConstant(count_inside, 1),isEqualSem(hash, hash_new)),
					decrement(count_inside),1);
		b.addTransition(2,OBSERVE,new int[]{o,hash_new},isEqualSem(hash, hash_new),2);
		b.addFinalStates(1, 2);
		b.setSkipStates(1);				
		QEA optimal = b.make();
		
		Monitor one = MonitorFactory.create(orig);
		Monitor two = MonitorFactory.create(optimal);
		
		long start = System.currentTimeMillis();
		do_persistent_work(one,two,true);
		System.out.println("one is "+one.end());
		long middle = System.currentTimeMillis();
		do_persistent_work(one,two,false);
		System.out.println("two is "+two.end());
		long end = System.currentTimeMillis();
		System.out.println("one: "+(middle-start)+", two: "+(end-middle));
		System.out.println((persistent_events/2)+" events");
		
	}
	private static int persistent_events=0;
	
	private static class A { public int id=5; public Object s = null;public int hashCode(){ return id;}}
	
	private static void do_add(Monitor m1,Monitor m2, boolean select, Object set, A a){
		if(select){
			m1.step(1,set,a,a.hashCode());
		}else{
			m2.step(1,a,a.hashCode());
		}
		persistent_events++;
	}
	private static void do_remove(Monitor m1,Monitor m2, boolean select, Object set, A a){
		if(select){
			m1.step(3,set,a,a.hashCode());
		}else{
			m2.step(3,a,a.hashCode());
		}	
		persistent_events++;
	}
	private static void do_observe(Monitor m1,Monitor m2, boolean select, Object set, A a){
		if(select){
			m1.step(2,a,a.hashCode());
		}else{
			m2.step(2,a,a.hashCode());
		}	
		persistent_events++;
	}
	
	private static void do_persistent_work(Monitor m1,Monitor m2, boolean select){
		
		int k=100;
		while(k-->0){
		
			Set<Set<A>> all = new HashSet<Set<A>>();
			int i=100;
			while(i-->0){
				Object s = new Object();
				int j=100;
				Set<A> added = new HashSet<A>();
				all.add(added);
				while(j-->0){ A a = new A(); a.s=s;added.add(a); do_add(m1,m2,select,s,a);}
				for(A a : added){ do_observe(m1,m2,select,s,a);}				
			}
			for(Set<A> added : all) for(A a : added) do_observe(m1,m2,select,a.s,a);
			for(Set<A> added : all) for(A a : added) do_observe(m1,m2,select,a.s,a);
		
		}
		
		Object s = new Object();
		A a = new A();
		do_add(m1,m2,select,s,a);
		a.id=10;
		do_observe(m1,m2,select,s,a);
	}
	
	public static void publishers(){
		
		int p = -1;
		int s = -2;
		int SEND = 1; int REPLY = 2;
		int fs = 1;
		
		QEABuilder b1 = new QEABuilder("pub1");
		b1.addQuantification(FORALL, p);
		b1.addQuantification(EXISTS, s);
		b1.addTransition(1,SEND, new int[]{p,s},2);
		b1.addTransition(2,REPLY, new int[]{s,p},3);
		b1.setSkipStates(1,2,3);
		b1.addFinalStates(3);
		QEA orig = b1.make();
		
		QEABuilder b2 = new QEABuilder("pub1");
		b2.addQuantification(FORALL, p);
		b2.addTransition(1,SEND, new int[]{p,fs},2);
		b2.addTransition(2,REPLY, new int[]{fs,p},3);
		b2.setSkipStates(1,2,3);
		b2.addFinalStates(3);
		QEA optimal = b2.make();
		
		Monitor one = MonitorFactory.create(orig);
		Monitor two = MonitorFactory.create(optimal);
		
		long start = System.currentTimeMillis();
		dopubwork(one);
		System.out.println("one is "+one.end());
		long middle = System.currentTimeMillis();
		dopubwork(two);
		System.out.println("two is "+two.end());
		long end = System.currentTimeMillis();
		System.out.println("one: "+(middle-start)+", two: "+(end-middle));
		System.out.println((pub_events/2)+" events");		
		
	}
	private static int pub_events=0;
	
	private static void dopubwork(Monitor m){
		int x = 100000;
		while(x-->0){
			Object p = new Object(); Object s = new Object();
			m.step(1,p,s);
			m.step(2,s,p);
			pub_events+=2;
		}
		Object p = new Object(); Object s = new Object();
		m.step(1,p,s);	
		pub_events++;
	}
	
}
