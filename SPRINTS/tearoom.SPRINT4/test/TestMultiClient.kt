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
import kotlinx.coroutines.withContext
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.Deferred


class testMultiClient {

	var waiter: ActorBasic? = null
	var smartbell: ActorBasic? = null
	var barman: ActorBasic? = null
	var waiterwalker: ActorBasic? = null
	var simclient: ActorBasic? = null

	val mqttTest = MqttUtils("test")
	val initDelayTime = 1500L   //
	var table = 1
	var i = 0
	var IDC = 0
	var j = 0

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


	fun checkResource(value: ActorBasic): String {
		if (value != null) {
			return value!!.geResourceRep()
		} else
			return "null"
	}


	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun main() = runBlocking {
		GlobalScope.massiveRun { counterAtomic.incrementAndGet() }
	}

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun GlobalScope.massiveRun(action: suspend () -> Unit) {

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

		val handler = CoroutineExceptionHandler { _, exception ->
			println("=========	Coroutine exception caught")
		}


		var c1: Deferred<Boolean> = GlobalScope.async(handler) {
			client1()
		}

		var c2: Deferred<Boolean> = GlobalScope.async(handler) {
			client2()
		}

		var c3: Deferred<Boolean> = GlobalScope.async(handler) {
			client3()
		}

		var c4: Deferred<Boolean> = GlobalScope.async(handler) {
			client4()
		}

		assertTrue(c1.await())
		println("=============== 1_OK =============")
		assertTrue(c2.await())
		println("=============== 2_OK =============")
		assertTrue(c3.await())
		println("=============== 3_OK =============")
		assertTrue(c4.await())
		println("=============== 4_OK =============")


	}


	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun client1(): Boolean = runBlocking {
		//-----------------TEST CLIENT1---------------

		i = 0
		delay(5000)
		println("===========================	TEST CLIENT1		===========================")
		MsgUtil.sendMsg(MsgUtil.buildRequest("test10", "ringBell", "ringBell(36)", "smartbell"), smartbell!!)
		delay(2000)

		println(checkResource(smartbell!!))
		if (checkResource(smartbell!!) == "Accept") {

			testClient(1, 10)
			true
		} else false
	}

	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun client2(): Boolean = runBlocking {
		//-----------------TEST CLIENT2---------------

		i = 0
		delay(15000)
		println("===========================	TEST CLIENT2		===========================")
		MsgUtil.sendMsg(MsgUtil.buildRequest("test20", "ringBell", "ringBell(39)", "smartbell"), smartbell!!)
		delay(1000)
		
		println(checkResource(smartbell!!))

		if (checkResource(smartbell!!) == "Discard") {
			true
		} else false
	}

	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun client3(): Boolean = runBlocking {
		//-----------------TEST CLIENT3---------------
		i = 0
		delay(30000)
		println("===========================	TEST CLIENT3		===========================")
		MsgUtil.sendMsg(MsgUtil.buildRequest("test30", "ringBell", "ringBell(36)", "smartbell"), smartbell!!)
		delay(1000)

		println(checkResource(smartbell!!))
		if (checkResource(smartbell!!) == "Accept") {
			testClient(2, 30)
			true
		} else false
	}

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun client4(): Boolean = runBlocking {
		//-----------------TEST CLIENT4---------------
		i = 0
		delay(45000)
		println("===========================	TEST CLIENT4		===========================")
		MsgUtil.sendMsg(MsgUtil.buildRequest("test40", "ringBell", "ringBell(36)", "smartbell"), smartbell!!)
		MsgUtil.sendMsg(MsgUtil.buildRequest("test40", "waitTime", "waitTime(40)", "waiter"), waiter!!)


		while (checkResource(waiter!!) != "Client_must_wait") {
		}
		true
	}

	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun testClient(table: Int, IDC: Int) {
		//-----------------TEST DEPLOYMENT--------------
		println("---------------------------	TESTDEPLOYMENT		---------------------------")
		MsgUtil.sendMsg(
				MsgUtil.buildRequest("test" + IDC, "deploy", "deploy(entrancedoor, table, $IDC)", "waiter"),
				waiter!!
		)
		delay(100)
	}
}
	
	