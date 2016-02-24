package gui;

import javax.swing.JLabel;
import javax.swing.JTextField;

import project2.Server;

public class EditPane extends BasicPanes {
	private JTextField patientIdT;
	private JTextField doctorIdT;
	private JTextField nurseIdT;
	private JTextField divisionT;
	private JTextField titleT;
	private JTextField dataT;
	private CreateButton confirm;

	public EditPane(Server server) {
		super(server);
		Edit();
	}

	private void Edit() {
		String text = server.getRecordData();
		String[] u = text.split("\n");

		JLabel patientId = new JLabel("Patient ID: ");
		patientId.setBounds(37, 92, 74, 14);
		gui.add(patientId);
		patientIdT = new JTextField();
		patientIdT.setEditable(false);
		patientIdT.setBounds(121, 92, 214, 20);
		patientIdT.setText(u[0]);
		gui.add(patientIdT);

		JLabel doctorId = new JLabel("Doctor ID: ");
		doctorId.setBounds(37, 162, 74, 14);
		gui.add(doctorId);
		doctorIdT = new JTextField();
		doctorIdT.setEditable(false);
		doctorIdT.setBounds(121, 162, 214, 20);
		doctorIdT.setText(u[1]);
		gui.add(doctorIdT);

		JLabel nurseId = new JLabel("Nurse ID: ");
		nurseId.setBounds(37, 232, 74, 14);
		gui.add(nurseId);
		nurseIdT = new JTextField();
		nurseIdT.setEditable(false);
		nurseIdT.setBounds(121, 232, 214, 20);
		nurseIdT.setText(u[2]);
		gui.add(nurseIdT);

		JLabel division = new JLabel("Division: ");
		division.setBounds(37, 302, 74, 14);
		gui.add(division);
		divisionT = new JTextField();
		divisionT.setEditable(false);
		divisionT.setBounds(121, 302, 214, 20);
		divisionT.setText(u[3]);
		gui.add(divisionT);

		JLabel title = new JLabel("Title: ");
		title.setBounds(37, 372, 74, 14);
		gui.add(title);
		titleT = new JTextField();
		titleT.setEditable(false);
		titleT.setBounds(121, 372, 214, 20);
		titleT.setText(u[4]);
		gui.add(titleT);

		JLabel dataJ = new JLabel("Data: ");
		dataJ.setBounds(506, 58, 143, 26);
		gui.add(dataJ);
		dataT = new JTextField();
		dataT.setEditable(true);
		dataT.setBounds(506, 89, 415, 350);
		dataT.setText(u[5]);
		gui.add(dataT);

		confirm = new CreateButton(560, 480, "Confirm", server);
		CreateButton cancel = new CreateButton(772, 480, "Cancel", null);
		cancel.setCurrentPane(this);
		confirm.setCurrentPane(this);
		gui.add(confirm.getbutton());
		gui.add(cancel.getbutton());
	}

	@Override
	public String getInfo() {

		String information;
		information = patientIdT.getText() + "," + doctorIdT.getText() + ","
				+ nurseIdT.getText() + "," + divisionT.getText() + ","
				+ titleT.getText() + "," + dataT.getText();
		if (patientIdT.getText().length() != 10
				|| nurseIdT.getText().length() != 10
				|| doctorIdT.getText().length() != 10) {
			information = null;
		}
		return information;

	}

}
