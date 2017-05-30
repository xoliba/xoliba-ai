import AI.AI;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import com.google.gson.Gson;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

@WebSocket
public class AiWebSocket {
	private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

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
		Gson gson = new Gson();
		System.out.println("Got: " + JsonConverter.jsonify(JsonConverter.parse(message)));
                AI ai = new AI(-1);
		session.getRemote().sendString(JsonConverter.jsonify(ai.move(JsonConverter.parse(message))));
	}
}
