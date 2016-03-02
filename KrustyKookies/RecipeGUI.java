package KrustyKookies;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.Font;


public class RecipeGUI  {
	private JFrame f;
	private JLabel label;
	
	public RecipeGUI(String cookie, String recipe){
		f = new JFrame("KrustyKookies Production");
		f.getContentPane().setBackground(Color.PINK);
		f.setSize(400, 400);
		f.setLocation(400,200);
		f.getContentPane().setLayout(null);
		
		label = new JLabel(cookie);
		label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label.setBounds(10, 28, 123, 44);
		f.getContentPane().add(label);
		
		JTextPane textPane = new JTextPane();
		textPane.setBounds(131, 28, 243, 211);
		String[] text = recipe.split(";");
		StringBuilder build = new StringBuilder();
		for(int i = 0; i < text.length; i++){
			build.append(text[i]).append("\n");
		}
		textPane.setText(build.toString());
		f.getContentPane().add(textPane);
		
		JButton btnDone = new JButton("Done");
		btnDone.setBounds(128, 273, 123, 39);
		btnDone.addActionListener(new DoneListener());
		f.getContentPane().add(btnDone);
		f.setVisible(true);
	}
	class DoneListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			f.setVisible(false);
			
		}
		
	}


}
