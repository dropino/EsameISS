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

	
	
	var home:String?    =
		"|r, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, X, 1, X, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|X, X, X, X, X, X, X, X,".trim();
	
	var mapGetDrink:String?    =
		"|1, 1, 1, 1, r, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, 0, 1, 1, 1, 1, X, " + "\n"+
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
		 "|1, 1, 1, 1, 1, 0, 1, X, " + "\n"+
		 "|X, X, X, X, X, X, X, X,".trim();
	
	var mapOrder:String?    =
		"|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, 1, 1, 1, 1, 1, X, " + "\n"+
		 "|1, 1, 0, 1, 1, 1, 1, X, " + "\n"+
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
				
//				WAITER GO TO ENTRANCEDOOR	
				delay(9100L)

				println("%%%  DESIRED MAP:")
				println(mapConvoyEntrance)
		
				println("%%%  ACTUAL MAP:")
				println(plannerUtil.getMap().trim())
		
				Assert.assertEquals( mapConvoyEntrance, plannerUtil.getMap().trim() )
				
				
						
//				WAITER GO TO TABLE					
				delay(5000L)
				
				println("%%%  DESIRED MAP:")
				println(mapOrder)
		
				println("%%%  ACTUAL MAP:")
				println(plannerUtil.getMap().trim())
		
				Assert.assertEquals( mapOrder, plannerUtil.getMap().trim() )
				
				
				
//				WAITER GO TO BARMAN TO GET DRINK	
				delay(7575L)

				println("%%%  DESIRED MAP:")
				println(mapGetDrink)
		
				println("%%%  ACTUAL MAP:")
				println(plannerUtil.getMap().trim())
		
				Assert.assertEquals( mapGetDrink, plannerUtil.getMap().trim() )
				
				
				
//				WAITER GO TO TABLE TO BRING DRINK	
				delay(7100L)

				println("%%%  DESIRED MAP:")
				println(mapBringDrink)
		
				println("%%%  ACTUAL MAP:")
				println(plannerUtil.getMap().trim())
		
				Assert.assertEquals( mapBringDrink, plannerUtil.getMap().trim() )
				
				
				
//				WAITER GO HOME AND WAIT A TASK		
				delay(7300L)

				println("%%%  DESIRED MAP:")
				println(home)
		
				println("%%%  ACTUAL MAP:")
				println(plannerUtil.getMap().trim())
		
				Assert.assertEquals( home, plannerUtil.getMap().trim() )
				
				
				
//				WAITER GO TO TABLE TO TAKE PAYMENT	
				delay(6800L)

				println("%%%  DESIRED MAP:")
				println(mapPay)
		
				println("%%%  ACTUAL MAP:")
				println(plannerUtil.getMap().trim())
		
				Assert.assertEquals( mapPay, plannerUtil.getMap().trim() )
				
				
				
//				WAITER GO TO EXITDOOR	
				delay(6700L)

				println("%%%  DESIRED MAP:")
				println(mapConvoyExit)
		
				println("%%%  ACTUAL MAP:")
				println(plannerUtil.getMap().trim())
		
				Assert.assertEquals( mapConvoyExit, plannerUtil.getMap().trim() )
				
				
				
//				WAITER GO HOME AND WAIT A TASK		
				delay(7300L)

				println("%%%  DESIRED MAP:")
				println(home)
		
				println("%%%  ACTUAL MAP:")
				println(plannerUtil.getMap().trim())
		
				Assert.assertEquals( home, plannerUtil.getMap().trim() )
				
			}
			
		println("testWaiter BYE  ")			  
	}
	

}
