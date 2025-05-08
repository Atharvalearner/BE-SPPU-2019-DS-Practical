import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Calculator extends Remote {
    String sendNumber(int number) throws RemoteException;
}
