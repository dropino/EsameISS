package json

import org.json.JSONObject

class smartBellJson {
	var jobj = JSONObject()
	
	init {
		reset()
	}
	
	fun setBusy(stat: Boolean){
		jobj.put("busy", stat)
	}
	
	fun setClientArrived(stat: Boolean){
		jobj.put("ClientArrived", stat)
	}
	
	fun setClientAccepted(stat: Int){
		jobj.put("ClientAccepted", stat)
	}
	
	fun setClientDenied(stat: Int){
		jobj.put("ClientDenied", stat)
	}
	
	fun toJson():String{
		return jobj.toString()
	}
	
	fun reset(){
		jobj.put("busy", false)
		jobj.put("ClientArrived", false)
		jobj.put("ClientAccepted", -1)
		jobj.put("ClientDenied", -1)
	}
	
}
