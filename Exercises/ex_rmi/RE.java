// RE.java
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RE {
    public static void main(String[] args) {
        try {
            // Get host and command from arguments
            String host = args[0];
            String command = args[1];

            // Get the registry
            Registry registry = LocateRegistry.getRegistry(host);

            // Lookup the remote exec service
            RemoteExecService service = (RemoteExecService) registry.lookup("RemoteExecService");

            // Execute the command
            service.localExec(command);
        } catch (Exception e) {
            System.err.println("RE exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
