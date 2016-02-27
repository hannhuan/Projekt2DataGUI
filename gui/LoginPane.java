package gui;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import project2.Server;

/**
 * LoginPane
 * 
 * @author Administrator
 *
 */
public class LoginPane extends JFrame {
	private String userName;
	private String passWord;
	private JTextField userNameField;
	private JTextField passWordField;
	private Server server;

	public LoginPane(Server server) {
		this.server = server;
		createDemo();

	}

	/**
	 * Creates interface
	 */
	private void createDemo() {
		Container gui = getContentPane();
		gui.setLayout(null);

		setTitle("Hospital Project");
		setSize(1200, 800);

		JLabel welcome = new JLabel("Please login");
		welcome.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		welcome.setBounds(525, 200, 200, 60);
		gui.add(welcome);

		JLabel id = new JLabel("ID: ");
		id.setOpaque(true);
		id.setBounds(470, 300, 20, 30);
		gui.add(id);

		userNameField = new JTextField();
		userNameField.setBounds(500, 300, 200, 30);
		userNameField.setEditable(true);
		gui.add(userNameField);

		passWordField = new JPasswordField();
		passWordField.setBounds(500, 350, 200, 30);
		passWordField.setEditable(true);

		gui.add(passWordField);

		JLabel pwd = new JLabel("Password: ");
		pwd.setBounds(425, 350, 100, 30);
		gui.add(pwd);

		JButton login = new JButton("Login");
		login.setBounds(550, 450, 80, 25);
		gui.add(login);
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginActionPerformed(e);
			}
		});
		setVisible(true);
	}

	/**
	 * ActionListener for Login button. Database to be implemented.
	 * 
	 * @param e
	 *            , ActionEvent
	 */
	private void loginActionPerformed(ActionEvent e) {
		userName = userNameField.getText();
		passWord = passWordField.getText();
		if (server.checkUser(userName, passWord) == true) {
			AfterLogin newWindow = new AfterLogin(userName, server);
			setVisible(false);
		} else {
			JOptionPane.showMessageDialog(this,
					"Login failed. Please check your ID and password.",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 
	 * @return userName as logged in user
	 */
	public String getuserName() {
		return userName;
	}
	

}
