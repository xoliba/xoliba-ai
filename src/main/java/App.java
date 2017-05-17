/*
 * This Java source file was generated by the Gradle 'init' task.
 */
import static spark.Spark.*;
import spark.Spark;
import spark.utils.IOUtils;
import spark.Request;
import java.util.Map;
import java.util.HashMap;


public class App {
	
    public static void main(String[] args) {
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
		staticFileLocation("/public/clientdeps/pixi.js");
		staticFileLocation("/public");
		
		options("/*", (request,response)->{

    	   String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
    	   if (accessControlRequestHeaders != null) {
    	       response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
    	   }

    	   String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
    	   if(accessControlRequestMethod != null){
    		response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
    	   }

    	   return "OK";
    	});
		
        get("/", (req, res) -> IOUtils.toString(Spark.class.getResourceAsStream("public/index.html")));
    }
}
