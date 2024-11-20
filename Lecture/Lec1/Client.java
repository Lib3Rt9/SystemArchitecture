
import java.net.*;
import java.io.*;

public class Client {
   public static void main (String[] str) {
      try {
         Socket csock = new Socket("sd-160040.dedibox.fr",9999);
         ObjectInputStream ois = new ObjectInputStream(
                              csock.getInputStream());
          Person v = (Person)ois.readObject();
          System.out.println("Received person: "+ v.toString());
         csock.close();
		} catch (Exception e) {
			System.out.println("An error has occurred ...");
		}
	}
}

