package it.unibo.tearoom.SPRINT4.ui.model;

public class ClientRequest {

	private String name;
	private String id;
	private String table;
	private String payment;
	private String order;
	private String clientid;
	private String deployFrom;
	private String deployTo;

	public ClientRequest() {
		super();
		name = "";
		table = "";
		payment = "";
		clientid = "";
		order = "";
		deployFrom = "";
		deployTo = "";
		id = "";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getDeployFrom() {
		return deployFrom;
	}

	public void setDeployFrom(String deployFrom) {
		this.deployFrom = deployFrom;
	}

	public String getDeployTo() {
		return deployTo;
	}

	public void setDeployTo(String deployTo) {
		this.deployTo = deployTo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

}
