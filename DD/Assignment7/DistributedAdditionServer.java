import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DistributedAdditionServer {
    private static Integer firstNumber = null;
    private static Integer secondNumber = null;
    private static Integer result = null;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/input", new InputHandler());
        server.createContext("/result", new ResultHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started at http://localhost:8000/");
    }

    static class InputHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = Arrays.stream(query.split("&"))
                    .map(s -> s.split("="))
                    .collect(Collectors.toMap(a -> a[0], a -> a[1]));

            int client = Integer.parseInt(params.get("client"));
            int num = Integer.parseInt(params.get("num"));

            String response;
            synchronized (DistributedAdditionServer.class) {
                if (client == 1) {
                    firstNumber = num;
                    response = "Received first number: " + num;
                } else if (client == 2) {
                    secondNumber = num;
                    response = "Received second number: " + num;
                } else {
                    response = "Invalid client ID!";
                }

                if (firstNumber != null && secondNumber != null && result == null) {
                    result = firstNumber + secondNumber;
                    response += "\nBoth numbers received. Sum = " + result;
                }
            }

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*"); // Allow CORS
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class ResultHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response;
            synchronized (DistributedAdditionServer.class) {
                if (result != null) {
                    response = "Result: " + result;
                } else {
                    response = "Result not ready yet.";
                }
            }

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*"); // Allow CORS
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}


//javac DistributedAdditionServer.java
//java DistributedAdditionServer
