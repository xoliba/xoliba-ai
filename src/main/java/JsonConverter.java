import Game.TurnData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonConverter {
	private static Gson gson = new Gson();
	
	public static int[][] parseTable(String message) {
		return gson.fromJson(message, int[][].class);
	}
	
	public static String jsonify(TurnData data) {
		return gson.toJson(data);
	}

	public static String jsonify(int[][] table) {
		return gson.toJson(table);
	}
	
	public static boolean ping(String message) {
		try {
			TurnData msg = parseMessage(message);
			return msg.type.equals("ping");
		} catch (Exception e) {
			System.out.println("EXCEPTION @ JsonConverter ping");
			return false;
		}
	}
	
	public static TurnData parseMessage(String message) {
		return gson.fromJson(message, TurnData.class);
	}
}
