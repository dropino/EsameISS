
// CONNECTION
var stompClient = null;

function showMsg(message) {
    console.log(message );
    $("#applmsgs").html( "<pre>"+message.replace(/\n/g,"<br/>")+"</pre>" );
        //$("#applmsgintable").append("<tr><td>" + message + "</td></tr>");
}

function connect() {
    var socket = new SockJS('/it-unibo-iss');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        // setConnected(true);
        stompClient.subscribe('/topic/display', function (msg) {
             showMsg(JSON.parse(msg.body).content);
        });
    });
}

$(document).on("click", "#btn-smartbell", function(event) {

    console.log("sending Smartbell request");
    stompClient.send("/app/smartbell");
});


var btnState = "calling1";


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