package qea.structure.intf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import qea.util.ArrayUtil;

public abstract class Assignment {

	/**
	 * Applying the assignment
	 * 
	 * Assignments should always create a *new* binding
	 * 
	 * @return updated binding
	 */
	public abstract Binding apply(Binding binding, boolean copy);

	public Binding apply(Binding binding, boolean copy, int qvar, Object firstQval){
		throw new RuntimeException("Quantified variables have not been implemented for this assignement");
	}
	
	/**
	 * Get all variables used in this assignment
	 * 
	 * @return used variables
	 */
	public abstract int[] vars();

	private final String name;
	public String toString(){ return name; }

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
	public static Assignment storeVar(final int var0, final int var1) {
		return new Assignment("store(x_" + var0 + ", x_" + var1 + ")") {
			@Override
			public Binding apply(Binding binding, boolean copy) {
				Object val1 = binding.getForced(var1);
				Binding newBinding = binding;
				if (copy) {
					newBinding = binding.copy();
				}
				newBinding.setValue(var0, val1);
				return newBinding;
			}
			public Binding apply(Binding binding, boolean copy, int qvar, Object firstQval){
				if(var1==qvar){
					Object val1 = firstQval;
					Binding newBinding = binding;
					if(copy){ newBinding = binding.copy(); }
					newBinding.setValue(var0,val1);
					return newBinding;
				}else{
					if(var0==qvar) throw new RuntimeException("Cannot store to quantified variable");
					return apply(binding,copy);
				}
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}
		};
	}
	
	/*
	 * Produce an assignment capturing binding += [var0 -> binding[var1]-binding[var2]]
	 * 
	 * @ returns an assignment for this property
	 */
	public static Assignment storeDifference(final int var0, final int var1, final int var2) {
		return new Assignment("store(x_" + var0 + ", x_" + var1 + "-x_"+var2+")") {
			@Override
			public Binding apply(Binding binding, boolean copy) {
				Integer val1 = (Integer) binding.getForced(var1);
				Integer val2 = (Integer) binding.getForced(var2);
				Integer diff = val1 -val2;			
				Binding newBinding = binding;
				if (copy) {
					newBinding = binding.copy();
				}
				newBinding.setValue(var0, diff);
				return newBinding;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1, var2 };
			}
		};
	}	

	public static Assignment createSetFromElement(final int varSet,
			final int varElement) {
		return new Assignment("createSet_" + varSet + "_FromElement_"
				+ varElement) {

			@Override
			public Binding apply(Binding binding, boolean copy) {
				HashSet<Object> set = new HashSet<Object>();
				set.add(binding.getForced(varElement));
				Binding newBinding = binding;
				if (copy) {
					newBinding = binding.copy();
				}
				newBinding.setValue(varSet, set);
				return newBinding;
			}

			@Override
			public int[] vars() {
				return new int[] { varSet, varElement };
			}
		};
	}

	public static Assignment addElementToSet(final int varSet,
			final int varElement) {
		return new Assignment("addElement_" + varElement + "ToSet_" + varSet) {

			@Override
			public Binding apply(Binding binding, boolean copy) {
				if (binding.getValue(varSet) != null) {
					HashSet<Object> set = (HashSet<Object>) binding.getForced(varSet);
					set.add(binding.getForced(varElement));
					Binding newBinding = binding;
					if (copy) {
						newBinding = binding.copy();
					}
					newBinding.setValue(varSet, set);
					return newBinding;
				}
				HashSet<Object> set = new HashSet<Object>();
				set.add(binding.getForced(varElement));
				Binding newBinding = binding;
				if (copy) {
					newBinding = binding.copy();
				}
				newBinding.setValue(varSet, set);
				return newBinding;
			}

			@Override
			public int[] vars() {
				return new int[] { varSet, varElement };
			}
		};
	}

	public static Assignment addElementToCopiedSet(final int varSet,
			final int varElement) {
		return new Assignment("addElement_" + varElement + "ToSet_" + varSet) {

			@Override
			public Binding apply(Binding binding, boolean copy) {
				if (binding.getValue(varSet) != null) {
					HashSet<Object> set = new HashSet<Object>((HashSet<Object>) binding.getForced(varSet));
					set.add(binding.getForced(varElement));
					Binding newBinding = binding;
					if (copy) {
						newBinding = binding.copy();
					}
					newBinding.setValue(varSet, set);
					return newBinding;
				}
				HashSet<Object> set = new HashSet<Object>();
				set.add(binding.getForced(varElement));
				Binding newBinding = binding;
				if (copy) {
					newBinding = binding.copy();
				}
				newBinding.setValue(varSet, set);
				return newBinding;
			}

			@Override
			public int[] vars() {
				return new int[] { varSet, varElement };
			}
		};
	}	
	
	public static Assignment removeElementFromSet(final int varSet,
			final int varElement) {
		return new Assignment("removeElement_" + varElement + "_FromSet_"
				+ varSet) {

			@Override
			public Binding apply(Binding binding, boolean copy) {
				HashSet<Object> set = (HashSet<Object>) binding
						.getForced(varSet);
				set.remove(binding.getForced(varElement));

				Binding newBinding = binding;
				if (copy) {
					newBinding = binding.copy();
				}
				newBinding.setValue(varSet, set);
				return newBinding;
			}

			@Override
			public int[] vars() {
				return new int[] { varSet, varElement };
			}
		};
	}

	public static Assignment setVal(final int var0, final Object value) {
		return new Assignment("x_" + var0 + "=" + value) {
			@Override
			public Binding apply(Binding binding, boolean copy) {
				Binding newBinding = binding;
				if (copy) {
					newBinding = binding.copy();
				}
				newBinding.setValue(var0, value);
				return newBinding;
			}

			@Override
			public int[] vars() {
				return new int[] { var0 };
			}
		};
	}
	public static Assignment ensure(final int var0, final Object value) {
		return new Assignment("x_" + var0 + "=" + value) {
			@Override
			public Binding apply(Binding binding, boolean copy) {
				Object old_value = binding.getValue(var0);				
				Binding newBinding = binding;
				if(old_value==null){
					if (copy) {
						newBinding = binding.copy();
					}
					newBinding.setValue(var0, value);
				}
				return newBinding;
			}

			@Override
			public int[] vars() {
				return new int[] { var0 };
			}
		};
	}	

	public static Assignment increment(final int var0) {
		return new Assignment("x_" + var0 + "++") {
			@Override
			public Binding apply(Binding binding, boolean copy) {
				Integer val0 = (Integer) binding.getForced(var0);
				Binding newBinding = binding;
				if (copy) {
					newBinding = binding.copy();
				}
				newBinding.setValue(var0, val0 + 1);
				return newBinding;
			}

			@Override
			public int[] vars() {
				return new int[] { var0 };
			}
		};
	}

	public static Assignment incrementOrSet(final int var0) {
		return new Assignment("x_" + var0 + "++") {
			@Override
			public Binding apply(Binding binding, boolean copy) {
				Object val0_obj = binding.getValue(var0);
				if(val0_obj == null) val0_obj = 0;
				Integer val0 = (Integer) val0_obj;
				Binding newBinding = binding;
				if (copy) {
					newBinding = binding.copy();
				}
				newBinding.setValue(var0, val0 + 1);
				return newBinding;
			}

			@Override
			public int[] vars() {
				return new int[] { var0 };
			}
		};
	}	
	
	public static Assignment add(final int var0, final int var1) {
		return new Assignment("x_" + var0 + "+= x_"+var1) {
			@Override
			public Binding apply(Binding binding, boolean copy) {
				Integer val0 = (Integer) binding.getForced(var0);
				Integer val1 = (Integer) binding.getForced(var1);
				
				Binding newBinding = binding;
				if (copy) {
					newBinding = binding.copy();
				}
				newBinding.setValue(var0, val0 + val1);
				return newBinding;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1 };
			}
		};
	}		
	
	public static Assignment addDifference(final int var0, final int var1, final int var2) {
		return new Assignment("x_" + var0 + "+= x_"+var1+" - x_"+var2) {
			@Override
			public Binding apply(Binding binding, boolean copy) {
				Integer val0 = (Integer) binding.getForced(var0);
				Integer val1 = (Integer) binding.getForced(var1);
				Integer val2 = (Integer) binding.getForced(var2);
				
				Binding newBinding = binding;
				if (copy) {
					newBinding = binding.copy();
				}
				newBinding.setValue(var0, val0 + (val1-val2));
				return newBinding;
			}

			@Override
			public int[] vars() {
				return new int[] { var0, var1, var2 };
			}
		};
	}	
	
	public static Assignment decrement(final int var0) {
		return new Assignment("x_" + var0 + "--") {
			@Override
			public Binding apply(Binding binding, boolean copy) {
				Integer val0 = (Integer) binding.getForced(var0);
				Binding newBinding = binding;
				if (copy) {
					newBinding = binding.copy();
				}
				newBinding.setValue(var0, val0 - 1);
				return newBinding;
			}

			@Override
			public int[] vars() {
				return new int[] { var0 };
			}
		};
	}
	
	public Assignment createEmptyList(final int varList){
		return new Assignment("createEmptyList_"+varList){

			@Override
			public Binding apply(Binding binding, boolean copy) {
				ArrayList<Object> list = new ArrayList<Object>();
				Binding newBinding = binding;
				if(copy){
					newBinding = binding.copy();
				}
				newBinding.setValue(varList, list);
				return newBinding;
			}

			@Override
			public int[] vars() {
				return new int[]{varList};
			}
			
		};
	}
	
	public static Assignment createListFromElement(final int varList,
			final int varElement) {
		return new Assignment("createList_" + varList + "_FromElement_"
				+ varElement) {

			@Override
			public Binding apply(Binding binding, boolean copy) {
				ArrayList<Object> list = new ArrayList<Object>();
				list.add(binding.getForced(varElement));
				Binding newBinding = binding;
				if (copy) {
					newBinding = binding.copy();
				}
				newBinding.setValue(varList, list);
				return newBinding;
			}

			@Override
			public int[] vars() {
				return new int[] { varList, varElement };
			}
		};
	}
	public static Assignment storeListIndexVal(final int varList,
			final int indexVal, final int varOutput) {
		return new Assignment("storeList_" + varList + "_atIndexVal_"
				+ indexVal+"_into_"+varOutput) {

			@Override
			public Binding apply(Binding binding, boolean copy) {
				ArrayList<Object> list = (ArrayList<Object>) binding.getForced(varList);
				
				Binding newBinding = binding;
				if (copy) {
					newBinding = binding.copy();
				}
				
				newBinding.setValue(varList, list);
				return newBinding;
			}

			@Override
			public int[] vars() {
				return new int[] { varList, varOutput };
			}
		};
	}	
	
	public static Assignment then(final Assignment one, final Assignment two){
		return new Assignment(one+" then "+two){
			@Override
			public Binding apply(Binding binding, boolean copy){
				return two.apply(one.apply(binding, copy),copy);
			}
			
			@Override
			public int[] vars(){
				return ArrayUtil.concat(one.vars(),two.vars());
			}
		};
	}
		
		public static Assignment list(final Assignment... ass){
			return new Assignment("list of "+Arrays.toString(ass)){
				@Override
				public Binding apply(Binding binding, boolean copy){
					for(Assignment a : ass)
						binding = a.apply(binding,copy);
					return binding;
				}
				
				@Override
				public int[] vars(){
					int[] vs = new int[]{};
					for(Assignment a : ass){
						int[] av = a.vars();
						vs = ArrayUtil.concat(vs,av);
					}
					return vs;					
				}
			};		
		
	}
	
}
