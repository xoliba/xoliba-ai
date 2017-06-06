import AI.AI;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

@WebSocket
public class AiWebSocket {
	private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

	@OnWebSocketConnect
	public void connected(Session session) {
		sessions.add(session);
		System.out.println("client connected");
	}

	@OnWebSocketClose
	public void closed(Session session, int statusCode, String reason) {
		sessions.remove(session);
	}
	
	@OnWebSocketMessage
	public void message(Session session, String message) throws IOException {
		/*
		if (JsonConverter.ping(message)) {
			System.out.println("ping");
			return;
		}*/
		System.out.println("Got a message!\n" + message);

		handleTable(session, message);
		//handleData(session, message);
	}

	private void handleTable(Session session, String message) throws IOException {
		System.out.println("Got: " + JsonConverter.jsonify(JsonConverter.parseTable(message)));

		AI ai = new AI(1);
		session.getRemote().sendString(JsonConverter.jsonify(ai.move(JsonConverter.parseTable(message))));
	}

	private void handleData(Session session, String message) throws IOException {
		System.out.println("Got: " + JsonConverter.jsonify(JsonConverter.parseMessage(message)));
		
        AI ai = new AI(1);
		session.getRemote().sendString(JsonConverter.jsonify(ai.move(JsonConverter.parseMessage(message).board)));
	}
}
