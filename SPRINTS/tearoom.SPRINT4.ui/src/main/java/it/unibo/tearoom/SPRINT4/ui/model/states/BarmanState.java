package it.unibo.tearoom.SPRINT4.ui.model.states;

public class BarmanState {
	int ordersReceived;
	int teasPreared;
	int teasReady;
	String sender = "barman";

	static BarmanState barmanState = null;

	public static BarmanState getInstance() {
		if (barmanState == null)
			barmanState = new BarmanState(0, 0, 0);
		
		return barmanState;
	}
	
	private BarmanState() {

	}

	private BarmanState(int ordersReceived, int teasPreared, int teasReady) {
		super();
		this.ordersReceived = ordersReceived;
		this.teasPreared = teasPreared;
		this.teasReady = teasReady;
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

}
