package benchmark.rovers;

import benchmark.rovers.qea.QeaDoEval;
import benchmark.rovers.ruler.RuleRDoEval;

public class DoAllEval {

	public static void main(String[] args) {

		for (int i = 0; i < 2; i++) {

			// System.err.println("Category: MOP");
			// MopDoEval.main(null);

			System.err.println("Category: RuleR");
			RuleRDoEval.main(null);

			QeaDoEval.main(new String[] { "1" });

		}

	}

}
