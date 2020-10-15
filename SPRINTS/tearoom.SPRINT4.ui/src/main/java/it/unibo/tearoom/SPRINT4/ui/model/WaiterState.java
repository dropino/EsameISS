package it.unibo.tearoom.SPRINT4.ui.model;

public class WaiterState {
	String freeTables;
	String deployedToTable;
	String teasDelivered;
	String dirtyTables;
	String deployedToExit;
	String earnings;

	public WaiterState() {

	}

	public WaiterState(String freeTables, String deployedToTable, String teasDelivered, String dirtyTables,
			String deployedToExit, String earnings) {
		this.freeTables = freeTables;
		this.deployedToTable = deployedToTable;
		this.teasDelivered = teasDelivered;
		this.dirtyTables = dirtyTables;
		this.deployedToExit = deployedToExit;
		this.earnings = earnings;
	}

	public String getFreeTables() {
		return freeTables;
	}

	public void setFreeTables(String freeTables) {
		this.freeTables = freeTables;
	}

	public String getDeployedToTable() {
		return deployedToTable;
	}

	public void setDeployedToTable(String deployedToTable) {
		this.deployedToTable = deployedToTable;
	}

	public String getTeasDelivered() {
		return teasDelivered;
	}

	public void setTeasDelivered(String teasDelivered) {
		this.teasDelivered = teasDelivered;
	}

	public String getDirtyTables() {
		return dirtyTables;
	}

	public void setDirtyTables(String dirtyTables) {
		this.dirtyTables = dirtyTables;
	}

	public String getDeployedToExit() {
		return deployedToExit;
	}

	public void setDeployedToExit(String deployedToExit) {
		this.deployedToExit = deployedToExit;
	}

	public String getEarnings() {
		return earnings;
	}

	public void setEarnings(String earnings) {
		this.earnings = earnings;
	}

}
