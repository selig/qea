package qea.transactionsystem;

public class UserSession {
	protected Integer sid;
	protected String log;
	protected Integer owner;

	public UserSession(Integer uid, Integer sid) {
		this.sid = sid;
		owner = uid;
		log = "";
	}

	public Integer getId() {
		return sid;
	}

	public Integer getOwner() {
		return owner;
	}

	public String getLog() {
		return log;
	}

	public void openSession() {
	}

	public void log(String l) {
		log += l + "\n";
	}

	public void closeSession() {
	}

}
