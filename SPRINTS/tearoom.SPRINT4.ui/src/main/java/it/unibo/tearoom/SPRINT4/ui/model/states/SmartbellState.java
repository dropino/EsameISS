package it.unibo.tearoom.SPRINT4.ui.model.states;

public class SmartbellState {

	int clientsProcessed;
	int clientsAdmitted;
	int clientsWaiting;
	int clientsWaitedLong;

	String sender = "smartbell";
	private String currentTask;

	static SmartbellState smartBellState = null;

	public static SmartbellState getInstance() {
		if (smartBellState == null)
			smartBellState = new SmartbellState(0, 0, 0, 0, "");

		return smartBellState;
	}

	private SmartbellState() {

	}

	private SmartbellState(int clientsProcessed, int clientsAdmitted, int clientsWaiting, int clientsWaitedLong,
			String currentTask) {
		this.clientsProcessed = clientsProcessed;
		this.clientsAdmitted = clientsAdmitted;
		this.clientsWaiting = clientsWaiting;
		this.clientsWaitedLong = clientsWaitedLong;
		this.currentTask = currentTask;

	}

	public String getSender() {
		return sender;
	}

	public int getClientsProcessed() {
		return clientsProcessed;
	}

	public void increaseClientsProcessed() {
		this.clientsProcessed++;
	}

	public int getClientsAdmitted() {
		return clientsAdmitted;
	}

	public void increaseClientsAdmitted() {
		this.clientsAdmitted++;
	}

	public int getClientsWaiting() {
		return clientsWaiting;
	}

	public void increaseClientsWaiting() {
		clientsWaiting++;

	}

	public void decreaseClientsWaiting() {
		this.clientsWaiting--;
	}

	public int getClientsWaitedLong() {
		return clientsWaitedLong;
	}

	public void increaseClientsWaitedLong() {
		this.clientsWaitedLong++;
	}

	public String getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(String currentTask) {
		this.currentTask = currentTask;
	}

}
