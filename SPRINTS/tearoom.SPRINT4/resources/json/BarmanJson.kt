package json

import org.json.JSONObject

class BarmanJson : ActorJson {
	var jobj = JSONObject()
	
	init {
		reset()
	}
	
	fun setBusy(stat: Boolean){
		jobj.put("busy", stat)
	}
	
	fun setPreparingForTable(stat: Int){
		jobj.put("PreparingForTable", stat)
	}
	
	fun setPreparingOrder(stat: String){
		jobj.put("PreparingOrder", stat)
	}
	
	fun setOrderReadyTable(stat: Int){
		jobj.put("OrderReadyTable", stat)
	}

	fun setOrderReady(stat: Boolean){
		jobj.put("OrderReady", stat)
	}
	
	//transforms the jsonObject in a json string string
	override fun toJson():String{
		return jobj.toString()
	}
	
	
	//resets to initial values the json object
	override fun reset(){
		jobj.put("busy", false)
		jobj.put("PreparingForTable", -1)
		jobj.put("PreparingOrder", "")
		jobj.put("ClientDenied", -1)
		jobj.put("OrderReady", false)
	}
}