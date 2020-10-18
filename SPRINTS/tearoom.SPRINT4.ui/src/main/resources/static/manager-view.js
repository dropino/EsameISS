
// CONNECTION
var stompClient = null;


function updateView(msg) {
	var sender = JSON.parse(msg.body).sender;

	if (sender == "waiter") {
		waiter(msg);
	} else if (sender == "smartbell") {
		smartbell(msg);
	} else if (sender == "barman") {
		barman(msg);
	}
}


//as soon as we load the page, we send a request to the Server to load the information gathered up until now
function initialSetup() {
    stompClient.send("/app/manager", {}, JSON.stringify({'name': 'update'}));
	$( "#connection-error").hide();
}

function connect() {
	$("#connection-error").hide();
    var socket = new SockJS( '/it-unibo-iss');
    stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function (frame) {
    	try {
    	stompClient.subscribe('/topic/manager', updateView);
    	initialSetup();
    	} catch (error) {
    		console.log(error);
    	}
    }, onConnectionError);
}

function onConnectionError(msg) {
	$("#connection-error").show();
}

function waiter (msg) {
	$( "#freeTables" ).text(JSON.parse(msg.body).freeTables);
	$( "#deployedToTable" ).text(JSON.parse(msg.body).deployedToTable);
	$( "#teasDelivered" ).text(JSON.parse(msg.body).teasDelivered);
	$( "#dirtyTables" ).text(JSON.parse(msg.body).dirtyTables);
	$( "#deployedToExit" ).text(JSON.parse(msg.body).deployedToExit);
	$( "#earnings" ).text(JSON.parse(msg.body).earnings);
	$( "#waiterCurrentTask" ).text(JSON.parse(msg.body).currentTask);
	$( "#waiterCurrentMovement" ).text(JSON.parse(msg.body).currentMovement);
	$( "#waiterCurrentPosition" ).text(JSON.parse(msg.body).positionX + ", " + JSON.parse(msg.body).positionY);
}

function barman (msg) {
	$( "#ordersReceived" ).text(JSON.parse(msg.body).ordersReceived);
	$( "#teasPreared" ).text(JSON.parse(msg.body).teasPreared);
	$( "#teasReady" ).text(JSON.parse(msg.body).teasReady);
	$( "#barmanCurrentTask" ).text(JSON.parse(msg.body).currentTask);
}

function smartbell (msg) {
	$( "#clientsProcessed" ).text(JSON.parse(msg.body).clientsProcessed);
	$( "#clientsAdmitted" ).text(JSON.parse(msg.body).clientsAdmitted);
	$( "#clientsWaiting" ).text(JSON.parse(msg.body).clientsWaiting);
	$( "#clientsWaitedLong" ).text(JSON.parse(msg.body).clientsWaitedLong);
	$( "#smartbellCurrentTask" ).text(JSON.parse(msg.body).currentTask);
}
