%===========================================
% tearoomkb.pl
%===========================================
 
maxStayTime(20000).
maxWaitTime(120000).
timeToGoHome(15000).

clientRequestPayload(order, ok).
clientRequestPayload(pay, 5).

%% ------------------------------------------ 
%% Teatables
	%% busy
	%% free		(not busy but not clean)
	%% dirty	(not clean)
	%% clean	(not dirty)
	%% available (free and clean)	
%% ------------------------------------------ 
teatable( 1, available ).
teatable( 2, available ).

numavailabletables(N) :-
	findall( N,teatable( N,available ), NList),
	%% stdout <- println( tearoomkb_numavailabletables(NList) ),
	length(NList,N).

engageTable(N,CID)	 :-
	%%stdout <- println( tearoomkb_engageTable(N) ),
	retract( teatable( N, available ) ),
	!,
	assert( teatable( N, busy(CID) ) ).
engageTable(_,_).	

tableavailable(N):- teatable(N,	available ).

dirtyTable(N, CID)	 :-
	%% stdout <- println( tearoomkb_dirtyTable(N) ),
	retract( teatable( N, busy(CID) ) ),
	!,
	assert( teatable( N, dirty ) ).
dirtyTable(N, CID). 
	
cleanTable(N)	 :-
	%% stdout <- println( tearoomkb_cleanTable(N) ),
	retract( teatable( N, dirty ) ),
	!,
	assert( teatable( N, clean ) ).
cleanTable(N).	
 
stateOfTeatables( [teatable1(V1),teatable2(V2)] ) :-
	teatable( 1, V1 ),
	teatable( 2, V2 ).

%% ------------------------------------------ 
%% Waiter
	%%  athome
	%%	serving( CLIENTID )
	%%	movingto( cell(X,Y) )
	%%	cleaning( table(N) )
%% ------------------------------------------ 

waiter( athome ).	

changeWaiterState( STATE, NEWSTATE )	 :-
	%%stdout <- println( tearoomkb_changeWaiterState( STATE, NEWSTATE ) ),
	retract( waiter( STATE ) ),
	!,
	assert( waiter( NEWSTATE ) ).
changeWaiterState(_).

%% ------------------------------------------ 
%% Room as a whole
%% ------------------------------------------ 
roomstate(  state( waiter(S), tables(V) )  ):-
	 waiter(S), stateOfTeatables(V) .