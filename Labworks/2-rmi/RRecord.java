import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RRecord extends Remote {
	public String getName () throws RemoteException;
	public String getEmail () throws RemoteException;
}



