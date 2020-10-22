%====================================================================================
% clientsys description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/basicrobot").
context(ctxclient, "localhost",  "TCP", "8071").
context(ctxtearoom, "localhost",  "TCP", "8072").
 qactor( smartbell, ctxtearoom, "external").
  qactor( waiter, ctxtearoom, "external").
  qactor( simclient, ctxclient, "it.unibo.simclient.Simclient").
