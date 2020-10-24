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
import connQak.connQakCoap
import it.unibo.kactor.ApplMessage
import java.util.LinkedList
import org.json.JSONObject


class testRobotMove {

	var waiterWalkerConn: connQakCoap? = null
	var walkerConn : connQakCoap? = null


	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() = runBlocking {
		kotlin.concurrent.thread(start = true) {
			it.unibo.ctxtearoom.main()
			it.unibo.ctxsmartbell.main()
			it.unibo.ctxbarman.main()
		}

		//avviare connessione
		//delay per essere sicuri che si avvii ctxtearoom
		delay(5000)

		waiterWalkerConn = connQakCoap("localhost", "8072", "waiterwalker", "ctxtearoom")
		waiterWalkerConn!!.createConnection()

		walkerConn = connQakCoap("localhost", "8050", "walker", "ctxwalker")
		walkerConn!!.createConnection()
		
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
	fun checkRobotPosition() {
		var x : Int = 0
		var y : Int = 0

		runBlocking {
			var ringMsg: ApplMessage? = null
			
			ringMsg = MsgUtil.buildRequest( "test", "moveForTask", "moveForTask(entrancedoor, 1)", "waiterWalker")
			waiterWalkerConn!!.request(ringMsg)
			
			var jobj1 = JSONObject(walkerConn!!.readRep())
			assertTrue(jobj1.get("positionX") == 0)
			assertTrue(jobj1.get("positionY") == 4)
			
			delay (2000)
			
			ringMsg = MsgUtil.buildRequest( "test", "moveForTask", "moveForTask(barman, 1)", "waiterWalker")
			waiterWalkerConn!!.request(ringMsg)
			
			var jobj2 = JSONObject(walkerConn!!.readRep())
			assertTrue(jobj2.get("positionX") == 4)
			assertTrue(jobj2.get("positionY") == 0)
	
			delay (2000)
			
			ringMsg = MsgUtil.buildRequest( "test", "moveForTask", "moveForTask(teatable, 2)", "waiterWalker")
			waiterWalkerConn!!.request(ringMsg)
			
			var jobj3 = JSONObject(walkerConn!!.readRep())
			assertTrue(jobj3.get("positionX") == 4)
			assertTrue(jobj3.get("positionY") == 2)
			
			delay (2000)
			
			ringMsg = MsgUtil.buildRequest( "test", "moveForTask", "moveForTask(exitdoor, 1)", "waiterWalker")
			waiterWalkerConn!!.request(ringMsg)
			
			var jobj4 = JSONObject(walkerConn!!.readRep())
			assertTrue(jobj4.get("positionX") == 5)
			assertTrue(jobj4.get("positionY") == 4)
			
			delay (2000)
			
			ringMsg = MsgUtil.buildRequest( "test", "moveForTask", "moveForTask(home, 1)", "waiterWalker")
			waiterWalkerConn!!.request(ringMsg)
			
			var jobj5 = JSONObject(walkerConn!!.readRep())
			assertTrue(jobj5.get("positionX") == 0)
			assertTrue(jobj5.get("positionY") == 0)
			
			
		}

		

	}
}




