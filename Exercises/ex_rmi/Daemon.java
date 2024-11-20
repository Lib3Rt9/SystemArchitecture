// Daemon.java
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Daemon {
    public static void main(String[] args) {
        try {
            // Create a new service
            RemoteExecService service = new RemoteExecServiceImpl();

            // Bind the service to the RMI Registry
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("RemoteExecService", service);

            System.out.println("Daemon ready");
        } catch (Exception e) {
            System.err.println("Daemon exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
