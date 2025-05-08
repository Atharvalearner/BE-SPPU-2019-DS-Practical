package prac6;
import java.util.*;

public class Ring {
    int n, coordinator;
    boolean[] active;
    ArrayList<Integer> pid = new ArrayList<>();

    Ring(int n) {
        this.n = n;
        active = new boolean[n];
        Arrays.fill(active, true);
        coordinator = n;
        System.out.println("Coordinator is P" + coordinator);
    }

    void display() {
        for (int i = 0; i < n; i++)
            System.out.println("P" + (i + 1) + " is " + (active[i] ? "Up" : "Down"));
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
        if (!active[id - 1]) {
            System.out.println("P" + id + " is Down. Cannot start election.");
            return;
        }
        System.out.println("P" + id + " initiates election...");
        pid.clear();
        int i = id - 1;
        do {
            if (active[i])
                pid.add(i + 1);
            i = (i + 1) % n;
        } while (i != (id - 1));

        System.out.print("Processes in election ring: ");
        System.out.println(pid);

        coordinator = Collections.max(pid);
        System.out.println("New coordinator is P" + coordinator);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Ring r = null;

        while (true) {
            System.out.println("1.Create 2.Display 3.Up 4.Down 5.Elect 6.Exit");
            int ch = sc.nextInt();
            switch (ch) {
                case 1:
                    System.out.print("No. of processes: ");
                    r = new Ring(sc.nextInt());
                    break;
                case 2:
                    if (r != null) r.display();
                    break;
                case 3:
                    if (r != null)
                        System.out.print("Enter process to up: ");
                        r.up(sc.nextInt());
                    break;
                case 4:
                    if (r != null) 
                        System.out.print("Enter process to down: ");
                        r.down(sc.nextInt());
                    break;
                case 5:
                    if (r != null) 
                        System.out.print("Election by process: ");
                        r.elect(sc.nextInt());
                    break;
                case 6:
                    return;
            }
        }
    }
}
