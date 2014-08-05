package structure.intf;

public interface QEA_single {

	public boolean isQuantificationUniversal();

	public void setGlobalGuard(Guard g);
	public boolean checkGlobalGuard(Object qVarValue);	
	
}
