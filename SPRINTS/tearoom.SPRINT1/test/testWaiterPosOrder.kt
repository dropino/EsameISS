package test
 	//"tcp://mqtt.eclipse.org:1883"
	//mqtt.eclipse.org
	//tcp://test.mosquitto.org
	//mqtt.fluux.io
	//"tcp://broker.hivemq.com" 

import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.MqttUtils
import itunibo.planner.plannerUtil
import org.junit.Assert
import org.junit.*
import itunibo.planner.model.RoomMap
 

class testWaiterPosOrder {
	var waiter             : ActorBasic? = null
	var client             : ActorBasic? = null
	var smartbell          : ActorBasic? = null
	val initDelayTime     = 1000L   // 

	
	
var entrance:String?    =
		"|r, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, X, 1, X, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|X, X, X, X, X, X, X, X,".trim();
	
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

	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
		@Test
		fun testPosConvoyEntrance(){
			runBlocking {
				println("%%%	testPosConvoyEntrance waiter	%%%")
//				waiter = it.unibo.kactor.sysUtil.getActor("waiter")
				
				while( waiter == null /*|| client==null || smartbell==null*/){
					println("testWaiterMap wait for robot ... ")
					delay(initDelayTime)  //time for robot to start
//					client = it.unibo.kactor.sysUtil.getActor("client")
					waiter = it.unibo.kactor.sysUtil.getActor("waiter")
//					smartbell = it.unibo.kactor.sysUtil.getActor("smartbell")
				}
				
				
				//RESETTING FOR THE NEXT INTERACTION (WAITER - BARMAN - CLIENT)
				delay(1000L)
				
//				MsgUtil.sendMsg("client","clientRequest","ready(0)",waiter!!)
				
				println("%%%--------------------TRY:")
				MsgUtil.sendMsg(MsgUtil.buildDispatch("clientID", "smartbell", "waiter", "client"), waiter!!)
				
				
				println("%%%  DESIRED MAP:")
				println(entrance)
//		
//				println("%%%  ACTUAL MAP:")
//				println(plannerUtil.getMap().trim())
//		
//				Assert.assertEquals( entrance, plannerUtil.getMap().trim() )
//				
//				
//				
//				//WAITER GO TO ENTRANCEDOOR
//				delay(1000L)
//				
////			MsgUtil.sendMsg("client","clientRequest","ready(0)",waiter!!)
//				
//				println("%%%  DESIRED MAP:")
//				println(mapConvoyEntrance)
//		
//				println("%%%  ACTUAL MAP:")
//				println(plannerUtil.getMap().trim())
//		
//				Assert.assertEquals( mapConvoyEntrance, plannerUtil.getMap().trim() )
			}
			
		println("testWaiter BYE  ")			  
	}
	

}
