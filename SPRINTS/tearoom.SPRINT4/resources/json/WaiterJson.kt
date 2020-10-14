package json

import org.json.JSONObject

class waiterJson : ActorJson {
	//JsonObject is similar to a map
	var jobj = JSONObject()
	
	init {
		reset()
	}
	
	fun setBusy(stat: Boolean){
		jobj.put("busy", stat)
	}
	
	fun setClientID(stat: String){
		jobj.put("clientID", stat)
	}
	
	fun setTable(stat: Int?){
		jobj.put("table", stat)
	}
	
	fun setOrder(stat: String){
		jobj.put("order", stat)
	}
	
	fun setPayment(stat: Boolean){
		jobj.put("payment", stat)
	}
	
	fun setWaitTime(stat: Int){
		jobj.put("waitTime", stat)
	}
	
	fun setMovingTo(stat: String){
		jobj.put("movingTo", stat)
	}
	
	fun setMovingFrom(stat: String){
		jobj.put("movingFrom", stat)
	}
	
	fun setReceivedRequest(stat: String){
		jobj.put("receivedRequest", stat)
	}
	
	
	//transforms the jsonObject in a json string string
	override fun toJson():String{
		return jobj.toString()
	}
	
	//resets to initial values the json object
	override fun reset(){
		jobj.put("busy", false)
		jobj.put("clientID", "")
		jobj.put("table", -1)
		jobj.put("order", "")
		jobj.put("payment", false)
		jobj.put("waitTime", -1)
		jobj.put("movingTo", "")
		jobj.put("movingFrom", "")
		jobj.put("receivedRequest", "")
	}
}
