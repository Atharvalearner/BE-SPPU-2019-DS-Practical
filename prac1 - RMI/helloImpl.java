package prac1;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class helloImpl extends UnicastRemoteObject implements helloIntf{    
    helloImpl() throws RemoteException{};

    public String sayHello(String name) throws RemoteException {    // Remote method which is called by the client
        String msg = "Hello " + name + " from Server";
        System.out.println("Received msg from Client : " + name);
        return msg;
    }
}
