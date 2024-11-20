
public class LoadBalancer {
	static String hosts[] = {"localhost", "localhost"};
	static int ports[] = {8081,8082};
	static int nbHosts = 2;
	static Random rand = new Random();
	
	
	public static void main(String args[]) {
	
		ServerSocket ss = new ServerSocket(8080);
	
		while (true) {
			Socket s = ss.accept();
			Slave sl = new Slave(s);
			sl.start();
	
		}
	}
}

class Slave extends Thread {

	Socket csock;
	
	public Slave(Socket s) {
		csock = s;
	}
	
	public void run() {
	
		
		InputStream cis = csock.getInputStream();
		OutputStream cos = csock.getOutputStream();
		
		int target = rand.nextInt(2);
		
		Socket ssock = new Socket(hosts[target], ports[target]);
		
		InputStream sis = ssock.getInputStream();
		OutputStream sos = ssock.getOutputStream();
	
		byte[] buffer = new byte[1024];
		
		//read request from client
		int nb_read = cis.read(buffer);
		
		// write request to server
		sos.write(buffer,0,nb_read);
		
		// read response from server
		nb_read = sis.read(buffer);
		
		// write response to client
		cos.write(buffer,0,nb_read);
		csock.close();
		ssock.close();
	}
}

