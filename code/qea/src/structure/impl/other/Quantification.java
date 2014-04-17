package structure.impl.other;

public enum Quantification {
	FORALL, EXISTS, NOT_FORALL, NOT_EXISTS, NONE;

	/*
	 * TODO: IMPORTANT - how do the negated quantifications
	 * 					 effect normality
	 */
	public boolean isUniversal() {
		switch(this){
		case FORALL : return true;
		case NOT_FORALL : return true;
		}
		return false;
	}
}
