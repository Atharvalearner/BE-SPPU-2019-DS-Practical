import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 2000);
            Calculator stub = (Calculator) registry.lookup("Calculator");

            Scanner sc = new Scanner(System.in);
            System.out.print("Enter your number: ");
            int num = sc.nextInt();

            String response = stub.sendNumber(num);
            System.out.println("Server response: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
