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

import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import kotlinx.coroutines.channels.actor
 

class testRobotMove {
	
var waiter             : ActorBasic? = null
var smartbell          : ActorBasic? = null
var waiterwalker       : ActorBasic? = null
val mqttTest   	      = MqttUtils("test") 
val initDelayTime     = 1500L   //

		
	
   
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi    
	@Before
	fun systemSetUp() {
   		kotlin.concurrent.thread(start = true) {
			it.unibo.ctxtearoom.main()
			it.unibo.ctxwalker.main()
		}
	}

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@After
	fun terminate() {
		println("%%%  Test terminate ")
	}
	
	//functions
	fun checkResource(value: String){		
		if( waiter != null ){
//			println(" --- checkResource --- ${waiter!!.geResourceRep()}")
			assertTrue( waiter!!.geResourceRep() == value)
		}  
	}
//	
//	fun checkResourceFalse(value: String){		
//		if( waiter != null ){
//			println(" --- checkResource --- ${waiter!!.geResourceRep()}")
//			assert( waiter!!.geResourceRep() != value)
//		}  
//	}

	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
    fun testWaiter(){
		runBlocking{
		
//			while( waiter == null /*|| client==null || smartbell==null*/){
//					println("testWaiterMap wait for robot ... ")
//					delay(initDelayTime)  //time for robot to start
////					client = it.unibo.kactor.sysUtil.getActor("client")
//					waiter = it.unibo.kactor.sysUtil.getActor("waiter")
////					smartbell = it.unibo.kactor.sysUtil.getActor("smartbell")
//				}
			
			while( waiter == null ){
				delay(1000)
				waiter = it.unibo.kactor.sysUtil.getActor("waiter") 
			}
			
			while( smartbell == null ){
				delay(1000)
				smartbell = it.unibo.kactor.sysUtil.getActor("smartbell") 
			}
			
			
			
			//-----------------TEST RIFIUTO CLIENTE---------------
//			delay(2000)
//			println("---------------------------	MESSAGGIO INVIATO		---------------------------")
//			MsgUtil.sendMsg( "test", "ringBell", "38", smartbell!!   )
//			delay(1000)
//			checkResource("listening")
			
			//-----------------TEST MOVIMENTO WAITER---------------
			delay(2000)
			println("---------------------------	MESSAGGIO INVIATO		---------------------------")
//			MsgUtil.sendMsg (MsgUtil.buildRequest( "test", "waiterwalker", "moveForTask", "moveForTask(barman, 1)") , waiter!!   )
			MsgUtil.sendMsg("request", "moveForTask", "moveForTask(barman, 1)", waiter!!)
			delay(1000)
//			checkResource("listening")

			

		}
	}
}