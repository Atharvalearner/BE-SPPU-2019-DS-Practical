import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            Calculator stub = (Calculator) registry.lookup("CalculatorService");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter first number: ");
            int a = scanner.nextInt();
            System.out.print("Enter second number: ");
            int b = scanner.nextInt();

            // Create a thread for each operation
            Thread t1 = new Thread(() -> {
                try {
                    System.out.println("Addition: " + stub.add(a, b));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread t2 = new Thread(() -> {
                try {
                    System.out.println("Subtraction: " + stub.subtract(a, b));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread t3 = new Thread(() -> {
                try {
                    System.out.println("Multiplication: " + stub.multiply(a, b));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread t4 = new Thread(() -> {
                try {
                    System.out.println("Division: " + stub.divide(a, b));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Start all threads
            t1.start(); t2.start(); t3.start(); t4.start();

            // Wait for all threads to complete
            t1.join(); t2.join(); t3.join(); t4.join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
