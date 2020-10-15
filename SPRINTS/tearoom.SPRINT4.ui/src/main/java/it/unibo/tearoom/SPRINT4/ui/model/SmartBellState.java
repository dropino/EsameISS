package it.unibo.tearoom.SPRINT4.ui.model;

public class SmartBellState {

	int clientsProcessed;
	int clientsAdmitted;
	int clientsWaiting;
	int clientsWaitedLong;

	public SmartBellState() {

	}

	public SmartBellState(int clientsProcessed, int clientsAdmitted, int clientsWaiting,
			int clientsWaitedLong) {
		this.clientsProcessed = clientsProcessed;
		this.clientsAdmitted = clientsAdmitted;
		this.clientsWaiting = clientsWaiting;
		this.clientsWaitedLong = clientsWaitedLong;
	}

	public int getClientsProcessed() {
		return clientsProcessed;
	}

	public void setClientsProcessed(int clientsProcessed) {
		this.clientsProcessed = clientsProcessed;
	}

	public int getClientsAdmitted() {
		return clientsAdmitted;
	}

	public void setClientsAdmitted(int clientsAdmitted) {
		this.clientsAdmitted = clientsAdmitted;
	}

	public int getClientsWaiting() {
		return clientsWaiting;
	}

	public void setClientsWaiting(int clientsWaiting) {
		this.clientsWaiting = clientsWaiting;
	}

	public int getClientsWaitedLong() {
		return clientsWaitedLong;
	}

	public void setClientsWaitedLong(int clientsWaitedLong) {
		this.clientsWaitedLong = clientsWaitedLong;
	}

}
