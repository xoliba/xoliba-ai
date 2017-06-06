import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonConverter {
	private static Gson gson = new Gson();
	
	public static int[][] parseTable(String message) {
		TurnData msg = parseMessage(message);
		return msg.table;
	}
	
	public static String jsonify(TurnData data) {
		return gson.toJson(data);
	}
	
	public static boolean ping(String message) {
		TurnData msg = parseMessage(message);
		return msg.type.equals("ping");
	}
	
	private static TurnData parseMessage(String message) {
		return gson.fromJson(message, TurnData.class);
	}
}
