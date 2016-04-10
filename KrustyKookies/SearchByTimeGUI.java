package KrustyKookies;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class SearchByTimeGUI implements Observer  {
	private DataBase db;
	private JFrame f;
	private JTextField startTime;
	private JButton blocked;
	private JButton unblocked;
	private JButton status;
	private JTextField cookie;
	private JTextField endTime;
	private JTextArea searchArea;
	private Map<String, List<String>> pallets;

	public SearchByTimeGUI(DataBase db) {
		this.db = db;
		//pallets = null;

		f = new JFrame("KrustyKookies Production");
		f.getContentPane().setBackground(Color.PINK);
		f.setSize(1000, 700);
		f.setLocation(200, 0);
		f.getContentPane().setLayout(null);
		JLabel time = new JLabel("Insert Time Interval:");
		time.setBounds(85, 9, 136, 24);
		f.getContentPane().add(time);

		startTime = new JTextField();
		startTime.setFont(new Font("Tahoma", Font.ITALIC, 11));
		startTime.setForeground(Color.GRAY);
		startTime.setText("start");
		startTime.setBounds(214, 11, 124, 20);
		startTime.addKeyListener(new startPressListener());
		f.getContentPane().add(startTime);
		startTime.setColumns(10);

		JButton ok = new JButton("Search");
		ok.setBounds(392, 49, 91, 23);
		ok.addActionListener(new EnterListener());
		f.getContentPane().add(ok);

		JLabel explain = new JLabel("YYYY-MM-DD HH:MM");
		explain.setFont(new Font("Tahoma", Font.ITALIC, 11));
		explain.setForeground(Color.GRAY);
		explain.setBounds(214, 34, 146, 14);
		f.getContentPane().add(explain);

		unblocked = new JButton("Unblock");
		unblocked.setBounds(543, 30, 91, 23);
		unblocked.addActionListener(new UnBlockedListener());
		f.getContentPane().add(unblocked);

		status = new JButton("Change Status");
		status.setBounds(754, 30, 146, 23);
		status.addActionListener(new StatusListener());
		f.getContentPane().add(status);

		JLabel lblInsertCookie = new JLabel("Insert Cookie :");
		lblInsertCookie.setBounds(85, 53, 114, 14);
		f.getContentPane().add(lblInsertCookie);

		cookie = new JTextField();
		cookie.setColumns(10);
		cookie.setBounds(214, 50, 124, 20);
		f.getContentPane().add(cookie);

		endTime = new JTextField();
		endTime.setFont(new Font("Tahoma", Font.ITALIC, 11));
		endTime.setForeground(Color.GRAY);
		endTime.setText("end");
		endTime.setColumns(10);
		endTime.setBounds(372, 11, 124, 20);
		endTime.addKeyListener(new endPressListener());
		f.getContentPane().add(endTime);

		JLabel label = new JLabel("YYYY-MM-DD HH:MM");
		label.setForeground(Color.GRAY);
		label.setFont(new Font("Tahoma", Font.ITALIC, 11));
		label.setBounds(372, 34, 146, 14);
		f.getContentPane().add(label);

		searchArea = new JTextArea();
		searchArea.setBounds(85, 115, 815, 505);
		searchArea.setEditable(false);
		f.getContentPane().add(searchArea);

		blocked = new JButton("Block");
		blocked.setBounds(644, 30, 89, 23);
		blocked.addActionListener(new BlockedListener());
		f.getContentPane().add(blocked);
		f.setVisible(true);

	}

	class startPressListener implements KeyListener {
		public void keyPressed(KeyEvent arg0) {
			startTime.setText("");
			startTime.setFont(new Font("Tahoma", Font.PLAIN, 11));
			startTime.setForeground(Color.BLACK);
			startTime.removeKeyListener(this);

		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}
	}

	class endPressListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			endTime.setText("");
			endTime.setFont(new Font("Tahoma", Font.PLAIN, 11));
			endTime.setForeground(Color.BLACK);
			endTime.removeKeyListener(this);
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}
	}

	class EnterListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (endTime.equals("") || startTime.equals("") || cookie.equals("")) {
				JOptionPane.showMessageDialog(null,
						"Please fill out all fields");
			} else {
				pallets = db.searchTime(startTime.getText(), endTime.getText(),
						cookie.getText());
				printPallets();

			}

		}
	}

	class StatusListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if (pallets != null) {
				Map<String, List<String>> temp = new HashMap<String, List<String>> ();
				for (List<String> val : pallets.values()){
					if(val.get(4).equals("Not Blocked")){
						List<String> list = new ArrayList<String>();
						for(String s: val){
							list.add(s);
						}
						temp.put(val.get(0), list);
					}
						
				}
				db.statusAll(temp);
				for (List<String> values : pallets.values()) {
					boolean exist = false;
					for(List<String> t : temp.values()){
						if(t.get(0).equals(values.get(0))){
							exist=true;
						}
					} 
					if(exist==true){
					switch (values.get(3)) {
					case "Just Produced":
						values.set(3, "Freezer");
						break;
					case "Freezer":
						values.set(3, "Transport");
						break;
					case "Transport":
						values.set(3, "Delivered");
						break;
					}
					}
				}
				printPallets();
			} else
				JOptionPane.showMessageDialog(null,
						"Please fill out all the fields");
		}
	}

	class BlockedListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (pallets != null ) {
				List<String> dates = db.blockAll(pallets);
				int i = 0;
				if(!dates.isEmpty()){
				for (List<String> values : pallets.values()) {
					if(values.get(4).equals("Not Blocked") && (values.get(3).equals("Freezer"))){
					values.set(4, dates.get(i));
					i++;
					}
				}
				printPallets();
				}
			}else
				JOptionPane.showMessageDialog(null,
						"Please fill out all the fields");
		}

	}


	class UnBlockedListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (pallets != null) {
				db.unBlockAll(pallets);
				for (List<String> values :pallets.values()){
					if(!values.get(4).equals("Not Blocked")){
						values.set(4, "Not Blocked");
						}
				}
			printPallets();	
			} else
				JOptionPane.showMessageDialog(null,
						"Please fill out all the fields");
		}

	}
	public void printPallets(){
		searchArea.setText("");
		for (List<String> values : pallets.values()) {
			StringBuilder build = new StringBuilder();
			for (String info : values) {
				build.append(info).append("\t");

			}
			build.append("\n");
			String output = build.toString();
			searchArea.append(output);
		}
		
		
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		String close = (String) arg1;
		if(arg1!=null){
			if (close.equals("close")) {
				f.setVisible(false);
			}
		}
	}

	
}
