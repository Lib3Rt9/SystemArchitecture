import java.net.*;
import java.io.*;
import java.util.*;


public class LoadBalancer extends Thread {

    static String hosts[] = {"localhost", "localhost"};
    static int ports[] = {8081, 8082};
    static int nbPorts = 2;
    static Random rand = new Random();
    Socket input;

    public LoadBalancer (Socket s) {
        input = s;
    }

    private void printOneLine(String message, byte buf[]) throws IOException
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        BufferedReader br = new BufferedReader(new InputStreamReader(bais));

        System.out.println(message + " [" + br.readLine() + "]");
    }

    public void run() {
        try {
            int i = rand.nextInt(nbPorts);
            Socket output = new Socket(hosts[i], ports[i]);
            OutputStream inputOutputstream = input.getOutputStream();

            InputStream inputInputstream = input.getInputStream();
            OutputStream outputOutputstream = output.getOutputStream();

            InputStream outputInputstream = output.getInputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            bytesRead = inputInputstream.read(buffer);
            printOneLine("forward request", buffer);
            outputOutputstream.write(buffer, 0, bytesRead);

            bytesRead = outputInputstream.read(buffer);
            
            String s = new String(buffer, 0, bytesRead);
            System.out.println("[" + s + "]");

            printOneLine("forward response", buffer);
            
            inputOutputstream.write(buffer, 0, bytesRead);
            inputOutputstream.close();
            inputInputstream.close();

            outputOutputstream.close();
            outputInputstream.close();
            
            output.close();
            input.close();
        } catch (IOException e) {
            System.err.println("Error: " + e);
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        try {
            ServerSocket ss = new ServerSocket(8080);
            while (true) {
                Thread  t = new LoadBalancer(ss.accept());
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}