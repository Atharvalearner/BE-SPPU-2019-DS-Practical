package prac1;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class client {
    public static void main(String[] args) {
        for(int i = 0; i < 5; i++){
            int curr = i+1;
            new Thread(() -> {
                try {
                    Registry registry = LocateRegistry.getRegistry("localhost");        // Connects, Locate & get the registry
                    helloIntf intf = (helloIntf) registry.lookup("RegistryService");    // find the remote object from the registry
                    String response = intf.sayHello("Client: " + curr);                     // Call the remote method 
                    System.out.println("Received message " + response);                     // Print the response
                } catch (Exception e) {
                    System.out.println(e);
                }
            }).start();
        }
    }
}
