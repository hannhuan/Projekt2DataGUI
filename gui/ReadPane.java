package gui;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import project2.Server;

public class ReadPane extends BasicPanes {
	private String data;
	public ReadPane(Server server){
		super(server);
		Read();
		
	}
	
	/**
	 * A reading interface containing detail of selected information
	 */
	private void Read(){
		JTextField test = new JTextField();
		test.setBounds(100, 100, 800, 400);
		test.setBackground(new Color(255,255,255));
		test.setEditable(false);
		gui.add(test);
		test.setText(getRecordData());
		setVisible(true);
	}

	private String getRecordData() {
		data = server.getRecordData();
		return data;
	}

	@Override
	public String getInfo() {
		return null;
	}

}
