package it.unibo.tearoom.SPRINT4.ui.model;

public class ServerReply {

	private String redir;
	private String clientid;
	private String table;
	private String waitTime;
	private String result;

	public ServerReply() {
		this.redir = "";
		this.table = "";
		this.waitTime = "";
		this.result = "";
		this.clientid = "";
	}

	public String getRedir() {
		return redir;
	}

	public void setRedir(String redir) {
		this.redir = redir;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}
}
