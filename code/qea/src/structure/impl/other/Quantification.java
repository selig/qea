package structure.impl.other;

public enum Quantification {
	FORALL, EXISTS, NONE;

	/*
	 * TODO: IMPORTANT - how do the negated quantifications effect normality
	 */
	public boolean isUniversal() {

		switch (this) {
		case FORALL:
			return true;
		default:
			return false;
		}
	}
}
