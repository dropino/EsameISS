%====================================================================================
% walkersys description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/basicrobot").
context(ctxwalker, "localhost",  "TCP", "8050").
context(ctxbasicrobot, "localhost",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( walker, ctxwalker, "it.unibo.walker.Walker").
