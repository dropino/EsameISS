%====================================================================================
% tearoom description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/polar").
context(ctxtearoom, "localhost",  "TCP", "8072").
 qactor( waiterwalker, ctxtearoom, "it.unibo.waiterwalker.Waiterwalker").
  qactor( client, ctxtearoom, "it.unibo.client.Client").
  qactor( waiter, ctxtearoom, "it.unibo.waiter.Waiter").
  qactor( smartbell, ctxtearoom, "it.unibo.smartbell.Smartbell").
  qactor( barman, ctxtearoom, "it.unibo.barman.Barman").
