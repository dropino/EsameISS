package it.unibo.tearoom.SPRINT4.ui.model;

public class BarmanState {
	String ordersReceived;
	String teasPreared;
	String teasReady;

	public BarmanState() {

	}

	public BarmanState(String ordersReceived, String teasPreared, String teasReady) {
		super();
		this.ordersReceived = ordersReceived;
		this.teasPreared = teasPreared;
		this.teasReady = teasReady;
	}

	public String getOrdersReceived() {
		return ordersReceived;
	}

	public void setOrdersReceived(String ordersReceived) {
		this.ordersReceived = ordersReceived;
	}

	public String getTeasPreared() {
		return teasPreared;
	}

	public void setTeasPreared(String teasPreared) {
		this.teasPreared = teasPreared;
	}

	public String getTeasReady() {
		return teasReady;
	}

	public void setTeasReady(String teasReady) {
		this.teasReady = teasReady;
	}

}
