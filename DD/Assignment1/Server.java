import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            CalculatorImpl obj = new CalculatorImpl();
            Registry registry = LocateRegistry.createRegistry(2000);
            registry.rebind("Calculator", obj);
            System.out.println("Server ready on port 2000.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
