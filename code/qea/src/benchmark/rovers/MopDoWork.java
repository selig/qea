package benchmark.rovers;

public class MopDoWork extends DoWork<String>{

	@Override
	public void run_with_spec(String spec, String name, int[] args) {
		// Doesn't do anything as we use aspects
		
	}		
	
	  /*
	   * THE EMPTY METHODS THAT THE ASPECTJ GRABS ONTO!!
	   */
	   public static String last_event = "";
	   
	   public void command(int x){ last_event = "command"; }
	   public void request(Object o){ last_event = "request";}
	   public void grant(Object o){ last_event = "grant";}
	   public void deny(Object o){ last_event = "deny";}
	   public void rescind(Object o){ last_event = "rescind";}
	   public void cancel(Object o){ last_event = "cancel";}
	   public void succeed(Object o){ last_event = "succeed";}
	   public void command(Object o){ last_event = "command";}
	   public void set_ack_timeout(int x){ last_event = "set_ack_timeout";}
	   public void command(Object a,Object b,Object c,int d){ last_event = "command";}
	   public void ack(Object o,int x){ last_event = "ack";}
	   public void grant(Object a, Object b){ last_event = "grant";}
	   public void cancel(Object a, Object b){ last_event = "cancel";}
	   public void schedule(Object a, Object b){ last_event = "schedule";}
	   public void finish(Object o){ last_event = "finish";}
	   public void conflict(Object a, Object b){ last_event = "conflict";}
	   public void ping(Object a, Object b){ last_event = "ping";}
	   public void ack(Object a, Object b){ last_event = "ack";}
	   public void ack(Object a, Object b, int c){ last_event = "ack";}
	   public void send(Object a, Object b, int c){ last_event = " send";}

	
}
