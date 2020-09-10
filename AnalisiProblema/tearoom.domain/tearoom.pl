%====================================================================================
% tearoom description   
%====================================================================================
context(ctxtearoom, "localhost",  "TCP", "8072").
 qactor( client, ctxtearoom, "it.unibo.client.Client").
  qactor( waiter, ctxtearoom, "it.unibo.waiter.Waiter").
  qactor( smartbell, ctxtearoom, "it.unibo.smartbell.Smartbell").
  qactor( barman, ctxtearoom, "it.unibo.barman.Barman").
  qactor( waiterwalker, ctxtearoom, "it.unibo.waiterwalker.Waiterwalker").
