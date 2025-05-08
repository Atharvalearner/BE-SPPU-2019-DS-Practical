import java.io.*;
import java.net.*;
import java.util.*;

public class RingNode {
    private int id;
    private int port;
    private List<Integer> allPorts;
    private boolean isLeader = false;
    private int leaderId = -1;

    public RingNode(int id, int port, List<Integer> allPorts) {
        this.id = id;
        this.port = port;
        this.allPorts = allPorts;
    }

    public void start() {
        new Thread(this::listen).start();
        Scanner scanner = new Scanner(System.in);
        System.out.println("[Node " + id + "] Started on port " + port);
        System.out.println("[Node " + id + "] Press ENTER to start election...");
        scanner.nextLine();
        sendElection(id, getNextPort());
    }

    private void listen() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message = in.readLine();
                socket.close();

                if (message.startsWith("ELECTION:")) {
                    int receivedId = Integer.parseInt(message.split(":")[1]);
                    handleElection(receivedId);
                } else if (message.startsWith("LEADER:")) {
                    int newLeaderId = Integer.parseInt(message.split(":")[1]);
                    handleLeader(newLeaderId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleElection(int receivedId) {
        System.out.println("[Node " + id + "] Received ELECTION from " + receivedId);
        if (receivedId > id) {
            sendElection(receivedId, getNextPort());
        } else if (receivedId < id) {
            sendElection(id, getNextPort());
        } else {
            // Received back own id => won the election
            System.out.println("[Node " + id + "] I am the new leader!");
            isLeader = true;
            leaderId = id;
            sendLeader(id, getNextPort());
        }
    }

    private void handleLeader(int leaderId) {
        System.out.println("[Node " + id + "] Leader elected: " + leaderId);
        if (this.leaderId != leaderId) {
            this.leaderId = leaderId;
            sendLeader(leaderId, getNextPort());
        }
    }

    private void sendElection(int electionId, int targetPort) {
        sendMessage("ELECTION:" + electionId, targetPort);
        System.out.println("[Node " + id + "] Sent ELECTION:" + electionId + " to " + targetPort);
    }

    private void sendLeader(int leaderId, int targetPort) {
        sendMessage("LEADER:" + leaderId, targetPort);
        System.out.println("[Node " + id + "] Sent LEADER:" + leaderId + " to " + targetPort);
    }

    private void sendMessage(String message, int targetPort) {
        try (Socket socket = new Socket("localhost", targetPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(message);
        } catch (IOException e) {
            System.out.println("[Node " + id + "] Error sending to " + targetPort + ": " + e.getMessage());
        }
    }

    private int getNextPort() {
        int index = allPorts.indexOf(port);
        return allPorts.get((index + 1) % allPorts.size());
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java RingNode <id> <port> <port1,port2,port3,...>");
            return;
        }
        int id = Integer.parseInt(args[0]);
        int port = Integer.parseInt(args[1]);
        List<Integer> allPorts = new ArrayList<>();
        for (String p : args[2].split(",")) {
            allPorts.add(Integer.parseInt(p));
        }

        new RingNode(id, port, allPorts).start();
    }
}


//javac RingNode.java
//java RingNode 1 5001 5001,5002,5003
//java RingNode 2 5002 5001,5002,5003
//java RingNode 3 5003 5001,5002,5003
