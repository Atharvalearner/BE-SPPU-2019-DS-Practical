import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

public class Client {
    static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 8080);
        ExecutorService pool = Executors.newFixedThreadPool(2);

        System.out.println("[Client] Connected to server.");

        // Send local clock time periodically
        pool.execute(() -> {
            try (OutputStream os = socket.getOutputStream()) {
                while (true) {
                    String now = LocalDateTime.now().format(formatter);
                    os.write((now + "\n").getBytes());
                    os.flush();
                    System.out.println("[Client] Sent local time: " + now);
                    Thread.sleep(5000); // every 5 seconds
                }
            } catch (Exception ignored) {}
        });

        // Receive synchronized time from server
        pool.execute(() -> {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println("[Client] Synchronized time received: " + line);
                }
            } catch (Exception ignored) {}
        });
    }
}
