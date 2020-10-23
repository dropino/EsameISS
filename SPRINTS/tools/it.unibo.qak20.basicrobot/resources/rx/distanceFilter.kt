package rx
 
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import alice.tuprolog.Term
import alice.tuprolog.Struct
import robotVirtual.virtualrobotSonarSupportActor

 
class distanceFilter (name : String ) : ActorBasic( name ) {
val LimitDistance = 10
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg: ApplMessage) {
		if( msg.msgId() != virtualrobotSonarSupportActor.eventId) return //AVOID to handle other events
		if( msg.msgSender() == name) return //AVOID to handle the event emitted by itself
  		
	elabData( msg )
 	}

 	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	  suspend fun elabData( msg: ApplMessage ){ //OPTIMISTIC
	println( "distanceFilter elaborating data of ${msg.msgContent()}"   ) 
 		val content  = (Term.createTerm( msg.msgContent() ) as Struct)
		val dist = content.getArg(0).toString()
		val collisionObj = content.getArg(1).toString()
//  		println("$tt $name |  data = $data ")
		var sendEvent = true;

		val Distance = Integer.parseInt( dist )
		if( Distance >= LimitDistance ) {
			sendEvent = false;
			println( "distanceFilter not sending event since $Distance >= $LimitDistance"   ) 
     	}

		if (sendEvent) {   
			println( "distanceFilter sending event obstacle($dist, $collisionObj)"   ) 

			val m1 = MsgUtil.buildEvent(name, "obstacle", "obstacle($dist, $collisionObj)")
				emitLocalStreamEvent( m1 )
		}
 	}
}