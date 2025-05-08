import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CalculatorImpl extends UnicastRemoteObject implements Calculator {
    private Integer number1 = null;
    private Integer number2 = null;

    public CalculatorImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized String sendNumber(int number) throws RemoteException {
        if (number1 == null) {
            number1 = number;
            System.out.println("Received first number: " + number1);
            return "Server received your number. Waiting for second client...";
        } else if (number2 == null) {
            number2 = number;
            System.out.println("Received second number: " + number2);
            int sum = number1 + number2;
            String result = "Sum of " + number1 + " + " + number2 + " = " + sum;
            // Reset for next pair of clients
            number1 = null;
            number2 = null;
            return result;
        } else {
            return "Server is busy processing. Please try again later.";
        }
    }
}
