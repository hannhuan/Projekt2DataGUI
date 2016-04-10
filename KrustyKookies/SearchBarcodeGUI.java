package KrustyKookies;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;

public class SearchBarcodeGUI implements Observer {
	private DataBase db;
	private JFrame f;
	private JTextField palletId;
	private JTextPane textPane;
	private JButton blocked;
	private JButton status;
	private List<String> pallet;

	public SearchBarcodeGUI(DataBase db) {
		this.db = db;
		db.addObserver(this);
		pallet = new ArrayList<String>();

		f = new JFrame("KrustyKookies Production");
		f.getContentPane().setBackground(Color.PINK);
		f.setSize(355, 250);
		f.setLocation(300, 200);
		f.getContentPane().setLayout(null);

		JLabel palletID = new JLabel("Insert PalletID:");
		palletID.setBounds(36, 33, 92, 24);
		f.getContentPane().add(palletID);

		palletId = new JTextField();
		palletId.setBounds(138, 35, 125, 20);
		f.getContentPane().add(palletId);
		palletId.setColumns(10);

		JButton ok = new JButton("OK");
		ok.setBounds(273, 33, 56, 23);
		ok.addActionListener(new EnterListener());
		f.getContentPane().add(ok);

		textPane = new JTextPane();
		textPane.setBounds(36, 82, 263, 86);
		f.getContentPane().add(textPane);

		blocked = new JButton("Block/Unblock");
		blocked.setBounds(39, 177, 135, 23);
		blocked.addActionListener(new BlockedListener());
		f.getContentPane().add(blocked);

		status = new JButton("Edit Status");
		status.setBounds(184, 177, 115, 23);
		status.addActionListener(new StatusListener());
		f.getContentPane().add(status);
		textPane.setEditable(false);
		f.setVisible(true);
	}

	class EnterListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {				
				
				pallet.clear();
				List<String> palletInfo = db.searchPallet(palletId.getText());
				StringBuilder build = new StringBuilder();
				for (String info : palletInfo) {
					pallet.add(info);
					System.out.println(info);
					build.append(info).append("\n");
				}
				textPane.setText(build.toString());				
		}

	}

	class StatusListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			String currentStatus = pallet.get(3);
			if (currentStatus.equals("Delivered")) {
					JOptionPane.showMessageDialog(null,
							"The pallet is already delivered");
					
			} else {
				
				if (!pallet.isEmpty() && pallet.get(4).equals("Not Blocked")) {
					String ObjButtons[] = { "Yes", "No" };
					int result = JOptionPane.showOptionDialog(null,
							"Are you want to change the status "
									+ " is finished?", "Confirmation",
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE, null, ObjButtons,
							ObjButtons[1]);
					if (result == 0) {
						db.status(pallet.get(0));
						switch (currentStatus) {
						case "Just Produced":
							pallet.set(3, "Freezer");
							break;
						case "Freezer":
							pallet.set(3, "Transport");
							break;
						case "Transport":
							pallet.set(3, "Delivered");
							db.setDelivered(pallet.get(0));
							break;
						}
					}
					StringBuilder build = new StringBuilder();
					for (String info : pallet) {
						build.append(info).append("\n");
					}
					textPane.setText(build.toString());


				} else {
					JOptionPane.showMessageDialog(null,
							"The pallet is blocked");
				}
			}
		}

	}

	class BlockedListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (!pallet.isEmpty() && pallet.get(4).equals("Not Blocked")) {
				String date = db.block(pallet.get(0));
				pallet.set(4, date);

			} else {
				db.unblock(pallet.get(0));
				pallet.set(4, "Not Blocked");
			}
			StringBuilder build = new StringBuilder();
			for (String info : pallet) {
				build.append(info).append("\n");
			}
			textPane.setText(build.toString());
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
