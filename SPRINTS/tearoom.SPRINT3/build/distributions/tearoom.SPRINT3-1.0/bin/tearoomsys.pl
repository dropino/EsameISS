%====================================================================================
% tearoomsys description   
%====================================================================================
%====mqttBroker("localhost", "1883", "unibo/qak/tearoom").
context(ctxtearoom, "localhost",  "TCP", "8072").
 qactor( walker, ctxtearoom, "it.unibo.walker.Walker").
  qactor( waiter, ctxtearoom, "it.unibo.waiter.Waiter").
  qactor( waiterwalker, ctxtearoom, "it.unibo.waiterwalker.Waiterwalker").
  qactor( smartbell, ctxtearoom, "it.unibo.smartbell.Smartbell").
  qactor( barman, ctxtearoom, "it.unibo.barman.Barman").
