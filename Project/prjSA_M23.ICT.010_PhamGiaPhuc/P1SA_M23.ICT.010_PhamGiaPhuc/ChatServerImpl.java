import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {

    private Vector<ChatClient> clients;

    protected ChatServerImpl() throws RemoteException {
        super();
        clients = new Vector<ChatClient>();
    }

    // TESTING VERSION 10 PART 1
    public static void main(String argv[]) {
        try {
            ChatServer server = new ChatServerImpl();
            LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost:1099/ChatServer", server);
            System.out.println("Server ready and listening on port " + "1099");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Server: Server error, can not start.");
        }
    }


    @Override
    public void enter(String name, Callback cb) throws RemoteException {
        // Check if the name is already taken
        boolean isNameTaken = false;
        for (ChatClient client : clients) {
            if (client.getName().equals(name)) {
                isNameTaken = true;
                break;
            }
        }

        // Generate a unique name if the provided name is already taken
        if (isNameTaken) {
            System.err.println(name + " already exists. Please choose a different name.");
            throw new RemoteException("User already exists");
            // int counter = 1;
            // String uniqueName = name + "_" + counter;
            // while (isNameTaken) {
            //     for (ChatClient client : clients) {
            //         if (client.getName().equals(uniqueName)) {
            //             counter++;
            //             uniqueName = name + "_" + counter;
            //             break;
            //         }
            //     }
            //     isNameTaken = false;
            // }
            // name = uniqueName;
        }
        
        ChatClient newClient = new ChatClient(name, cb);
        this.clients.add(newClient);
        write("Server", name + " joined the chat.");
        System.out.println("Server: " + name + " joined the chat.");
        // ChatClient.print("admin", "Server: '" + name + "'" +" entered");
    }

    @Override
    public void leave(String name) throws RemoteException {
        for (ChatClient client : clients) {
            if (client.getName().equals(name)) {
                client.setCallback(null); // Set the callback of client to null
                clients.remove(client);
                write("Server", name + " left the chat.");
                System.out.println("Server: " + "'" + name + "'" + " left the chat.");
                break;
            }
        }
    }

    @Override
    public String[] who() throws RemoteException {
        String[] allClients = new String[clients.size()];
        int i = 0;
        for (ChatClient client : clients) {
            allClients[i] = client.getName().toString();
            i++;
        }
        System.out.println("Server: " + allClients + " in the chat.");
        return allClients;
    }

    @Override
    public void write(String name, String text) throws RemoteException {
        for (ChatClient client : clients) {
            Callback clientCallback = client.getCallback();
            if (clientCallback != null) {
                clientCallback.receive(name, text);
            }
        }
        System.out.println("Server: Received message from " + name + ": " + text);
    }

}
