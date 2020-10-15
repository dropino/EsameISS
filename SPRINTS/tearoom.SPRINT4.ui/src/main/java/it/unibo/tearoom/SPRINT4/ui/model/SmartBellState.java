package it.unibo.tearoom.SPRINT4.ui.model;

public class SmartBellState {

	String clientsProcessed;
	String clientsAdmitted;
	String clientsWaiting;
	String clientsWaitedLong;

	public SmartBellState() {

	}

	public SmartBellState(String clientsProcessed, String clientsAdmitted, String clientsWaiting,
			String clientsWaitedLong) {
		this.clientsProcessed = clientsProcessed;
		this.clientsAdmitted = clientsAdmitted;
		this.clientsWaiting = clientsWaiting;
		this.clientsWaitedLong = clientsWaitedLong;
	}

	public String getClientsProcessed() {
		return clientsProcessed;
	}

	public void setClientsProcessed(String clientsProcessed) {
		this.clientsProcessed = clientsProcessed;
	}

	public String getClientsAdmitted() {
		return clientsAdmitted;
	}

	public void setClientsAdmitted(String clientsAdmitted) {
		this.clientsAdmitted = clientsAdmitted;
	}

	public String getClientsWaiting() {
		return clientsWaiting;
	}

	public void setClientsWaiting(String clientsWaiting) {
		this.clientsWaiting = clientsWaiting;
	}

	public String getClientsWaitedLong() {
		return clientsWaitedLong;
	}

	public void setClientsWaitedLong(String clientsWaitedLong) {
		this.clientsWaitedLong = clientsWaitedLong;
	}

}
