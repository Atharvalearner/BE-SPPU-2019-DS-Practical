package prac7;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class web {
    public static void main(String[] args) throws Exception {
        HttpServer.create(new InetSocketAddress(8000), 0).createContext("/", new Handler()).getServer().start();
        System.out.println("Server started at http://localhost:8000/");
    }

    static class Handler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            String method = ex.getRequestMethod();
            String res = method.equals("GET") ? form() : result(readBody(ex.getRequestBody()));
            send(ex, res);
        }

        String form() {
            return "<form method='POST'>Num1:<input name='num1'/> Num2:<input name='num2'/>"
                 + "<input type='submit' name='operation' value='Add'/>"
                 + "<input type='submit' name='operation' value='Subtract'/></form>";
        }

        String result(String body) {
            Map<String, String> p = parse(body);
            try {
                int a = Integer.parseInt(p.get("num1")), b = Integer.parseInt(p.get("num2"));
                int r = p.get("operation").equals("Add") ? a + b : a - b;
                System.out.println("num1: " + a + ", num2: " + b + ", operation: " + p.get("operation") + ", result: " + r);
                return "<h1>Result: " + r + "</h1><a href='/'>Back</a>";
            } catch (Exception e) {
                return "<h1>Error</h1><a href='/'>Try Again</a>";
            }
        }

        String readBody(InputStream is) throws IOException {
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            return r.readLine();
        }

        Map<String, String> parse(String q) {
            Map<String, String> m = new HashMap<>();
            for (String s : q.split("&")) {
                String[] kv = s.split("=");
                if (kv.length == 2) m.put(kv[0], kv[1]);
            }
            return m;
        }

        void send(HttpExchange ex, String res) throws IOException {
            byte[] b = res.getBytes("UTF-8");
            ex.getResponseHeaders().set("Content-Type", "text/html");
            ex.sendResponseHeaders(200, b.length);
            ex.getResponseBody().write(b);
            ex.close();
        }
    }
}