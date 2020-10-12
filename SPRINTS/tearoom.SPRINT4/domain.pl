%====================================================================================
% domain description   
%====================================================================================
context(ctxwalker, "localhost",  "TCP", "8050").
context(ctxbasicrobot, "localhost",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( waiterwalker, ctxwalker, "it.unibo.waiterwalker.Waiterwalker").
