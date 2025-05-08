package prac5;
import java.util.Scanner;

public class TokenRing1 {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        int token = 0;
        System.out.print("Enter the number of nodes: ");
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            System.out.print(" " + i);
        }
        System.out.println(" " + 0);
        while (true) {
            System.out.print("Enter sender: ");
            int s = sc.nextInt();
            System.out.print("Enter receiver: ");
            int r = sc.nextInt();
            System.out.print("Enter Data: ");
            sc.nextLine();
            String d = sc.nextLine();

            System.out.print("Token passing: ");
            int current = token;
            while (current != s) {
                System.out.print(" " + current + "->");
                current = (current + 1) % n;
            }
            System.out.println(" " + s);
            System.out.println("Sender " + s + " sending data: " + d);

            int forward = (s + 1) % n;
            while (forward != r) {
                System.out.println("Data " + d + " forwarded by " + forward);
                forward = (forward + 1) % n;
            }

            System.out.println("Receiver " + r + " received data: " + d);
            token = s;
            sc.close();
        }
    }
}
