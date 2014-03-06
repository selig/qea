package structure.intf;


public abstract class Assignment {

	/**
	 * Applying the assignment
	 * 
	 * Assignments should always create a *new* binding
	 * 
	 * @return updated binding
	 */
	public abstract Binding apply(Binding binding);

	private final String name;

	public Assignment(String name) {
		this.name = name;
	}

	/*
	 * Get the name of the assignment
	 * 
	 * @return the name of the assignment
	 */
	public String getName() {
		return name;
	}

	/*
	 * Produce an assignment capturing binding += [var0 -> binding[var1]]
	 * 
	 * @ returns an assignment for this property
	 */
	public static Assignment store(final int var0, final int var1) {
		return new Assignment("store(x_" + var0 + ", x_" + var1 + ")") {
			@Override
			public Binding apply(Binding binding) {
				Object val1 = binding.getForced(var1);
				Binding newBinding = binding.copy();
				binding.setValue(var0, val1);
				return newBinding;
			}
		};
	}

}
