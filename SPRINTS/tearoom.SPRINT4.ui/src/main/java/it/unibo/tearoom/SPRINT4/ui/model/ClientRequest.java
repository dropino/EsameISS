package it.unibo.tearoom.SPRINT4.ui.model;

public class ClientRequest {

	private String name;
	private String payload0;
	private String payload1;
	private String payload2;

	public ClientRequest() {
		super();
	}

	public ClientRequest(String name, String payload0) {
		super();
		this.name = name;
		this.payload0 = payload0;
	}

	public ClientRequest(String name, String payload0, String payload1) {
		super();
		this.name = name;
		this.payload0 = payload0;
		this.payload1 = payload1;
	}

	public ClientRequest(String name, String payload0, String payload1, String payload2) {
		super();
		this.name = name;
		this.payload0 = payload0;
		this.payload1 = payload1;
		this.payload2 = payload2;
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

	public String getPayload2() {
		return payload2;
	}

	public void setPayload2(String payload2) {
		this.payload2 = payload2;
	}

}
