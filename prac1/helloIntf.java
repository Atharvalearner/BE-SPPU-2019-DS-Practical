package prac1;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface helloIntf extends Remote{      // Remote interface which declares the remote method
    String sayHello(String msg) throws RemoteException;
}
