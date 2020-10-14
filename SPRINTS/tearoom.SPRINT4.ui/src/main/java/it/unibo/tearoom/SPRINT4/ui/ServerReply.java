package it.unibo.tearoom.SPRINT4.ui;

public class ServerReply {

	private String redir;
	private String payload0;
	private String payload1;
	private String payload2;
	private String payload3;
	private String payload4;

	public ServerReply(String redir) {
		super();
		this.redir = redir;
	}

	public ServerReply(String redir, String payload0) {
		super();
		this.redir = redir;
		this.payload0 = payload0;
	}

	public ServerReply(String redir, String payload0, String payload1) {
		super();
		this.redir = redir;
		this.payload1 = payload1;
		this.payload0 = payload0;
	}

	public ServerReply(String redir, String payload0, String payload1, String payload2) {
		super();
		this.redir = redir;
		this.payload0 = payload0;
		this.payload1 = payload1;
		this.payload2 = payload2;
	}

	public ServerReply(String redir, String payload0, String payload1, String payload2, String payload3) {
		super();
		this.redir = redir;
		this.payload1 = payload1;
		this.payload2 = payload2;
		this.payload3 = payload3;
		this.payload0 = payload0;
	}

	public ServerReply(String redir, String payload0, String payload1, String payload2, String payload3,
			String payload4) {
		super();
		this.redir = redir;
		this.payload1 = payload1;
		this.payload2 = payload2;
		this.payload3 = payload3;
		this.payload4 = payload4;
		this.payload0 = payload0;
	}

	public String getRedir() {
		return redir;
	}

	public void setRedir(String redir) {
		this.redir = redir;
	}

	public String getPayload1() {
		return payload1;
	}

	public void setPayload1(String payload1) {
		this.payload1 = payload1;
	}

	public String getPayload2() {
		return payload2;
	}

	public void setPayload2(String payload2) {
		this.payload2 = payload2;
	}

	public String getPayload3() {
		return payload3;
	}

	public void setPayload3(String payload3) {
		this.payload3 = payload3;
	}

	public String getPayload4() {
		return payload4;
	}

	public void setPayload4(String payload4) {
		this.payload4 = payload4;
	}

	public String getPayload0() {
		return payload0;
	}

	public void setPayload0(String payload0) {
		this.payload0 = payload0;
	}
}
