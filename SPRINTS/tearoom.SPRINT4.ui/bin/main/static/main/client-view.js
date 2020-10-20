
// CONNECTION
var stompClient = null;
var time = false;


function showMsg(message) {
    console.log(message );
//    $("#applmsgs").html( "<pre>"+message.replace(/\n/g,"<br/>")+"</pre>" );
        //$("#applmsgintable").append("<tr><td>" + message + "</td></tr>");
}

function startTimer(duration, display) {
    console.log("Timer started");
    time = true;

    var timer = duration, minutes, seconds;
    setInterval(function () {
        minutes = parseInt(timer / 60, 10);
        seconds = parseInt(timer % 60, 10);

        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        display.text(minutes + ":" + seconds);

        if (--timer < 0) {
        	console.log("TIMER EXPIRED " + timer);
        	var url = new URL(window.location.href + "/badreq");
        	url.searchParams.append('timer', true);
        	url.searchParams.append('badtemp', false);
        	window.location.assign(url);
        }
    }, 1000);
}

function handleSmartbellReply(msg) {
	var redir = JSON.parse(msg.body).redir;
  	var CID = JSON.parse(msg.body).payload0;
    var ttw = JSON.parse(msg.body).payload1;
    
    console.log(msg.body);
    
    if (ttw == 0)    {
    	var url = new URL(window.location.href + redir);
    	
    	if (CID != 0) {
    		url.searchParams.append('cid', CID);
    	}
    	else {
    		url.searchParams.append('timer', false);
        	url.searchParams.append('badtemp', true);
    	}
    	
    	stompClient.disconnect(function(){
    	    console.log("disconnected from stompClient");
    	});
    	
    	window.location.assign(url);
    }
    else if (time == false) {
	    console.log("Client has to wait");
        $( "#title" ).text("The tearoom is currently full :(");
        $( "#caption" ).text("If a table frees up in the following " + ttw/1000 + " seconds the waiter will come and get you. Otherwiase we hope to see you again another time!");
	    $( "#btn-smartbell" ).hide();
        $( "#h-countdown" ).show();
        $( "#h-countdown" ).text("Time before you have to go:");
        $( "#countdown" ).show();
		$( "#clickOnce").hide();
        startTimer(ttw/1000, $( "#countdown" ));
    }
}

function connect() {
	
    var socket = new SockJS('/it-unibo-iss');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/user/queue/main', handleSmartbellReply);
    }, onConnectionError);
}

function onConnectionError(msg) {
	$("#connection-error").show();
}

function intialSetup() {
	$( "#h-countdown" ).hide();
	$( "#countdown" ).hide();
	$( "#connection-error").hide();
	$( "#clickOnce").hide();
}


$(document).on("click", "#btn-smartbell", function(event) {

    console.log("sending Smartbell request");
    stompClient.send("/app/smartbell");
	$( "#btn-smartbell").hide();
	$( "#clickOnce").show();
});