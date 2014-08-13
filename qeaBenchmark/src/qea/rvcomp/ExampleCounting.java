package qea.rvcomp;

// Java - Team3 - Bench1

public class ExampleCounting {

	public static void main(String... argv) {
		ExampleCounting example = new ExampleCounting();
		long time = System.currentTimeMillis();
		for (int i = 1; i < 10000; ++i) {
			example.step(i);
			if ((i + 1) % 500 == 0) {
				System.out.println("(" + (System.currentTimeMillis() - time)
						+ ", " + (i + 1) + ")");
			}
		}
	}

	public void step(int i) {
	}
}
