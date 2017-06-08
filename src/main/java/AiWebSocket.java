import AI.AI;
import Game.TurnData;
import com.google.gson.JsonElement;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
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

		TurnData data = JsonConverter.parseTurnData(message);

		ai = new AI(data.color, 1); //so that ai always plays at the right color even if the websocket connection hasnt been reset
		//updateAI(data);

		System.out.println("got a message @ " + new java.util.Date() + "\ntables received: " + howManyTablesReceived);

		long s = System.nanoTime();
		data = ai.move(data.board); //lets update the turn data with AIs move
		session.getRemote().sendString(JsonConverter.jsonify(data.board));
		long e = System.nanoTime();
		System.out.println("It took AI " + (e - s) / 1e9 + " seconds to compute the move");
	}

	/**
	 * Instantiate AI if it is null.
	 * Change AI to better one if the game has progressed enough
	 */
	private void updateAI(TurnData data) {
		if (ai == null) {
			System.out.println("instantiated AI, difficulty of 1");
			ai = new AI(data.color, 1);
		} else if (howManyTablesReceived > 5 && ai.getDifficulty() < 2) {
			System.out.println("updated AI, difficulty is now 2");
			ai = new AI(data.color, 2);
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
		TurnData data = JsonConverter.parseTurnData(message);
		session.getRemote().sendString(JsonConverter.jsonify(ai.move(data.board)));
	}
}
