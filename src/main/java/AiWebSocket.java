import AI.AI;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.*;

@WebSocket
public class AiWebSocket {
	private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

	private AI ai;
	private int howManyTablesReceived = 0;

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
		if (JsonConverter.ping(message)) {
			System.out.println("ping");
			return;
		}
		howManyTablesReceived++;
		updateAI();

		System.out.println("got a message @ " + new java.util.Date() + "\ntables received: " + howManyTablesReceived);

		handleTableSendTurnData(session, message);
		//handleData(session, message);
	}

	/**
	 * Instantiate AI if it is null.
	 * Change AI to better one if the game has progressed enough
	 */
	private void updateAI() {
		if (ai == null) {
			System.out.println("instantiated AI, difficulty of 1");
			ai = new AI(1, 1);
		} else if (howManyTablesReceived > 5 && ai.getDifficulty() < 2) {
			System.out.println("updated AI, difficulty is now 3");
			ai = new AI(1, 2);
		}
	}

	/**
	message = 2d array
	return = 2d array
	 */
	private void handleTable(Session session, String message) throws IOException {
		System.out.println("Got: " + JsonConverter.jsonify(JsonConverter.parseTable(message)) + "\n");
		session.getRemote().sendString(JsonConverter.jsonify(ai.move(JsonConverter.parseTable(message)).board));
	}

	/**
	message = 2d array
	return = turn data
	*/
	private void handleTableSendTurnData(Session session, String message) throws IOException {
		System.out.println("Got: " + JsonConverter.jsonify(JsonConverter.parseTable(message)) + "\n");
		session.getRemote().sendString(JsonConverter.jsonify(ai.move(JsonConverter.parseTable(message))));
	}

	/**
	message = turn data
	return = turn data
	*/
	private void handleData(Session session, String message) throws IOException {
		System.out.println("Got: " + JsonConverter.jsonify(JsonConverter.parseMessage(message)) + "\n");
		session.getRemote().sendString(JsonConverter.jsonify(ai.move(JsonConverter.parseMessage(message).board)));
	}
}
