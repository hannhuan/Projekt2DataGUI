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

import KrustyKookies.LoginGUI.EnterListener;
import java.awt.Font;


public class SearchGUI implements Observer{
	private DataBase db;
	private JFrame f;

	public SearchGUI(DataBase db) {
		this.db=db;
		db.addObserver(this);
		
		f = new JFrame("KrustyKookies Production");
		f.getContentPane().setBackground(Color.PINK);
		f.setSize(355, 250);
		f.setLocation(300,200);
		f.getContentPane().setLayout(null);
		
		JButton barcode = new JButton("Barcode");
		barcode.setBounds(42, 96, 89, 40);
		barcode.addActionListener(new BarcodeListener());
		f.getContentPane().add(barcode);
		
		JButton prodTime = new JButton("Prod. time");
		prodTime.setBounds(191, 96, 105, 40);
		prodTime.addActionListener(new TimeListener());
		f.getContentPane().add(prodTime);
		
		JLabel lblSearchByBarcode = new JLabel("Search by:");
		lblSearchByBarcode.setFont(new Font("Sitka Small", Font.PLAIN, 14));
		lblSearchByBarcode.setBounds(122, 40, 160, 23);
		f.getContentPane().add(lblSearchByBarcode);
		f.setVisible(true);
	}
	
	class BarcodeListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			SearchBarcodeGUI barcodeSearch = new SearchBarcodeGUI(db);
			if(db.countObservers()>1){
				db.notifyObservers("close");
			}
			System.out.println(db.countObservers());
		}
	}	
	class TimeListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			SearchByTimeGUI timeSearch = new SearchByTimeGUI(db);
			if(db.countObservers()>1){
				db.notifyObservers("close");
			}
			System.out.println(db.countObservers());
		}
		
	}
	@Override
	public void update(Observable o, Object arg) {
		
	}
}
