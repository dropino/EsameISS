package connQak

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import org.eclipse.californium.core.CoapResponse

class connQakCoap( hostIP : String,  port : String,  destName : String, context : String ) :
										           connQakBase(hostIP, port, destName, context){

lateinit var client   : CoapClient
	
//		 fun createConnection(  ){
// 			val url = "coap://${configurator.hostAddr}:${configurator.port}/${configurator.ctxqadest}/${configurator.qakdest}"
// 			System.out.println("connQakCoap | url=${url.toString()}")
// 			//uriStr: coap://192.168.1.22:8060/ctxdomains/waiter
//			//client = CoapClient(  )
//		    client.uri = url.toString()
//			client.setTimeout( 1000L )
// 			//initialCmd: to make console more reactive at the first user cmd
// 		    val respGet  = client.get( ) //CoapResponse
//			if( respGet != null )
//				System.out.println("connQakCoap | createConnection doing  get | CODE=  ${respGet.code} content=${respGet.getResponseText()}")
//			else
//				System.out.println("connQakCoap | url=  ${url} FAILURE")
//	}

	override fun createConnection(  ){
 			println("connQakCoap | createConnection hostIP=${hostIP} port=${port}")
			val url = "coap://$hostIP:$port/$context/$destName"
			client = CoapClient( url )
			client.setTimeout( 1000L )
 			//initialCmd: to make console more reactive at the first user cmd
 		    val respGet  = client.get( ) //CoapResponse
			if( respGet != null )
				println("connQakCoap | createConnection doing  get | CODE=  ${respGet.code} content=${respGet.getResponseText()}")
			else
				println("connQakCoap | url=  ${url} FAILURE")
	}
	
	override fun forward( msg: ApplMessage ){		
        val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
        //println("connQakCoap | PUT forward ${d} RESPONSE CODE=  ${respPut.code}")		
	}
	
	override fun request( msg: ApplMessage ) : ApplMessage {
 		val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
  		println("connQakCoap | answer= ${respPut.getResponseText()}")
		return ApplMessage(respPut.getResponseText())
		
	}
	
	override fun emit( msg: ApplMessage){
		val url = "coap://$hostIP:$port/ctx$destName"		//TODO
		client = CoapClient( url )
        //println("PUT emit url=${url} ")		
         val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
        //println("connQakCoap | PUT emit ${msg} RESPONSE CODE=  ${respPut.code}")		
		
	}
	
	fun readRep(   ) : String{
		val respGet : CoapResponse = client.get( )
		return respGet.getResponseText()
	}	
}