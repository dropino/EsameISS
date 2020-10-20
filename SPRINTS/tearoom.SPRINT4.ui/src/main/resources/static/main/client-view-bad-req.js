const urlParams = new URLSearchParams(window.location.search);
var timer = urlParams.get('timer');
var badtemp = urlParams.get('badtemp');

if (timer == 'true') {
    $("#caption").text("Your timeout expired! We need to ask you to leave. Hope you will come back on a less busy day!");
}
else if (badtemp == 'true') {
	$("#caption").text("Your temperature is too high. Come back when you're feeling better!");
}