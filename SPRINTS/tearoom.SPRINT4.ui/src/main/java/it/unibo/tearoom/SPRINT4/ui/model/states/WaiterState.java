package it.unibo.tearoom.SPRINT4.ui.model.states;

public class WaiterState {
	int freeTables;
	int deployedToTable;
	int teasDelivered;
	int dirtyTables;
	int deployedToExit;
	int earnings;
	int positionX;
	int positionY;
	
	String sender = "waiter";

	
	static WaiterState waiterState = null;

	public static WaiterState getInstance() {
		if (waiterState == null)
			waiterState = new WaiterState(2, 0, 0, 0, 0, 0, 0, 0);
		
		return waiterState;
	}

	
	private WaiterState() {

	}

	private WaiterState(int freeTables, int deployedToTable, int teasDelivered, int dirtyTables,
			int deployedToExit, int earnings, int positionX, int positionY) {
		this.freeTables = freeTables;
		this.deployedToTable = deployedToTable;
		this.teasDelivered = teasDelivered;
		this.dirtyTables = dirtyTables;
		this.deployedToExit = deployedToExit;
		this.earnings = earnings;
		this.positionX = positionX;
		this.positionY = positionY;
	}

	public int getFreeTables() {
		return freeTables;
	}
	
	public void increaseFreeTables() {
		this.freeTables++;
	}
	
	public void decreaseFreeTables() {
		this.freeTables--;
	}

	public int getDeployedToTable() {
		return deployedToTable;
	}
	
	public void increaseDeployedToTable() {
		this.deployedToTable++;
	}

	public int getTeasDelivered() {
		return teasDelivered;
	}

	public void increaseTeasDelivered() {
		this.teasDelivered++;
	}

	public int getDirtyTables() {
		return dirtyTables;
	}

	public void increaseDirtyTables() {
		this.dirtyTables++;
	}
	
	public void decreaseDirtyTables() {
		this.dirtyTables--;
	}

	public int getDeployedToExit() {
		return deployedToExit;
	}

	public void increaseDeployedToExit() {
		this.deployedToExit++;
	}

	public int getEarnings() {
		return earnings;
	}

	public void increaseEarnings(int payment) {
		this.earnings+=payment;
	}
	
	public int getPositionX() {
		return positionX;
	}
	
	public int getPositionY() {
		return positionY;
	}

	public void setPosition(int x, int y) {
		this.positionX = x;
		this.positionY = y;
	}

}
