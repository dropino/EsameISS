/* Generated by AN DISI Unibo */ 
package it.unibo.waiter

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.LinkedList
import java.util.Queue
	
class Waiter ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				var CCID 			= ""	
				var CTABLE : Int?	= 0
				var BILL			= 5
				var KIND			= ""
				var CDRINK			= ""
				var WaitingClient : Boolean	= false
				var MaxStayTime		= 20000L
				var MaxWaitTime		= 30000L
				var TimeToGoHome	= 15000L
				
				var Ntables			= 0
				val clientQueue : Queue<String> = LinkedList<String>()
				
				var CurST			= ""
				var PL				= ""
				var Dest			= ""
		
				var wJson = json.WaiterJson()
				
				var TimeCleaned : LongArray = longArrayOf( 0L, 0L)
				var LastCleanedTable= 0
				var CleaningInrement = 500L
				var MaxCleaning 	= 5000L
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("WAITER | INIT ")
						discardMessages = false
						solve("consult('tearoomkb.pl')","") //set resVar	
						println("WAITER | solved tearoomkb.pl ")
						solve("maxStayTime(T)","") //set resVar	
						if( currentSolution.isSuccess() ) { MaxStayTime = getCurSol("T").toString().toLong()  
						}
						else
						{}
						solve("maxWaitTime(T)","") //set resVar	
						if( currentSolution.isSuccess() ) { MaxWaitTime = getCurSol("T").toString().toLong()  
						}
						else
						{}
						solve("timeToGoHome(T)","") //set resVar	
						if( currentSolution.isSuccess() ) { TimeToGoHome = getCurSol("T").toString().toLong()  
						}
						else
						{}
						println("Current MaxStay: $MaxStayTime, MaxWait: $MaxWaitTime, TimeToGoHome: $TimeToGoHome")
					}
					 transition(edgeName="t00",targetState="listening",cond=whenEvent("waiterwalkerstarted"))
				}	 
				state("listening") { //this:State
					action { //it:State
						
									CCID = ""
									CTABLE = 0
						
									wJson.reset()
						println("WAITER | listening... ")
						updateResourceRep(wJson.toJson() 
						)
						stateTimer = TimerActor("timer_listening", 
							scope, context!!, "local_tout_waiter_listening", TimeToGoHome )
					}
					 transition(edgeName="t01",targetState="goHome",cond=whenTimeout("local_tout_waiter_listening"))   
					transition(edgeName="t02",targetState="answerTime",cond=whenRequest("waitTime"))
					transition(edgeName="t03",targetState="handleDeploy",cond=whenRequest("deploy"))
					transition(edgeName="t04",targetState="answerClientRequest",cond=whenRequest("clientRequest"))
					transition(edgeName="t05",targetState="getDrink",cond=whenDispatch("orderReady"))
					transition(edgeName="t06",targetState="cleanTable",cond=whenDispatch("tableDirty"))
				}	 
				state("goHome") { //this:State
					action { //it:State
						solve("waiter(ST)","") //set resVar	
						if( currentSolution.isSuccess() ) { CurST = getCurSol("ST").toString()   
						println("WAITER | current waiter state: $CurST")
						}
						else
						{}
						println("WAITER | current waiter state: $CurST")
						if(  CurST != "athome"  
						 ){request("moveForTask", "moveForTask(home,1)" ,"waiterwalker" )  
						println("WAITER | Changing movement state")
						solve("changeWaiterState(athome)","") //set resVar	
						if( currentSolution.isSuccess() ) {
						 				wJson.setMovingTo("home")
						}
						else
						{}
						updateResourceRep(wJson.toJson() 
						)
						}
						else
						 {forward("rest", "rest(0)" ,"waiter" ) 
						 }
					}
					 transition(edgeName="t07",targetState="listening",cond=whenReply("movementDone"))
					transition(edgeName="t08",targetState="error",cond=whenReply("walkbreak"))
					transition(edgeName="t09",targetState="listening",cond=whenDispatch("rest"))
				}	 
				state("answerTime") { //this:State
					action { //it:State
						 var WaitTime = 0L  
						if( checkMsgContent( Term.createTerm("waitTime(CID)"), Term.createTerm("waitTime(CID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CCID = payloadArg(0).toString() 
						}
						solve("numavailabletables(N)","") //set resVar	
						if( currentSolution.isSuccess() ) { Ntables = getCurSol("N").toString().toInt()   
						println("WAITER | numavailabletables=$Ntables")
						}
						else
						{}
						if(  Ntables != 0  
						 ){ 
										if (WaitingClient) {
											WaitingClient = false
											wJson.setAcceptedWaiting(true)
										} 
						solve("tableavailable(N)","") //set resVar	
						if( currentSolution.isSuccess() ) { CTABLE = getCurSol("N").toString().toInt()   
						println("WAITER | tableavailable=$CTABLE")
						solve("engageTable($CTABLE,$CCID)","") //set resVar	
						println("WAITER | Going to DEPLOY Client $CCID to table $CTABLE")
						}
						else
						{}
						answer("waitTime", "wait", "wait(0)"   )  
						}
						else
						 { 
						 				WaitTime = MaxWaitTime
						 				WaitingClient = true
						 				clientQueue.add(CCID) 
						 answer("waitTime", "wait", "wait($WaitTime)"   )  
						 }
						
									wJson.setBusy(true)
									wJson.setWaitTime(WaitTime)
									wJson.setClientID(CCID)
									wJson.setReceivedRequest("waitTime")
						updateResourceRep(wJson.toJson() 
						)
					}
					 transition( edgeName="goto",targetState="listening", cond=doswitch() )
				}	 
				state("handleDeploy") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("deploy(FROM,TO,CID)"), Term.createTerm("deploy(FROM,TO,CID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
								  				CTABLE = payloadArg(0).toString().toIntOrNull()
								  				Dest = payloadArg(1).toString()
								  				CCID = payloadArg(2).toString()	
								println("WAITER | handling deployment request for client $CCID... ")
						}
					}
					 transition( edgeName="goto",targetState="goToEntrance", cond=doswitchGuarded({ Dest == "table"  
					}) )
					transition( edgeName="goto",targetState="deployClientExit", cond=doswitchGuarded({! ( Dest == "table"  
					) }) )
				}	 
				state("goToEntrance") { //this:State
					action { //it:State
								
									wJson.setBusy(true)
									wJson.setClientID(CCID)
									wJson.setMovingTo("entrancedoor")
									wJson.setReceivedRequest("DeployEntrance")	  				
						updateResourceRep(wJson.toJson() 
						)
						println("WAITER | GOING to ENTRANCE door ")
						request("moveForTask", "moveForTask(entrancedoor,1)" ,"waiterwalker" )  
						solve("changeWaiterState(moving)","") //set resVar	
					}
					 transition(edgeName="t010",targetState="deployClientEntrance",cond=whenReply("movementDone"))
					transition(edgeName="t011",targetState="error",cond=whenReply("walkbreak"))
				}	 
				state("deployClientEntrance") { //this:State
					action { //it:State
						solve("teatable(T,busy($CCID))","") //set resVar	
						if( currentSolution.isSuccess() ) { CTABLE = getCurSol("T").toString().toInt()  
						}
						else
						{}
						println("WAITER | DEPLOYING Client $CCID to table $CTABLE")
								
									wJson.setBusy(true)
									wJson.setClientID(CCID)
									wJson.setMovingFrom("entrancedoor")
									wJson.setMovingTo("table " + CTABLE)
									wJson.setTable(CTABLE)
									wJson.setReceivedRequest("deployEntrance")	  				
						updateResourceRep(wJson.toJson() 
						)
						request("moveForTask", "moveForTask(teatable,$CTABLE)" ,"waiterwalker" )  
					}
					 transition(edgeName="t012",targetState="confirmClientArrival",cond=whenReply("movementDone"))
					transition(edgeName="t013",targetState="error",cond=whenReply("walkbreak"))
				}	 
				state("deployClientExit") { //this:State
					action { //it:State
						println("WAITER | deploying simclient from table to exit door... ")
						forward("tableDirty", "tableDirty($CTABLE)" ,"waiter" ) 
						request("moveForTask", "moveForTask(exitdoor,1)" ,"waiterwalker" )  
								
									wJson.setBusy(true)
									wJson.setClientID(CCID)
									wJson.setTable(CTABLE)
									wJson.setTableDirty(true)
									wJson.setMovingFrom("table " + CTABLE)
									wJson.setMovingTo("exitdoor")
									wJson.setReceivedRequest("deployExit")	  				
						updateResourceRep(wJson.toJson() 
						)
					}
					 transition(edgeName="t014",targetState="confirmClientArrival",cond=whenReply("movementDone"))
					transition(edgeName="t015",targetState="error",cond=whenReply("walkbreak"))
				}	 
				state("confirmClientArrival") { //this:State
					action { //it:State
						println("WAITER | SENDING CONFIRMATION to simclient $CCID: arrived $Dest $CTABLE")
						answer("deploy", "arrived", "arrived($CTABLE)"   )  
								
									wJson.setMovingFrom("")
									wJson.setMovingTo("")
									wJson.setArrival(Dest)
						updateResourceRep(wJson.toJson() 
						)
					}
					 transition( edgeName="goto",targetState="listening", cond=doswitch() )
				}	 
				state("answerClientRequest") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("clientRequest(KIND,TABLE,CID)"), Term.createTerm("clientRequest(KIND,TABLE,CID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								  
												KIND	= payloadArg(0).toString()
												CTABLE 	= payloadArg(1).toString().toInt()
												CCID 	= payloadArg(2).toString()
												wJson.setBusy(true)
												wJson.setMovingTo("table " + CTABLE)
												wJson.setReceivedRequest(KIND)
												wJson.setClientID(CCID)
												wJson.setTable(CTABLE)
						}
						updateResourceRep(wJson.toJson() 
						)
						println("WAITER | received CLIENTREQUEST $KIND $CTABLE ")
						request("moveForTask", "moveForTask(teatable,$CTABLE)" ,"waiterwalker" )  
						solve("changeWaiterState(moving)","") //set resVar	
					}
					 transition(edgeName="t016",targetState="atTableForRequest",cond=whenReply("movementDone"))
					transition(edgeName="t017",targetState="error",cond=whenReply("walkbreak"))
				}	 
				state("atTableForRequest") { //this:State
					action { //it:State
						solve("clientRequestPayload($KIND,P)","") //set resVar	
						if( currentSolution.isSuccess() ) { PL = getCurSol("P").toString()  
						}
						else
						{}
						println("WAITER | CLIENT $CCID requested $KIND")
										
									wJson.setMovingTo("")
									wJson.setArrival("table " + CTABLE)
						updateResourceRep(wJson.toJson() 
						)
						answer("clientRequest", "atTable", "atTable($PL)"   )  
					}
					 transition(edgeName="t018",targetState="transferOrder",cond=whenDispatch("order"))
					transition(edgeName="t019",targetState="payment",cond=whenDispatch("pay"))
				}	 
				state("payment") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("pay(MONEY)"), Term.createTerm("pay(MONEY)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								  
												var amount	= payloadArg(0).toString()
												wJson.reset()
												wJson.setBusy(true)
												wJson.setPayment(amount.toInt())
												wJson.setReceivedRequest(KIND)
												wJson.setClientID(CCID)
												wJson.setTable(CTABLE)
								updateResourceRep(wJson.toJson() 
								)
						}
					}
					 transition(edgeName="t020",targetState="handleDeploy",cond=whenRequest("deploy"))
				}	 
				state("transferOrder") { //this:State
					action { //it:State
						println("WAITER | sending order to barman $CCID $CDRINK $CTABLE... ")
						if( checkMsgContent( Term.createTerm("order(TEA)"), Term.createTerm("order(DRINK)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								  CDRINK	= payloadArg(0).toString()  
						}
							
										wJson.reset()
										wJson.setBusy(true)
										wJson.setOrder(CDRINK)
										wJson.setReceivedRequest(KIND)
										wJson.setClientID(CCID)
										wJson.setTable(CTABLE)
						updateResourceRep(wJson.toJson() 
						)
						forward("sendOrder", "sendOrder($CDRINK,$CTABLE,$CCID)" ,"barman" ) 
					}
					 transition( edgeName="goto",targetState="listening", cond=doswitch() )
				}	 
				state("cleanTable") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("tableDirty(N)"), Term.createTerm("tableDirty(N)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								  CTABLE = payloadArg(0).toString().toInt()  
						}
						if(  LastCleanedTable != 0 
									&& LastCleanedTable != CTABLE
									 && TimeCleaned[LastCleanedTable - 1] < MaxCleaning  
						 ){forward("tableDirty", "tableDirty($CTABLE)" ,"waiter" ) 
						  CTABLE = LastCleanedTable  
						}
						  LastCleanedTable = CTABLE!!   
						println("WAITER | going to table for CLEANING $CTABLE... ")
						request("moveForTask", "moveForTask(teatable,$CTABLE)" ,"waiterwalker" )  
						solve("changeWaiterState(moving)","") //set resVar	
						
									wJson.setBusy(true)
									wJson.setTable(CTABLE)
									wJson.setTableDirty(true)
									wJson.setMovingTo("table "+ CTABLE)
									wJson.setReceivedRequest("tableDirty")						
					}
					 transition(edgeName="t021",targetState="atTableToClean",cond=whenReply("movementDone"))
					transition(edgeName="t022",targetState="error",cond=whenReply("walkbreak"))
				}	 
				state("atTableToClean") { //this:State
					action { //it:State
						println("WAITER | cleaning table $CTABLE... ")
						
									wJson.setBusy(true)
									wJson.setTable(CTABLE)
									wJson.setMovingTo("")
									wJson.setTableDirty(true)
									wJson.setArrival("table " + CTABLE)
									wJson.setReceivedRequest("tableDirty")						
						updateResourceRep( wJson.toJson()  
						)
						if(  TimeCleaned[CTABLE!! - 1] < MaxCleaning  
						 ){ TimeCleaned[CTABLE!! - 1] += CleaningInrement  
						delay(500) 
						forward("tableDirty", "tableDirty($CTABLE)" ,"waiter" ) 
						}
						else
						 { 
						 				wJson.setArrival("")
						 				wJson.setTableDirty(false)	
						 				//We use to return null if there are no more clients waiting
						 				wJson.setClientID(clientQueue.poll())	
						 				LastCleanedTable = 0
						 updateResourceRep( wJson.toJson()  
						 )
						 solve("cleanTable($CTABLE)","") //set resVar	
						 }
					}
					 transition( edgeName="goto",targetState="listening", cond=doswitch() )
				}	 
				state("getDrink") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("orderReady(TEA,TABLE,CID)"), Term.createTerm("orderReady(TEA,TABLE,CID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												CDRINK = payloadArg(0).toString()
												CTABLE = payloadArg(1).toString().toInt()
												CCID =  payloadArg(2).toString()
						}
						println("WAITER | going to barman... ")
						request("moveForTask", "moveForTask(barman,1)" ,"waiterwalker" )  
						solve("changeWaiterState(moving)","") //set resVar	
						
									wJson.setBusy(true)
									wJson.setTable(CTABLE)
									wJson.setReceivedRequest("drinkReady")
									wJson.setOrder(CDRINK)
									wJson.setMovingTo("barman")	
									wJson.setClientID(CCID)					
						updateResourceRep( wJson.toJson()  
						)
					}
					 transition(edgeName="t023",targetState="bringDrinkToClient",cond=whenReply("movementDone"))
					transition(edgeName="t024",targetState="error",cond=whenReply("walkbreak"))
				}	 
				state("bringDrinkToClient") { //this:State
					action { //it:State
						println("WAITER | taking $CDRINK... ")
						println("WAITER | GOING TO CLIENT $CCID table $CTABLE... ")
						
									wJson.setMovingFrom("barman")	
									wJson.setMovingTo("table " + CTABLE)					
						updateResourceRep( wJson.toJson()  
						)
						request("moveForTask", "moveForTask(teatable,$CTABLE)" ,"waiterwalker" )  
					}
					 transition(edgeName="t025",targetState="leaveDrinkAtTable",cond=whenReply("movementDone"))
					transition(edgeName="t026",targetState="error",cond=whenReply("walkbreak"))
				}	 
				state("leaveDrinkAtTable") { //this:State
					action { //it:State
						println("WAITER | GIVING the CLIENT the tea")
						
									wJson.setMovingFrom("")	
									wJson.setMovingTo("")
									wJson.setArrival("table " + CTABLE)					
									wJson.setWaitTime(MaxStayTime)
						updateResourceRep( wJson.toJson()  
						)
						emit("deliver", "deliver(tea,$CTABLE)" ) 
					}
					 transition( edgeName="goto",targetState="listening", cond=doswitch() )
				}	 
				state("error") { //this:State
					action { //it:State
						
									wJson.reset()
									wJson.setArrival("error")
						updateResourceRep( wJson.toJson()  
						)
						println("&&& WAITER | an error occurred while walking. ")
					}
					 transition( edgeName="goto",targetState="listening", cond=doswitch() )
				}	 
			}
		}
}
