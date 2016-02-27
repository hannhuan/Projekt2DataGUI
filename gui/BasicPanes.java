package gui;

import java.awt.Container;

import javax.swing.JFrame;

import project2.Server;

public abstract class BasicPanes extends JFrame {
	Container gui;
	protected Server server;
	
	public BasicPanes(Server server) {
		this.server=server;
		createDemo();
		setVisible(true);

	}

	private void createDemo() {
		gui = getContentPane();
		gui.setLayout(null);

		setTitle("Record Management");
		setSize(1000, 600);
	}

	public abstract String getInfo() ;
		
}
