package structure.intf;

public abstract class Guard {

	/**
	 * Checking the guard
	 * 
	 * @return true if guard evaluates to true on binding
	 */
	public abstract boolean check(Binding binding);

	private final String name;

	public Guard(String name) {
		this.name = name;
	}

	/*
	 * Get the name of the guard
	 * 
	 * @return the name of the guard
	 */
	public String getName() {
		return name;
	}

	/*
	 * Produce a guard capturing binding(var0) == binding(var1)
	 * 
	 * @ returns a guard for this property
	 */
	public static Guard isEqual(final int var0, final int var1) {
		return new Guard("x_" + var0 + " == x_" + var1) {
			@Override
			public boolean check(Binding binding) {
				Object val0 = binding.getForced(var0);
				Object val1 = binding.getForced(var1);
				return (val0 == val1);
			}
		};
	}

	/*
	 * Produce a guard capturing binding(var0) != binding(var1)
	 * 
	 * @ returns a guard for this property
	 */
	public static Guard isNotEqual(final int var0, final int var1) {
		return new Guard("x_" + var0 + " != x_" + var1) {
			@Override
			public boolean check(Binding binding) {
				Object val0 = binding.getForced(var0);
				Object val1 = binding.getForced(var1);
				return (val0 != val1);
			}
		};
	}

	/*
	 * Produce a guard capturing binding(var0) equals binding(var1)
	 * 
	 * @ returns a guard for this property
	 */
	public static Guard isEqualSem(final int var0, final int var1) {
		return new Guard("x_" + var0 + " equals x_" + var1) {
			@Override
			public boolean check(Binding binding) {
				Object val0 = binding.getForced(var0);
				Object val1 = binding.getForced(var1);
				return val0.equals(val1);
			}
		};
	}

	/*
	 * Produce a guard capturing binding(var0) > binding(var1)
	 * 
	 * @ returns a guard for this property
	 */
	public static Guard isGreaterThan(final int var0, final int var1) {
		return new Guard("x_" + var0 + " > x_" + var1) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				Integer val1 = binding.getForcedAsInteger(var1);
				return (val0 > val1);
			}
		};
	}

	/*
	 * Produce a guard capturing binding(var0) <= binding(var1)
	 * 
	 * @ returns a guard for this property
	 */
	public static Guard isLessThanOrEqualTo(final int var0, final int var1) {
		return new Guard("x_" + var0 + " <= x_" + var1) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				Integer val1 = binding.getForcedAsInteger(var1);
				return (val0 <= val1);
			}
		};
	}

	public static Guard isGreaterThanConstant(final int var0, final int val1) {
		return new Guard("x_" + var0 + " > "+ val1) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				return (val0 > val1);
			}
		};
	}

}
