// RemoteExecServiceImpl.java
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteExecServiceImpl extends UnicastRemoteObject implements RemoteExecService {
    protected RemoteExecServiceImpl() throws RemoteException {
        super();
    }

    public void localExec(String command) throws RemoteException {
        try {
            Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            throw new RemoteException("Error executing command", e);
        }
    }
}
