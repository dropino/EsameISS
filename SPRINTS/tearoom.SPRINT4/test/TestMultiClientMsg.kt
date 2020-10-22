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
import connQak.connQakCoap
import it.unibo.kactor.ApplMessage
import connQak.utils.ApplMessageUtils


class testMultiClientMsg {

	var waiterConn: connQakCoap? = null
	var smartbellConn: connQakCoap? = null

	var i = 0
	var IDC = 0
	var j = 0
	var CID = "0"
	var CID1 = "0"
	var CID2 = "0"
	var CID3 = "0"
	var TABLE = "0"
	var TABLE1 = "0"
	var TABLE2 = "0"
	var TABLE3 = "0"
	var Status = "0"
	var V = "0"
	var TIME = "0"

	var counterAtomic = java.util.concurrent.atomic.AtomicInteger()


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

		smartbellConn = connQakCoap("localhost", "8071", "smartbell", "ctxsmartbell")
		smartbellConn!!.createConnection()

		waiterConn = connQakCoap("localhost", "8072", "waiter", "ctxtearoom")
		waiterConn!!.createConnection()


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
	fun main() = runBlocking {
		GlobalScope.massiveRun { counterAtomic.incrementAndGet() }
	}

	//	GlobalScope.massiveRun( action: suspend () -> Unit )
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun GlobalScope.massiveRun(action: suspend () -> Unit) {

		var c1: Deferred<Boolean>
		var c2: Deferred<Boolean>
		var c3: Deferred<Boolean>


		val handler = CoroutineExceptionHandler { _, exception ->
			println("=========	Coroutine exception caught")
		}

//--------------CLIENT1 ARRIVED---------------------
		c1 = GlobalScope.async(handler) {
			testRing(36)
		}

		assertTrue(c1.await())
		CID1 = CID

//--------------CHECK WAITTIME FOR C1----------------------		
		c1 = GlobalScope.async(handler) {
			testWaitTime(CID1.toInt())
		}
		assertTrue(c1.await())


//--------------WAITER GO TO ENTRANCEDOOR FOR C1	&&	CLIENT2 ARRIVED------------		
		c1 = GlobalScope.async(handler) {
			testDeployEntrance(CID1.toInt())
		}
		delay(1000)
		c2 = GlobalScope.async(handler) {
			testRing(37)
		}

		assertTrue(c2.await())
		assertTrue(c1.await())
		TABLE1 = TABLE
		CID2 = CID
		assertTrue(CID1 != CID2)

//--------------CHECK WAITTIME FOR C2----------------------		
		c2 = GlobalScope.async(handler) {
			testWaitTime(CID2.toInt())
		}
		assertTrue(c2.await())

//--------------WAITER GO TO ENTRANCEDOOR FOR C2	&&	C1 ORDER	&& 	CLIENT3 ARRIVED------------		
		c2 = GlobalScope.async(handler) {
			testDeployEntrance(CID2.toInt())
		}
		c1 = GlobalScope.async(handler) {
			testRequestOrder(CID1.toInt(), TABLE1.toInt())
		}
		delay(5000)
		c3 = GlobalScope.async(handler) {
			testRing(36)
		}
		assertTrue(c1.await())
		assertTrue(c2.await())
		assertTrue(c3.await())
		CID3 = CID
		println("&&&&&&&&&&=========	CID1: $CID1 CID2: $CID2 CID3: $CID3")
		assertTrue((CID3 != CID2) && (CID3 != CID1))

		TABLE2 = TABLE
		assertTrue(TABLE1 != TABLE2)

//--------------CHECK WAITTIME FOR C3------------		
		c3 = GlobalScope.async(handler) {
			testWaitTime(CID3.toInt())
		}
		assertTrue(c3.await())

//--------------WAITER GO TO ENTRANCEDOOR FOR C3------------		
		c3 = GlobalScope.async(handler) {
			testDeployEntrance(CID3.toInt())
		}
		assertTrue(c3.await())

		delay(5000)
		c3 = GlobalScope.async(handler) {
			testDeployEntrance(CID3.toInt())
		}
		assertTrue(c3.await())
		delay(5000)
		c3 = GlobalScope.async(handler) {
			testDeployEntrance(CID3.toInt())
		}
		assertTrue(c3.await())

//--------------PAY REQUEST FOR C1	&& ORDER REQUEST FOR C2------------		
		c2 = GlobalScope.async(handler) {
			testRequestOrder(CID2.toInt(), TABLE2.toInt())
		}
		delay(5000)
		c1 = GlobalScope.async(handler) {
			testRequestPay(CID1.toInt(), TABLE1.toInt())
		}
		assertTrue(c1.await())
		assertTrue(c2.await())

		c3 = GlobalScope.async(handler) {
			testDeployEntrance(CID3.toInt())
		}
		assertTrue(c3.await())

//--------------WAITER GO TO EXITDOOR FOR C1------------		
		c1 = GlobalScope.async(handler) {
			testDeployExit(CID1.toInt(), TABLE1.toInt())
		}
		assertTrue(c1.await())

//--------------PAY REQUEST FOR C2------------
		c2 = GlobalScope.async(handler) {
			testRequestPay(CID2.toInt(), TABLE2.toInt())
		}
		assertTrue(c2.await())

//--------------WAITER GO TO EXITDOOR FOR C2------------		
		c2 = GlobalScope.async(handler) {
			testDeployExit(CID2.toInt(), TABLE2.toInt())
		}
		assertTrue(c2.await())
	}


	//------------------RINGBELL FUNCTION------------------------
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun testRing(temp: Int): Boolean {
		var ringMsg: ApplMessage? = null
		var reply: ApplMessage? = null
		var ringRepArgs = arrayOf<String>()

		//-----------------TEST ACCETTO CLIENTE---------------
		ringMsg = MsgUtil.buildRequest("web", "ringBell", "ringBell(36)", "smartbell")
		reply = smartbellConn!!.request(ringMsg)
		ringRepArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply)
		Status = ringRepArgs[0].toString()
		CID = ringRepArgs[1].toString()

		if ((ringRepArgs.size == 2) &&
				(Status.matches("-?\\d+(\\.\\d+)?".toRegex())) &&
				(CID.matches("-?\\d+(\\.\\d+)?".toRegex())) &&
				(Status.toInt() == 1)) return true
		else return false
	}

	//------------------DEPLOY FUNCTION------------------------	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun testDeployEntrance(cid: Int): Boolean {
		var ringMsg: ApplMessage? = null
		var reply: ApplMessage? = null
		var ringRepArgs = arrayOf<String>()

		ringMsg = MsgUtil.buildRequest("web", "deploy", "deploy(entrancedoor, table, $cid)", "waiter")
		reply = waiterConn!!.request(ringMsg)
		ringRepArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply)
		TABLE = ringRepArgs[0].toString()


		if ((ringRepArgs.size == 1) &&
				(TABLE.matches("-?\\d+(\\.\\d+)?".toRegex()) || TABLE == "ko")) return true
		else return false
	}

	//------------------WAITTIME FUNCTION------------------------
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun testWaitTime(cid: Int): Boolean {
		var ringMsg: ApplMessage? = null
		var reply: ApplMessage? = null
		var ringRepArgs = arrayOf<String>()

		ringMsg = MsgUtil.buildRequest("web", "waitTime", "waitTime($cid)", "waiter")
		reply = waiterConn!!.request(ringMsg)
		ringRepArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply)
		TIME = ringRepArgs[0].toString()

		assertTrue(ringRepArgs.size == 1)
		assertTrue(TIME.matches("-?\\d+(\\.\\d+)?".toRegex()))

		if ((ringRepArgs.size == 1) &&
				(TIME.matches("-?\\d+(\\.\\d+)?".toRegex()))) return true
		else return false
	}

	//------------------ORDERREQUEST FUNCTION------------------------
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun testRequestOrder(cid: Int, table: Int): Boolean {
		var ringMsg: ApplMessage? = null
		var reply: ApplMessage? = null
		var ringRepArgs = arrayOf<String>()

		ringMsg = MsgUtil.buildRequest("web", "clientRequest", "clientRequest(order, $table, $cid)", "waiter")
		reply = waiterConn!!.request(ringMsg)
		ringRepArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply)
		V = ringRepArgs[0].toString()

		ringMsg = MsgUtil.buildDispatch("web", "order", "order(tea)", "waiter")
		waiterConn!!.forward(ringMsg)

		if ((ringRepArgs.size == 1) &&
				(V.toString() == "ok")) return true
		else return false
	}


	//------------------PAYREQUEST FUNCTION------------------------
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun testRequestPay(cid: Int, table: Int): Boolean {
		var ringMsg: ApplMessage? = null
		var reply: ApplMessage? = null
		var ringRepArgs = arrayOf<String>()

		ringMsg = MsgUtil.buildRequest("web", "clientRequest", "clientRequest(pay, $table, $cid)", "waiter")
		reply = waiterConn!!.request(ringMsg)
		ringRepArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply)
		V = ringRepArgs[0].toString()
		ringMsg = MsgUtil.buildDispatch("web", "pay", "pay($V)", "waiter")
		waiterConn!!.forward(ringMsg)


		if ((ringRepArgs.size == 1) &&
				(V.matches("-?\\d+(\\.\\d+)?".toRegex()))) return true
		else return false
	}

	//------------------DEPLOY EXIT FUNCTION------------------------
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun testDeployExit(cid: Int, table: Int): Boolean {
		var ringMsg: ApplMessage? = null
		var reply: ApplMessage? = null
		var ringRepArgs = arrayOf<String>()

		ringMsg = MsgUtil.buildRequest("web", "deploy", "deploy($table, exitdoor, $cid)", "waiter")
		reply = waiterConn!!.request(ringMsg)
		ringRepArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply)
		V = ringRepArgs[0].toString()

		if ((ringRepArgs.size == 1) &&
				(V.matches("-?\\d+(\\.\\d+)?".toRegex()))) return true
		else return false
	}

}
	
	