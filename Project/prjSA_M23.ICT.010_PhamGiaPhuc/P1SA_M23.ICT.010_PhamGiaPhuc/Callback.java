import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Callback extends Remote {
    void receive(String name, String message) throws RemoteException;
    
}