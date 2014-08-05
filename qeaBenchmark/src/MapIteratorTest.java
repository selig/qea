import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MapIteratorTest {

	public final static int NUM = 50;

	public static void main(String[] args) {

		Map<String, Integer> m1 = new HashMap<String, Integer>();
		Map<String, Integer> m2 = new HashMap<String, Integer>();
		for (int i = 0; i < NUM; i++) {
			m1.put(String.valueOf(i), i);
			m2.put(String.valueOf(i), i);
		}
		m1.put(String.valueOf(51), 51);
		;

		Set<String> keys1 = m1.keySet();
		Iterator i1 = keys1.iterator();
		while (i1.hasNext()) {
			System.out.println(i1.next());
		}
		System.out.println("i1 is safe!");

		Set<String> keys2 = m2.keySet();
		m2.put(String.valueOf(51), 51);
		Iterator i2 = keys2.iterator();
		while (i2.hasNext()) {
			System.out.println(i2.next());
		}
		System.out.println("i2 is safe!");

		Iterator i3 = keys2.iterator();
		for (int j = 0; j < NUM - 4; j++) {
			System.out.println(i3.next());
		}
		m2.put(String.valueOf(52), 52);
		System.out.println(i3.next());

	}

}
