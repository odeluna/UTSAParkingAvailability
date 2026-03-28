import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws Exception {
        // Start server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Create /predict endpoint
        server.createContext("/predict", new PredictHandler());
        server.setExecutor(null);
        server.start();

        System.out.println("Server running on http://localhost:8080/predict");
    }

    // Handles requests to /predict
    static class PredictHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Allow frontend to call this
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            // Parse query parameters
            String query = exchange.getRequestURI().getQuery();
            HashMap<String, String> params = parseQuery(query);

            String lot = params.get("lot");
            String time = params.get("time");

            // Run your parking logic
            int chance = calculateChance(lot, time);
            String message = "Prediction complete.";

            // Build JSON response
            String json = "{ \"chance\": " + chance + ", \"message\": \"" + message + "\" }";

            // Send response
            exchange.sendResponseHeaders(200, json.length());
            OutputStream os = exchange.getResponseBody();
            os.write(json.getBytes());
            os.close();
        }
    }

    // Parses ?lot=BK1&time=10am
    private static HashMap<String, String> parseQuery(String query) {
        HashMap<String, String> map = new HashMap<>();
        if (query == null) return map;

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] parts = pair.split("=");
            if (parts.length == 2) {
                map.put(parts[0], parts[1]);
            }
        }
        return map;
    }

    // time-dependent parking logic
    private static int calculateChance(String lot, String time) {
        if (lot == null || time == null) return 0;

        switch (time) {

            case "4pm":
            case "5pm":
            case "6pm":
            case "7pm":
            case "8pm":
            case "9pm":
            case "10pm":
            case "11pm":
            case "12am":
            case "1am":
            case "2am":
            case "3am":
            case "4am":
            case "5am":
            case "6am":
            case "7am":
                switch (lot) {
                    case "G":   return 100;
                    case "R":   return 10;
                    case "LOT": return 100;
                    default: return 0;
                }
            case "8am":
                switch (lot) {
                    case "G":   return 50;
                    case "R":   return 30;
                    case "LOT": return 50;
                    default: return 0;
                }
            case "9am":
            case "10am":
            case "11am":
            case "12pm":
            case "1pm":
            case "2pm":
                switch (lot) {
                    case "G": return 15;
                    case "R": return 30;
                    case "LOT": return 15;
                    default: return 0;
                }

            case "3pm":
                switch (lot) {
                    case "G":   return 50;
                    case "R":   return 30;
                    case "LOT": return 50;
                    default: return 0;
                }





            default:
                return 0;
        }
    }
}
