package it.unibo.tearoom.SPRINT4.ui.model;

public class ClientRequest {

	private String name;
	private String payload0;
	private String payload1;
	private String clientid;

	public ClientRequest() {
		super();
	}

	public ClientRequest(String name, String clientid) {
		super();
		this.name = name;
		this.clientid = clientid;
	}

	public ClientRequest(String name, String payload0, String clientid) {
		super();
		this.name = name;
		this.payload0 = payload0;
		this.clientid = clientid;
	}

	public ClientRequest(String name, String payload0, String payload1, String clientid) {
		super();
		this.name = name;
		this.payload0 = payload0;
		this.payload1 = payload1;
		this.clientid = clientid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPayload0() {
		return payload0;
	}

	public void setPayload0(String payload0) {
		this.payload0 = payload0;
	}

	public String getPayload1() {
		return payload1;
	}

	public void setPayload1(String payload1) {
		this.payload1 = payload1;
	}

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

}
