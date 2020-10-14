package json

import org.json.JSONObject

class waiterWalkerJson {
	var jobj = JSONObject()
	
	init {
		reset()
	}
	
	fun setPosition(x: Int, y: Int){
		jobj.put("positionX", x)
		jobj.put("positionY", y)
	}
	
	fun toJson():String{
		return jobj.toString()
	}
	
	fun reset(){
		setPosition(0,0)
	}
}
