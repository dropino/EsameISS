package it.unibo.tearoom.SPRINT4.ui.model;

public class WaiterState {
	int freeTables;
	int deployedToTable;
	int teasDelivered;
	int dirtyTables;
	int deployedToExit;
	int earnings;

	public WaiterState() {

	}

	public WaiterState(int freeTables, int deployedToTable, int teasDelivered, int dirtyTables,
			int deployedToExit, int earnings) {
		this.freeTables = freeTables;
		this.deployedToTable = deployedToTable;
		this.teasDelivered = teasDelivered;
		this.dirtyTables = dirtyTables;
		this.deployedToExit = deployedToExit;
		this.earnings = earnings;
	}

	public int getFreeTables() {
		return freeTables;
	}

	public void setFreeTables(int freeTables) {
		this.freeTables = freeTables;
	}

	public int getDeployedToTable() {
		return deployedToTable;
	}

	public void setDeployedToTable(int deployedToTable) {
		this.deployedToTable = deployedToTable;
	}

	public int getTeasDelivered() {
		return teasDelivered;
	}

	public void setTeasDelivered(int teasDelivered) {
		this.teasDelivered = teasDelivered;
	}

	public int getDirtyTables() {
		return dirtyTables;
	}

	public void setDirtyTables(int dirtyTables) {
		this.dirtyTables = dirtyTables;
	}

	public int getDeployedToExit() {
		return deployedToExit;
	}

	public void setDeployedToExit(int deployedToExit) {
		this.deployedToExit = deployedToExit;
	}

	public int getEarnings() {
		return earnings;
	}

	public void setEarnings(int earnings) {
		this.earnings = earnings;
	}

}
