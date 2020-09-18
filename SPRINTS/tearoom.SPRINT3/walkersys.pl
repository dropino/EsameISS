%====================================================================================
% walkersys description   
%====================================================================================
context(ctxwalker, "localhost",  "TCP", "8050").
context(ctxbasicrobot, "192.168.1.55",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( walker, ctxwalker, "it.unibo.walker.Walker").
tracing.
