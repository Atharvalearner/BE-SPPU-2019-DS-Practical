package prac5;
import java.util.Scanner;

public class TokenRingWithMutualExclusion {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int token = 0;

        System.out.print("Enter the number of nodes: ");
        int n = sc.nextInt();

        System.out.println("\nNodes in the ring:");
        for (int i = 0; i < n; i++) {
            System.out.print(" " + i);
        }
        System.out.println(" " + 0); 

        while (true) {
            System.out.println("\nToken is at node " + token);
            System.out.print("Does node " + token + " want to enter critical section? (yes/no/exit): ");
            String choice = sc.next().toLowerCase();

            if (choice.equals("yes")) {
                System.out.println("Node " + token + " is entering the critical section...");
                try {
                    Thread.sleep(3000); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Node " + token + " exits the critical section.");
            } else if (choice.equals("exit")) {
                System.out.println("Exiting token ring simulation.");
                break;
            } else {
                System.out.println("Node " + token + " skips critical section.");
            }
            
            token = (token + 1) % n;
        }
        sc.close();
    }
}
