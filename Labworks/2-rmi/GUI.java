import java.awt.Button;
import java.awt.Choice;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;

import javax.swing.JApplet;
import javax.swing.JFrame;


public class GUI extends JApplet {
	private static final long serialVersionUID = 1;
	TextField name, email;
	Choice pads;
	Label message;
	public void init() {
		setSize(300,200);
		setLayout(new GridLayout(6,2));
		add(new Label("  Name : "));
		name = new TextField(30);
		add(name);
		add(new Label("  Email : "));
		email = new TextField(30);
		add(email);
		add(new Label("  Pad : "));
		pads = new Choice();
		pads.addItem("Pad1");
		pads.addItem("Pad2");
		add(pads);
		add(new Label(""));
		add(new Label(""));
		Button Abutton = new Button("add");
		Abutton.addActionListener(new AButtonAction());
		add(Abutton);
		Button Cbutton = new Button("consult");
		Cbutton.addActionListener(new CButtonAction());
		add(Cbutton);
		message = new Label();
		add(message);
	}

	class CButtonAction implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			String n, c;
			n = name.getText();
			c = pads.getSelectedItem();
			message.setText("consult("+n+","+c+")        ");
			// to be completed / the user clicked on the consult button
		}
	}
	class AButtonAction implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			String n, e, c;
			n = name.getText();
			e = email.getText();
			c = pads.getSelectedItem();
			message.setText("add("+n+","+e+","+c+")");
			// to be completed / the user clicked on the add button
		}
	}
	
	public static void main(String args[]) {
		GUI a = new GUI();
		a.init();
		a.start();
		JFrame frame = new JFrame("Applet");
		frame.setSize(400,200);
  		frame.getContentPane().add(a);
  		frame.setVisible(true);
	}
	
}


