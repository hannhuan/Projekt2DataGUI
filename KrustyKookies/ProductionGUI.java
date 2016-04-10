package KrustyKookies;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.UIManager;

public class ProductionGUI implements Observer {
	private DataBase db;
	private JFrame f;
	private JPanel myPanel;

	public ProductionGUI(DataBase db) {
		this.db = db;

		f = new JFrame("KrustyKookies Production");
		f.getContentPane().setBackground(Color.PINK);
		f.setSize(400, 400);
		f.setLocation(300, 200);
		f.getContentPane().setLayout(null);

		JLabel header = new JLabel("Welcome to production management!");
		header.setBounds(70, 11, 382, 76);
		header.setFont(new Font("Tahoma", Font.PLAIN, 15));
		header.setForeground(new Color(0, 0, 0));
		f.getContentPane().add(header);
		
		JButton storeRawMaterial = new JButton("Store Raw Material");
		storeRawMaterial.setBounds(193, 116, 159, 49);
		storeRawMaterial.addActionListener(new ClickListener());
		f.getContentPane().add(storeRawMaterial);
		
		JButton createPallets = new JButton("Create Pallet(s)");
		createPallets.setBounds(35, 116, 148, 49);
		createPallets.addActionListener(new ClickListener()) ;
		f.getContentPane().add(createPallets);
		
		JButton help = new JButton("HELP ME!");
		help.setBounds(139, 195, 98, 33);
		help.addActionListener(new ClickListener()) ;
		f.getContentPane().add(help);

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Add...");
		menu.setBackground(UIManager.getColor("Button.light"));
		menu.setBounds(0, 0, 107, 22);

		JMenuItem customer = new JMenuItem("Customer");
		customer.addActionListener(new CListener());
		menu.add(customer);
		JMenuItem recipe = new JMenuItem("Recipe");
		recipe.addActionListener(new RListener());
		menu.add(recipe);

		menuBar.add(menu);
		f.setJMenuBar(menuBar);
		
		JMenu mnRemove = new JMenu("Remove...");
		menuBar.add(mnRemove);
		
		JMenuItem mntmCustomer = new JMenuItem("Customer");
		mnRemove.add(mntmCustomer);
		mntmCustomer.addActionListener(new RemoveCustomer());
		
		
		JMenuItem mntmRecipe = new JMenuItem("Recipe");
		mnRemove.add(mntmRecipe);
		mntmRecipe.addActionListener(new RemoveRecipe());
		db.addObserver(this);
		f.setVisible(true);
	}

	class ClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getActionCommand().equals("Store Raw Material") ) {
				StoreMaterialGUI sm = new StoreMaterialGUI(db);
			} else if(arg0.getActionCommand().equals("Create Pallet(s)") ) {
				CreatePalletsGUI cp = new CreatePalletsGUI(db);
			}else if(arg0.getActionCommand().equals("HELP ME!") ) {
				JOptionPane.showMessageDialog(null,
						"Help will always be given at hogwarts to those who ask for it" + "\n" + "- Dumbledore");
			}
		}	
	}

	class CListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			JTextField name = new JTextField(15);
			JTextField address = new JTextField(15);

			myPanel = new JPanel();
			myPanel.add(new JLabel("Fill in Customer name:"));
			myPanel.add(name);
			myPanel.add(Box.createHorizontalStrut(15)); // a spacer
			myPanel.add(new JLabel("Fill in Customer address:"));
			myPanel.add(address);
			int result = JOptionPane.showConfirmDialog(null, myPanel,
					"Add customer information", JOptionPane.OK_CANCEL_OPTION);
			String customerN = name.getText();
			String customerA = address.getText();
			if (result == 0) {
				while (customerN.equals("") || customerA.equals("")) {
					System.out.println(result);
					result = JOptionPane.showConfirmDialog(null, myPanel,
							"Add customer information",
							JOptionPane.OK_CANCEL_OPTION);
					customerN = name.getText();
					customerA = address.getText();
					if (result != 0) {
						return;
					}
				}
				db.addCustomer(customerN, customerA);
			}
		}
	}

	class RListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			RecipeGUI k3 = new RecipeGUI(db);

		}
	}
	class RemoveCustomer implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextField name = new JTextField(15);
			myPanel = new JPanel();
			myPanel.add(new JLabel("Fill in Customer name:"));
			myPanel.add(name);
			int result = JOptionPane.showConfirmDialog(null, myPanel,
					"Remove customer", JOptionPane.OK_CANCEL_OPTION);
			String customerN = name.getText();
			if (result == 0) {
				while (customerN.equals("")) {
					result = JOptionPane.showConfirmDialog(null, myPanel,
							"Add customer information",
							JOptionPane.OK_CANCEL_OPTION);
					customerN = name.getText();
					if (result != 0) {
						return;
					}
				}
				db.removeCustomer(customerN);
			
			}
		}
	}	
	
	class RemoveRecipe implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextField name = new JTextField(15);

			myPanel = new JPanel();
			myPanel.add(new JLabel("Fill in Recipe name:"));
			myPanel.add(name);
			int result = JOptionPane.showConfirmDialog(null, myPanel,
					"Remove Recipe", JOptionPane.OK_CANCEL_OPTION);
			String cookieN = name.getText();
			if (result == 0) {
				while (cookieN.equals("")) {
					result = JOptionPane.showConfirmDialog(null, myPanel,
							"Add recipe information",
							JOptionPane.OK_CANCEL_OPTION);
					cookieN = name.getText();
					if (result != 0) {
						return;
					}
				}
				db.removeRecipe(cookieN);

			}
		}
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		String success = (String) arg1;
		if(arg1!=null){
			if (success.equals("success")) {
				JOptionPane.showMessageDialog(null, "Customer successfully added to database");
			} else if(success.equals("nope")) {
				JOptionPane.showMessageDialog(null, "Customer was not added to database");
			}else if (success.equals("recipeRemoved")){
				JOptionPane.showMessageDialog(null,
						"Recipe successfully removed from database");
			} else if (success.equals("DoesNotExist")){
				JOptionPane.showMessageDialog(null,
						"Item could not be found in database");
			}else if (success.equals("costumersRemoved")){
				JOptionPane.showMessageDialog(null,
						"Customer successfully removed from database");
			}	
		}
	}
}