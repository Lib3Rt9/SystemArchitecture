import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Pad extends Remote {
	public void add(SRecord sr) throws RemoteException;
	public RRecord consult(String n, boolean forward) throws RemoteException;
}
