
var btnState = "calling1";

var reqID = -1;

function connect() {
    var socket = new SockJS('/it-unibo-iss');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/display', handleWaiterReply);
    });
}

function initialSetup() {
	
	stompClient.send("/app/waiter", {}, {"name": "1", "payload":""});
	if (reqID == 1) {
		showWaitMessage();
	}
}

function handleWaiterReply(msg) {
	if (reqID == 1) {
		$( "#title" ).text('Ready to order?');
	    $( "#caption" ).show();
	    $( "#caption" ).text('Call the Waiter by clicking the button below.');
	    $( "#btn-waiter" ).show();	    
	}
}

function showWaitMessage() {
	$( "#title" ).text('Please, wait...');
    $( "#caption" ).show();
    $( "#caption" ).text('The waiter will arrive as soon as possible.');
    $( "#btn-waiter" ).hide();
}

$(document).on("click", "#btn-waiter", function(event) {

    console.log("sending Waiter request");
    stompClient.send("/app/waiter");
});

$( "#btn-waiter" ).click(function() { 

    if (btnState === "goodbye") {
        $( "#title" ).text('So long, and thanks for all the tea!');
        $( this ).hide();
        return;
    }

    if (btnState === "order") {
        $( "#title" ).text('Thanks for your order!');
        $( "#caption" ).show();
        $( "#caption" ).text('It will get to you soon.');
        $( "#txt-input" ).hide();
        $( this ).hide();
    }

    if (btnState === "calling1" || btnState === "calling2") {
        $( "#title" ).text('Please, wait...');
        $( "#caption" ).text('The waiter will arrive as soon as possible.');
        $( this ).hide();
    }

    setup();

 });

function setup() {
    if(btnState === "calling1") {
        setTimeout(function(){
            $( "#title" ).text('What would you like to order?');
            $( "#caption" ).hide();
            $( "#txt-input" ).show();
            $( "#btn-waiter" ).show();
            btnState = "order";
         }, 3000);
    }

    if(btnState === "order") {
        setTimeout(function(){
            $( "#title" ).text('Enjoy your tea!');
            $( "#caption" ).show();
            $( "#caption" ).text("When you're ready to pay, call the Waiter by clicking the button below.");
            $( "#txt-input" ).hide();
            $( "#btn-waiter" ).show();
            btnState = "calling2";
         }, 3000);
    }

    if(btnState === "calling2") {
        setTimeout(function(){
            $( "#title" ).text("Your bill is 7.45$");
            $( "#caption" ).hide();
            $( "#txt-input" ).hide();
            $( "#btn-waiter" ).show();
            btnState = "goodbye";
         }, 3000);
    }
}