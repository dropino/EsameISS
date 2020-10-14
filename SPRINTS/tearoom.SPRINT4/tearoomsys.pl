%====================================================================================
% tearoomsys description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/basicrobot").
context(ctxtearoom, "localhost",  "TCP", "8072").
context(ctxsmartbell, "localhost",  "TCP", "8071").
context(ctxbarman, "localhost",  "TCP", "8070").
 qactor( walker, ctxtearoom, "it.unibo.walker.Walker").
  qactor( waiter, ctxtearoom, "it.unibo.waiter.Waiter").
  qactor( waiterwalker, ctxtearoom, "it.unibo.waiterwalker.Waiterwalker").
  qactor( smartbell, ctxsmartbell, "it.unibo.smartbell.Smartbell").
  qactor( barman, ctxbarman, "it.unibo.barman.Barman").
msglogging.
