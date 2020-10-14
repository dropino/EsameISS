package json

import org.json.JSONObject

class waiterJson {
	var jobj = JSONObject()
	
	init {
		reset()
	}
	
	fun setBusy(stat: Boolean){
		jobj.put("busy", stat)
	}
	
	fun setClientID(stat: Int){
		jobj.put("clientID", stat)
	}
	
	fun setTable(stat: Int){
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
	
	fun toJson():String{
		return jobj.toString()
	}
	
	fun reset(){
		jobj.put("busy", false)
		jobj.put("clientID", -1)
		jobj.put("table", -1)
		jobj.put("order", "")
		jobj.put("payment", false)
		jobj.put("waitTime", -1)
		jobj.put("movingTo", "")
		jobj.put("movingFrom", "")
		jobj.put("receivedRequest", "")
	}
}
