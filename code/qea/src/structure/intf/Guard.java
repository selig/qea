package structure.intf;

import java.util.HashSet;

import exceptions.ShouldNotHappenException;

public abstract class Guard {

	/**
	 * Checking the guard
	 * 
	 * @return true if guard evaluates to true on binding
	 */
	public abstract boolean check(Binding binding);

	public abstract boolean check(Binding binding, int qvar, Object firstQval);

	public abstract boolean usesQvars();
	public abstract int[] vars();

	public Guard replace(int var, Object val) {
		throw new ShouldNotHappenException(
				"Not implemented - experimental feature!");
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
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Object val0 = (var0 == qvar) ? firstQval : binding
						.getForced(var0);
				Object val1 = (var1 == qvar) ? firstQval : binding
						.getForced(var1);
				return (val0 == val1);
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}
			@Override
			public int[] vars(){ return new int[]{var0,var1};}
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
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Object val0 = (var0 == qvar) ? firstQval : binding
						.getForced(var0);
				Object val1 = (var1 == qvar) ? firstQval : binding
						.getForced(var1);
				return (val0 != val1);
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}
			@Override
			public int[] vars(){ return new int[]{var0,var1};}			
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
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Object val0 = (var0 == qvar) ? firstQval : binding
						.getForced(var0);
				Object val1 = (var1 == qvar) ? firstQval : binding
						.getForced(var1);
				return val0.equals(val1);
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}
			@Override
			public int[] vars(){ return new int[]{var0,var1};}			
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
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) ((var0 == qvar) ? firstQval : binding
						.getForced(var0));
				Integer val1 = (Integer) ((var1 == qvar) ? firstQval : binding
						.getForced(var1));
				return (val0 > val1);
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}
			@Override
			public int[] vars(){ return new int[]{var0,var1};}			

			/*
			 * public Guard replace(final int var, final Object val){
			 * if(var!=var0 && var!=var1) return this; return new Guard(""){
			 * public boolean check(Binding binding){ Integer val0 = (Integer)
			 * ((var0==var) ? val : binding.getForcedAsInteger(var0)); Integer
			 * val1 = (Integer) ((var1==var) ? val :
			 * binding.getForcedAsInteger(var1)); return (val0 > val1); }
			 * 
			 * @Override public boolean usesQvars() { return false; } }; }
			 */
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
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) ((var0 == qvar) ? firstQval : binding
						.getForced(var0));
				Integer val1 = (Integer) ((var1 == qvar) ? firstQval : binding
						.getForced(var1));
				return (val0 <= val1);
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}
			@Override
			public int[] vars(){ return new int[]{var0,var1};}			
		};
	}

	public static Guard isLessThan(final int var0, final int var1) {
		return new Guard("x_" + var0 + " <= x_" + var1) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				Integer val1 = binding.getForcedAsInteger(var1);
				return (val0 < val1);
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) ((var0 == qvar) ? firstQval : binding
						.getForced(var0));
				Integer val1 = (Integer) ((var1 == qvar) ? firstQval : binding
						.getForced(var1));
				return (val0 < val1);
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}
			@Override
			public int[] vars(){
				return new int[]{var0,var1};
			}
		};
	}

	public static Guard isGreaterThanOrEqualTo(final int var0, final int var1) {
		return new Guard("x_" + var0 + " >= x_" + var1) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				Integer val1 = binding.getForcedAsInteger(var1);
				return (val0 >= val1);
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) ((var0 == qvar) ? firstQval : binding
						.getForced(var0));
				Integer val1 = (Integer) ((var1 == qvar) ? firstQval : binding
						.getForced(var1));
				return (val0 >= val1);
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}
			@Override
			public int[] vars(){
				return new int[]{var0,var1};
			}			
		};
	}

	public static Guard isGreaterThanConstant(final int var0, final int val1) {
		return new Guard("x_" + var0 + " > " + val1) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				return (val0 > val1);
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) ((var0 == qvar) ? firstQval : binding
						.getForced(var0));
				return (val0 > val1);
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0;
			}
			@Override
			public int[] vars(){ return new int[]{var0};}			
		};
	}

	public static Guard setContainsElement(final int varElement,
			final int varSet) {
		return new Guard("set_" + varSet + "_ContainsElement_" + varElement) {

			@Override
			public boolean usesQvars() {
				return varSet < 0 || varElement < 0;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				HashSet<Object> set = (HashSet<Object>) ((varSet == qvar) ? firstQval
						: binding.getForced(varSet));
				Object element = (varElement == qvar) ? firstQval : binding
						.getForced(varElement);
				return set.contains(element);
			}

			@Override
			public boolean check(Binding binding) {
				HashSet<Object> set = (HashSet<Object>) binding
						.getForced(varSet);
				Object element = binding.getForced(varElement);
				return set.contains(element);
			}
			@Override
			public int[] vars(){ return new int[]{varElement,varSet};}			
		};
	}

	public static Guard setNotContainsElement(final int varElement,
			final int varSet) {
		return new Guard("set_" + varSet + "_NotContainsElement_" + varElement) {

			@Override
			public boolean usesQvars() {
				return varSet < 0 || varElement < 0;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				HashSet<Object> set = (HashSet<Object>) ((varSet == qvar) ? firstQval
						: binding.getForced(varSet));
				Object element = (varElement == qvar) ? firstQval : binding
						.getForced(varElement);
				return !set.contains(element);
			}

			@Override
			public boolean check(Binding binding) {
				HashSet<Object> set = (HashSet<Object>) binding
						.getForced(varSet);
				Object element = binding.getForced(varElement);
				return !set.contains(element);
			}
			@Override
			public int[] vars(){ return new int[]{varElement,varSet};}				
		};
	}

	public static Guard setContainsOnlyElement(final int varElement,
			final int varSet) {
		return new Guard("set_" + varSet + "_ContainsOnlyElement_" + varElement) {

			@Override
			public boolean usesQvars() {
				return varSet < 0 || varElement < 0;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				HashSet<Object> set = (HashSet<Object>) ((varSet == qvar) ? firstQval
						: binding.getForced(varSet));
				Object element = (varElement == qvar) ? firstQval : binding
						.getForced(varElement);
				return set.size() == 1 && set.contains(element);
			}

			@Override
			public boolean check(Binding binding) {
				HashSet<Object> set = (HashSet<Object>) binding
						.getForced(varSet);
				Object element = binding.getForced(varElement);
				return set.size() == 1 && set.contains(element);
			}
			@Override
			public int[] vars(){ return new int[]{varElement,varSet};}				
		};
	}

	public static Guard setContainsMoreThanElement(final int varElement,
			final int varSet) {
		return new Guard("set_" + varSet + "_ContainsMoreThanElement_"
				+ varElement) {

			@Override
			public boolean usesQvars() {
				return varSet < 0 || varElement < 0;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				HashSet<Object> set = (HashSet<Object>) ((varSet == qvar) ? firstQval
						: binding.getForced(varSet));
				Object element = (varElement == qvar) ? firstQval : binding
						.getForced(varElement);
				return set.size() > 1 && set.contains(element);
			}

			@Override
			public boolean check(Binding binding) {
				HashSet<Object> set = (HashSet<Object>) binding
						.getForced(varSet);
				Object element = binding.getForced(varElement);
				return set.size() > 1 && set.contains(element);
			}
			@Override
			public int[] vars(){ return new int[]{varElement,varSet};}				
		};
	}
}