package json

import org.json.JSONObject

class WalkerJson : ActorJson {
	//JsonObject is similar to a map
	var jobj = JSONObject()

	init {
		reset()
	}

	fun setPosition(x: Int, y: Int) {
		jobj.put("positionX", x)
		jobj.put("positionY", y)
	}
	
	fun printPosition() {
		println("x="+jobj.getInt("positionX")+" y="+jobj.getInt("positionY"))
	}

	//transforms the jsonObject in a json string string
	override fun toJson(): String {
		return jobj.toString()
	}

	//resets to initial values the json object
	override fun reset() {
		setPosition(0, 0)
	}
}
