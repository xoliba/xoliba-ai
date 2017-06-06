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

		//System.out.println("Got: " + JsonConverter.jsonify(JsonConverter.parseMessage(message)));

		System.out.println("got a message @ " + new java.util.Date());

		handleTableSendTurnData(session, message);
		//handleData(session, message);
	}

	/**
	message = 2d array
	return = 2d array
	 */
	private void handleTable(Session session, String message) throws IOException {
		System.out.println("Got: " + JsonConverter.jsonify(JsonConverter.parseTable(message)) + "\n");

		AI ai = new AI(1);
		session.getRemote().sendString(JsonConverter.jsonify(ai.move(JsonConverter.parseTable(message)).board));
	}

	/**
	message = 2d array
	return = turn data
	*/
	private void handleTableSendTurnData(Session session, String message) throws IOException {
		System.out.println("Got: " + JsonConverter.jsonify(JsonConverter.parseTable(message)) + "\n");

		AI ai = new AI(1);
		session.getRemote().sendString(JsonConverter.jsonify(ai.move(JsonConverter.parseTable(message))));
	}

	/**
	message = turn data
	return = turn data
	*/
	private void handleData(Session session, String message) throws IOException {
		System.out.println("Got: " + JsonConverter.jsonify(JsonConverter.parseMessage(message)) + "\n");

        AI ai = new AI(1);
		session.getRemote().sendString(JsonConverter.jsonify(ai.move(JsonConverter.parseMessage(message).board)));
	}
}
