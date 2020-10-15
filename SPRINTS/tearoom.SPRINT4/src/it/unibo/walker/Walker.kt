/* Generated by AN DISI Unibo */ 
package it.unibo.walker

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Walker ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 
				var XT = "0"
				var YT = "0"
				var CurrentPlannedMove 	= ""
				var StepTime    	   	= 355L
				var obstacleFound      	= false  
				
				val inmapname			= "teaRoomExplored"
				val PauseTime          	= 250L 
				val BackTime           	= 2 * StepTime / 3 
				
				val jobj = json.WalkerJson()		
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("walker | INIT")
						itunibo.planner.plannerUtil.initAI(  )
						itunibo.planner.plannerUtil.loadRoomMap( inmapname  )
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						forward("cmd", "cmd(l)" ,"basicrobot" ) 
						delay(500) 
						forward("cmd", "cmd(r)" ,"basicrobot" ) 
						delay(500) 
						emit("walkerstarted", "walkerstarted(0)" ) 
						 
							    	var Curx = itunibo.planner.plannerUtil.getPosX()
							       	var Cury = itunibo.planner.plannerUtil.getPosY()	
							       	jobj.setPosition(Curx,Cury)
						updateResourceRep(jobj.toJson() 
						)
						println("walker | STARTS")
					}
					 transition( edgeName="goto",targetState="waitReq", cond=doswitch() )
				}	 
				state("waitReq") { //this:State
					action { //it:State
						println("walker | waiting for a doPlan message")
					}
					 transition(edgeName="t00",targetState="solveReq",cond=whenRequest("doPlan"))
				}	 
				state("solveReq") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("doPlan(X,Y)"), Term.createTerm("doPlan(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												XT = payloadArg(0).toString()
												YT = payloadArg(1).toString()
						}
						println("walker | MOVING to ($XT,$YT)")
						itunibo.planner.plannerUtil.planForGoal( "$XT", "$YT"  )
					}
					 transition( edgeName="goto",targetState="execPlannedMoves", cond=doswitchGuarded({ itunibo.planner.plannerUtil.existActions()  
					}) )
					transition( edgeName="goto",targetState="noPlan", cond=doswitchGuarded({! ( itunibo.planner.plannerUtil.existActions()  
					) }) )
				}	 
				state("noPlan") { //this:State
					action { //it:State
						println("walker | NO PLAN FOUND for MOVING to ($XT,$YT)")
						answer("doPlan", "walkerError", "walkerError($XT,$YT)"   )  
					}
					 transition( edgeName="goto",targetState="waitReq", cond=doswitch() )
				}	 
				state("execPlannedMoves") { //this:State
					action { //it:State
						  CurrentPlannedMove = itunibo.planner.plannerUtil.getNextPlannedMove()  
						delay(PauseTime)
					}
					 transition( edgeName="goto",targetState="wMove", cond=doswitchGuarded({ CurrentPlannedMove == "w"  
					}) )
					transition( edgeName="goto",targetState="otherPlannedMove", cond=doswitchGuarded({! ( CurrentPlannedMove == "w"  
					) }) )
				}	 
				state("wMove") { //this:State
					action { //it:State
						request("step", "step($StepTime)" ,"basicrobot" )  
					}
					 transition(edgeName="t01",targetState="stepDone",cond=whenReply("stepdone"))
					transition(edgeName="t02",targetState="stepFailed",cond=whenReply("stepfail"))
				}	 
				state("stepDone") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.updateMap( "w"  )
						 
							    	var Curx = itunibo.planner.plannerUtil.getPosX()
							       	var Cury = itunibo.planner.plannerUtil.getPosY()	
							       	jobj.setPosition(Curx,Cury)
						updateResourceRep(jobj.toJson() 
						)
					}
					 transition( edgeName="goto",targetState="execPlannedMoves", cond=doswitchGuarded({ CurrentPlannedMove.length > 0  
					}) )
					transition( edgeName="goto",targetState="sendSuccessAnswer", cond=doswitchGuarded({! ( CurrentPlannedMove.length > 0  
					) }) )
				}	 
				state("stepFailed") { //this:State
					action { //it:State
						 obstacleFound = true  
						println("waiterwalker | stepFailed")
						if( checkMsgContent( Term.createTerm("stepfail(DURATION,CAUSE)"), Term.createTerm("stepfail(DURATION,CAUSE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												val D = payloadArg(0).toLong()  ; 
												val Dt = Math.abs(StepTime-D); 
												val BackT = D/2 
								println("waiterwalker stepFail D= $D, BackTime = ${BackTime}")
								if(  D > BackTime  
								 ){forward("cmd", "cmd(s)" ,"basicrobot" ) 
								delay(BackT)
								forward("cmd", "cmd(h)" ,"basicrobot" ) 
								}
						}
						itunibo.planner.plannerUtil.updateMapObstacleOnCurrentDirection(  )
					}
					 transition( edgeName="goto",targetState="sendFailureAnswer", cond=doswitch() )
				}	 
				state("otherPlannedMove") { //this:State
					action { //it:State
						if(  CurrentPlannedMove == "l" || CurrentPlannedMove == "r"   
						 ){forward("cmd", "cmd($CurrentPlannedMove)" ,"basicrobot" ) 
						itunibo.planner.plannerUtil.updateMap( "$CurrentPlannedMove"  )
						}
					}
					 transition( edgeName="goto",targetState="execPlannedMoves", cond=doswitchGuarded({ CurrentPlannedMove.length > 0  
					}) )
					transition( edgeName="goto",targetState="sendSuccessAnswer", cond=doswitchGuarded({! ( CurrentPlannedMove.length > 0  
					) }) )
				}	 
				state("sendSuccessAnswer") { //this:State
					action { //it:State
						println("waiterwalker | POINT ($XT,$YT) REACHED")
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						answer("doPlan", "walkerDone", "walkerDone(ok)"   )  
					}
					 transition( edgeName="goto",targetState="waitReq", cond=doswitch() )
				}	 
				state("sendFailureAnswer") { //this:State
					action { //it:State
						println("waiterwalker | FAILS")
						 
							    	var Curx = itunibo.planner.plannerUtil.getPosX()
							       	var Cury = itunibo.planner.plannerUtil.getPosY()	
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						answer("doPlan", "walkerError", "walkerError($Curx,$Cury)"   )  
					}
					 transition( edgeName="goto",targetState="waitReq", cond=doswitch() )
				}	 
			}
		}
}
