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

	var waiter: ActorBasic? = null
	var smartbell: ActorBasic? = null
	var waiterwalker: ActorBasic? = null
	val mqttTest = MqttUtils("test")
	val initDelayTime = 1500L   //


	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() {
		kotlin.concurrent.thread(start = true) {
			it.unibo.ctxtearoom.main()
		}
	}

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@After
	fun terminate() {
		println("%%%  Test terminate ")
	}

	//functions
	fun checkResource(value: String) {
		if (waiter != null) {
//			println(" --- checkResource --- ${waiter!!.geResourceRep()}")
			assertTrue(waiter!!.geResourceRep() == value)
		}
	}

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun testWaiter() {
		runBlocking {

			while (waiterwalker == null) {
				delay(1000)
				waiterwalker = it.unibo.kactor.sysUtil.getActor("waiterwalker")
			}


			//-----------------TEST WAITER GO TO BARMAN---------------
			delay(2000)
			println("---------------------------	MESSAGGIO INVIATO		---------------------------")
			MsgUtil.sendMsg(MsgUtil.buildRequest("test", "moveForTask", "moveForTask(barman, 1)", "waiterwalker"), waiterwalker!!)
			delay(6000)
			checkResource("listening")

			//-----------------TEST WAITER GO TO TABLE2---------------
			delay(2000)
			println("---------------------------	MESSAGGIO INVIATO		---------------------------")
			MsgUtil.sendMsg(MsgUtil.buildRequest("test", "moveForTask", "moveForTask(teatable, 2)", "waiterwalker"), waiterwalker!!)
			delay(4000)
			checkResource("listening")

			//-----------------TEST WAITER GO TO EXITDOOR---------------
			delay(2000)
			println("---------------------------	MESSAGGIO INVIATO		---------------------------")
			MsgUtil.sendMsg(MsgUtil.buildRequest("test", "moveForTask", "moveForTask(exitdoor, 1)", "waiterwalker"), waiterwalker!!)
			delay(6000)
			checkResource("listening")

		}
	}
}