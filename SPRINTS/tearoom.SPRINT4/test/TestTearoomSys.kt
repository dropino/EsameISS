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
import org.json.JSONObject


class testTearoomSys {

	var waiter: ActorBasic? = null
	var smartbell: ActorBasic? = null
	var barman: ActorBasic? = null
	val mqttTest = MqttUtils("test")
	val initDelayTime = 1500L   //
	var bJson = json.BarmanJson()
	val sJson = json.SmartBellJson()
	
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


	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun testWaiter() {
		runBlocking {
			while (waiter == null) {
				delay(1000)
				waiter = it.unibo.kactor.sysUtil.getActor("waiter")
			}

			while (smartbell == null) {
				delay(1000)
				smartbell = it.unibo.kactor.sysUtil.getActor("smartbell")
			}

			while (barman == null) {
				delay(1000)
				barman = it.unibo.kactor.sysUtil.getActor("barman")
			}


			//-----------------TEST RIFIUTO CLIENTE---------------
			delay(1000)
			println("---------------------------	MESSAGGIO INVIATO		---------------------------")
			MsgUtil.sendMsg(MsgUtil.buildRequest("test", "ringBell", "ringBell(1)", "smartbell"), smartbell!!)
			delay(1000)
			assertTrue(sJson.getClientArrived() == false)

			//-----------------TEST ACCETTO CLIENTE---------------
			delay(1000)
			println("---------------------------	MESSAGGIO INVIATO		---------------------------")
			MsgUtil.sendMsg(MsgUtil.buildRequest("test", "ringBell", "ringBell(1)", "smartbell"), smartbell!!)
			delay(1000)
			assertTrue(sJson.getClientArrived() == false)

			//-----------------TEST ORDINAZIONE TEA---------------
			delay(1000)
			println("---------------------------	MESSAGGIO INVIATO		---------------------------")
			MsgUtil.sendMsg("test", "sendOrder", "sendOrder(tea, 2, 22)", barman!!)
			delay(1000)
			assertTrue(bJson.getPreparingOrder().matches(".*?".toRegex()))

		}
	}
}