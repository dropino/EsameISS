package json

import org.json.JSONObject

class SmartBellJson : ActorJson {
	//JsonObject is similar to a map
	var jobj = JSONObject()

	init {
		reset()
	}

	fun setBusy(stat: Boolean) {
		jobj.put("busy", stat)
	}

	fun setClientArrived(stat: Boolean) {
		jobj.put("ClientArrived", stat)
	}

	fun setClientAccepted(stat: Boolean) {
		jobj.put("ClientAccepted", stat)
	}

	fun setClientDenied(stat: Boolean) {
		jobj.put("ClientDenied", stat)
	}
	
	fun getClientAccepted() : Boolean {
		return jobj.getBoolean("ClientAccepted")
	}

	fun getClientDenied() : Boolean {
		return jobj.getBoolean("ClientDenied")
	}
	
	fun getClientArrived() : Boolean {
		return jobj.getBoolean("ClientArrived")
	}
	
	//transforms the jsonObject in a json string string
	override fun toJson(): String {
		return jobj.toString()
	}

	//resets to initial values the json object
	override fun reset() {
		jobj.put("busy", false)
		jobj.put("ClientArrived", false)
		jobj.put("ClientAccepted", false)
		jobj.put("ClientDenied", true)
	}

}
