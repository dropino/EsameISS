package it.unibo.robotWeb2020;

public class RequestMessageOnSock {

	private String name;
	private String payload;
	

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public RequestMessageOnSock() {
	}

	public RequestMessageOnSock(String name, String payload) {
		this.name = name;
		this.payload = payload;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
