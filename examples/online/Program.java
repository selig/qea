
import java.util.*;

public class Program{


	public static void main(String[] args){

		int total = 10;

		Set<Object> values = new HashSet<Object>();
		for(int i=0;i<total;i++) values.add(new Object());

		Iterator<Object> iterator = values.iterator();
		
		for(int i=0;i<=total;i++){
			Object value = iterator.next();
			System.out.println("Value "+i+" == "+value);
		} 

	}

}
