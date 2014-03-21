package structure.intf;

import exceptions.ShouldNotHappenException;

public abstract class Guard {

	/**
	 * Checking the guard
	 * 
	 * @return true if guard evaluates to true on binding
	 */
	public abstract boolean check(Binding binding);
	public abstract boolean usesQvars();
	public Guard replace(int var, Object val){ 
		throw new ShouldNotHappenException("Not implemented - experimental feature!");
	}

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
			@Override
			public boolean usesQvars(){ return var0<0 || var1 < 0;}
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
			@Override
			public boolean usesQvars(){ return var0<0 || var1 < 0;}			
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
			@Override
			public boolean usesQvars(){ return var0<0 || var1 < 0;}			
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
			@Override
			public boolean usesQvars(){ return var0<0 || var1 < 0;}		
			
			public Guard replace(final int var, final Object val){
				if(var!=var0 && var!=var1) return this;
				return new Guard(""){
					public boolean check(Binding binding){
						Integer val0 = (Integer) ((var0==var) ? val : binding.getForcedAsInteger(var0));
						Integer val1 = (Integer) ((var1==var) ? val : binding.getForcedAsInteger(var1));
						return (val0 > val1);
					}

					@Override
					public boolean usesQvars() {						
						return false;
					}
				};
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
			@Override
			public boolean usesQvars(){ return var0<0 || var1 < 0;}			
		};
	}

	public static Guard isGreaterThanConstant(final int var0, final int val1) {
		return new Guard("x_" + var0 + " > "+ val1) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				return (val0 > val1);
			}
			@Override
			public boolean usesQvars(){ return var0<0;}			
		};
	}


}
