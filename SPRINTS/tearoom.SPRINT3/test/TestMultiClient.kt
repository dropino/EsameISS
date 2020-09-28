package test

import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
//import mapRoomKotlin.mapUtil
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.MqttUtils
import kotlinx.coroutines.CoroutineScope
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import kotlinx.coroutines.channels.actor



class testMultiClient {
	
	var waiter             : ActorBasic? = null
	var smartbell          : ActorBasic? = null
	var barman       	   : ActorBasic? = null
	var waiterwalker       : ActorBasic? = null
	var simclient          : ActorBasic? = null
	
	val mqttTest   	      = MqttUtils("test") 
	val initDelayTime     = 1500L   //
	var table 			  = 1
	var i				  = 0
	var IDC				  = 0
	var j				  = 0

	var counterAtomic = java.util.concurrent.atomic.AtomicInteger()
	
	
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi    
	@Before
	fun systemSetUp() {
   		kotlin.concurrent.thread(start = true) {
			it.unibo.ctxtearoom.main()
			it.unibo.ctxclient.main()
		}
	}

	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@After
	fun terminate() {
		println("%%%  Test terminate ")
	}
	
	
	fun checkResource(value: ActorBasic) : String{
		if (value != null) {
			return value!!.geResourceRep()
		} else
			return "null"
	}
	
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun main() = runBlocking{
		GlobalScope.massiveRun{counterAtomic.incrementAndGet()}
	}
	
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
    suspend fun  CoroutineScope.massiveRun( action: suspend () -> Unit ){
		
			while( waiter == null ){
				delay(1000)
				waiter = it.unibo.kactor.sysUtil.getActor("waiter") 
			}
			
			while( smartbell == null ){
				delay(1000)
				smartbell = it.unibo.kactor.sysUtil.getActor("smartbell") 
			}
			
			while( barman == null ){
				delay(1000)
				barman = it.unibo.kactor.sysUtil.getActor("barman") 
			}
				
			
		val jobs = List(4){
			kotlin.concurrent.thread(start = true) { 
  				j++
				when (j) {
				1-> client1()
				2-> client2()
				3-> client3()
				4-> client4()
			 else-> {println("=====ERROR======")}
  				}	
			}
		}			
		jobs.forEach{ it.join()  }  //wait for termination of all threads
			

		
		

		
		

}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun client1() {
	//-----------------TEST CLIENT1---------------
	runBlocking{
		i=0
		delay(1000)
		println("===========================	TEST CLIENT1		===========================")
		MsgUtil.sendMsg (MsgUtil.buildRequest( "test", "ringBell", "ringBell(36)", "smartbell"), smartbell!!   )
		delay(1000)
		while (checkResource(smartbell!!) != "Accept" && i<30000) {
			i++
		}
		testClient(1, 10)
		println(checkResource(smartbell!!))
		assertTrue(i<30000)
	}		
}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun client2() {
	//-----------------TEST CLIENT2---------------
		runBlocking{
			i = 0	
			delay (10000)
			println("===========================	TEST CLIENT2		===========================")
			MsgUtil.sendMsg (MsgUtil.buildRequest( "test", "ringBell", "ringBell(39)", "smartbell"), smartbell!!   )
			delay(1000)
			while (checkResource(smartbell!!) != "Discard" && i<30000) {
				i++
			}
			println(checkResource(smartbell!!))
//			assertTrue(i<30000)
		}	
	
}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun client3() {
			
		//-----------------TEST CLIENT3---------------
	runBlocking{
		i=0
		delay (15000)
		println("===========================	TEST CLIENT3		===========================")
		MsgUtil.sendMsg (MsgUtil.buildRequest( "test", "ringBell", "ringBell(36)", "smartbell"), smartbell!!   )
		delay(1000)
		while (checkResource(smartbell!!) != "Accept" && i<30000) {
			i++
		}
		testClient(2, 20)
		println(checkResource(smartbell!!))
//		assertTrue(i<30000)
	}		
	
}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun client4() {
		//-----------------TEST CLIENT4---------------
	runBlocking{
		i=0
		delay (25000)
		println("===========================	TEST CLIENT4		===========================")
		MsgUtil.sendMsg (MsgUtil.buildRequest( "test", "ringBell", "ringBell(36)", "smartbell"), smartbell!!   )
		delay(1000)
		while (checkResource(waiter!!) != "Client_must_wait" && i<30000) {
			i++
		}
		println(checkResource(waiter!!))
//		assertTrue(i<30000)
	}
			
}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun testClient(table: Int, IDC: Int){
		
			//-----------------TEST DEPLOYMENT---------------
			delay(1000)
			println("---------------------------	TESTDEPLOYMENT		---------------------------")
			MsgUtil.sendMsg (MsgUtil.buildRequest( "test", "deploy", "deploy(entrancedoor, table, $IDC)", "waiter"), waiter!!   )
			delay(100)
		
		
			//-----------------TEST CLIENTREQUEST---------------
			delay(9000)
			println("---------------------------	TESTCLIENTREQUEST		---------------------------")
			MsgUtil.sendMsg (MsgUtil.buildRequest( "test", "clientRequest", "clientRequest(order, $table, $IDC)", "waiter"), waiter!!   )
			delay(100)
		
			//-----------------TEST ORDER---------------
			delay(9000)
			println("---------------------------	TESTORDER		---------------------------")
			MsgUtil.sendMsg ("test", "order", "order(tea)", waiter!! )
			delay(100)
			
			//-----------------TEST CLIENTREQUEST---------------
			delay(9000)
			println("---------------------------	TESTCLIENTREQUEST		---------------------------")
			MsgUtil.sendMsg (MsgUtil.buildRequest( "test", "clientRequest", "clientRequest(pay, $table, $IDC)", "waiter"), waiter!!   )
			delay(100)
			
			//-----------------TEST PAY---------------
			delay(9000)
			println("---------------------------	TESTPAY		---------------------------")
			MsgUtil.sendMsg ("test", "pay", "pay(3)", waiter!! )
			delay(100)
	
			//-----------------TEST DEPLOYMENT---------------
			delay(9000)
			println("---------------------------	TESTDEPLOYMENT		---------------------------")
			MsgUtil.sendMsg (MsgUtil.buildRequest( "test", "deploy", "deploy(teatable, exitdoor, $IDC)", "waiter"), waiter!!   )
			delay(100)
			

	}
}
	
	