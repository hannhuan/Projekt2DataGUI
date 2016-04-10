package KrustyKookies;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;


import javax.swing.JTextField;

import java.awt.Choice;

public class PlaceOrderGUI implements Observer {
	private DataBase db;
	private JFrame f;
	private String customer;
	private String cookie;
	private JTextField nbrPallets;
	private DefaultListModel<String> orderModel;
	private JList<String> orderList;
	private JTextField date;
	private List<String> customers;
	private List<String> cookies;
	private Choice customerChoice;
	private Choice cookieChoice;
	

	public PlaceOrderGUI(DataBase db) {
		this.db = db;
		db.addObserver(this);

		f = new JFrame("KrustyKookies Production");
		f.getContentPane().setBackground(Color.PINK);
		f.setSize(500, 500);
		f.setLocation(300, 200);

		JButton edit = new JButton("Edit input");
		edit.setBounds(335, 380, 120, 20);
		edit.addActionListener(new editListener());
		f.getContentPane().setLayout(null);
		f.getContentPane().add(edit);

		JButton ok = new JButton("OK");
		ok.setBounds(15, 405, 113, 42);
		ok.addActionListener(new OKorCancel());
		f.getContentPane().add(ok);

		JButton cancel = new JButton("Cancel");
		cancel.setBounds(143, 405, 113, 42);
		cancel.addActionListener(new OKorCancel());
		f.getContentPane().add(cancel);

		JLabel lblPleaseFillOut = new JLabel(
				"Please fill out the fields below to place an order ");
		lblPleaseFillOut.setBounds(100, 26, 406, 30);
		lblPleaseFillOut.setFont(new Font("Tahoma", Font.PLAIN, 14));
		f.getContentPane().add(lblPleaseFillOut);
		
		JLabel lblCustomer = new JLabel("Customer: ");
		lblCustomer.setBounds(10, 123, 68, 14);
		f.getContentPane().add(lblCustomer);

		JLabel lblcookie = new JLabel("Cookie:");
		lblcookie.setBounds(10, 162, 58, 17);
		f.getContentPane().add(lblcookie);

		JLabel lblAmount = new JLabel("Number of Pallets: ");
		lblAmount.setBounds(10, 205, 118, 17);
		f.getContentPane().add(lblAmount);

		nbrPallets = new JTextField();
		nbrPallets.setBounds(138, 202, 92, 20);
		f.getContentPane().add(nbrPallets);
		nbrPallets.setColumns(10);

		JButton btnAddMoreIngredients = new JButton("+ cookies to order");
		btnAddMoreIngredients.setBounds(61, 303, 169, 33);
		btnAddMoreIngredients.setToolTipText("");
		btnAddMoreIngredients.addActionListener(new AddMoreCookies());
		f.getContentPane().add(btnAddMoreIngredients);

		orderModel = new DefaultListModel<String>();
		orderList = new JList<String>(orderModel);
		orderList.setBounds(259, 123, 195, 250);
		orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		f.getContentPane().add(orderList);

		date = new JTextField();
		date.setBounds(138, 251, 92, 20);
		date.setColumns(10);
		f.getContentPane().add(date);

		JLabel lblDate = new JLabel("Date: YYYY-MM-DD");
		lblDate.setBounds(15, 251, 113, 17);
		f.getContentPane().add(lblDate);
		
		customerChoice = new Choice();
		customerChoice.setBounds(78, 117, 152, 20);
		customerChoice.setFont(new Font("Arial", Font.PLAIN, 11));
		customerChoice.add("Choose customer");
		customers = db.getCustomers(); 
		for(String s: customers){
			customerChoice.add(s);
		}
		customerChoice.addItemListener(new Items());
		f.getContentPane().add(customerChoice);
		
		Choice cookieChoice = new Choice();
		cookieChoice.setBounds(78, 159, 152, 20);
		cookieChoice.setFont(new Font("Arial", Font.PLAIN, 11));
		cookieChoice.add("Choose cookie");
		cookies = db.getCookies(); 
		for(String s: cookies){
			cookieChoice.add(s);
		}
		cookieChoice.addItemListener(new Items());
		f.getContentPane().add(cookieChoice);
		
		JLabel lblCookieAmount = new JLabel("Cookie     |     Amount");
		lblCookieAmount.setBounds(259, 98, 152, 14);
		f.getContentPane().add(lblCookieAmount);

		f.setVisible(true);

	}

	class AddMoreCookies implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			StringBuilder build = new StringBuilder();
			String input3 = nbrPallets.getText();
			String input4 = date.getText();
			build.append(cookie).append(" ").append(input3);
			if (input3.equals("") || input4.equals("")) {
				JOptionPane.showMessageDialog(null, "Please fill out all fields");
			} else {
				String checkNbrPallets = CheckInput.checkNbr(nbrPallets.getText());
				String checkDate = CheckInput.checkDate(date.getText());
				String checkChoose = CheckInput.checkChoose(customer, cookie);
				if(checkNbrPallets.equals("invalid") || checkDate.equals("invalid") || checkChoose.equals("invalid") ){
					if (checkNbrPallets.equals("invalid")) {
						JOptionPane.showMessageDialog(null, "Invalid pallet amount");
					}
					if (checkDate.equals("invalid")) {
						JOptionPane.showMessageDialog(null, "Invalid date format or date cannot be chosen");	
					}
					if (checkChoose.equals("invalid")) {
						JOptionPane.showMessageDialog(null, "Invalid choice for customer or cookie");	
					}
					
				} else {
					nbrPallets.setText("");
					date.setEditable(false);
					customerChoice.setEnabled(false);
					orderModel.addElement(build.toString());
				}
			}
		}
	}

	class OKorCancel implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getActionCommand().equals("OK")) {
				if (orderModel.isEmpty()) {
					JOptionPane.showMessageDialog(null,
							"No order to add, please fill out the fields");
				} else {
					List<String> orders = new ArrayList<String>();
					for (int i = 0; i < orderModel.getSize(); i++) {
						String order = orderModel.elementAt(i);
						System.out.println(order);
						orders.add(order);
					}
					String ObjButtons[] = { "Yes", "No" };
					int result = JOptionPane.showOptionDialog(null,
							"Are you sure this order is finished?",
							"Confirmation", JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE, null, ObjButtons,
							ObjButtons[1]);
					if (result == 0) {
						db.addOrder(customer, orders, date.getText());
					}
				}
			} else {
				String ObjButtons[] = { "Yes", "No" };
				int result = JOptionPane.showOptionDialog(null,
						"Are you sure you want to discard this recipe?",
						"Confirmation", JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, ObjButtons,
						ObjButtons[1]);
				if (result == 0) {
					f.setVisible(false);
				}
			}

		}

	}

	class editListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (!orderList.isSelectionEmpty()) {
				String text = orderList.getSelectedValue();
				int i = orderList.getSelectedIndex();
				String[] info = text.split(" ");
				orderModel.remove(i);
				nbrPallets.setText(info[1]);

			} else
				JOptionPane.showMessageDialog(null,
						"You must select an order item to be able to edit");
		}

	}
	class Items implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent arg) {
			String input = arg.getItem().toString();
			if(cookies.contains(input)){
				cookie = input;
			} else if (customers.contains(input)){
				customer = input;
			}		
		}
	}	

	@Override
	public void update(Observable arg0, Object arg1) {
		String info = (String) arg1;
		if (info.equals("failed")) {
			JOptionPane.showMessageDialog(null,
					"Something went wrong");
		} else if (info.equals("invalidFormat")) {
			JOptionPane.showMessageDialog(null,
					"Invalid format for inserted amount, please edit");
		} else {
			JOptionPane.showMessageDialog(null,
					"Order inserted, your orderID is" + arg1);
		PlaceOrderGUI po = new PlaceOrderGUI(db);
		db.deleteObserver(this);
		f.setVisible(false);
		}	

	}
}