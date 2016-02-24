package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import project2.Record;
import project2.Server;

public class CreateButton extends JFrame {
	private int x;
	private int y;
	private String type;
	JButton button;
	private Record record;
	private Server server;
	private String information;
	private BasicPanes p;
	private AfterLogin al;

	/**
	 * 
	 * @param x x-coordinate where the button should be placed
	 * @param y y-coordinate where the button should be placed
	 * @param createPane 
	 * @param type, what kind of function is wished, eg. read/create/delete profile
	 */
	public CreateButton(int x, int y, String type, Server server) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.record=null;
		this.server=server;
		
		
		
		button = new JButton(type);
		button.setBounds(x, y, 100, 40);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (type.equals("Read"))
					readAction(e);
				if (type.equals("Create"))
					createAction(e);
				if (type.equals("Delete"))
					deleteAction(e);
				if (type.equals("Edit"))
					editAction(e);
				if (type.equals("OK"))
					okAction(e);
				if (type.equals("Cancel"))
					cancelAction(e);
				if (type.equals("Confirm"))
					confirmAction(e);
			}
		});
	}

	/**
	 * Opens a window containing detail about selected information
	 * @param e
	 */
	private void readAction(ActionEvent e) {
		if(server.recordCheck() == true){
			ReadPane read = new ReadPane(server);
			p.setVisible(false);
		} else
			JOptionPane.showConfirmDialog(this, "You must select a record to read",
				"Error", JOptionPane.ERROR_MESSAGE);	
	}
	
	private void cancelAction (ActionEvent e){
		p.setVisible(false);
	}
	
	private void okAction (ActionEvent e){
		String s = getInfo();
		
		if(s!=null && server.createRecord(s)==true ){
			JPanel pane = new JPanel();
			pane.add(new JLabel("Successfully performed action"));
			JOptionPane.showConfirmDialog(this, pane,
					"Success!", JOptionPane.OK_CANCEL_OPTION);	
					p.setVisible(false);
			
		} else {
			JOptionPane.showMessageDialog(this,
					"Failed to perform action, check input",
					"Error", JOptionPane.ERROR_MESSAGE);
			
		}
	}
	
	private void confirmAction (ActionEvent e){
		String s = getInfo();
		
		if(s!=null && server.editRecord(s)==true ){
			JPanel pane = new JPanel();
			pane.add(new JLabel("Successfully performed action"));
			JOptionPane.showConfirmDialog(this, pane,
					"Success!", JOptionPane.OK_CANCEL_OPTION);	
					p.setVisible(false);
			
		} else {
			JOptionPane.showMessageDialog(this,
					"Failed to perform action, check input",
					"Error", JOptionPane.ERROR_MESSAGE);
			
		}
	}

	/**
	 * Deletes the selected information
	 * @param e
	 */
	private void deleteAction(ActionEvent e) {
		if(server.recordCheck() == true){
			DeletePane delete = new DeletePane(server, al);
		} else
			JOptionPane.showConfirmDialog(this, "You must select a record to read",
				"Error", JOptionPane.ERROR_MESSAGE);	
	}

	

	/**
	 * Creates a new profile
	 * @param e
	 */
	private void createAction(ActionEvent e) {
		CreatePane create = new CreatePane(server);

	}

	/**
	 * 
	 * @return a button
	 */
	public JButton getbutton() {
		return button;
	}
	private void editAction (ActionEvent e){
		if(server.recordCheck() == true){
			EditPane read = new EditPane(server);
			String data = p.getInfo();
			String[] m = data.split(",");
			server.writeTo(m[5]);
			p.setVisible(false);
		} else
			JOptionPane.showConfirmDialog(this, "You must select a record to read",
				"Error", JOptionPane.ERROR_MESSAGE);	
	}

	public String getInfo(){
		information=p.getInfo();
		return information;
	}
	public void setCurrentPane(BasicPanes p){
		this.p=p;
	}

	public void setAfterLogin(AfterLogin afterLogin) {
		al=afterLogin;
		
	}

	

}

