package test

import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.MqttUtils
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import kotlinx.coroutines.channels.actor
import connQak.connQakCoap
import it.unibo.kactor.ApplMessage
import connQak.utils.ApplMessageUtils;
import kotlinx.coroutines.CoroutineScope
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class testTearoomSysMsg {
	
var waiterConn             : connQakCoap? = null
var smartbellConn          : connQakCoap? = null		

var setUpDone 			   : Boolean = false	
  
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi    
@Before
fun systemSetUp()  = runBlocking{
	
//	if (setUpDone)
//	{
//		return
//	}
//	
//	setUpDone = true
	


	kotlin.concurrent.thread(start = true) {
		it.unibo.ctxtearoom.main()
	}
	//avviare connessione
	//delay per essere sicuri che si avvii ctxtearoom
	
	delay(5000)
	
	if (waiterConn == null) {
		waiterConn = connQakCoap("localhost", "8072", "waiter", "ctxtearoom"  )  
		waiterConn!!.createConnection()
	}
	
	if (smartbellConn == null) {
		smartbellConn = connQakCoap("localhost", "8072", "smartbell", "ctxtearoom"  )  
		smartbellConn!!.createConnection()
	
	
}

} 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
@After
fun terminate() {
	println("%%%  Test terminate ")
}

	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
@Test
fun test_smartbellRingbell(){
	var CID 	= "0"
	var Status 	= "0"
	var V 		= "0"
	var ringMsg	: ApplMessage? = null
	var reply	: ApplMessage? = null
	var ringRepArgs = arrayOf<String>()
	
	
//-----------------TEST RIFIUTO CLIENTE---------------
	ringMsg = MsgUtil.buildRequest( "web", "ringBell", "ringBell(38)", "smartbell")
	reply = smartbellConn!!.request(ringMsg)
	ringRepArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply)		
	Status = ringRepArgs[0].toString()
	CID = ringRepArgs[1].toString()
	
	assertTrue(ringRepArgs.size == 2)
	assertTrue(Status.matches("-?\\d+(\\.\\d+)?".toRegex()))
	assertTrue(CID.matches("-?\\d+(\\.\\d+)?".toRegex()))
	assertTrue(Status.toInt() == 0)
			
	
	
//-----------------TEST ACCETTO CLIENTE---------------
	ringMsg = MsgUtil.buildRequest( "web", "ringBell", "ringBell(36)", "smartbell")
	reply = smartbellConn!!.request(ringMsg)
	ringRepArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply)		
	Status = ringRepArgs[0].toString()
	CID = ringRepArgs[1].toString()
	
	assertTrue(ringRepArgs.size == 2)
	assertTrue(Status.matches("-?\\d+(\\.\\d+)?".toRegex()))
	assertTrue(CID.matches("-?\\d+(\\.\\d+)?".toRegex()))
	assertTrue(Status.toInt() == 1)
	
	
//-----------------TEST WAITER DEPLOY ---------------	
	ringMsg = MsgUtil.buildRequest( "web", "deploy", "deploy(entrancedoor, table, 10)", "waiter")
	reply = waiterConn!!.request(ringMsg)
	ringRepArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply)		
	V = ringRepArgs[0].toString()
	
	assertTrue(ringRepArgs.size == 1)
//	assertTrue(V.matches("[a-z]".toRegex()))
	assertTrue(V.matches("-?\\d+(\\.\\d+)?".toRegex()))
	
	
//-----------------TEST CLIENT ORDER ---------------		
	ringMsg = MsgUtil.buildRequest( "web", "clientRequest", "clientRequest(order, $V, 10)", "waiter")
	reply = waiterConn!!.request(ringMsg)
	ringRepArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply)		
	V = ringRepArgs[0].toString()

	assertTrue(ringRepArgs.size == 1)
//	assertTrue(V.matches("[a-z]".toRegex()))
	assertTrue(V.toString() == "ok")
	
	
	ringMsg = MsgUtil.buildDispatch("web", "order", "order(tea)", "waiter")
	waiterConn!!.forward(ringMsg)
	
	
	
	
	ringMsg = MsgUtil.buildRequest( "web", "clientRequest", "clientRequest(pay, 1, 10)", "waiter")
	reply = waiterConn!!.request(ringMsg)
	ringRepArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply)		
	V = ringRepArgs[0].toString()

	assertTrue(ringRepArgs.size == 1)
	assertTrue(V.matches("-?\\d+(\\.\\d+)?".toRegex()))
		
	
	ringMsg = MsgUtil.buildDispatch("web", "pay", "pay($V)", "waiter")
	waiterConn!!.forward(ringMsg)
	
	
	
	//-----------------TEST WAITER DEPLOY ---------------	
	ringMsg = MsgUtil.buildRequest( "web", "deploy", "deploy(1, exitdoor, 10)", "waiter")
	reply = waiterConn!!.request(ringMsg)
	ringRepArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply)		
	V = ringRepArgs[0].toString()
	
	assertTrue(ringRepArgs.size == 1)
//	assertTrue(V.matches("[a-z]".toRegex()))
	assertTrue(V.matches("-?\\d+(\\.\\d+)?".toRegex()))
}
	
	
}