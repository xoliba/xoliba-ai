import AI.AI;
import Game.TurnData;
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

	//todo we should have an ai for every single session differently to prevent bugs from race-con etc
	@OnWebSocketMessage
	public void message(Session session, String message) throws IOException {
		if (JsonConverter.ping(message)) {
			System.out.println("ping");
		} else if (JsonConverter.startRound(message)) {
			handleStartingRound(session, message);
		} else {
			handleNormalTurn(session, message);
		}
	}

	//we get completely new board, we should decide if we surrender or not
	private void handleStartingRound(Session session, String message) throws IOException {
		TurnData data = JsonConverter.parseTurnData(message);

		System.out.println("got a start round message! AI color " + data.color + " difficulty " + data.difficulty);
		ai = new AI(data.color, data.difficulty);
		data = new TurnData(true, ai.doesWantToSurrender(data.board), data.color);

		session.getRemote().sendString(JsonConverter.jsonifyTurnData(data));
	}

	private void handleNormalTurn(Session session, String message) throws IOException {
		howManyTablesReceived++;
		TurnData data = JsonConverter.parseTurnData(message);
		//updateAI(data);

		System.out.println("got a message @ " + new java.util.Date() + "\ntables received: " + howManyTablesReceived);
		long s = System.nanoTime();

		if (ai.doesWantToStopPlaying(data)) {
			data = new TurnData(false, true);
		} else {
			data = ai.move(data.board, data.color, data.difficulty); //lets update the turn data with AIs move
		}
		session.getRemote().sendString(JsonConverter.jsonifyTurnData(data));

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
}
