import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonConverter {
	private static Gson gson = new Gson();
	
	public static int[][] parseTable(String message) {
		JsonObject msg = parseMessage(message);
		return gson.fromJson(msg.get("table"), int[][].class);
	}
	
	public static String jsonify(int[][] board) {
		return gson.toJson(board);
	}
	
	public static boolean ping(String message) {
		JsonObject msg = parseMessage(message);
		return msg.get("type").getAsString().equals("ping");
	}
	
	private static JsonObject parseMessage(String message) {
		return gson.fromJson(message, JsonObject.class);
	}
}
