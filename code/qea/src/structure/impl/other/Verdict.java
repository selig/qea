package structure.impl.other;

public enum Verdict {
	SUCCESS, FAILURE, WEAK_SUCCESS, WEAK_FAILURE, UNKNOWN;
	
	public Verdict negated(){
		switch(this){
			case SUCCESS:
				return FAILURE;
			case FAILURE:
				return SUCCESS;
			case WEAK_SUCCESS:
				return WEAK_FAILURE;
			case WEAK_FAILURE:
				return WEAK_SUCCESS;
			case UNKNOWN:
				return UNKNOWN;			
		}
		return UNKNOWN;
	}
	
}
