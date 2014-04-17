package structure.impl.other;

import java.util.Arrays;
import java.util.Comparator;

import structure.intf.Binding;

/**
 * A binding structure to represent bindings of quantified variables. One of the
 * key things is that we use -variableName instead of variableName to index the
 * array
 */
public class QBindingImpl extends FBindingImpl {

	public QBindingImpl(int variablesCount) {
		super(variablesCount);
	}

	/**
	 * Returns the value of the quantified variable with the specified name
	 * 
	 * @param variableName
	 *            Variable name
	 * @return Value of the variable
	 */
	@Override
	public Object getValue(int variableName) {
		return super.getValue(-variableName);
	}

	@Override
	public void setValue(int variableName, Object value) {
		super.setValue(-variableName, value);
	}

	@Override
	public Binding copy() {

		QBindingImpl binding = new QBindingImpl(values.length);
		for (int i = 0; i < values.length; i++) {
			binding.values[i]=values[i];
		}
		return binding;
	}	
	
	private static QBindingImpl empty;


	public boolean consistentWith(QBindingImpl other) {
		for(int i=0;i<values.length;i++){
			Object other_value = other.values[i];
			if(other_value!=null && values[i]!=null){
				if(other_value!=values[i]) return false;
			}
		}
		return true;
	}

	@Override
	public boolean update(int[] variableNames, Object[] args) {
		assert(variableNames.length==args.length);
		
		//System.err.println(Arrays.toString(variableNames)+" with "+Arrays.toString(args));
		
		for(int i=0;i<variableNames.length;i++){
			int var = variableNames[i];
			if(var<0){
				Object value = getValue(var);
				if(value!=null && args[i]!=value) return false;
			}
		}		
		for(int i=0;i<variableNames.length;i++){
			int var = variableNames[i];
			if(var<0){
				setValue(var,args[i]);
			}
		}
		return true;
	}
	
	
	public QBindingImpl updateWith(QBindingImpl b) {
		QBindingImpl new_binding = new QBindingImpl(values.length);
		for(int i=0;i<values.length;i++){
			Object value = b.values[i];
			if(value==null) value = values[i];
			new_binding.values[i]=value;
		}
		return new_binding;
	}

	public boolean isTotal() {
		for(int i=0;i<values.length;i++){
			if(values[i]==null) return false;
		}
		return true;
	}

	public boolean contains(QBindingImpl other){
		for(int i=0;i<values.length;i++){
			Object other_value = other.values[i];
			if(other_value!=null){
				if(values[i]==null) return false;
				if(other_value!=values[i]) return false;
			}
		}
		return true;		
	}
	
	public static class QBindingImplComparator implements Comparator<QBindingImpl> {

		/*
		 * If o1 equals o2 then return 0
		 * if o1 contains o2 then return -1
		 * if o2 contains o1 then return 1
		 * if no ordering then result does not matter, but return -1
		 */
		private static final int no_ordering = -1;
		
		@Override
		public int compare(QBindingImpl o1, QBindingImpl o2) {

			boolean o1_larger = false;
			boolean o2_larger = false;
			
			for(int i=0;i<o1.values.length;i++){
				Object o1_value = o1.values[i];
				Object o2_value = o2.values[i];
				if(o1_value==null && o2_value == null) continue;
				if(o1_value==null){
					if(o1_larger) return no_ordering;
					o2_larger=true;
				}									
				if(o2_value==null){
					if(o2_larger) return no_ordering;
					o1_larger=true;
				}				
				if(o1_value != o2_value) return no_ordering;
			}
			if(o1_larger) return -1;
			if(o2_larger) return 1;
			return 0;
		}
		
	}

	public QBindingImpl[] submaps() {
		// first get boolean map of contained values
		boolean[] contains = new boolean[values.length];
		int contains_count=0;
		for(int i=0;i<contains.length;i++){
			if(values[i]!=null){
				contains[i]=true;
				contains_count++;
			}
		}
		// if we are singleton then there's only one sub
		if(contains_count==1) return new QBindingImpl[]{};
		// if we are double then there's two
		if(contains_count==2){
			QBindingImpl[] ret = new QBindingImpl[2];
			int p = 0;
			for(int i=0;i<contains.length;i++){
				if(contains[i]){
					ret[p] = new QBindingImpl(values.length);
					ret[p].values[i]=this.values[i];		
					p++;		
				}
			}
			
			return ret;
		}
		//do the rest when we need them!!
		throw new RuntimeException("Not implemented yet - only required for non-normal qeas");
	}	
	
}
