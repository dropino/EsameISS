package connQak 
import it.unibo.`is`.interfaces.IObserver
import java.util.Observable
import it.unibo.kactor.ApplMessage
 
enum class ConnectionType {
    TCP, MQTT, COAP, HTTP
}

abstract class connQakBase(val hostIP : String,   val port : String,   val destName : String, val context : String) {
lateinit var currQakConn  : connQakBase
	
	companion object{
	fun create(connType: ConnectionType,   hostIP : String,   port : String,
			   destName : String, context : String) : connQakBase{
		  showSystemInfo()
		  when( connType ){
				 ConnectionType.MQTT -> 
				 	{return connQakMqtt(hostIP, port, destName, context)}  
				 ConnectionType.TCP ->
				 	{return connQakTcp(hostIP, port, destName, context)}  
				 ConnectionType.COAP ->
				 	{return connQakCoap(hostIP, port, destName, context)}  
  				 ConnectionType.HTTP ->  
 				 	{return connQakHttp(hostIP, port, destName, context)} 
//   				 else -> //println("WARNING: protocol unknown")
 		  }		
	}
	fun showSystemInfo(){
		println(
			"connQakBase  | COMPUTER memory="+ Runtime.getRuntime().totalMemory() +
					" num of processors=" +  Runtime.getRuntime().availableProcessors());
		println(
			"connQakBase  | NUM of threads="+ Thread.activeCount() +
					" currentThread=" + Thread.currentThread() );
	}
	}//object

	
	  abstract fun createConnection( )     
      abstract fun forward( msg : ApplMessage )
      abstract fun request( msg : ApplMessage ) : ApplMessage?
      abstract fun emit( msg : ApplMessage )
	
}

 
 
 