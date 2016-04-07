package qea.structure.intf;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import qea.exceptions.ShouldNotHappenException;

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

	protected final String name;
	public String toString(){ return name; }

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

	public static Guard isTrue(final int var0) {
		return new Guard("x_" + var0 + "== true") {
			@Override
			public boolean check(Binding binding) {
				boolean val0 = (Boolean) binding.getForced(var0);
				return val0;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				boolean val0 = (Boolean) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				return val0;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0 };
			}
		};
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
				return val0 == val1;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Object val0 = var0 == qvar ? firstQval : binding
						.getForced(var0);
				Object val1 = var1 == qvar ? firstQval : binding
						.getForced(var1);
				return val0 == val1;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}
		};
	}

	public static Guard varIsEqualToIntVal(final int var, final int val) {
		return new Guard("x_" + var + " == " + val) {
			@Override
			public boolean check(Binding binding) {
				int varVal = binding.getForcedAsInteger(var);
				return varVal == val;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				int varVal = (Integer) (var == qvar ? firstQval : binding
						.getForced(var));
				return varVal == val;
			}

			@Override
			public boolean usesQvars() {
				return var < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var };
			}
		};
	}

	public static Guard isSemEqualToConstant(final int var0, final Object value) {
		return new Guard("x_" + var0 + " equals x_" + value) {
			@Override
			public boolean check(Binding binding) {
				Object val0 = binding.getForced(var0);
				return val0.equals(value);
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Object val0 = var0 == qvar ? firstQval : binding
						.getForced(var0);
				return val0.equals(value);
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0 };
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
				
				return val0 != val1;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Object val0 = var0 == qvar ? firstQval : binding
						.getForced(var0);
				Object val1 = var1 == qvar ? firstQval : binding
						.getForced(var1);
				return val0 != val1;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
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

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Object val0 = var0 == qvar ? firstQval : binding
						.getForced(var0);
				Object val1 = var1 == qvar ? firstQval : binding
						.getForced(var1);
				return val0.equals(val1);
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}
		};
	}

	public static Guard varIsEqualSemToVal(final int var, final Object val) {
		return new Guard("x_" + var + " == " + val) {
			@Override
			public boolean check(Binding binding) {
				Object varVal = binding.getForced(var);
				return varVal.equals(val);
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Object varVal = var == qvar ? firstQval : binding
						.getForced(var);
				return varVal.equals(val);
			}

			@Override
			public boolean usesQvars() {
				return var < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var };
			}
		};
	}

	public static Guard varIsNotEqualSemToVal(final int var, final Object val) {
		return new Guard("x_" + var + " != " + val) {
			@Override
			public boolean check(Binding binding) {
				Object varVal = binding.getForced(var);
				return !varVal.equals(val);
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Object varVal = var == qvar ? firstQval : binding
						.getForced(var);
				return !varVal.equals(val);
			}

			@Override
			public boolean usesQvars() {
				return var < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var };
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
				return val0 > val1;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				Integer val1 = (Integer) (var1 == qvar ? firstQval : binding
						.getForced(var1));
				return val0 > val1;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}

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

	public static Guard varIsLessThanVal(final int var, final int val) {
		return new Guard("x_" + var + " < " + val) {

			@Override
			public int[] vars() {
				return new int[] { var };
			}

			@Override
			public boolean usesQvars() {
				return var < 0;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer varVal = (Integer) (var == qvar ? firstQval : binding
						.getForced(var));
				return varVal < val;
			}

			@Override
			public boolean check(Binding binding) {
				int varVal = binding.getForcedAsInteger(var);
				return varVal < val;
			}
		};
	}

	public static Guard varIsLessThanOrEqualToVal(final int var, final int val) {
		return new Guard("x_" + var + " <= " + val) {

			@Override
			public int[] vars() {
				return new int[] { var };
			}

			@Override
			public boolean usesQvars() {
				return var < 0;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer varVal = (Integer) (var == qvar ? firstQval : binding
						.getForced(var));
				return varVal <= val;
			}

			@Override
			public boolean check(Binding binding) {
				int varVal = binding.getForcedAsInteger(var);
				return varVal <= val;
			}
		};
	}

	public static Guard varIsGreaterThanVal(final int var, final int val) {
		return new Guard("x_" + var + " > " + val) {

			@Override
			public int[] vars() {
				return new int[] { var };
			}

			@Override
			public boolean usesQvars() {
				return var < 0;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer varVal = (Integer) (var == qvar ? firstQval : binding
						.getForced(var));
				return varVal > val;
			}

			@Override
			public boolean check(Binding binding) {
				int varVal = binding.getForcedAsInteger(var);
				return varVal > val;
			}
		};
	}

	public static Guard doubleVarIsGreaterThanOrEqualToVal(final int var,
			final double val) {
		return new Guard("x_" + var + " >= " + val) {

			@Override
			public int[] vars() {
				return new int[] { var };
			}

			@Override
			public boolean usesQvars() {
				return var < 0;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				double varVal = (double) (var == qvar ? firstQval : binding
						.getForced(var));
				return varVal >= val;
			}

			@Override
			public boolean check(Binding binding) {
				double varVal = (double) binding.getForced(var);
				return varVal >= val;
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
				return val0 <= val1;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				Integer val1 = (Integer) (var1 == qvar ? firstQval : binding
						.getForced(var1));
				return val0 <= val1;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}
		};
	}

	public static Guard isLessThan(final int var0, final int var1) {
		return new Guard("x_" + var0 + " <= x_" + var1) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				Integer val1 = binding.getForcedAsInteger(var1);
				return val0 < val1;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				Integer val1 = (Integer) (var1 == qvar ? firstQval : binding
						.getForced(var1));
				return val0 < val1;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}
		};
	}

	public static Guard differenceEqualToVal(final int var0, final int var1, final int val) {
		return new Guard("x_" + var0 + " - x_" + var1+" = " + val) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				Integer val1 = binding.getForcedAsInteger(var1);				
				return (val0 - val1) == val;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				Integer val1 = (Integer) (var1 == qvar ? firstQval : binding
						.getForced(var1));
				return (val0 - val1) == val;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}
		};	
	}	
	public static Guard differenceNotEqualToVal(final int var0, final int var1, final int val) {
		return new Guard("x_" + var0 + " - x_" + var1+" != " + val) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				Integer val1 = binding.getForcedAsInteger(var1);
				return (val0 - val1) != val;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				Integer val1 = (Integer) (var1 == qvar ? firstQval : binding
						.getForced(var1));
				return (val0 - val1) != val;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}
		};	
	}		
	
	
	public static Guard differenceLessThanVal(final int var0, final int var1, final int val) {
		return new Guard("x_" + var0 + " - x_" + var1+" < " + val) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				Integer val1 = binding.getForcedAsInteger(var1);
				return (val0 - val1) < val;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				Integer val1 = (Integer) (var1 == qvar ? firstQval : binding
						.getForced(var1));
				return (val0 - val1) < val;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}
		};	
	}
	
	public static Guard differenceLessThanOrEqualToVal(final int var0, final int var1, final int val) {
		return new Guard("x_" + var0 + " - x_" + var1+" <= " + val) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				Integer val1 = binding.getForcedAsInteger(var1);
				return (val0 - val1) <= val;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				Integer val1 = (Integer) (var1 == qvar ? firstQval : binding
						.getForced(var1));
				return (val0 - val1) <= val;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}
		};	
	}	
	
	public static Guard absoluteDifferenceGreaterThanOrEqualToVal(final int var0, final int var1, final int val) {
		return new Guard("|x_" + var0 + " - x_" + var1+"| >= " + val) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				Integer val1 = binding.getForcedAsInteger(var1);
				int abs = (val0 - val1);
				if(abs<0) abs=-abs;
				return abs >= val;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				Integer val1 = (Integer) (var1 == qvar ? firstQval : binding
						.getForced(var1));
				int abs = (val0 - val1);
				if(abs<0) abs=-abs;
				return abs >= val;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}
		};	
	}	
	
	public static Guard differenceGreaterThanOrEqualToVal(final int var0, final int var1, final int val) {
		return new Guard("x_" + var0 + " - x_" + var1+" < " + val) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				Integer val1 = binding.getForcedAsInteger(var1);
				return (val0 - val1) >= val;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				Integer val1 = (Integer) (var1 == qvar ? firstQval : binding
						.getForced(var1));
				return (val0 - val1) >= val;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}
		};	
	}	
	
	public static Guard differenceGreaterThanVal(final int var0, final int var1, final int val) {
		return new Guard("x_" + var0 + " - x_" + var1+" < " + val) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				Integer val1 = binding.getForcedAsInteger(var1);
				return (val0 - val1) > val;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				Integer val1 = (Integer) (var1 == qvar ? firstQval : binding
						.getForced(var1));
				return (val0 - val1) > val;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}
		};	
	}		
	
	public static Guard sumLessThanOrEqualToVal(final int var0, final int var1, final int val) {
		return new Guard("x_" + var0 + " - x_" + var1+" <= " + val) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				Integer val1 = binding.getForcedAsInteger(var1);
				return (val0 + val1) <= val;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				Integer val1 = (Integer) (var1 == qvar ? firstQval : binding
						.getForced(var1));
				return (val0 + val1) <= val;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}
		};	
	}		
	
	public static Guard sumGreaterThanVal(final int var0, final int var1, final int val) {
		return new Guard("x_" + var0 + " - x_" + var1+" <= " + val) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				Integer val1 = binding.getForcedAsInteger(var1);
				return (val0 + val1) > val;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				Integer val1 = (Integer) (var1 == qvar ? firstQval : binding
						.getForced(var1));
				return (val0 + val1) > val;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}
		};	
	}			
	
	public static Guard isIdentityLessThan(final int var0, final int var1) {
		return new Guard("x_" + var0 + " <= x_" + var1) {
			@Override
			public boolean check(Binding binding) {
				Object val0 = binding.getForced(var0);
				Object val1 = binding.getForced(var1);
				return System.identityHashCode(val0) < System
						.identityHashCode(val1);
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Object val0 = var0 == qvar ? firstQval : binding
						.getForced(var0);
				Object val1 = var1 == qvar ? firstQval : binding
						.getForced(var1);
				return System.identityHashCode(val0) < System
						.identityHashCode(val1);
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}
		};
	}

	public static Guard isGreaterThanOrEqualTo(final int var0, final int var1) {
		return new Guard("x_" + var0 + " >= x_" + var1) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				Integer val1 = binding.getForcedAsInteger(var1);
				return val0 >= val1;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				Integer val1 = (Integer) (var1 == qvar ? firstQval : binding
						.getForced(var1));
				return val0 >= val1;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0 || var1 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}
		};
	}

	public static Guard isGreaterThanConstant(final int var0, final int val1) {
		return new Guard("x_" + var0 + " > " + val1) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				return val0 > val1;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				return val0 > val1;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0 };
			}
		};
	}
	public static Guard isGreaterThanConstant(final int var0, final double val1) {
		return new Guard("x_" + var0 + " > " + val1) {
			@Override
			public boolean check(Binding binding) {
				Double val0 = binding.getForcedAsDouble(var0);
				return val0 > val1;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Double val0 = (Double) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				return val0 > val1;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0 };
			}
		};
	}	
	public static Guard isLessThanConstant(final int var0, final int val1) {
		return new Guard("x_" + var0 + " < " + val1) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				return val0 < val1;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				return val0 < val1;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0 };
			}
		};
	}	
	public static Guard isLessThanConstant(final int var0, final double val1) {
		return new Guard("x_" + var0 + " < " + val1) {
			@Override
			public boolean check(Binding binding) {
				Double val0 = binding.getForcedAsDouble(var0);
				return val0 < val1;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Double val0 = (Double) (var0 == qvar ? firstQval : binding.getForced(var0));
				return val0 < val1;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0 };
			}
		};
	}	
	
	public static Guard isLessThanOrEqualToConstant(final int var0, final int val1) {
		return new Guard("x_" + var0 + " < " + val1) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				return val0 <= val1;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				return val0 <= val1;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0 };
			}
		};
	}		
	
	public static Guard isEqualToConstant(final int var0, final int val1) {
		return new Guard("x_" + var0 + " = " + val1) {
			@Override
			public boolean check(Binding binding) {
				Integer val0 = binding.getForcedAsInteger(var0);
				return val0 == val1;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Integer val0 = (Integer) (var0 == qvar ? firstQval : binding
						.getForced(var0));
				return val0 == val1;
			}

			@Override
			public boolean usesQvars() {
				return var0 < 0;
			}

			@Override
			public int[] vars() {
				return new int[] { var0 };
			}
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
				Object oset = binding.getValue(varSet);
				if (oset == null) {
					return false;
				}
				HashSet<Object> set = (HashSet<Object>) (varSet == qvar ? firstQval
						: oset);
				Object element = varElement == qvar ? firstQval : binding
						.getForced(varElement);
				return set.contains(element);
			}

			@Override
			public boolean check(Binding binding) {
				Object oset = binding.getValue(varSet);
				if (oset == null) {
					return false;
				}
				HashSet<Object> set = (HashSet<Object>) oset;
				Object element = binding.getForced(varElement);
				return set.contains(element);
			}

			@Override
			public int[] vars() {
				return new int[] { varElement, varSet };
			}
		};
	}

	public static Guard setisEmpty(final int varSet) {
		return new Guard("set_" + varSet + "_isEmpty") {

			@Override
			public boolean usesQvars() {
				return varSet < 0;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object firstQval) {
				Object oset = binding.getValue(varSet);
				if (oset == null) {
					return false;
				}
				HashSet<Object> set = (HashSet<Object>) (varSet == qvar ? firstQval
						: oset);
				return set.size() == 0;
			}

			@Override
			public boolean check(Binding binding) {
				Object oset = binding.getValue(varSet);
				if (oset == null) {
					return false;
				}
				HashSet<Object> set = (HashSet<Object>) oset;
				return set.size() == 0;
			}

			@Override
			public int[] vars() {
				return new int[] { varSet };
			}
		};
	}

	
	public static Guard setNotContainsElement(final int varElement,
			final int varSet) {
		return not(setContainsElement(varElement, varSet));
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
				Object oset = binding.getValue(varSet);
				if (oset == null) {
					return false;
				}
				HashSet<Object> set = (HashSet<Object>) (varSet == qvar ? firstQval
						: oset);
				Object element = varElement == qvar ? firstQval : binding
						.getForced(varElement);
				return set.size() == 1 && set.contains(element);
			}

			@Override
			public boolean check(Binding binding) {
				Object oset = binding.getValue(varSet);
				if (oset == null) {
					return false;
				}
				HashSet<Object> set = (HashSet<Object>) oset;
				Object element = binding.getForced(varElement);
				return set.size() == 1 && set.contains(element);
			}

			@Override
			public int[] vars() {
				return new int[] { varElement, varSet };
			}
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
				Object oset = binding.getValue(varSet);
				if (oset == null) {
					return false;
				}
				HashSet<Object> set = (HashSet<Object>) (varSet == qvar ? firstQval
						: oset);
				Object element = varElement == qvar ? firstQval : binding
						.getForced(varElement);
				return set.size() > 1 && set.contains(element);
			}

			@Override
			public boolean check(Binding binding) {
				Object oset = binding.getValue(varSet);
				if (oset == null) {
					return false;
				}
				HashSet<Object> set = (HashSet<Object>) oset;
				Object element = binding.getForced(varElement);
				return set.size() > 1 && set.contains(element);
			}

			@Override
			public int[] vars() {
				return new int[] { varElement, varSet };
			}
		};
	}

	public static Guard and(final Guard g1, final Guard g2) {
		return new Guard(g1 + " and " + g2) {

			@Override
			public boolean check(Binding binding) {
				return g1.check(binding) && g2.check(binding);
			}

			@Override
			public boolean check(Binding binding, int qvar, Object qval) {
				return g1.check(binding, qvar, qval)
						&& g2.check(binding, qvar, qval);
			}

			@Override
			public boolean usesQvars() {
				return g1.usesQvars() || g2.usesQvars();
			}

			@Override
			public int[] vars() {
				int[] g1v = g1.vars();
				int[] g2v = g2.vars();
				int[] both = new int[g1v.length + g2v.length];
				// we should probably filter out duplicates
				// but we don't yet!
				System.arraycopy(g1v, 0, both, 0, g1v.length);
				System.arraycopy(g2v, 0, both, g1v.length, g2v.length);
				return both;
			}
		};
	}
	
	public static Guard or(final Guard g1, final Guard g2) {
		return new Guard(g1 + " or " + g2) {

			@Override
			public boolean check(Binding binding) {
				return g1.check(binding) || g2.check(binding);
			}

			@Override
			public boolean check(Binding binding, int qvar, Object qval) {
				return g1.check(binding, qvar, qval)
						|| g2.check(binding, qvar, qval);
			}

			@Override
			public boolean usesQvars() {
				return g1.usesQvars() || g2.usesQvars();
			}

			@Override
			public int[] vars() {
				int[] g1v = g1.vars();
				int[] g2v = g2.vars();
				int[] both = new int[g1v.length + g2v.length];
				// we should probably filter out duplicates
				// but we don't yet!
				System.arraycopy(g1v, 0, both, 0, g1v.length);
				System.arraycopy(g2v, 0, both, g1v.length, g2v.length);
				return both;
			}
		};
	}
	public static Guard or(final Guard... gs) {
		return new Guard("or "+Arrays.toString(gs)) {

			@Override
			public boolean check(Binding binding) {
				for(int i=0;i<gs.length;i++){
					if(gs[i].check(binding)) return true;
				}
				return false;
			}

			@Override
			public boolean check(Binding binding, int qvar, Object qval) {
				for(int i=0;i<gs.length;i++){
					if(gs[i].check(binding,qvar,qval)) return true;
				}
				return false;
			}

			@Override
			public boolean usesQvars() {
				for(int i=0;i<gs.length;i++){
					if(gs[i].usesQvars()) return true;
				}
				return false;
			}

			@Override
			public int[] vars() {
				Set<Integer> vars = new HashSet<Integer>();
				for(int i=0;i<gs.length;i++){
					for(int j=0;j<gs[i].vars().length;j++){
						vars.add(gs[i].vars()[j]);
					}
				}
				Integer[] ret = new Integer[vars.size()];
				vars.toArray(ret);
				int[] r = new int[ret.length];
				for(int i=0;i<ret.length;i++) r[i]=ret[i];
				return r;
			}
		};
	}	

	public static Guard not(final Guard g1) {
		return new Guard("not " + g1) {

			@Override
			public boolean check(Binding binding) {
				return !g1.check(binding);
			}

			@Override
			public boolean check(Binding binding, int qvar, Object qval) {
				return !g1.check(binding, qvar, qval);
			}

			@Override
			public boolean usesQvars() {
				return g1.usesQvars();
			}

			@Override
			public int[] vars() {
				return g1.vars();
			}
		};
	}

}
