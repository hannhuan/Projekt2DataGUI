package KrustyKookies;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JLabel;

import com.jgoodies.forms.factories.DefaultComponentFactory;

import javax.swing.JPanel;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;


public class KrustGui implements Observer{
	
	private JPasswordField textField;
	private DataBase db;
	private JLabel bottomText;
	private JButton login;
	private JFrame f;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public KrustGui(DataBase db){
		this.db=db;
		db.addObserver(this);

		f = new JFrame("KrustyKookies Production");
		f.getContentPane().setBackground(Color.PINK);
		f.setSize(355, 250);
		f.setLocation(300,200);
		f.getContentPane().setLayout(null);
		
		textField = new JPasswordField();
		textField.setBounds(109, 74, 86, 20);
		f.getContentPane().add(textField);
		textField.setColumns(10);
		textField.addActionListener(new EnterListener());
		textField.setEchoChar('*');
		JLabel lblUsername = new JLabel("Password: ");
		lblUsername.setBounds(30, 71, 69, 27);
		f.getContentPane().add(lblUsername);
		
		bottomText = new JLabel("Not connected to database");
		bottomText.setBounds(30, 173, 197, 27);
		f.getContentPane().add(bottomText);
		
		JPanel panel = new JPanel();
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, new Color(64, 64, 64), Color.BLACK, null, null));
		panel.setBounds(0, 160, 339, 51);
		f.getContentPane().add(panel);
		
		login = new JButton("Login");
		login.setBounds(213, 73, 89, 23);
		f.getContentPane().add(login);
		f.setVisible(true);
		login.addActionListener(new EnterListener());
		
	}
	class EnterListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			bottomText.setText("Trying to connect to database...");
			if (db.openConnection("db133", "qoq059ex")) {
				bottomText.setText("Connected to database");
			} else {
				bottomText.setText("Could not connect to database");
			}
			db.checkUser(textField.getPassword());
			textField.setText("");
			
		}
		
	}
	@Override
	public void update(Observable o, Object arg) {
		boolean trutru=(boolean) arg;
		if(!trutru){
			System.out.println("fel lösen");
			textField.setText("");
			bottomText.setText("Not connected to database");
			
		} else {
			if(CurrentUser.instance().getCurrentUserId().equals("Barcode") ||CurrentUser.instance().getCurrentUserId().equals("barcode")){
				Barcode1 bc1 = new Barcode1(db);
			} else{
				KrustGui2 k2 = new KrustGui2(db);
			}
			f.setVisible(false);
			db.deleteObserver(this);
			
		}
		
		
	}
}
