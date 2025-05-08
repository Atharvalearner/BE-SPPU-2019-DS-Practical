package prac4;
import java.util.*;

class Node {
    int id; 
    double time;
    Node(int id, double time) { 
        this.id = id;
        this.time = time; 
    }
}

public class Berkeley {
    public static void main(String[] args) throws InterruptedException {
        List<Node> nodes = new ArrayList<>();
        Random rand = new Random();
        int masterId = 0;

        for (int i = 0; i < 4; i++) {
            nodes.add(new Node(i, System.currentTimeMillis()/1000.0 * (0.8 + 0.4*rand.nextDouble())));
        }        
        for (int r = 1; r <= 3; r++) {
            System.out.println("\nRound " + r + ":");
            double masterTime = nodes.get(masterId).time;
            System.out.printf("Master time: %.2f\n", masterTime);
            
            List<Double> times = new ArrayList<>();
            for (Node n : nodes) {
                n.time += 1.0 * (0.8 + 0.4*rand.nextDouble());
                times.add(n.time);
                System.out.printf("Node %d: %.3f\n", n.id, n.time);
            }
            double avg = times.stream().mapToDouble(d->d).average().getAsDouble();
            System.out.printf("Average: %.2f\n", avg);
            for (int i = 0; i < nodes.size(); i++) {
                double adj = avg - nodes.get(i).time;
                nodes.get(i).time += adj;
                System.out.printf("Node %d adjustment: %+.2f\n", i, adj);
            }
            
            Thread.sleep(2000);
        }
    }
}