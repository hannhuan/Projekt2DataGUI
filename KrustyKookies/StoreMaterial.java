package KrustyKookies;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import KrustyKookies.PlaceOrder.DoneListener;


public class StoreMaterial implements Observer {
	private DataBase db;
	private JFrame f;
	private JTextField ingredient;
	private JTextField amount;
	
	public StoreMaterial(DataBase db) {
		this.db=db;
		db.addObserver(this);
		f = new JFrame("Kookie Production");
		f.getContentPane().setBackground(Color.PINK);
		f.setSize(400, 400);
		f.setLocation(400,200);
		f.getContentPane().setLayout(null);

		JLabel label = new JLabel("Please fill out the fields below to make an order");
		label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label.setBounds(10, 28, 364, 44);
		f.getContentPane().add(label);
		
		JButton btnDone = new JButton("Done");
		btnDone.setBounds(51, 272, 123, 39);
		btnDone.addActionListener(new DoneListener());
		f.getContentPane().add(btnDone);
		JButton cancel = new JButton("Cancel");
		cancel.setBounds(212, 272, 123, 39);
		cancel.addActionListener(new DoneListener());
		f.getContentPane().add(cancel);
		
		JLabel lblCustomer = new JLabel("Ingredient: ");
		lblCustomer.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblCustomer.setBounds(46, 83, 87, 25);
		f.getContentPane().add(lblCustomer);
		
		ingredient = new JTextField();
		ingredient.setBounds(143, 83, 142, 25);
		f.getContentPane().add(ingredient);
		ingredient.setColumns(10);
		
		JLabel label2 = new JLabel("Amount: ");
		label2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label2.setBounds(46, 136, 87, 25);
		f.getContentPane().add(label2);
		
		amount = new JTextField();
		amount.setColumns(10);
		amount.setBounds(143, 139, 142, 25);
		f.getContentPane().add(amount);
		
		f.setVisible(true);
	}

	class DoneListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getActionCommand().equals("Cancel")){
				f.setVisible(false);
			} else {
				if(ingredient.getText().equals("")||amount.getText().equals("")){
				JOptionPane.showMessageDialog(null, "Check Input");
				} else {
					ingredient.setEditable(false);
					amount.setEditable(false);
					db.addRawMaterial(ingredient.getText(), amount.getText() );
				}	
			}
		}	
	}
	@Override
	public void update(Observable o, Object arg) {
		String info = (String) arg;
		if(arg==null){
			JOptionPane.showMessageDialog(null,
					"Something went wrong, action not performed");
			ingredient.setEditable(true);
			amount.setEditable(true);
		} else if( info.equals("IngredientAdded")){
			JOptionPane.showMessageDialog(null,
					"Raw material successfully inserted into database");
			f.setVisible(false);
			
		}
	}
}
