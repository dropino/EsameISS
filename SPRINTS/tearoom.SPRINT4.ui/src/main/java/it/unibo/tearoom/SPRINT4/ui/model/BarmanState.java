package it.unibo.tearoom.SPRINT4.ui.model;

public class BarmanState {
	int ordersReceived;
	int teasPreared;
	int teasReady;

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

	public void setOrdersReceived(int ordersReceived) {
		this.ordersReceived = ordersReceived;
	}

	public int getTeasPreared() {
		return teasPreared;
	}

	public void setTeasPreared(int teasPreared) {
		this.teasPreared = teasPreared;
	}

	public int getTeasReady() {
		return teasReady;
	}

	public void setTeasReady(int teasReady) {
		this.teasReady = teasReady;
	}

}
