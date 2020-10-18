package json

import org.json.JSONObject

class WaiterJson : ActorJson {
	//JsonObject is similar to a map
	var jobj = JSONObject()

	init {
		reset()
	}

	fun setBusy(stat: Boolean) {
		jobj.put("busy", stat)
	}

	fun setClientID(stat: String?) {
		jobj.put("clientID", stat)
	}

	fun setTable(stat: Int?) {
		jobj.put("table", stat)
	}

	fun setOrder(stat: String) {
		jobj.put("order", stat)
	}

	fun setPayment(stat: Int) {
		jobj.put("payment", stat)
	}

	fun setWaitTime(stat: Long) {
		jobj.put("waitTime", stat)
	}

	fun setMovingTo(stat: String) {
		jobj.put("movingTo", stat)
	}

	fun setMovingFrom(stat: String) {
		jobj.put("movingFrom", stat)
	}
	
	fun setArrival(stat: String) {
		jobj.put("arrival", stat)
	}

	fun setTableDirty(stat: Boolean) {
		jobj.put("tableDirty", stat)
	}

	fun setReceivedRequest(stat: String) {
		jobj.put("receivedRequest", stat)
	}
	fun setAcceptedWaiting(stat: Boolean){
		jobj.put("acceptedWaiting", stat)
	}

	//transforms the jsonObject in a json string string
	override fun toJson(): String {
		return jobj.toString()
	}

	//resets to initial values the json object
	override fun reset() {
		jobj.put("busy", false)
		jobj.put("clientID", "")
		jobj.put("table", -1)
		jobj.put("order", "")
		jobj.put("payment", false)
		jobj.put("waitTime", -1)
		jobj.put("movingTo", "")
		jobj.put("movingFrom", "")
		jobj.put("receivedRequest", "")
		jobj.put("arrival", "")
		jobj.put("acceptedWaiting", false)
		jobj.put("tableDirty", false)
	}
}
