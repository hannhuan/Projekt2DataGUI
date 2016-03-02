package KrustyKookies;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import KrustyKookies.KrustGui.EnterListener;


public class Barcode1 implements Observer{
	private DataBase db;
	private JFrame f;
	private JTextField textField;

	public Barcode1(DataBase db) {
		this.db=db;
		db.addObserver(this);
		
		f = new JFrame("KrustyKookies Production");
		f.getContentPane().setBackground(Color.PINK);
		f.setSize(355, 250);
		f.setLocation(300,200);
		f.getContentPane().setLayout(null);
		
		JLabel BarcodeSequence = new JLabel("Insert Barcode Sequence: ");
		BarcodeSequence.setBounds(98, 44, 173, 32);
		f.getContentPane().add(BarcodeSequence);
		
		textField = new JTextField();
		textField.setBounds(98, 87, 144, 20);
		f.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton ok = new JButton("OK");
		ok.setBounds(124, 138, 89, 40);
		f.getContentPane().add(ok);
		ok.addActionListener(new EnterListener());
		f.setVisible(true);
	}
	class EnterListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String barcode = textField.getText();
			textField.setText("");
		}
		
	}

	@Override
	public void update(Observable o, Object arg) {
		
	}
}
