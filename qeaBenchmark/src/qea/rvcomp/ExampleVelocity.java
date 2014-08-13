package qea.rvcomp;

import java.util.Random;

// Java - Team3 - Bench4

public class ExampleVelocity {

	public static void main(String... argv) {
		ExampleVelocity example = new ExampleVelocity();
		example.run(10000, 50, 5.0);
	}

	private final Random random = new Random(0);

	public void step(double pos, double time) {
	}

	public void run(int count, int resets, double vmax) {
		long runtime = System.currentTimeMillis();
		for (int j = 0, k = 0; j < count / resets; ++j) {
			double pos = 0.0;
			double time = 0.0;
			step(pos, time);
			for (int i = 0; i < resets; ++i, ++k) {
				double step = 0.5 + random.nextDouble();
				double v = vmax * random.nextDouble();
				double s = step * v;
				time += step;
				pos += s;
				step(pos, time);
				if ((k + 1) % 500 == 0) {
					System.out.println("(" + (k + 1) + ", "
							+ (System.currentTimeMillis() - runtime) + ")");
				}
			}
		}
	}
}
