
// CONNECTION
var stompClient = null;

function showMsg(message) {
    console.log(message );
//    $("#applmsgs").html( "<pre>"+message.replace(/\n/g,"<br/>")+"</pre>" );
        //$("#applmsgintable").append("<tr><td>" + message + "</td></tr>");
}

function startTimer(duration, display) {
    var timer = duration, minutes, seconds;
    setInterval(function () {
        minutes = parseInt(timer / 60, 10);
        seconds = parseInt(timer % 60, 10);

        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        display.text(minutes + ":" + seconds);

        if (--timer < 0) {
        	window.location.replace("/badtemp");
        }
    }, 1000);
}

function handleSmartbellReply(msg) {
    var redir = JSON.parse(msg.body).redir;
    var CID = JSON.parse(msg.body).payload0;
    var ttw = JSON.parse(msg.body).payload1;
    if (ttw == 0)
    {
    	var url = new URL(window.location.href + redir)
    	
    	if (CID != 0) url.searchParams.append('cid', CID);
    	
    	stompClient.disconnect(function(){
    	    console.log("disconnected from stompClient");
    	});
    	
    	window.location.replace(url);
    }
    else {
        $( "#btn-waiter" ).hide();
        $( "#h-countdown" ).show();
        $( "#countdown" ).show();
        startTimer(ttw/1000, $( "#countdown" ));
    }
    
}

function connect() {
    var socket = new SockJS('/it-unibo-iss');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/display', handleSmartbellReply);
    });
}


$(document).on("click", "#btn-smartbell", function(event) {

    console.log("sending Smartbell request");
    stompClient.send("/app/smartbell");
});