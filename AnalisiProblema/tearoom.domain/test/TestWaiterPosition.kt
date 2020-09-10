package test

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert.assertTrue

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import org.junit.Assert
import itunibo.planner.plannerUtil
import kotlinx.coroutines.delay

class TestWaiterPosition {
	var waiter             : ActorBasic? = null
	var client             : ActorBasic? = null
	var smartbell             : ActorBasic? = null
	
	var mapGetDrink:String?    =
		"|1, 1, 1, 1, 1, r, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, X, 1, X, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|X, X, X, X, X, X, X, X,".trim();
	
	var mapBringDrink:String?    =
		"|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, r, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, X, 1, X, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|X, X, X, X, X, X, X, X,".trim();
	
	var mapPay:String?    =
		"|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, r, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, X, 1, X, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|X, X, X, X, X, X, X, X,".trim();
	
	var mapOrder:String?    =
		"|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, r, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, X, 1, X, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|X, X, X, X, X, X, X, X,".trim();
	
	var mapConvoyExit:String?    =
		"|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, X, 1, X, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, r, 1, X, " + "\n"+
		 "|X, X, X, X, X, X, X, X,".trim();
	
	var mapConvoyEntrance:String?    =
		"|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, X, 1, X, 1, 1, X, " + "\n"+
		 "|1, r, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|X, X, X, X, X, X, X, X,".trim();	

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp(){
		//println("%%%  boudaryTest prepare the result map expected ")
		//println( mapRoomKotlin.mapUtil.refMapForTesting )
		//activate the application: SEE boundaryTest
   		kotlin.concurrent.thread(start = true) {
			it.unibo.ctxtearoom.main()  //WARNING: elininate the autostart
		}
	}
	
	@After
	fun terminate() {
		println("%%%  testWaiterPos terminate ")
	}

	
	fun checkResource(value: String):Boolean{		
		if( waiter != null ){
			println(" --- checkResource --- ${waiter!!.geResourceRep()}")
			if(waiter!!.geResourceRep() == value){
				return true
			}
			else return false
		}
		else return false
	}

	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	suspend fun testPosConvoyEntrance(){
		println("%%%  testPosConvoyEntrance waiter ")
		waiter = it.unibo.kactor.sysUtil.getActor("waiter")
		
		MsgUtil.sendMsg("client","clientRequest","ready(0)",waiter!!)
		
		while(checkResource("waiter_arrived") == false){
			delay(10)
		}
		
		println("%%%  DESIRED MAP:")
		println(mapConvoyEntrance)
		
		println("%%%  ACTUAL MAP:")
		println(plannerUtil.getMap().trim())
		
		Assert.assertEquals( mapConvoyEntrance, plannerUtil.getMap().trim() )
	}
	
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	suspend fun testPosOrder(){
		MsgUtil.sendMsg("client","clientRequest","ready(0)",waiter!!)
		
		while(checkResource( "waiter_rdy_request") == false){
			delay(10)
		}
		
		println("%%%  DESIRED MAP:")
		println(mapOrder)
		
		println("%%%  ACTUAL MAP:")
		println(plannerUtil.getMap().trim())
		
		Assert.assertEquals( mapOrder, plannerUtil.getMap().trim() )
	}
	
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	suspend fun testPosGetDrink(){		 			
		MsgUtil.sendMsg("client","order","ready(0)",waiter!!)
		
		delay(3000)
		
		MsgUtil.sendMsg("barman","orderReady","ready(0)",waiter!!)
		
		while(checkResource( "waiter_rdy_getDrink") == false){
			delay(10)
		}
		
		println("%%%  DESIRED MAP:")
		println(mapGetDrink)
		
		println("%%%  ACTUAL MAP:")
		println(plannerUtil.getMap().trim())
		
		Assert.assertEquals( mapGetDrink, plannerUtil.getMap().trim() )
	}
	
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	suspend fun testPosBringDrink(){		 					
		while(checkResource( "waiter_rdy_bringDrink") == false){
			delay(10)
		}
		
		println("%%%  DESIRED MAP:")
		println(mapBringDrink)
		
		println("%%%  ACTUAL MAP:")
		println(plannerUtil.getMap().trim())
		
		Assert.assertEquals( mapBringDrink, plannerUtil.getMap().trim() )
	}

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	suspend fun testPosPay(){
		MsgUtil.sendMsg("client","clientRequest","ready(0)",waiter!!)
		
		while(checkResource( "waiter_rdy_bringDrink") == false){
			delay(10)
		}
		
		println("%%%  DESIRED MAP:")
		println(mapPay)
		
		println("%%%  ACTUAL MAP:")
		println(plannerUtil.getMap().trim())
		
		Assert.assertEquals( mapPay, plannerUtil.getMap().trim() )
	}
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	suspend fun testPosConvoyExit(){
		MsgUtil.sendMsg("client","paid","ready(0)",waiter!!)
		
		while(checkResource( "waiter_rdy_leave") == false){
			delay(10)
		}
		
		println("%%%  DESIRED MAP:")
		println(mapConvoyExit)
		
		println("%%%  ACTUAL MAP:")
		println(plannerUtil.getMap().trim())
		
		Assert.assertEquals( mapConvoyExit, plannerUtil.getMap().trim() )		
	}

}