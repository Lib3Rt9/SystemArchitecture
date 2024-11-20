import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CallbackImpl extends UnicastRemoteObject implements Callback {

    protected CallbackImpl() throws RemoteException {
        super();
    }

    // TESTING VERSION 10 PART 1
    @Override
    public void receive(String name, String message) throws RemoteException {
        System.out.println("Callback: " + name + " send: " + message);
        ChatClient.print("Callback: " + name, message);
    }
    
}