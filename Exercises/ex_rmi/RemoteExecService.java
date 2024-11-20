// RemoteExecService.java
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteExecService extends Remote {
    void localExec(String command) throws RemoteException;
}
