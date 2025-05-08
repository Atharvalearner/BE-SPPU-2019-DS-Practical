import java.io.*;
import java.net.*;
import java.util.*;

public class BullyNode {
    private int myPort;
    private List<Integer> otherPorts;
    private boolean isLeader = false;
    private boolean electionInProgress = false;
    private int currentLeader = -1;  // keeps track of known leader

    public BullyNode(int myPort, List<Integer> otherPorts) {
        this.myPort = myPort;
        this.otherPorts = otherPorts;
    }

    public void start() {
        // Start server to listen for incoming messages
        new Thread(() -> listenForMessages()).start();

        System.out.println("[Node " + myPort + "] Started on port " + myPort);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("[Node " + myPort + "] Press ENTER to start election...");
            scanner.nextLine();
            if (!electionInProgress) {
                startElection();
            } else {
                System.out.println("[Node " + myPort + "] Election already in progress...");
            }
        }
    }

    private void listenForMessages() {
        try (ServerSocket serverSocket = new ServerSocket(myPort)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message = in.readLine();
                if (message != null) {
                    handleMessage(message);
                }
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("[Node " + myPort + "] Error in server socket: " + e.getMessage());
        }
    }

    private void handleMessage(String message) {
        if (message.startsWith("ELECTION")) {
            int senderPort = Integer.parseInt(message.split(":")[1]);
            System.out.println("[Node " + myPort + "] Received ELECTION from " + senderPort);
            sendMessage(senderPort, "OK:" + myPort);

            if (!electionInProgress) {
                startElection();  // forward election if not already doing
            }
        } else if (message.startsWith("OK")) {
            int senderPort = Integer.parseInt(message.split(":")[1]);
            System.out.println("[Node " + myPort + "] Received OK from " + senderPort);
            electionInProgress = true;
        } else if (message.startsWith("COORDINATOR")) {
            int leaderPort = Integer.parseInt(message.split(":")[1]);
            if (currentLeader != leaderPort) {  // only print once per leader change
                System.out.println("[Node " + myPort + "] Node " + leaderPort + " is the leader.");
                currentLeader = leaderPort;
            }
            isLeader = (myPort == leaderPort);
            electionInProgress = false;
        }
    }

    private void startElection() {
        electionInProgress = true;
        boolean higherNodeExists = false;
        for (int port : otherPorts) {
            if (port > myPort) {
                sendMessage(port, "ELECTION:" + myPort);
                higherNodeExists = true;
            }
        }

        if (!higherNodeExists) {
            declareLeader();
        }
    }

    private void declareLeader() {
        isLeader = true;
        currentLeader = myPort;
        System.out.println("[Node " + myPort + "] I am the new leader!");
        for (int port : otherPorts) {
            sendMessage(port, "COORDINATOR:" + myPort);
        }
        electionInProgress = false;
    }

    private void sendMessage(int port, String message) {
        try (Socket socket = new Socket("localhost", port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(message);
            System.out.println("[Node " + myPort + "] Sent message: " + message + " to " + port);
        } catch (IOException e) {
            // System.err.println("[Node " + myPort + "] Failed to send message to " + port + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java BullyNode <myPort> <comma-separated-peerPorts>");
            return;
        }

        int myPort = Integer.parseInt(args[0]);
        List<Integer> otherPorts = new ArrayList<>();
        for (String p : args[1].split(",")) {
            int port = Integer.parseInt(p.trim());
            if (port != myPort) {
                otherPorts.add(port);
            }
        }

        BullyNode node = new BullyNode(myPort, otherPorts);
        node.start();
    }
}

//javac BullyNode.java
//java BullyNode 5001 5001,5002,5003
//java BullyNode 5002 5001,5002,5003
//java BullyNode 5003 5001,5002,5003