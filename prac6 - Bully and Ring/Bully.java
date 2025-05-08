package prac6;

import java.util.*;

public class Bully {
    int coordinator, n;
    boolean[] active;

    Bully(int n) {
        this.n = n;
        coordinator = n;
        active = new boolean[n];
        Arrays.fill(active, true);
        System.out.println("Coordinator is P" + coordinator);
    }

    void display() {
        for (int i = 0; i < n; i++) {
            System.out.println("P" + (i + 1) + " is " + (active[i] ? "Up" : "Down"));
        }
        System.out.println("Coordinator is P" + coordinator);
    }

    void up(int id) {
        if (!active[id - 1]) {
            active[id - 1] = true;
            System.out.println("P" + id + " is Up");
        } else {
            System.out.println("Already Up");
        }
    }

    void down(int id) {
        if (active[id - 1]) {
            active[id - 1] = false;
            System.out.println("P" + id + " is Down");
        } else {
            System.out.println("Already Down");
        }
    }

    void elect(int id) {
        System.out.println("P" + id + " initiates election...");
        for (int i = id; i < n; i++) {
            if (active[i]){
                System.out.println("P" + id + " sends message to P" + (i + 1));
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            if (active[i]) {
                coordinator = i + 1;
                break;
            }
        }
        System.out.println("New coordinator is P" + coordinator);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bully b = null;
        while (true) {
            System.out.println("\n1.Create 2.Display 3.Up 4.Down 5.Elect 6.Exit");
            int ch = sc.nextInt();
            switch (ch) {
                case 1:
                    System.out.print("No. of processes: ");
                    b = new Bully(sc.nextInt());
                    break;
                case 2:
                    b.display();
                    break;
                case 3:
                    System.out.print("Enter process to up: ");
                    b.up(sc.nextInt());
                    break;
                case 4:
                    System.out.print("Enter process to down: ");
                    b.down(sc.nextInt());
                    break;
                case 5:
                    System.out.print("Election by process: ");
                    b.elect(sc.nextInt());
                    break;
                case 6:
                    return;
            }
        }
    }
}