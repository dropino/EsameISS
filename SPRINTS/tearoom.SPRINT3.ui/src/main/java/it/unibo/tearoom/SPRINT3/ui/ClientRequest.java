package it.unibo.tearoom.SPRINT3.ui;

public class ClientRequest {

	private String name;
	private String payload;
	
	public ClientRequest(String name, String payload) {
		super();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}
	
	
	
}
