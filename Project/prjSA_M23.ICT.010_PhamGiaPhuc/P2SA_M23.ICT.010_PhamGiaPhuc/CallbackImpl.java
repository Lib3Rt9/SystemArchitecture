import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CallbackImpl extends UnicastRemoteObject implements Callback {

    protected CallbackImpl() throws RemoteException {
        super();
    }

    // TESTING VERSION 10 PART 2
    @Override
    public void receive(String name, String message) throws RemoteException {
        System.out.println("Callback: " + name + " send: " + message);
        ChatClient.print("Callback: " + name, message);
    }

    @Override
    public void receiveFile(String username, String hostAddress, int senderPort) throws RemoteException{
        try {
            Socket cbSocket = new Socket(hostAddress, senderPort);
            ObjectInputStream ois = new ObjectInputStream(cbSocket.getInputStream());

            byte[] fileData = (byte[]) ois.readObject();
            Files.write(Paths.get(username + "_received"), fileData);
            
            ois.close();
            cbSocket.close();
        } catch (Exception e) {
            System.err.println("Callback: receiveFile error.");
        }
    }
}