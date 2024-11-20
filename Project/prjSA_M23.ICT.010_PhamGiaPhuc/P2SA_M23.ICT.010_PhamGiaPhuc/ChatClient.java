
import java.awt.Button;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
// import java.util.Timer;
// import java.util.TimerTask;
// import java.util.Enumeration;
import java.util.Vector;

public class ChatClient {
	public static TextArea		text;
	public static TextField		data;
	public static Frame 		frame;

	public static Vector<String> users = new Vector<String>();
	public static String myName;
	
	public static boolean isConnected = false;
	public String uniqueName;

	private static ChatServer server;
	private Callback callback;
	// private Timer pingTimer;


	public ChatClient(String name, Callback cb) {
		// this.myName = name;
		ChatClient.myName = name;
		this.callback = cb;
		this.uniqueName = name;
		users.add(name);
	}

	public static void main(String argv[]) {

		// TESTING VERSION 10 PART 2
		try {
			Registry registry = LocateRegistry.getRegistry("localhost", 1099);
			// ChatServer server = (ChatServer) registry.lookup("ChatServer");
			ChatClient.server = (ChatServer) registry.lookup("ChatServer");
			// server.write("Client", "Hello");
		} catch (Exception ex) {
			ex.printStackTrace();
			print("Client", "Connection error. Please make sure server is running.");
			System.err.println("Client: Can not connect to the server. Please make sure server is running.");
		}

		if (argv.length != 1) {
			System.out.println("java ChatClient <name>");
			return;
		}

		myName = argv[0];

		// creation of the GUI 
		frame=new Frame();
		frame.setLayout(new FlowLayout());

		text=new TextArea(10,55);
		text.setEditable(false);
		text.setForeground(Color.red);
		frame.add(text);

		data=new TextField(55);
		frame.add(data);

		Button write_button = new Button("write");
		write_button.addActionListener(new WriteListener());
		frame.add(write_button);

		Button enter_button = new Button("enter");
		enter_button.addActionListener(new EnterListener());
		frame.add(enter_button);

		Button who_button = new Button("who");
		who_button.addActionListener(new WhoListener());
		frame.add(who_button);

		Button leave_button = new Button("leave");
		leave_button.addActionListener(new LeaveListener());
		frame.add(leave_button);

		Button attach_button = new Button("attach");
		attach_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileDialog fileDialog = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
				fileDialog.setVisible(true);
				String filename = fileDialog.getDirectory() + fileDialog.getFile();
				attach(myName, filename);
			}
		});
		frame.add(attach_button);

		frame.setSize(470,300);
		text.setBackground(Color.black); 
		frame.setVisible(true);
	}

	public static void enter(String username) {
		// print("admin", username+" entered");
		// here you should invoke the RMI server
	
		try {
			if (ChatClient.isConnected == false) {
				server.enter(username, new CallbackImpl());
				print("Client", "You joined the chat.");
				System.out.println("Client: You have entered the chat.");
				ChatClient.isConnected = true;
			} else {
				print("Client", "You are in the chat.");
				System.out.println("Client: You are in the chat!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			print("Client", "Connection error. Try using a different name.");
		}
	}
	
	public static void leave(String username) {
		// print("admin", username+" left");
		// here you should invoke the RMI server
		
		try {
			if (ChatClient.isConnected == true) {
				server.leave(username);
				print("Client", "Your have left the chat.");
				System.out.println("Client: You have left the chat.");
				ChatClient.isConnected = false;
			} else {
				print("Client", "You are not in the chat.");
				System.err.println("Client: You are not in the chat.");
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			System.err.println("Client: leave error.");
		}
	}
	
	public static void who() {
		// print("admin", "who() invoked");
		// here you should invoke the RMI server
	
		try {
			String[] users = server.who();
			print("Client", "Users in the chat: " + Arrays.toString(users));
			print("Client", "Your are " + ChatClient.myName);
			System.out.println("Client: Users in the chat: " + Arrays.toString(users));
		} catch (RemoteException e) {
			e.printStackTrace();
			System.err.println("Client: who error.");
		}
	}
	
	public static void write(String username, String text) {
		// print(username, text);
		// here you should invoke the RMI server
	
		if (ChatClient.isConnected == false) {
			print("Client", "Hit enter to chat.");
			System.out.println("Client: Hit enter to chat.");
			return;
		} else {
			try {
				server.write(username, text);
			} catch (RemoteException e) {
				e.printStackTrace();
				System.err.println("Client: write error.");
			}
		}
	}
	
	// called when the user (username) has selected
    // a file (filename) to broadcast to all users
	private static void attach(String username, String filename) {

		try {
			ServerSocket serverSocket = new ServerSocket(0);
			System.out.println("Client: attach 0");
			int port = serverSocket.getLocalPort();
			new Thread(() -> {
				try {
					System.out.println("Client: attach 1");
					Socket socket = serverSocket.accept();
					System.out.println("Client: attach 2");
					serverSocket.setSoTimeout(5000);
					System.out.println("Client: attach 3");
					
					new Thread(() -> {
						try {
							if (isConnected == true) {
								File file = new File(filename);
								byte[] fileData = Files.readAllBytes(file.toPath());
								
								ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
								oos.writeObject(fileData);
	
								oos.close();
								socket.close();
								System.out.println("Client: get file successfully");
							}
						} catch (IOException e) {
							e.printStackTrace();
							System.err.println("Client: attach 'socket' error.");
						} finally {
							try {
								serverSocket.close();
							} catch (Exception e) {
								e.printStackTrace();
								System.err.println("Client: attach 'serverSocket' error.");
							}
						}
					}).start();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}).start();
		
			server.broadcastFile(username, InetAddress.getLocalHost().getHostAddress(), port);

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Client: attach error.");
		}
	}

	public static void print(String username, String text) {
		try {
			if (ChatClient.text != null) {
				ChatClient.text.append(username+" says : "+text+"\n");
			} else {
				System.err.println("Client: ChatClient.text is not initialized");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Client: print error.");
		}	
	}

	public  String getName() {
		return uniqueName;
	}

	public Callback getCallback() {
		return callback;
	}

	public void setCallback(Object object) {
		if (object instanceof Callback) {
			callback = (Callback) object;
		} else {
			System.err.println("Client: setCallback error.");
		}
	}

	// ping

}

	// action invoked when the "write" button is clicked
	class WriteListener implements ActionListener {
		public void actionPerformed (ActionEvent ae) {
			try {
				ChatClient.write(ChatClient.myName, ChatClient.data.getText());
			} catch (Exception ex) {
				ex.printStackTrace();
				System.err.println("Client: write listener error.");
			}
		}
	}

	// action invoked when the "connect" button is clicked
	class EnterListener implements ActionListener {
		public void actionPerformed (ActionEvent ae) {
			try {  
				ChatClient.enter(ChatClient.myName);
			} catch (Exception ex) {
				ex.printStackTrace();
				System.err.println("Client: enter listener error.");
			}
		}
	}  

	// action invoked when the "who" button is clicked
	class WhoListener implements ActionListener {
		public void actionPerformed (ActionEvent ae) {
			try {
				ChatClient.who();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.err.println("Client: who listener error.");
			}
		}
	}

	// action invoked when the "leave" button is clicked
	class LeaveListener implements ActionListener {
		public void actionPerformed (ActionEvent ae) {
			try {
				ChatClient.leave(ChatClient.myName);
			} catch (Exception ex) {
				ex.printStackTrace();
				System.err.println("Client: leave listener error.");
			}
		}
	}
	


