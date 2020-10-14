package json

import org.json.JSONObject

class smartBellJson : ActorJson {
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

	fun setClientAccepted(stat: Int) {
		jobj.put("ClientAccepted", stat)
	}

	fun setClientDenied(stat: Int) {
		jobj.put("ClientDenied", stat)
	}


	//transforms the jsonObject in a json string string
	override fun toJson(): String {
		return jobj.toString()
	}

	//resets to initial values the json object
	override fun reset() {
		jobj.put("busy", false)
		jobj.put("ClientArrived", false)
		jobj.put("ClientAccepted", -1)
		jobj.put("ClientDenied", -1)
	}

}
