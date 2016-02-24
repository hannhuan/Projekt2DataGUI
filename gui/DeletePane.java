package gui;

import java.awt.Container;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import project2.Server;

public class DeletePane extends Observable {
	private Server server;
	private JFrame j;
	public DeletePane(Server server, AfterLogin al){
		this.server=server;	
		j = new JFrame();
		String ObjButtons[] = {"Yes", "No"};
		int result = JOptionPane.showOptionDialog(null, "Are you sure you want to delete this profile?", "Confirmation", 
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
		if (result == 0){
			Delete delete = new Delete();
			delete.Delete();
			setChanged();
			notifyObservers("Del");
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

