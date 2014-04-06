package structure.intf;

import java.util.HashSet;

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

	public static Assignment createSetFromElement(final int varSet,
			final int varElement) {
		return new Assignment("createSet_" + varSet + "_FromElement_"
				+ varElement) {

			@Override
			public Binding apply(Binding binding) {
				HashSet<Object> set = new HashSet<>();
				set.add(binding.getForced(varElement));
				Binding newBinding = binding.copy();
				newBinding.setValue(varSet, set);
				return newBinding;
			}
		};
	}

	public static Assignment addElementToSet(final int varSet,
			final int varElement) {
		return new Assignment("addElement_" + varElement + "ToSet_" + varSet) {

			@Override
			public Binding apply(Binding binding) {
				if (binding.getValue(varSet) != null) {
					HashSet<Object> set = (HashSet<Object>) binding
							.getForced(varSet);
					set.add(binding.getForced(varElement));
					Binding newBinding = binding.copy();
					newBinding.setValue(varSet, set);
					return newBinding;
				}
				HashSet<Object> set = new HashSet<>();
				set.add(binding.getForced(varElement));
				Binding newBinding = binding.copy();
				newBinding.setValue(varSet, set);
				return newBinding;
			}
		};
	}

	public static Assignment removeElementFromSet(final int varSet,
			final int varElement) {
		return new Assignment("removeElement_" + varElement + "_FromSet_"
				+ varSet) {

			@Override
			public Binding apply(Binding binding) {
				HashSet<Object> set = (HashSet<Object>) binding
						.getForced(varSet);
				set.remove(binding.getForced(varElement));
				// TODO Do we always need to create a copy of the binding in the
				// assignments?
				Binding newBinding = binding.copy();
				newBinding.setValue(varSet, set);
				return newBinding;
			}
		};
	}
}
