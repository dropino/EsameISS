%===========================================
% waiterwalkerkb.pl
%===========================================

%% ------------------------------------------ 
%% Positions
%% ------------------------------------------ 
pos( home, 1, 0, 0 ).
pos( teatable, 1, 2, 2 ).
pos( teatable, 2, 4, 2 ).
pos( entrancedoor, 1, 0, 4 ).
pos( exitdoor, 1, 5, 4 ).
pos( barman, 1, 4, 0 ).
pos( barman, 2, 5, 0 ).

corrPos(gateIn, downDir, 0, 4).

corrPos(table1, upDir, 2, 4).
corrPos(table1, downDir, 2, 2).
corrPos(table1, rightDir, 1, 3).
corrPos(table1, leftDir, 3, 3).
corrPos(table2, upDir, 4, 4).
corrPos(table2, downDir, 4, 2).
corrPos(table2, rightDir, 3, 3).
corrPos(table2, leftDir, 5, 3).
corrPos(wallUp, upDir, -1, 0).
corrPos(wallDown, downDir, -1, 4).
corrPos(wallLeft, leftDir, 0, -1).
corrPos(wallRight, rightDir, 4, -1).