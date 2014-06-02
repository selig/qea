package benchmark.competition.java.jUnitRV_MMT;

import java.util.Random;

// Java - Team3 - Bench2

public class ExampleRequestResponse {

	public static void main(String... argv) {
		ExampleRequestResponse example = new ExampleRequestResponse();
		example.run(10000, 5, 5);
	}

	private final Random random = new Random(0);

	public void request(int service) {
	}

	public void respond(int service, int provider) {
	}

	public void run(int count, int size, int size2) {
		boolean[] opened = new boolean[size];
		long time = System.currentTimeMillis();
		for (int i = 0; i < count; ++i) {
			int p = random.nextInt(size);
			if (opened[p]) {
				opened[p] = false;
				respond(p, random.nextInt(size2));
			} else {
				opened[p] = true;
				request(p);
			}
			if ((i + 1) % 500 == 0) {
				System.out.println("(" + (i + 1) + ", "
						+ (System.currentTimeMillis() - time) + ")");
			}
		}
		for (int p = 0; p < size; ++p) {
			if (opened[p]) {
				respond(p, random.nextInt(size2));
			}
		}
	}
}