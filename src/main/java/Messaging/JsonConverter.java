package Messaging;

import AI.ParametersAI;
import Game.TurnData;
import com.google.gson.Gson;

public class JsonConverter {
	private static Gson gson = new Gson();
	
	public static int[][] parseTable(String message) {
		try {
			int[][] t = gson.fromJson(message, int[][].class);
			return t;
		} catch (Exception e) {
			System.out.println("EXCEPTION @ Messaging.JsonConverter parseTable");
			return new int[0][0];
		}
	}

	public static String jsonifyTable(int[][] table) {
		return gson.toJson(table);
	}

	public static ParametersAI parseParameters(String message) {
		return gson.fromJson(message, ParametersAI.class);
	}

	public static String jsonifyParameters(ParametersAI p) {
		return gson.toJson(p);
	}

	public static String jsonifyTurnData(TurnData data) {
		return gson.toJson(data);
	}


	public static boolean ping(String message) {
		try {
			TurnData msg = parseTurnData(message);
			return msg.type.equals("ping");
		} catch (Exception e) {
			System.out.println("THIS SHOULD NEVER HAPPEN: EXCEPTION @ Messaging.JsonConverter ping");
			return false;
		}
	}
	
	public static TurnData parseTurnData(String message) {
		return gson.fromJson(message, TurnData.class);
	}

	public static boolean startRound(String message) {
		try {
			TurnData msg = parseTurnData(message);
			return msg.type.equals("startRound");
		} catch (Exception e) {
			System.out.println("THIS SHOULD NEVER HAPPEN: EXCEPTION @ Messaging.JsonConverter startRound");
			return false;
		}
	}
}
