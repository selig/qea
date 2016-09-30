package online;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ExampleProgram{


	public static void main(String[] args){

		int total = 100000;

		Map<Object,Integer> map = new HashMap<Object,Integer>();
		
		
		for(int i=0;i<total;i++) map.put(new Object(),i);

		Collection<Object> keys = map.keySet();
		Iterator<Object> iterator = keys.iterator();
		
		Integer x = 0;
		for(int i=0;i<=total;i++){
			Object value = iterator.next();
			x += value.hashCode();
			if(i+1==total){
				map.put(new Object(),i);
			}
		} 

	}

}
