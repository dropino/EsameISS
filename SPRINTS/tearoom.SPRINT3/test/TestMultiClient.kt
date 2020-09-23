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


class testMultiClient {
	
	var waiter             : ActorBasic? = null
	var smartbell          : ActorBasic? = null
	var barman       	   : ActorBasic? = null
	var waiterwalker       : ActorBasic? = null
	var simclient          : ActorBasic? = null
	
	val mqttTest   	      = MqttUtils("test") 
	val initDelayTime     = 1500L   //

	
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
	
	fun checkResource(value: String) : Boolean {		
		if( waiter != null ){
			assertTrue( waiter!!.geResourceRep() == value)
			return waiter!!.geResourceRep() == value
		} else	return false
	}
	
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun testNewClient() {
		
		//CLIENT 1
		runBlocking {
			
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
			
			while( simclient == null ){
				delay(1000)
				simclient = it.unibo.kactor.sysUtil.getActor("simclient") 
			}
			
			while( waiterwalker == null ){
				delay(1000)
				waiterwalker = it.unibo.kactor.sysUtil.getActor("waiterwalker") 
			}
//			
			
			//Arrivo nuovo cliente
			delay(1000)
			println("---------------------------	MESSAGGIO INVIATO		---------------------------")
			MsgUtil.sendMsg (MsgUtil.buildRequest( "test", "ringBell", "ringBell(39)", "smartbell") , smartbell!!   )
			delay(1000)
			
			if ( checkResource("Accept") == true ) {
				println ("OK")
			} else println ("NO")

		}
		
		
		
		//CLIENT 2
		runBlocking {
			
			//Arrivo nuovo cliente
			delay(10000)
			println("---------------------------	MESSAGGIO INVIATO		---------------------------")
			MsgUtil.sendMsg (MsgUtil.buildRequest( "test", "ringBell", "ringBell(37)", "smartbell") , smartbell!!   )
			delay(1000)
			
			
		}
		
		//CLIENT 3
		runBlocking {
			
			//Arrivo nuovo cliente
			delay(40000)
			println("---------------------------	MESSAGGIO INVIATO		---------------------------")
			MsgUtil.sendMsg (MsgUtil.buildRequest( "test", "ringBell", "ringBell(37)", "smartbell") , smartbell!!   )
			delay(1000)
			if ( checkResource("Accept") == true ) {
				
			}
			
			
		}
		
	}
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	suspend fun	clientTask () {
		
		
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
			
			while( simclient == null ){
				delay(1000)
				simclient = it.unibo.kactor.sysUtil.getActor("simclient") 
			}
			
			while( waiterwalker == null ){
				delay(1000)
				waiterwalker = it.unibo.kactor.sysUtil.getActor("waiterwalker") 
			}
			
			
			//-----------------TEST ORDINAZIONE TEA---------------
			delay(1000)
			println("---------------------------	MESSAGGIO INVIATO		---------------------------")
			MsgUtil.sendMsg ("test", "sendOrder", "sendOrder(tea, 2)",barman!! )
			delay(1000)
			
			
			
			
			
		
		
	}	 
	
	
	
}