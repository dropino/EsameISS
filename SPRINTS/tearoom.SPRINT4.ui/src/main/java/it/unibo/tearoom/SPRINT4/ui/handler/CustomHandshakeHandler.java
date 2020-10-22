package it.unibo.tearoom.SPRINT4.ui.handler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import it.unibo.tearoom.SPRINT4.ui.model.StompPrincipal;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {
	@Override
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
			Map<String, Object> attributes) {
		// generate user name by UUID
		return new StompPrincipal(UUID.randomUUID().toString());
	}
}
