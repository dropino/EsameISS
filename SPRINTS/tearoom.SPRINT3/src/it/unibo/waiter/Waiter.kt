/* Generated by AN DISI Unibo */ 
package it.unibo.waiter

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
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
				
				var Ntables			= 0
				
				var CurST			= ""
				var PL				= ""
				var Dest			= ""
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("WAITER | INIT ")
						discardMessages = false
						solve("consult('tearoomkb.pl')","") //set resVar	
						println("WAITER | solved tearoomkb.pl ")
					}
					 transition(edgeName="t00",targetState="listening",cond=whenEvent("waiterwalkerstarted"))
				}	 
				state("listening") { //this:State
					action { //it:State
						
						  				CCID = ""
						  				CTABLE = 0
						println("WAITER | listening... ")
						updateResourceRep("listening" 
						)
						stateTimer = TimerActor("timer_listening", 
							scope, context!!, "local_tout_waiter_listening", 5000.toLong() )
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
						if(  CurST != "athome"  
						 ){request("moveForTask", "moveForTask(home,1)" ,"waiterwalker" )  
						solve("changeWaiterState(athome)","") //set resVar	
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
						solve("numavailabletables(N)","") //set resVar	
						if( currentSolution.isSuccess() ) { Ntables = getCurSol("N").toString().toInt()   
						println("WAITER | numavailabletables=$Ntables")
						}
						else
						{}
						if(  Ntables != 0  
						 ){answer("waitTime", "wait", "wait(0)"   )  
						}
						else
						 {answer("waitTime", "wait", "wait(20000)"   )  
						 }
					}
					 transition( edgeName="goto",targetState="listening", cond=doswitch() )
				}	 
				state("handleDeploy") { //this:State
					action { //it:State
						println("WAITER | handling simclient request... ")
						if( checkMsgContent( Term.createTerm("deploy(FROM,TO,CID)"), Term.createTerm("deploy(FROM,TO,CID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
								  				CTABLE = payloadArg(0).toString().toIntOrNull()
								  				Dest = payloadArg(1).toString()
								  				CCID = payloadArg(2).toString()
						}
					}
					 transition( edgeName="goto",targetState="goToEntrance", cond=doswitchGuarded({ Dest == "table"  
					}) )
					transition( edgeName="goto",targetState="deployClientExit", cond=doswitchGuarded({! ( Dest == "table"  
					) }) )
				}	 
				state("goToEntrance") { //this:State
					action { //it:State
						println("WAITER | GOING to ENTRANCE door ")
						request("moveForTask", "moveForTask(entrancedoor,1)" ,"waiterwalker" )  
						solve("changeWaiterState(moving)","") //set resVar	
					}
					 transition(edgeName="t010",targetState="deployClientEntrance",cond=whenReply("movementDone"))
					transition(edgeName="t011",targetState="error",cond=whenReply("walkbreak"))
				}	 
				state("deployClientEntrance") { //this:State
					action { //it:State
						updateResourceRep( "waiter_arrived"  
						)
						println("WAITER | DEPLOYING simclient $CCID to table $CTABLE")
						solve("tableavailable(N)","") //set resVar	
						if( currentSolution.isSuccess() ) { CTABLE = getCurSol("N").toString().toInt()   
						println("WAITER | tableavailable=$CTABLE")
						solve("engageTable($CTABLE,$CCID)","") //set resVar	
						}
						else
						{}
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
					}
					 transition(edgeName="t014",targetState="confirmClientArrival",cond=whenReply("movementDone"))
					transition(edgeName="t015",targetState="error",cond=whenReply("walkbreak"))
				}	 
				state("confirmClientArrival") { //this:State
					action { //it:State
						updateResourceRep( "waiter_rdy_leave"  
						)
						println("WAITER | SENDING CONFIRMATION to simclient $CCID: arrived $Dest")
						answer("deploy", "arrived", "arrived($CTABLE)"   )  
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
						}
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
						answer("clientRequest", "atTable", "atTable($PL)"   )  
					}
					 transition(edgeName="t018",targetState="transferOrder",cond=whenDispatch("order"))
					transition(edgeName="t019",targetState="listening",cond=whenDispatch("pay"))
				}	 
				state("transferOrder") { //this:State
					action { //it:State
						println("WAITER | sending order to barman... ")
						if( checkMsgContent( Term.createTerm("order(TEA)"), Term.createTerm("order(DRINK)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								  CDRINK	= payloadArg(0).toString()  
						}
						forward("sendOrder", "sendOrder($CDRINK,$CTABLE)" ,"barman" ) 
					}
					 transition( edgeName="goto",targetState="listening", cond=doswitch() )
				}	 
				state("cleanTable") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("tableDirty(N)"), Term.createTerm("tableDirty(N)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								  CTABLE = payloadArg(0).toString().toInt()  
						}
						println("WAITER | going to table for CLEANING $CTABLE... ")
						request("moveForTask", "moveForTask(teatable,$CTABLE)" ,"waiterwalker" )  
						solve("changeWaiterState(moving)","") //set resVar	
					}
					 transition(edgeName="t020",targetState="atTableToClean",cond=whenReply("movementDone"))
					transition(edgeName="t021",targetState="error",cond=whenReply("walkbreak"))
				}	 
				state("atTableToClean") { //this:State
					action { //it:State
						println("WAITER | cleaning table $CTABLE... ")
						delay(5000) 
						solve("cleanTable($CTABLE)","") //set resVar	
					}
					 transition( edgeName="goto",targetState="listening", cond=doswitch() )
				}	 
				state("getDrink") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("orderReady(TEA,TABLE)"), Term.createTerm("orderReady(TEA,TABLE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 CTABLE = payloadArg(1).toString().toInt()  
						}
						println("WAITER | going to barman... ")
						request("moveForTask", "moveForTask(barman,1)" ,"waiterwalker" )  
						solve("changeWaiterState(moving)","") //set resVar	
					}
					 transition(edgeName="t022",targetState="bringDrinkToClient",cond=whenReply("movementDone"))
					transition(edgeName="t023",targetState="error",cond=whenReply("walkbreak"))
				}	 
				state("bringDrinkToClient") { //this:State
					action { //it:State
						updateResourceRep( "waiter_rdy_getDrink"  
						)
						println("WAITER | taking tea... ")
						println("WAITER | GOING TO CLIENT table $CTABLE... ")
						request("moveForTask", "moveForTask(teatable,$CTABLE)" ,"waiterwalker" )  
					}
					 transition(edgeName="t024",targetState="leaveDrinkAtTable",cond=whenReply("movementDone"))
					transition(edgeName="t025",targetState="error",cond=whenReply("walkbreak"))
				}	 
				state("leaveDrinkAtTable") { //this:State
					action { //it:State
						println("WAITER | giving the simclient the tea")
						updateResourceRep( "waiter_rdy_bringDrink"  
						)
						emit("deliver", "deliver(tea,$CTABLE)" ) 
					}
					 transition( edgeName="goto",targetState="listening", cond=doswitch() )
				}	 
				state("error") { //this:State
					action { //it:State
						println("&&& WAITER | an error occurred while walking. ")
					}
					 transition( edgeName="goto",targetState="listening", cond=doswitch() )
				}	 
			}
		}
}
