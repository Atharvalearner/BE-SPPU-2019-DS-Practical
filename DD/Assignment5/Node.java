import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Node {
    private final int myPort;
    private final String nextHost;
    private final int nextPort;
    private boolean hasToken;

    public Node(int myPort, String nextHost, int nextPort, boolean initialToken) {
        this.myPort = myPort;
        this.nextHost = nextHost;
        this.nextPort = nextPort;
        this.hasToken = initialToken;
    }

    public void start() {
        Thread listenerThread = new Thread(this::listenForToken);
        listenerThread.start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("[Node " + myPort + "] Press ENTER to request critical section...");
            scanner.nextLine();

            if (hasToken) {
                enterCriticalSection();
                passToken();
            } else {
                System.out.println("[Node " + myPort + "] Waiting for token...");
            }
        }
    }

    private void listenForToken() {
        try (ServerSocket serverSocket = new ServerSocket(myPort)) {
            while (true) {
                Socket socket = serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String token = br.readLine();
                if ("TOKEN".equals(token)) {
                    hasToken = true;
                    System.out.println("[Node " + myPort + "] Received token.");
                }
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enterCriticalSection() {
        System.out.println("[Node " + myPort + "] *** Entering critical section ***");
        try {
            Thread.sleep(3000); // Simulate work in critical section
        } catch (InterruptedException ignored) {}
        System.out.println("[Node " + myPort + "] *** Exiting critical section ***");
    }

    private void passToken() {
        try (Socket socket = new Socket(nextHost, nextPort);
             PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {
            pw.println("TOKEN");
            System.out.println("[Node " + myPort + "] Token passed to " + nextPort);
            hasToken = false;
        } catch (IOException e) {
            System.out.println("[Node " + myPort + "] Error passing token to " + nextPort + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java Node <myPort> <nextHost> <nextPort> <initialToken>");
            return;
        }
        int myPort = Integer.parseInt(args[0]);
        String nextHost = args[1];
        int nextPort = Integer.parseInt(args[2]);
        boolean initialToken = Boolean.parseBoolean(args[3]);

        Node node = new Node(myPort, nextHost, nextPort, initialToken);
        node.start();
    }
}


//javac Node.java
//java Node 5001 localhost 5002 true
//java Node 5002 localhost 5003 false
//java Node 5003 localhost 5001 false