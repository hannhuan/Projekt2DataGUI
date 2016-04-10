package KrustyKookies;


import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class RecipeGUI  implements Observer{
	private DataBase db;
	private JFrame f;
	private JTextField cookie;
	private JTextField ingredient;
	private JTextField amount;
	private DefaultListModel<String> ingredientModel;
	private JList<String> list;
	private JLabel label;
	private int count=0;
	
	public RecipeGUI(DataBase db){
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
	
	JLabel lblPleaseFillOut = new JLabel("Please fill out the fields below to add ingredients to a recipe ");
	lblPleaseFillOut.setFont(new Font("Tahoma", Font.PLAIN, 14));
	lblPleaseFillOut.setBounds(68, 26, 406, 71);
	f.getContentPane().add(lblPleaseFillOut);
	
	JLabel lblCookieSort = new JLabel("Cookie sort: ");
	lblCookieSort.setBounds(10, 123, 86, 14);
	f.getContentPane().add(lblCookieSort);
	
	cookie = new JTextField();
	cookie.setBounds(80, 120, 150, 20);
	f.getContentPane().add(cookie);
	cookie.setColumns(10);
	
	JLabel lblIngredient = new JLabel("Ingredient:");
	lblIngredient.setBounds(10, 162, 86, 17);
	f.getContentPane().add(lblIngredient);
	
	ingredient = new JTextField();
	ingredient.setBounds(80, 160, 150, 20);
	f.getContentPane().add(ingredient);
	ingredient.setColumns(10);
	
	JLabel lblAmount = new JLabel("Amount: ");
	lblAmount.setBounds(10, 205, 59, 14);
	f.getContentPane().add(lblAmount);
	
	amount = new JTextField();
	amount.setBounds(80, 202, 59, 20);
	f.getContentPane().add(amount);
	amount.setColumns(10);
	
	JButton btnAddMoreIngredients = new JButton("Add more \r\ningredients");
	btnAddMoreIngredients.setToolTipText("");
	btnAddMoreIngredients.addActionListener(new AddMoreIng());
	
	btnAddMoreIngredients.setBounds(61, 254, 169, 33);
	f.getContentPane().add(btnAddMoreIngredients);
	
	JRadioButton dl = new JRadioButton("dl");
	dl.setBounds(145, 201, 38, 23);
	f.getContentPane().add(dl);
	dl.addActionListener(new RadioListener());
	
	
	JRadioButton g = new JRadioButton("g");
	g.setBounds(185, 201, 38, 23);
	f.getContentPane().add(g);
	g.addActionListener(new RadioListener());
	ButtonGroup group = new ButtonGroup();
	group.add(dl);
	group.add(g);
	
	ingredientModel = new DefaultListModel<String>();
	list = new JList<String>(ingredientModel);
	list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	list.setBounds(259, 123, 195, 250);
	f.getContentPane().add(list);
	
	label = new JLabel("<Recipe>");
	label.setBounds(259, 98, 195, 23);
	f.getContentPane().add(label);
	f.setVisible(true);


	}
	class RadioListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getActionCommand().equals("dl")){
				count = 1;
			} else {
				count = 2;
			}
		}
		
	}
	class AddMoreIng implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			StringBuilder build = new StringBuilder();
			String input1 = cookie.getText();
			String input2 = ingredient.getText() + " ";
			String input3 = amount.getText();
			boolean integer = false;
			for(int i = 0; i < input3.length(); i++){
				 if (Character.getNumericValue(input3.charAt(i))>=0 && Character.getNumericValue(input3.charAt(i))<=9){
					 integer = true;
					 if(i==0 && Character.getNumericValue(input3.charAt(i))==0){
						 integer=false;
					 }
				 } else
					 integer = false;
			}
			if(input1.equals("")||input2.equals("")||input3.equals("")||count==0 || integer==false){
				JOptionPane.showMessageDialog(null,
						"Check input");
			} else {
				cookie.setEditable(false);
				label.setText(input1);
				if(count==1){
					build.append(input2).append(input3).append(" dl");
				} else {
					build.append(input2).append(input3).append(" g");
				}
				ingredientModel.addElement(build.toString());
				ingredient.setText("");
				amount.setText("");
				
			}
			
		}
		
	}
	class OKorCancel implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getActionCommand().equals("OK")){
				if (ingredientModel.isEmpty()){
					JOptionPane.showMessageDialog(null,
							"No recipe to add, please fill out the fields");					
				} else {
					List<String> ingredients = new ArrayList<String>();
					for(int i = 0; i < ingredientModel.getSize(); i++){
						String ingredient = ingredientModel.elementAt(i);
						System.out.println(ingredient);	
						ingredients.add(ingredient);
							
						
						
					}
					String ObjButtons[] = {"Yes", "No"};
					int result = JOptionPane.showOptionDialog(null, "Are you sure this recipe is finished?", "Confirmation", 
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
					if(result==0){
						db.addRecipe(cookie.getText(), ingredients);
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
				if(!list.isSelectionEmpty()){
					String text = list.getSelectedValue();
					int i = list.getSelectedIndex();
					String[] info = text.split(" ");
					ingredientModel.remove(i);
					ingredient.setText(info[0]);
					amount.setText(info[1]);
					
				} else 
					JOptionPane.showMessageDialog(null,
							"You must select a record to read");
		}
		
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		String info = (String) arg1;
		if(arg1==null){
			JOptionPane.showMessageDialog(null,
					"Something went wrong, action not performed");
		}
		else if(info.equals("ExistsAlready")){
			String ObjButtons[] = {"Change Cookie name", "Compare recipe with Existing Cookie", "Cancel"};
			int result = JOptionPane.showOptionDialog(null, "The Cookie name already exists in the data base", "Cookie exists already", 
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
			if(result == 0){
				cookie.setEditable(true);
			} else if (result == 1) {
				String recipe = db.getRecipe(cookie.getText());
				//RecipeGUI rp = new RecipeGUI(cookie.getText(), recipe);
			} else {
				f.setVisible(false);
			} 
				
		} else if (info.equals("recipeAdded")){
			JOptionPane.showMessageDialog(null,
					"Recipe successfully inserted into database");
			f.setVisible(false);
		} 
	}
}