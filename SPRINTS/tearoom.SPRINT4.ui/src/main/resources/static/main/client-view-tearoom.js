
// CONNECTION
var stompClient = null;


//LOGIC
var Table	 = -1;
var ClientID = -1;
var Bill	 = -1;
var reqID ;

var timeout;

//when the waiter replies, the handling will be done by this function, previously bound to the subscribe /topic/display
//	deployEntrance->the waiter has brought the client from the entrance door to the table
//	serviceOrder->the waiter is at the table, ready to receive the order
//	delivery->the waiter has brought the tea to the table
//	servicePay->the waiter is at the table, ready to receive the payment
//	deployExit->the waiter has brought the Client from the table to the exit door
function handleWaiterReply(msg) {
	
	if (JSON.parse(msg.body).payload0 == 'success')
		return;
	
	if(reqID == 'pay') {
      $( "#title" ).text('So long, and thanks for all the tea!');
      $( "#caption" ).hide();
      $( "#txt-input" ).hide();
      $( "#btn-waiter" ).hide();
	}
	if(reqID == 'servicePay') {
		Bill = JSON.parse(msg.body).payload0;
      $( "#title" ).text("Your bill is " + Bill + "$");
      $( "#caption" ).hide();
      $( "#txt-input" ).hide();
      $( "#btn-waiter" ).show();
      reqID='pay'
	}
	if(reqID == 'delivery') {
      $( "#title" ).text('Enjoy your tea!');
      $( "#caption" ).show();
      $( "#caption" ).text("When you're ready to pay, call the Waiter by clicking the button below.");
      $( "#txt-input" ).hide();
      $( "#btn-waiter" ).show();
      $("#countdown").show();
		reqID = 'servicePay';
		startTimer(JSON.parse(msg.body).payload0/1000, $("#countdown") );
	}
	if(reqID == 'serviceOrder') {
      $( "#title" ).text('What would you like to order?');
      $( "#caption" ).hide();
      $( "#txt-input" ).show();
      $( "#btn-waiter" ).show();
      reqID = 'order';
	}
	if (reqID == 'deployEntrance') {
	    Table = JSON.parse(msg.body).payload0;
      $( "#table" ).text('Table: ' + Table);

		$( "#title" ).text('Ready to order?');
	    $( "#caption" ).show();
	    $( "#caption" ).text('Call the Waiter by clicking the button below.');
	    $( "#btn-waiter" ).show();
	    reqID = 'serviceOrder';
	}
}

function startTimer(duration, display) {
    console.log("Timer started");

    var timer = duration, minutes, seconds;
    timeout = setInterval(function () {
        minutes = parseInt(timer / 60, 10);
        seconds = parseInt(timer % 60, 10);

        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        display.text(minutes + ":" + seconds);

        if (--timer < 0) {
			maxStayTimeOver();
			reqID = 'servicePay';
			stompClient.send("/app/waiter", {}, JSON.stringify({'name': reqID, 'payload0': 'pay', 'payload1': Table, 'clientid': ClientID}));
        	clearInterval(timeout);
        }
		
    }, 1000);
}


//as soon as we load the page, we send a request to Waiter of type deploy=deploy(FROM=entrancedoor, TO=table, CID=ClientID)
//and wait for the waiter to get us to the table
function initialSetup() {
	const urlParams = new URLSearchParams(window.location.search);
	
	ClientID = urlParams.get('cid');
	reqID = 'deployEntrance';
    $( "#cid" ).text('Client ID: ' + ClientID);

	stompClient.send("/app/waiter", {}, JSON.stringify({'name': reqID, 'payload0': 'entrancedoor', 'payload1': 'table', 'clientid': ClientID}));
	
	showDeploymentMessage();
}

function connect() {

    var socket = new SockJS('/it-unibo-iss');
    stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function (frame) {
    	try {
    	stompClient.subscribe('/user/queue/tearoom', handleWaiterReply);
    	initialSetup();
    	} catch (error) {
    		console.log(error);
    	}
    }, onConnectionError);
    
}

function onConnectionError(msg) {
	$("#connection-error").show();
}

function hideAll() {
	$( "#title" ).hide();
    $( "#caption" ).hide();
    $( "#txt-input" ).hide();
    $( "#btn-waiter" ).hide();
    $( "#countdown" ).hide();
    $("#connection-error").hide();
}

function showWaitMessage() {
	$( "#title" ).show();
	$( "#title" ).text('Please, wait...');
    $( "#caption" ).show();
    $( "#caption" ).text('The waiter will arrive as soon as possible.');
    $( "#txt-input" ).hide();
    $( "#btn-waiter" ).hide();
    $("#countdown").hide();
}

function showDeploymentMessage() {
    $( "#title" ).show();
	$( "#title" ).text('Please, let the waiter escort you.');
    $( "#caption" ).hide();
    $( "#txt-input" ).hide();
    $( "#btn-waiter" ).hide();
}

function maxStayTimeOver() {
    $( "#title" ).show();
	$( "#title" ).text('Your maxStayTime is over');
    $( "#caption" ).show();
    $( "#caption" ).text('Please, pay the amount due once the waiter gets to your table.');
	$( "#btn-waiter" ).hide();
	 $("#countdown").hide();
}

$(document).on("click", "#btn-waiter", function(event) {
	
	if(reqID == 'pay') {
		stompClient.send("/app/waiter", {}, JSON.stringify({'name': reqID, 'payload0': Bill, 'payload1': Table, 'clientid': ClientID}));
		
        showDeploymentMessage();
	}
	if(reqID == 'servicePay') {
		clearInterval(timeout);
		stompClient.send("/app/waiter", {}, JSON.stringify({'name': reqID, 'payload0': 'pay', 'payload1': Table, 'clientid': ClientID}));
        showWaitMessage();
	}
	if(reqID == 'order') { //this will be a dispatch and the client will have to wait for the event "delivery"
        stompClient.send("/app/waiter", {}, JSON.stringify({'name': reqID, 'payload0': $( "#txt-input" ).val(), 'clientid': ClientID}));
        showWaitMessage();
        reqID='delivery';
	}
	if(reqID == 'serviceOrder') {
		stompClient.send("/app/waiter", {}, JSON.stringify({'name': reqID, 'payload0': 'order', 'payload1': Table, 'clientid': ClientID}));
        showWaitMessage();
	}
});