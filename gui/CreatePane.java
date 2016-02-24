package gui;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import project2.Server;

public class CreatePane extends BasicPanes {
	private JTextField patientIdT;
	private JTextField doctorIdT;
	private JTextField nurseIdT;
	private JTextField divisionT;
	private JTextField titleT;
	private JTextField dataT;
	private CreateButton confirm;

	public CreatePane(Server server) {
		super(server);
		Create();
	}

	/**
	 * Creates a new profile
	 */
	private void Create() {
		JLabel patientId = new JLabel("Patient ID: ");
		patientId.setBounds(37, 92, 74, 14);
		gui.add(patientId);
		patientIdT = new JTextField();
		patientIdT.setEditable(true);
		patientIdT.setBounds(121, 92, 214, 20);
		gui.add(patientIdT);

		JLabel doctorId = new JLabel("Doctor ID: ");
		doctorId.setBounds(37, 162, 74, 14);
		gui.add(doctorId);
		doctorIdT = new JTextField();
		doctorIdT.setEditable(true);
		doctorIdT.setBounds(121, 162, 214, 20);
		gui.add(doctorIdT);

		JLabel nurseId = new JLabel("Nurse ID: ");
		nurseId.setBounds(37, 232, 74, 14);
		gui.add(nurseId);
		nurseIdT = new JTextField();
		nurseIdT.setEditable(true);
		nurseIdT.setBounds(121, 232, 214, 20);
		gui.add(nurseIdT);

		JLabel division = new JLabel("Division: ");
		division.setBounds(37, 302, 74, 14);
		gui.add(division);
		divisionT = new JTextField();
		divisionT.setEditable(true);
		divisionT.setBounds(121, 302, 214, 20);
		gui.add(divisionT);

		JLabel title = new JLabel("Title: ");
		title.setBounds(37, 372, 74, 14);
		gui.add(title);
		titleT = new JTextField();
		titleT.setEditable(true);
		titleT.setBounds(121, 372, 214, 20);
		gui.add(titleT);

		JLabel dataJ = new JLabel("Data: ");
		dataJ.setBounds(506, 58, 143, 26);
		gui.add(dataJ);
		dataT = new JTextField();
		dataT.setEditable(true);
		dataT.setBounds(506, 89, 415, 350);
		gui.add(dataT);

		confirm = new CreateButton(560, 480, "OK", server);
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
		if (patientIdT.getText().length() != 10 || nurseIdT.getText().length() != 10 || doctorIdT.getText().length() != 10){
			information = null;
		}
		return information;
	}

}
