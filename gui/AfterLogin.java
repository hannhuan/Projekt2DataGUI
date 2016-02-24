package gui;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import project2.Record;
import project2.Server;

public class AfterLogin extends JFrame implements Observer{
	private String UId;
	private int GId;
	private String name;
	private String division;
	private Server server;
	private DefaultListModel<String> list;
	private JList <String> selectList;
	private ArrayList<Record> records;
	private CreateButton CB;
	private CreateButton EB;
	private CreateButton DB;

	public AfterLogin(String userName, Server server) {
		this.list = new DefaultListModel<String>();
		this.selectList = new JList<String>(list);
		this.server = server;
		this.UId = userName;
		createDemo();
	}

	private void createDemo() {
		Container gui = getContentPane();
		gui.setLayout(null);

		setTitle("Hospital Project");
		setSize(1200, 800);

		JLabel logIn = new JLabel("You are logged in as " + server.getName(UId));
		logIn.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		logIn.setBounds(25, 20, 500, 10);
		gui.add(logIn);

		// Stuff from database should be sent in. Following codes are for
		// testing.
		this.GId = server.getGId();
		records = server.findUsersRecords(UId, GId,
				server.getDivision());
		for (Record r : records) {
			String s = r.getNbr() + ": " + r.getTitle();
			list.addElement(s);
			
		}
		selectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectList.addListSelectionListener(new RecordListener());
		selectList.setBounds(100, 100, 800, 550);
		selectList.setBorder(BorderFactory.createEtchedBorder());
		
		gui.add(selectList);
		
		
		
		// Always create read-button. Delete and Create-button will exist if
		// user has the access to the database
		CreateButton RB = new CreateButton(950, 200, "Read", server);
		gui.add(RB.getbutton());
			
		RB.setAfterLogin(this);
		if (GId == 4) {
			DB = new CreateButton(950, 400, "Delete", server);
			gui.add(DB.getbutton());
			

		}
		if (GId == 2) {
			EB = new CreateButton(950, 500, "Edit", server);
			gui.add(EB.getbutton());			
		}
		if (GId == 3) {
			CB = new CreateButton(950, 300, "Create", server);
			gui.add(CB.getbutton());
			EB = new CreateButton(950, 500, "Edit", server);
			gui.add(EB.getbutton());

		}
		setVisible(true);
	}

	class RecordListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent arg0) {
				if (selectList.isSelectionEmpty()) {
					return;
				}
				String record = (String) selectList.getSelectedValue();
				server.setRecord(record);
		}
	
		
	}


	@Override
	public void update(Observable o, Object arg) {
		records = server.findUsersRecords(UId, GId,
				server.getDivision());
		for (Record r : records) {
			String s = r.getNbr() + ": " + r.getTitle();
			list.addElement(s);	
		}
		
	}
}
