import Game.TurnData;
import com.google.gson.Gson;

public class JsonConverter {
	private static Gson gson = new Gson();
	
	public static int[][] parseTable(String message) {
		try {
			int[][] t = gson.fromJson(message, int[][].class);
			return t;
		} catch (Exception e) {
			System.out.println("EXCEPTION @ JsonConverter parseTable");
			return new int[0][0];
		}
	}
	
	public static String jsonifyTurnData(TurnData data) {
		return gson.toJson(data);
	}

	public static String jsonify(int[][] table) {
		return gson.toJson(table);
	}
	
	public static boolean ping(String message) {
		try {
			TurnData msg = parseTurnData(message);
			return msg.type.equals("ping");
		} catch (Exception e) {
			System.out.println("THIS SHOULD NEVER HAPPEN: EXCEPTION @ JsonConverter ping");
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
			System.out.println("THIS SHOULD NEVER HAPPEN: EXCEPTION @ JsonConverter startRound");
			return false;
		}
	}
}
