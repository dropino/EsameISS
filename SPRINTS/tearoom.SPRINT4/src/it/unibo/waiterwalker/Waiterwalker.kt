/* Generated by AN DISI Unibo */ 
package it.unibo.waiterwalker

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Waiterwalker ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				var XT = "0"
				var YT = "0"
				var TASK	= "" 
				var N		= ""
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("waiterwalker | INIT")
						solve("consult('waiterwalkerkb.pl')","") //set resVar	
						println("waiterwalker | STARTS")
						emit("waiterwalkerstarted", "waiterwalkerstarted(ok)" ) 
					}
					 transition(edgeName="t027",targetState="waitCmd",cond=whenEvent("walkerstarted"))
				}	 
				state("waitCmd") { //this:State
					action { //it:State
						println("waiterwalker | waiting for a moveForTask message")
					}
					 transition(edgeName="t028",targetState="locateObjective",cond=whenRequest("moveForTask"))
				}	 
				state("locateObjective") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("moveForTask(TASK,N)"), Term.createTerm("moveForTask(TASK,N)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												TASK = payloadArg(0).toString()
												N = payloadArg(1).toString()
								println("waiterwalker | task received: $TASK, jolly: $N")
								solve("pos($TASK,$N,X,Y)","") //set resVar	
								if( currentSolution.isSuccess() ) { 
													XT = getCurSol("X").toString()
													YT = getCurSol("Y").toString()
								}
								else
								{}
								println("waiterwalker | sending objective location ($XT,$YT) to walker")
								request("doPlan", "doPlan($XT,$YT)" ,"walker" )  
						}
					}
					 transition(edgeName="t129",targetState="movementCompleted",cond=whenReply("walkerDone"))
					transition(edgeName="t130",targetState="movementError",cond=whenReply("walkerError"))
				}	 
				state("movementCompleted") { //this:State
					action { //it:State
						println("waiterwalker | POINT ($XT,$YT) REACHED")
						answer("moveForTask", "movementDone", "movementDone(OK)"   )  
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("movementError") { //this:State
					action { //it:State
						println("waiterwalker | FAILS")
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("moveForTask(TASK,N)"), Term.createTerm("moveForTask(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												XT = payloadArg(0).toString()
												YT = payloadArg(1).toString()
								answer("moveForTask", "walkbreak", "walkbreak($XT,$YT)"   )  
						}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
			}
		}
}
