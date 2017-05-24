import com.google.gson.Gson;

public class JsonConverter {
	private static Gson gson = new Gson();
	
	public static int[][] parse(String message) {
		return gson.fromJson(message, int[][].class);
	}
	
	public static String jsonify(int[][] board) {
		return gson.toJson(board);
	}
}
