package gui;

import java.awt.Container;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import project2.Server;

public class DeletePane  {
	private Server server;

	public DeletePane(Server server){
		this.server=server;	
		String ObjButtons[] = {"Yes", "No"};
		int result = JOptionPane.showOptionDialog(null, "Are you sure you want to delete this profile?", "Confirmation", 
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
		if (result == 0){
			Delete delete = new Delete();
			delete.Delete();
		}
		
	}
	
	
	/**
	 * Deletes the selectedinformation.
	 */
	class Delete extends JFrame{
		
		private void Delete(){
			if(server.delete()==true){
				JOptionPane.showConfirmDialog(this, "Record was successfully deleted",
						"Success!", JOptionPane.OK_CANCEL_OPTION);
		
						
			} else
				JOptionPane.showConfirmDialog(this, "Record was not deleted",
						"Error", JOptionPane.ERROR_MESSAGE);
			
		}
	}
	
	

}

