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
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import KrustyKookies.RecipeGUI.RadioListener;

public class StoreMaterialGUI implements Observer {
	private DataBase db;
	private JFrame f;
	private JTextField ingredient;
	private JTextField amount;
	private String unit;
	private JRadioButton g;
	private JRadioButton dl;
	private JLabel label;
	private boolean select=true;
	
	public StoreMaterialGUI(DataBase db) {
		this.db=db;
		db.addObserver(this);
		f = new JFrame("Kookie Production");
		f.getContentPane().setBackground(Color.PINK);
		f.setSize(400, 400);
		f.setLocation(400,200);
		f.getContentPane().setLayout(null);

		JLabel lblPleaseFillOut = new JLabel("Please fill out the fields below to store raw material");
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
		
		dl = new JRadioButton("dl");
		dl.setBounds(145, 201, 38, 23);
		f.getContentPane().add(dl);
		dl.addActionListener(new RadioListener());
		
		
		g = new JRadioButton("g");
		g.setBounds(185, 201, 38, 23);
		f.getContentPane().add(g);
		g.addActionListener(new RadioListener());
		ButtonGroup group = new ButtonGroup();
		group.add(dl);
		group.add(g);
		g.setEnabled(false);
		dl.setEnabled(false);
		
		amount = new JTextField();
		amount.setBounds(143, 134, 142, 25);
		f.getContentPane().add(amount);
		amount.setColumns(10);
		
		label = new JLabel("");
		label.setBounds(51, 180, 298, 14);
		f.getContentPane().add(label);
		
		f.setVisible(true);
	}
	class RadioListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getActionCommand().equals("dl")){
				unit = "dl";
			} else {
				unit = "g";
			}
			if(select==false){
				if(g.isSelected()){
					label.setText("This ingredient is measured in g");
					db.addRawMaterial(ingredient.getText(), amount.getText(), unit);	
				} else if (dl.isSelected()){
					label.setText("This ingredient is measured in dl");	
					db.addRawMaterial(ingredient.getText(), amount.getText(), unit);
				} 	
			}
		}
		
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
					String un = db.getUnit(ingredient.getText());
					if(un==null){
						label.setText("The Ingredient does not exist, choose unit to store");
					} else {
						if(un.equals("g")){
							unit="g";
							g.setSelected(true);
							label.setText("This ingredient is measured in gram");
						}else if(un.equals("dl")){
							unit="dl";
							dl.setSelected(true);
							label.setText("This ingredient is measured in dl");
						}
					}if(g.isSelected()==false && dl.isSelected()==false){
						String ObjButtons[] = { "Ok" };
						int result = JOptionPane.showOptionDialog(null,
								"Select gram or dl",
								"Confirmation", JOptionPane.DEFAULT_OPTION,
								JOptionPane.WARNING_MESSAGE, null, ObjButtons,
								ObjButtons[0]);
						if (result == 0) {
							g.setEnabled(true);
							dl.setEnabled(true);
							select = false;
					}
					}
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
