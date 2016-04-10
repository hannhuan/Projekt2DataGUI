package KrustyKookies;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import KrustyKookies.StoreMaterialGUI.DoneListener;
import KrustyKookies.StoreMaterialGUI.RadioListener;

public class CreatePalletsGUI implements Observer{

	private DataBase db;
	private JFrame f;
	private JTextField cookieField;
	private JTextField amount;
	private String unit;
	private JLabel label;
	
	public CreatePalletsGUI(DataBase db) {
		this.db=db;
		db.addObserver(this);
		f = new JFrame("Kookie Production");
		f.getContentPane().setBackground(Color.PINK);
		f.setSize(400, 400);
		f.setLocation(400,200);
		f.getContentPane().setLayout(null);

		JLabel lblPleaseFillOut = new JLabel("Please fill out the fields below to create Pallets");
		lblPleaseFillOut.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPleaseFillOut.setBounds(10, 28, 364, 44);
		f.getContentPane().add(lblPleaseFillOut);
		
		JButton btnDone = new JButton("Done");
		btnDone.setBounds(51, 272, 123, 39);
		btnDone.addActionListener(new DoneListener());
		f.getContentPane().add(btnDone);
		JButton cancel = new JButton("Cancel");
		cancel.setBounds(212, 272, 123, 39);
		cancel.addActionListener(new DoneListener());
		f.getContentPane().add(cancel);
		
		JLabel cookie = new JLabel("Cookie Name: ");
		cookie.setFont(new Font("Tahoma", Font.PLAIN, 13));
		cookie.setBounds(46, 83, 87, 25);
		f.getContentPane().add(cookie);
		
		cookieField = new JTextField();
		cookieField.setBounds(143, 83, 142, 25);
		f.getContentPane().add(cookieField);
		cookieField.setColumns(10);
		
		JLabel amountLabel = new JLabel("Amount: ");
		amountLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		amountLabel.setBounds(46, 136, 87, 25);
		f.getContentPane().add(amountLabel);
		
		amount = new JTextField();
		amount.setBounds(143, 134, 142, 25);
		f.getContentPane().add(amount);
		amount.setColumns(10);
		

		
		f.setVisible(true);
	}
	
	class DoneListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Cancel")){
				f.setVisible(false);
			} else {
				cookieField.setEditable(false);
				amount.setEditable(false);
				db.createPallets(cookieField.getText(), amount.getText());
				
			}
		}
		
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		String info = (String) arg1;
		if(arg1==null){
			JOptionPane.showMessageDialog(null,
					"Something went wrong, action not performed");
			cookieField.setEditable(true);
			amount.setEditable(true);
		} else if( info.equals("created")){
			JOptionPane.showMessageDialog(null,
					"Pallets successfully inserted into database");
			f.setVisible(false);
			
		}
	}

}
