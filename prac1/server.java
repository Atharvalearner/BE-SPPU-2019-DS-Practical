package prac1;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class server {
    public static void main(String[] args) {
        try {
            helloImpl hello = new helloImpl();      // Create remote object
            Registry registry= LocateRegistry.createRegistry(1099);     // Start RMI registry on port 1099
            registry.rebind("RegistryService", hello);                  // Bind the remote object to the registry by name
            System.out.println("Server started .....");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
