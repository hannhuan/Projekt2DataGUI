package KrustyKookies;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;

import KrustyKookies.KrustGui3.AddMoreIng;
import KrustyKookies.KrustGui3.IngredientListener;
import KrustyKookies.KrustGui3.OKorCancel;
import KrustyKookies.KrustGui3.RadioListener;
import KrustyKookies.RecipeGUI.DoneListener;

import javax.swing.JTextField;


public class PlaceOrder implements Observer{
	private DataBase db;
	private JFrame f;
	private JTextField customer;
	private JTextField cookie;
	private JTextField nbrPallets;
	private DefaultListModel<String> orderModel;
	private JList<String> orderList;
	private JTextField date;
	
	public PlaceOrder(DataBase db){
	this.db=db;
	db.addObserver(this);
	
	f = new JFrame("KrustyKookies Production");
	f.getContentPane().setBackground(Color.PINK);
	f.setSize(500, 500);
	f.setLocation(300,200);
	f.getContentPane().setLayout(null);
	
	JButton edit = new JButton("Edit input");
	edit.setBounds(335, 380, 120, 20);
	edit.addActionListener(new IngredientListener());
	f.getContentPane().add(edit);
	
	JButton ok = new JButton("OK");
	ok.setBounds(15, 405, 113, 42);
	ok.addActionListener(new OKorCancel());
	f.getContentPane().add(ok);
	
	JButton cancel = new JButton("Cancel");
	cancel.setBounds(143, 405, 113, 42);
	cancel.addActionListener(new OKorCancel());
	f.getContentPane().add(cancel);
	
	JLabel lblPleaseFillOut = new JLabel("Please fill out the fields below to place an order ");
	lblPleaseFillOut.setFont(new Font("Tahoma", Font.PLAIN, 14));
	lblPleaseFillOut.setBounds(68, 26, 406, 71);
	f.getContentPane().add(lblPleaseFillOut);
	
	JLabel lblCustomer = new JLabel("Customer: ");
	lblCustomer.setBounds(10, 123, 86, 14);
	f.getContentPane().add(lblCustomer);
	
	customer = new JTextField();
	customer.setBounds(80, 120, 150, 20);
	f.getContentPane().add(customer);
	customer.setColumns(10);
	
	JLabel lblcookie = new JLabel("Cookie sort:");
	lblcookie.setBounds(10, 162, 86, 17);
	f.getContentPane().add(lblcookie);
	
	cookie = new JTextField();
	cookie.setBounds(80, 160, 150, 20);
	f.getContentPane().add(cookie);
	cookie.setColumns(10);
	
	JLabel lblAmount = new JLabel("Number of Pallets: ");
	lblAmount.setBounds(10, 205, 118, 17);
	f.getContentPane().add(lblAmount);
	
	nbrPallets = new JTextField();
	nbrPallets.setBounds(138, 202, 92, 20);
	f.getContentPane().add(nbrPallets);
	nbrPallets.setColumns(10);
	
	JButton btnAddMoreIngredients = new JButton("Add more cookies to order");
	btnAddMoreIngredients.setToolTipText("");
	btnAddMoreIngredients.addActionListener(new AddMoreCookies());
	
	btnAddMoreIngredients.setBounds(61, 303, 169, 33);
	f.getContentPane().add(btnAddMoreIngredients);
	
	orderModel = new DefaultListModel<String>();
	orderList = new JList<String>(orderModel);
	orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	orderList.setBounds(259, 123, 195, 250);
	f.getContentPane().add(orderList);
	
	date = new JTextField();
	date.setColumns(10);
	date.setBounds(138, 251, 92, 20);
	f.getContentPane().add(date);
	
	JLabel lblDate = new JLabel("Date: YYYY-MM-DD");
	lblDate.setBounds(15, 251, 113, 17);
	f.getContentPane().add(lblDate);
	
	
	f.setVisible(true);


	}
	class AddMoreCookies implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			StringBuilder build = new StringBuilder();
			String input1 = customer.getText();
			String input2 = cookie.getText() + " ";
			String input3 = nbrPallets.getText();
			build.append(input2).append(input3);
			if(input1.equals("")||input2.equals("")||input3.equals("")){
				JOptionPane.showMessageDialog(null,
						"Check input");
			} else {
				customer.setEditable(false);
				date.setEditable(false);
				orderModel.addElement(build.toString());
				cookie.setText("");
				nbrPallets.setText("");
				
			}
			
		}
		
	}
	class OKorCancel implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getActionCommand().equals("OK")){
				if (orderModel.isEmpty()){
					JOptionPane.showMessageDialog(null,
							"No order to add, please fill out the fields");					
				} else {
					List<String> orders = new ArrayList<String>();
					for(int i = 0; i < orderModel.getSize(); i++){
						String order = orderModel.elementAt(i);
						System.out.println(order);	
						orders.add(order);
					}
					String ObjButtons[] = {"Yes", "No"};
					int result = JOptionPane.showOptionDialog(null, "Are you sure this order is finished?", "Confirmation", 
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
					if(result==0){
						db.addOrder(customer.getText(), orders, date.getText());
					}
				}
			} else {
				String ObjButtons[] = {"Yes", "No"};
				int result = JOptionPane.showOptionDialog(null, "Are you sure you want to discard this recipe?", "Confirmation", 
						JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
				if (result == 0){
					f.setVisible(false);
				}
			}
			
		}
		
	}
	
	class IngredientListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
				if(!orderList.isSelectionEmpty()){
					String text = orderList.getSelectedValue();
					int i = orderList.getSelectedIndex();
					String[] info = text.split(" ");
					orderModel.remove(i);
					cookie.setText(info[0]);
					nbrPallets.setText(info[1]);
					
				} else 
					JOptionPane.showMessageDialog(null,
							"You must select a record to read");
		}
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		String info = (String) arg1;
		if(info.equals("failed")){
			System.out.println("nope");
		} else if (info.equals("succeded") ){
			System.out.println("yaaaaaaaaaaaaai");
		}
		
	}
}	