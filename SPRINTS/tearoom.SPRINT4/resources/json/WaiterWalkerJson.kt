package json

import org.json.JSONObject

class waiterWalkerJson : ActorJson {
	var jobj = JSONObject()
	
	init {
		reset()
	}
	
	fun setPosition(x: Int, y: Int){
		jobj.put("positionX", x)
		jobj.put("positionY", y)
	}
	
	
	//transforms the jsonObject in a json string string
	override fun toJson():String{
		return jobj.toString()
	}
	
	//resets to initial values the json object
	override fun reset(){
		setPosition(0,0)
	}
}
