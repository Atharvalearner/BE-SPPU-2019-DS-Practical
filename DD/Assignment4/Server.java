import java.io.*;
import java.net.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    static class ClientInfo {
        Socket socket;
        Duration offset;
        ClientInfo(Socket s) { socket = s; }
    }

    static Map<String, ClientInfo> clients = new ConcurrentHashMap<>();
    static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(8080);
        ExecutorService pool = Executors.newCachedThreadPool();

        System.out.println("[Server] Listening on port 8080...");

        // Accept incoming client connections
        pool.execute(() -> {
            while (true) {
                try {
                    Socket client = server.accept();
                    String id = client.getInetAddress() + ":" + client.getPort();
                    clients.put(id, new ClientInfo(client));
                    System.out.println("[Server] Client " + id + " connected.");

                    pool.execute(() -> handleClient(id, client));
                } catch (Exception ignored) {}
            }
        });

        // Periodically calculate average offset and send sync time
        pool.execute(() -> {
            while (true) {
                try {
                    if (!clients.isEmpty()) {
                        // Calculate average offset
                        Duration avg = clients.values().stream()
                            .map(c -> c.offset)
                            .reduce(Duration.ZERO, Duration::plus)
                            .dividedBy(clients.size() + 1); // +1 to include master clock

                        // Sync time to be sent
                        String syncTime = LocalDateTime.now().plus(avg).format(formatter);
                        System.out.println("[Server] Sending synchronized time: " + syncTime);

                        for (ClientInfo c : clients.values()) {
                            OutputStream os = c.socket.getOutputStream();
                            os.write((syncTime + "\n").getBytes());
                            os.flush();
                        }
                    }
                    Thread.sleep(10000); // every 10 seconds
                } catch (Exception ignored) {}
            }
        });
    }

    static void handleClient(String id, Socket client) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                LocalDateTime clientTime = LocalDateTime.parse(line, formatter);
                Duration diff = Duration.between(clientTime, LocalDateTime.now());
                clients.get(id).offset = diff;
                System.out.println("[Server] Received from " + id + " offset: " + diff.getSeconds() + "s");
            }
        } catch (Exception e) {
            clients.remove(id);
            System.out.println("[Server] Client " + id + " disconnected.");
        }
    }
}
