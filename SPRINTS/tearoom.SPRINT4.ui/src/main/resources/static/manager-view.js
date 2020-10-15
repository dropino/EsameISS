
// CONNECTION
var stompClient = null;


function updateView(msg) {

}


//as soon as we load the page, we send a request to the Server to load the information gathered up until now
function initialSetup() {
    stompClient.send("/app/waiter", {}, JSON.stringify({'name': 'update'}));
}

function connect() {

    var socket = new SockJS( '/it-unibo-iss');
    stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function (frame) {
    	try {
    	stompClient.subscribe('/topic/manager', handleWaiterReply);
    	initialSetup();
    	} catch (error) {
    		console.log(error);
    	}
    });
    
}