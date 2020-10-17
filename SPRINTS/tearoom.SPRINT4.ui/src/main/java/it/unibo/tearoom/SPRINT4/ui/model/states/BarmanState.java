package it.unibo.tearoom.SPRINT4.ui.model.states;

public class BarmanState {
	int ordersReceived;
	int teasPreared;
	int teasReady;
	String sender = "barman";
	private String currentTask;

	static BarmanState barmanState = null;

	public static BarmanState getInstance() {
		if (barmanState == null)
			barmanState = new BarmanState(0, 0, 0, "");

		return barmanState;
	}

	private BarmanState() {

	}

	private BarmanState(int ordersReceived, int teasPreared, int teasReady, String currentTask) {
		super();
		this.ordersReceived = ordersReceived;
		this.teasPreared = teasPreared;
		this.teasReady = teasReady;
		this.currentTask = currentTask;

	}

	public String getSender() {
		return sender;
	}

	public int getOrdersReceived() {
		return ordersReceived;
	}

	public int getTeasPreared() {
		return teasPreared;
	}

	public int getTeasReady() {
		return teasReady;
	}

	public void increaseOrdersReceived() {
		this.ordersReceived++;
	}

	public void decreaseOrdersReceived() {
		this.ordersReceived--;
	}

	public void increaseTeasPreared() {
		this.teasPreared++;
	}

	public void decreaseTeasPreared() {
		this.teasPreared--;
	}

	public void increaseTeasReady() {
		this.teasReady++;
	}

	public void decreaseTeasReady() {
		this.teasReady--;
	}

	public String getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(String currentTask) {
		this.currentTask = currentTask;
	}

}
