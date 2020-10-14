package json

interface ActorJson {
	var jobj = JSONObject()

	fun toJson():String{
		return jobj.toString()
	}
	
	fun reset()

}