package Messaging;

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
			session.getRemote().sendString("{\"type\":\"pong\"}");
		} else if (JsonConverter.startRound(message)) {
			handleStartingRound(session, message);
		} else {
			handleNormalTurn(session, message);
		}
	}

	//we get completely new board, we should decide if we surrender or not
	private void handleStartingRound(Session session, String message) throws IOException {
		TurnData data = JsonConverter.parseTurnData(message);
		long msgId = data.msgId;

		System.out.println("got a start round message! AI color " + data.color + " difficulty " + data.difficulty);
		ai = new AI(data.color, data.difficulty);
		data = new TurnData(true, ai.doesWantToSurrender(data), data.color);

		data.msgId = msgId;
		System.out.println("Message id: " + data.msgId);
		session.getRemote().sendString(JsonConverter.jsonifyTurnData(data));
	}

	private void handleNormalTurn(Session session, String message) throws IOException {
		howManyTablesReceived++;
		TurnData data = JsonConverter.parseTurnData(message);
		long msgId = data.msgId;
		//updateAI(data);

		System.out.println("got a message @ " + new java.util.Date() + "\ntables received: " + howManyTablesReceived);
		long s = System.nanoTime();

		if (data.surrender) { //player wants to resign
			if (ai.doesWantToStopPlaying(data)) { //AI agrees
				data = new TurnData(false, true);
			} else {
				data = new TurnData(false, false);
			}
		} else {
			data = ai.move(data.board, data.color, data.difficulty, data.withoutHit); //lets update the turn data with AIs move
		}
		data.msgId = msgId;
		System.out.println("Message id: " + data.msgId);
		long e = System.nanoTime();
		while (e - s < 1000) {
			e = System.nanoTime();
		}
		session.getRemote().sendString(JsonConverter.jsonifyTurnData(data));

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
